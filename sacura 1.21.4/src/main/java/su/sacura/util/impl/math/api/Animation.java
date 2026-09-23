package su.sacura.util.impl.math.api;

import net.minecraft.util.math.Direction;
import su.sacura.util.impl.math.helper.TimerUtil;

public class Animation {
    public TimerUtil timerUtil = new TimerUtil();
    protected int duration;
    protected double endPoint;
    protected Direction.AxisDirection direction;
    protected Easing easing;

    public Animation(int ms, double endPoint) {
        // Исправлено: field_11056 -> POSITIVE
        this(ms, endPoint, Direction.AxisDirection.POSITIVE);
    }

    public Animation(int ms, double endPoint, Direction.AxisDirection direction) {
        this.duration = ms;
        this.endPoint = endPoint;
        this.direction = direction;
        this.easing = Easing.LINEAR;
    }

    public Animation(int ms, double endPoint, Easing easing) {
        // Исправлено: field_11056 -> POSITIVE
        this(ms, endPoint, Direction.AxisDirection.POSITIVE);
        this.easing = easing;
    }

    public Animation(int ms, double endPoint, boolean direction, Easing easing) {
        this.duration = ms;
        this.endPoint = endPoint;
        // Исправлено: field_11056 -> POSITIVE, field_11060 -> NEGATIVE
        this.direction = direction ? Direction.AxisDirection.POSITIVE : Direction.AxisDirection.NEGATIVE;
        this.easing = easing;
    }

    public boolean finished(Direction.AxisDirection direction) {
        return (isDone() && this.direction.equals(direction));
    }

    public void setEndPoint(double endPoint) {
        this.endPoint = endPoint;
    }

    public void reset() {
        this.timerUtil.reset();
    }

    public boolean isDone() {
        return this.timerUtil.hasTimeElapsed(this.duration);
    }

    public Direction.AxisDirection getDirection() {
        return this.direction;
    }

    public void setDirection(Direction.AxisDirection direction) {
        if (this.direction != direction) {
            this.direction = direction;
            this.timerUtil.setTime(System.currentTimeMillis() - this.duration - Math.min(this.duration, this.timerUtil.getTime()));
        }
    }

    public Animation setDirection(boolean forwards) {
        // Исправлено: field_11056 -> POSITIVE, field_11060 -> NEGATIVE
        Direction.AxisDirection direction = forwards ? Direction.AxisDirection.POSITIVE : Direction.AxisDirection.NEGATIVE;
        if (this.direction != direction) {
            this.direction = direction;
            this.timerUtil.setTime(System.currentTimeMillis() - this.duration - Math.min(this.duration, this.timerUtil.getTime()));
        }
        return this;
    }

    public void update(boolean forwards) {
        setDirection(forwards);
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    protected boolean correctOutput() {
        return false;
    }

    public double getOutput() {
        // Исправлено: field_11056 -> POSITIVE
        if (this.direction == Direction.AxisDirection.POSITIVE) {
            if (isDone())
                return this.endPoint;
            return getEquation(this.timerUtil.getTime()) * this.endPoint;
        }
        if (isDone())
            return 0.0D;
        if (correctOutput()) {
            double revTime = Math.min(this.duration, Math.max(0L, this.duration - this.timerUtil.getTime()));
            return getEquation(revTime) * this.endPoint;
        }
        return (1.0D - getEquation(this.timerUtil.getTime())) * this.endPoint;
    }

    public float getValue() {
        return (float)getOutput();
    }

    public double getEndput() {
        // Исправлено: field_11060 -> NEGATIVE
        if (this.direction == Direction.AxisDirection.NEGATIVE) {
            if (isDone())
                return this.endPoint;
            return getEquation(this.timerUtil.getTime()) * this.endPoint;
        }
        if (isDone())
            return 0.0D;
        if (correctOutput()) {
            double revTime = Math.min(this.duration, Math.max(0L, this.duration - this.timerUtil.getTime()));
            return getEquation(revTime) * this.endPoint;
        }
        return (1.0D - getEquation(this.timerUtil.getTime())) * this.endPoint;
    }

    protected double getEquation(double x) {
        double progress = x / this.duration;
        if (progress > 1.0D)
            progress = 1.0D;
        if (progress < 0.0D)
            progress = 0.0D;
        if (this.easing != null)
            return this.easing.apply(progress);
        return progress;
    }
}