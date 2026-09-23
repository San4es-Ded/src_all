package wtf.wyvern.core.neuro;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Random;

/**
 * A compact one-hidden-layer MLP. The first two outputs reproduce the next
 * mouse step; the sigmoid output learns whether the player attacked in that
 * state. Everything required for inference is stored in the model JSON.
 *
 * Mouse steps are heavy-tailed (many micro steps, rare big flicks), so the
 * rotation targets are trained through a sign-preserving square-root
 * compression; {@link #predict} applies the inverse. This stops rare flicks
 * from drowning in the tanh saturation and makes small corrections precise.
 *
 * Training uses Adam with L2 regularization - plain SGD+momentum converged poorly
 * on this feature scale mix.
 */
public final class NeuroModel {
    public static final int VERSION = 2;
    private static final int OUTPUTS = 4;
    private static final int HIDDEN = 32;
    private static final int EPOCHS = 1200;
    private static final double WEIGHT_DECAY = 3.0E-5D;

    private int version;
    private String name;
    private double[][] inputWeights;
    private double[] hiddenBias;
    private double[][] outputWeights;
    private double[] outputBias;
    private double[] inputMean;
    private double[] inputDeviation;
    private double yawScale;
    private double pitchScale;
    private double attackThreshold;
    private double attackDelayMean;
    private double attackDelayDeviation;
    private double trainedSensitivity;
    private double trainedGcd;
    private int sampleCount;
    private int hitCount;
    private long trainedAt;

    private NeuroModel() {
    }

    public Prediction predict(double[] rawInputs) {
        if (!isValid() || rawInputs == null || rawInputs.length != NeuroInputs.SIZE) {
            return new Prediction(0.0F, 0.0F, 0.0F);
        }
        double[] hidden = new double[HIDDEN];
        for (int h = 0; h < HIDDEN; h++) {
            double sum = hiddenBias[h];
            for (int i = 0; i < NeuroInputs.SIZE; i++) {
                double normalized = (rawInputs[i] - inputMean[i]) / inputDeviation[i];
                sum += inputWeights[h][i] * clamp(normalized, -5.0D, 5.0D);
            }
            hidden[h] = Math.tanh(sum);
        }

        double[] output = new double[OUTPUTS];
        for (int o = 0; o < OUTPUTS; o++) {
            double sum = outputBias[o];
            for (int h = 0; h < HIDDEN; h++) {
                sum += outputWeights[o][h] * hidden[h];
            }
            output[o] = o == 2 ? sigmoid(sum) : Math.tanh(sum);
        }
        return new Prediction(
                (float) decompressStep(output[0], yawScale),
                (float) decompressStep(output[1], pitchScale),
                (float) output[2]
        );
    }

    public boolean isValid() {
        return version == VERSION
                && name != null && !name.isBlank()
                && matrixSize(inputWeights, HIDDEN, NeuroInputs.SIZE)
                && vectorSize(hiddenBias, HIDDEN)
                && matrixSize(outputWeights, OUTPUTS, HIDDEN)
                && vectorSize(outputBias, OUTPUTS)
                && vectorSize(inputMean, NeuroInputs.SIZE)
                && vectorSize(inputDeviation, NeuroInputs.SIZE)
                && allPositive(inputDeviation)
                && yawScale > 0.0D && pitchScale > 0.0D
                && allFinite();
    }

    static NeuroModel train(String name, List<NeuroSample> samples,
                            double sensitivity, double gcd, ProgressListener listener) {
        if (samples == null || samples.isEmpty()) {
            throw new IllegalArgumentException("Training samples are empty");
        }

        NeuroModel model = new NeuroModel();
        model.version = VERSION;
        model.name = name;
        model.sampleCount = samples.size();
        model.hitCount = (int) samples.stream().filter(NeuroSample::attack).count();
        model.trainedSensitivity = sensitivity;
        model.trainedGcd = gcd;
        model.trainedAt = System.currentTimeMillis();
        model.yawScale = percentileScale(samples, true);
        model.pitchScale = percentileScale(samples, false);
        model.inputMean = new double[NeuroInputs.SIZE];
        model.inputDeviation = new double[NeuroInputs.SIZE];
        calculateNormalization(samples, model.inputMean, model.inputDeviation);
        calculateAttackTiming(samples, model);

        Random random = new Random(0x4E4555524FL ^ name.toLowerCase(Locale.ROOT).hashCode());
        model.inputWeights = new double[HIDDEN][NeuroInputs.SIZE];
        model.hiddenBias = new double[HIDDEN];
        model.outputWeights = new double[OUTPUTS][HIDDEN];
        model.outputBias = new double[OUTPUTS];
        initializeWeights(model.inputWeights, random, NeuroInputs.SIZE);
        initializeWeights(model.outputWeights, random, HIDDEN);
        // Logit of the base attack rate: the sigmoid starts at the dataset
        // prior instead of at 0.5.
        double attackRate = clamp(model.hitCount / (double) model.sampleCount, 1.0E-4D, 1.0D - 1.0E-4D);
        model.outputBias[2] = Math.log(attackRate / (1.0D - attackRate));

        AdamState adam = new AdamState();
        double positiveWeight = Math.max(1.0D,
                Math.min(12.0D, (samples.size() - model.hitCount) / (double) Math.max(1, model.hitCount)));
        List<Integer> order = new ArrayList<>(samples.size());
        for (int index = 0; index < samples.size(); index++) {
            order.add(index);
        }

        for (int epoch = 1; epoch <= EPOCHS; epoch++) {
            Collections.shuffle(order, random);
            double learningRate = 0.004D * Math.pow(0.985D, epoch / 8.0D) + 0.00025D;
            double epochLoss = 0.0D;
            for (int sampleIndex : order) {
                NeuroSample sample = samples.get(sampleIndex);
                adam.timestep++;
                epochLoss += trainOne(model, sample, learningRate, positiveWeight, adam);
            }
            epochLoss /= samples.size();
            if (listener != null && (epoch == 1 || epoch % 40 == 0 || epoch == EPOCHS)) {
                listener.onProgress(epoch, EPOCHS, epochLoss);
            }
        }

        model.attackThreshold = chooseAttackThreshold(model, samples);
        if (!model.isValid()) {
            throw new IllegalStateException("The trained model contains invalid values");
        }
        return model;
    }

    private static double trainOne(NeuroModel model, NeuroSample sample, double learningRate,
                                   double positiveWeight, AdamState adam) {
        double[] input = new double[NeuroInputs.SIZE];
        for (int i = 0; i < input.length; i++) {
            input[i] = clamp((sample.inputs()[i] - model.inputMean[i])
                    / model.inputDeviation[i], -5.0D, 5.0D);
        }
        double[] hidden = new double[HIDDEN];
        for (int h = 0; h < HIDDEN; h++) {
            double sum = model.hiddenBias[h];
            for (int i = 0; i < input.length; i++) {
                sum += model.inputWeights[h][i] * input[i];
            }
            hidden[h] = Math.tanh(sum);
        }
        double[] output = new double[OUTPUTS];
        for (int o = 0; o < OUTPUTS; o++) {
            double sum = model.outputBias[o];
            for (int h = 0; h < HIDDEN; h++) {
                sum += model.outputWeights[o][h] * hidden[h];
            }
            output[o] = o == 2 ? sigmoid(sum) : Math.tanh(sum);
        }

        double[] target = {
                compressStep(sample.yawStep(), model.yawScale),
                compressStep(sample.pitchStep(), model.pitchScale),
                sample.attack() ? 1.0D : 0.0D
        };
        double attackWeight = sample.attack() ? positiveWeight : 1.0D;
        double[] outputGradient = new double[OUTPUTS];
        outputGradient[0] = (output[0] - target[0]) * (1.0D - output[0] * output[0]);
        outputGradient[1] = (output[1] - target[1]) * (1.0D - output[1] * output[1]);
        outputGradient[2] = 0.35D * attackWeight * (output[2] - target[2]);

        double[] hiddenGradient = new double[HIDDEN];
        for (int h = 0; h < HIDDEN; h++) {
            double gradient = 0.0D;
            for (int o = 0; o < OUTPUTS; o++) {
                gradient += outputGradient[o] * model.outputWeights[o][h];
            }
            hiddenGradient[h] = gradient * (1.0D - hidden[h] * hidden[h]);
        }

        final double beta1 = 0.9D;
        final double beta2 = 0.999D;
        final double epsilon = 1.0E-8D;
        double biasCorrection1 = 1.0D - Math.pow(beta1, adam.timestep);
        double biasCorrection2 = 1.0D - Math.pow(beta2, adam.timestep);

        for (int o = 0; o < OUTPUTS; o++) {
            for (int h = 0; h < HIDDEN; h++) {
                double gradient = outputGradient[o] * hidden[h]
                        + WEIGHT_DECAY * model.outputWeights[o][h];
                model.outputWeights[o][h] -= learningRate * adamStep(
                        adam.outputWeightsM, adam.outputWeightsV, o, h, gradient,
                        beta1, beta2, biasCorrection1, biasCorrection2, epsilon);
            }
            model.outputBias[o] -= learningRate * adamStep(
                    adam.outputBiasM, adam.outputBiasV, o, outputGradient[o],
                    beta1, beta2, biasCorrection1, biasCorrection2, epsilon);
        }
        for (int h = 0; h < HIDDEN; h++) {
            for (int i = 0; i < input.length; i++) {
                double gradient = hiddenGradient[h] * input[i]
                        + WEIGHT_DECAY * model.inputWeights[h][i];
                model.inputWeights[h][i] -= learningRate * adamStep(
                        adam.inputWeightsM, adam.inputWeightsV, h, i, gradient,
                        beta1, beta2, biasCorrection1, biasCorrection2, epsilon);
            }
            model.hiddenBias[h] -= learningRate * adamStep(
                    adam.hiddenBiasM, adam.hiddenBiasV, h, hiddenGradient[h],
                    beta1, beta2, biasCorrection1, biasCorrection2, epsilon);
        }

        double rotationLoss = 0.5D * (square(output[0] - target[0]) + square(output[1] - target[1]));
        double attackLoss = -attackWeight * (target[2] * Math.log(Math.max(output[2], 1.0E-7D))
                + (1.0D - target[2]) * Math.log(Math.max(1.0D - output[2], 1.0E-7D)));
        return rotationLoss + 0.35D * attackLoss;
    }

    private static double adamStep(double[][] m, double[][] v, int row, int column, double gradient,
                                   double beta1, double beta2,
                                   double biasCorrection1, double biasCorrection2, double epsilon) {
        m[row][column] = beta1 * m[row][column] + (1.0D - beta1) * gradient;
        v[row][column] = beta2 * v[row][column] + (1.0D - beta2) * gradient * gradient;
        double mHat = m[row][column] / biasCorrection1;
        double vHat = v[row][column] / biasCorrection2;
        return mHat / (Math.sqrt(vHat) + epsilon);
    }

    private static double adamStep(double[] m, double[] v, int index, double gradient,
                                   double beta1, double beta2,
                                   double biasCorrection1, double biasCorrection2, double epsilon) {
        m[index] = beta1 * m[index] + (1.0D - beta1) * gradient;
        v[index] = beta2 * v[index] + (1.0D - beta2) * gradient * gradient;
        double mHat = m[index] / biasCorrection1;
        double vHat = v[index] / biasCorrection2;
        return mHat / (Math.sqrt(vHat) + epsilon);
    }

    /** Sign-preserving sqrt compression of heavy-tailed mouse steps. */
    private static double compressStep(double step, double scale) {
        return clamp(Math.signum(step) * Math.sqrt(Math.abs(step) / scale), -1.0D, 1.0D);
    }

    private static double decompressStep(double output, double scale) {
        return Math.signum(output) * output * output * scale;
    }

    private static void calculateNormalization(List<NeuroSample> samples, double[] mean, double[] deviation) {
        for (NeuroSample sample : samples) {
            for (int i = 0; i < mean.length; i++) {
                mean[i] += sample.inputs()[i];
            }
        }
        for (int i = 0; i < mean.length; i++) {
            mean[i] /= samples.size();
        }
        for (NeuroSample sample : samples) {
            for (int i = 0; i < deviation.length; i++) {
                deviation[i] += square(sample.inputs()[i] - mean[i]);
            }
        }
        for (int i = 0; i < deviation.length; i++) {
            deviation[i] = Math.max(0.05D, Math.sqrt(deviation[i] / samples.size()));
        }
    }

    private static double percentileScale(List<NeuroSample> samples, boolean yaw) {
        double[] absolute = new double[samples.size()];
        for (int i = 0; i < samples.size(); i++) {
            absolute[i] = Math.abs(yaw ? samples.get(i).yawStep() : samples.get(i).pitchStep());
        }
        Arrays.sort(absolute);
        double percentile = absolute[Math.min(absolute.length - 1,
                (int) Math.floor((absolute.length - 1) * 0.98D))];
        return clamp(percentile * 1.12D, yaw ? 1.0D : 0.75D, yaw ? 90.0D : 60.0D);
    }

    private static void calculateAttackTiming(List<NeuroSample> samples, NeuroModel model) {
        List<Integer> attacks = new ArrayList<>();
        for (NeuroSample sample : samples) {
            if (sample.attack()) {
                attacks.add(sample.tick());
            }
        }
        if (attacks.size() < 2) {
            model.attackDelayMean = 10.0D;
            model.attackDelayDeviation = 2.0D;
            return;
        }
        List<Integer> delays = new ArrayList<>(attacks.size() - 1);
        for (int i = 1; i < attacks.size(); i++) {
            int delay = attacks.get(i) - attacks.get(i - 1);
            if (delay > 0 && delay <= 80) {
                delays.add(delay);
            }
        }
        if (delays.isEmpty()) {
            model.attackDelayMean = 10.0D;
            model.attackDelayDeviation = 2.0D;
            return;
        }
        model.attackDelayMean = delays.stream().mapToInt(Integer::intValue).average().orElse(10.0D);
        double variance = delays.stream().mapToDouble(delay -> square(delay - model.attackDelayMean))
                .average().orElse(1.0D);
        model.attackDelayMean = clamp(model.attackDelayMean, 2.0D, 30.0D);
        model.attackDelayDeviation = clamp(Math.sqrt(variance), 1.0D, 8.0D);
    }

    private static double chooseAttackThreshold(NeuroModel model, List<NeuroSample> samples) {
        double bestThreshold = 0.5D;
        double bestScore = -1.0D;
        for (double threshold = 0.20D; threshold <= 0.80D; threshold += 0.05D) {
            int truePositive = 0;
            int falsePositive = 0;
            int falseNegative = 0;
            for (NeuroSample sample : samples) {
                boolean predicted = model.predict(sample.inputs()).attackProbability() >= threshold;
                if (predicted && sample.attack()) truePositive++;
                if (predicted && !sample.attack()) falsePositive++;
                if (!predicted && sample.attack()) falseNegative++;
            }
            double precision = truePositive / (double) Math.max(1, truePositive + falsePositive);
            double recall = truePositive / (double) Math.max(1, truePositive + falseNegative);
            double f1 = 2.0D * precision * recall / Math.max(1.0E-8D, precision + recall);
            if (f1 > bestScore) {
                bestScore = f1;
                bestThreshold = threshold;
            }
        }
        return bestThreshold;
    }

    private static void initializeWeights(double[][] weights, Random random, int fanIn) {
        double scale = Math.sqrt(2.0D / (fanIn + weights.length));
        for (double[] row : weights) {
            for (int i = 0; i < row.length; i++) {
                row[i] = random.nextGaussian() * scale;
            }
        }
    }

    private boolean allFinite() {
        return finite(inputWeights) && finite(hiddenBias) && finite(outputWeights)
                && finite(outputBias) && finite(inputMean) && finite(inputDeviation)
                && Double.isFinite(yawScale) && Double.isFinite(pitchScale)
                && Double.isFinite(attackThreshold) && Double.isFinite(attackDelayMean)
                && Double.isFinite(attackDelayDeviation);
    }

    private static boolean matrixSize(double[][] matrix, int rows, int columns) {
        if (matrix == null || matrix.length != rows) return false;
        for (double[] row : matrix) {
            if (row == null || row.length != columns) return false;
        }
        return true;
    }

    private static boolean vectorSize(double[] vector, int size) {
        return vector != null && vector.length == size;
    }

    private static boolean finite(double[][] values) {
        if (values == null) return false;
        for (double[] row : values) {
            if (!finite(row)) return false;
        }
        return true;
    }

    private static boolean finite(double[] values) {
        if (values == null) return false;
        for (double value : values) {
            if (!Double.isFinite(value)) return false;
        }
        return true;
    }

    private static boolean allPositive(double[] values) {
        if (values == null) return false;
        for (double value : values) {
            if (!(value > 0.0D)) return false;
        }
        return true;
    }

    private static double sigmoid(double value) {
        if (value >= 0.0D) {
            double exp = Math.exp(-value);
            return 1.0D / (1.0D + exp);
        }
        double exp = Math.exp(value);
        return exp / (1.0D + exp);
    }

    private static double square(double value) {
        return value * value;
    }

    private static double clamp(double value, double minimum, double maximum) {
        return Math.max(minimum, Math.min(maximum, value));
    }

    public String getName() {
        return name;
    }

    public double getAttackThreshold() {
        return attackThreshold;
    }

    public double getAttackDelayMean() {
        return attackDelayMean;
    }

    public double getAttackDelayDeviation() {
        return attackDelayDeviation;
    }

    public double getTrainedSensitivity() {
        return trainedSensitivity;
    }

    public int getSampleCount() {
        return sampleCount;
    }

    public int getHitCount() {
        return hitCount;
    }

    public record Prediction(float yawStep, float pitchStep, float attackProbability) {
    }

    @FunctionalInterface
    interface ProgressListener {
        void onProgress(int epoch, int totalEpochs, double loss);
    }

    /** Per-parameter first/second moment buffers for Adam. */
    private static final class AdamState {
        private final double[][] inputWeightsM = new double[HIDDEN][NeuroInputs.SIZE];
        private final double[][] inputWeightsV = new double[HIDDEN][NeuroInputs.SIZE];
        private final double[] hiddenBiasM = new double[HIDDEN];
        private final double[] hiddenBiasV = new double[HIDDEN];
        private final double[][] outputWeightsM = new double[OUTPUTS][HIDDEN];
        private final double[][] outputWeightsV = new double[OUTPUTS][HIDDEN];
        private final double[] outputBiasM = new double[OUTPUTS];
        private final double[] outputBiasV = new double[OUTPUTS];
        private long timestep;
    }
}
