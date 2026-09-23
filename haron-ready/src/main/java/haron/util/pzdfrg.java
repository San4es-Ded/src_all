package haron.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

public class pzdfrg {
    public static int a;
    public static boolean b;

    public static BufferedReader b(InputStream inputStream) {
        int n = 953;
        return new BufferedReader(new InputStreamReader(inputStream));
    }

    public static BufferedWriter a(OutputStream outputStream) {
        return new BufferedWriter(new OutputStreamWriter(outputStream));
    }

    public static void a(OutputStream outputStream, String string) {
        try {
            pzdfrg.a(outputStream).write(string);
        }
        catch (IOException iOException) {
            throw new RuntimeException(iOException);
        }
    }

    public static String a(InputStream inputStream) {
        BufferedReader bufferedReader = pzdfrg.b(inputStream);
        StringBuilder stringBuilder = new StringBuilder();
        try {
            while (true) {
                String string;
                if ((string = bufferedReader.readLine()) == null) {
                    return stringBuilder.toString();
                }
                stringBuilder.append(string).append("\n");
            }
        }
        catch (IOException iOException) {
            throw new RuntimeException(iOException);
        }
    }
}

