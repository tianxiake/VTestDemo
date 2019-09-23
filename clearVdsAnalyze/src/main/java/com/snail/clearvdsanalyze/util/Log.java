package com.snail.clearvdsanalyze.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author yongjie created on 2019-08-04.
 */
public class Log {

    public static final String ERROR_FILE = "clearWeb.log";

    public static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd HH:mm:ss");

    public static void toAppendFile(String message, Throwable throwable) {
        FileOutputStream fileOutputStream = null;
        try {
            File file = new File(ERROR_FILE);
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("\n").append(simpleDateFormat.format(new Date())).append(":").append(message).append("\n");
            if (throwable != null) {
                stringBuilder.append(throwable.toString()).append("\n");
                StackTraceElement[] stackTrace = throwable.getStackTrace();
                for (int i = 0; i < stackTrace.length; i++) {
                    StackTraceElement stackTraceElement = stackTrace[i];
                    String errorStack = stackTraceElement.toString();
                    stringBuilder.append(errorStack).append("\n");
                }
            }
            fileOutputStream = new FileOutputStream(file, true);
            fileOutputStream.write(stringBuilder.toString().getBytes());
            fileOutputStream.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (fileOutputStream != null) {
                try {
                    fileOutputStream.close();
                } catch (IOException e) {

                }
            }
        }
    }

    public static void toAppendFile(String message) {
        toAppendFile(message, null);
    }


    public static void log(String TAG, String message) {
        StringBuilder builder = new StringBuilder();
        builder.append(simpleDateFormat.format(new Date())).append(" ").append(TAG).append(":").append(message);
        System.out.println(builder);
    }

}
