package com.snail.vds.util;

import android.os.Process;
import android.util.Log;


import com.snail.vds.BuildConfig;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author create by yongjie on 2018/6/29
 * Log工具类
 */
public class HiLogger {

    private static volatile boolean OUTPUT_LOG = BuildConfig.LOG_ENABLE;

    public static void logEnable(boolean enable) {
        OUTPUT_LOG = enable;
    }

    public static void v(String tag, String message) {
        if (OUTPUT_LOG) {
            Log.v(tag, basicMessage(message));
        }
    }

    public static void v(String tag, String format, Object... obj) {
        if (OUTPUT_LOG) {
            String message = String.format(format, obj);
            Log.v(tag, basicMessage(message));
        }
    }


    public static void d(String tag, String message) {
        if (OUTPUT_LOG) {
            Log.d(tag, basicMessage(message));
        }
    }

    public static void d(String tag, String format, Object... obj) {
        if (OUTPUT_LOG) {
            String message = String.format(format, obj);
            Log.v(tag, basicMessage(message));
        }
    }

    public static void i(String tag, String message) {
        if (OUTPUT_LOG) {
            Log.i(tag, basicMessage(message));
        }
    }

    public static void i(String tag, String format, Object... obj) {
        if (OUTPUT_LOG) {
            String message = String.format(format, obj);
            Log.v(tag, basicMessage(message));
        }
    }

    public static void w(String tag, String message) {
        if (OUTPUT_LOG) {
            Log.w(tag, basicMessage(message));
        }
    }

    public static void w(String tag, String format, Object... obj) {
        if (OUTPUT_LOG) {
            String message = String.format(format, obj);
            Log.v(tag, basicMessage(message));
        }
    }

    public static void e(String tag, String message) {
        if (OUTPUT_LOG) {
            Log.e(tag, basicMessage(message));
        }
    }

    public static void e(String tag, String message, Throwable throwable) {
        if (OUTPUT_LOG) {
            Log.e(tag, basicMessage(message), throwable);
        }
    }

    public static void e(String tag, String format, Object... obj) {
        if (OUTPUT_LOG) {
            String message = String.format(format, obj);
            Log.v(tag, basicMessage(message));
        }
    }

    private static String basicMessage(String message) {
        long startTime = System.currentTimeMillis();
        if (message == null) {
            message = "null";
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("[")
                .append(new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").
                        format(new Date(System.currentTimeMillis())))
                .append(" /").append(Process.myPid())
                .append("/").append(Thread.currentThread().getId())
                .append("/").append(Thread.currentThread().getName())
                .append(" 耗时:").append(System.currentTimeMillis() - startTime)
                .append("ms]")
                .append("==>");

        String format = String.format("%s %s", stringBuilder.toString(), message);
        return format;
    }


    /**
     * 打印方法栈帧信息，写5的原因是根据栈的入栈规律，第5个元素就是我们需要的信息
     * 暂时测试没有问题
     */
    private static String getMethodMessage() {
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        StringBuilder builder = new StringBuilder();
        String className = stackTrace[5].getClassName();
        String methodName = stackTrace[5].getMethodName();
        int lineName = stackTrace[5].getLineNumber();
        builder.append("[").append(className).append(", ").append(methodName).append("(), ").append(lineName).append("]");
        return builder.toString();
    }


}

