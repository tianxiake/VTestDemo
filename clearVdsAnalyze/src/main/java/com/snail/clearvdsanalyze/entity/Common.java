package com.snail.clearvdsanalyze.entity;

/**
 * @author yongjie created on 2019-08-04.
 */
public class Common {

    private static int weekMinHeight;
    private static int weekMaxHeight;

    public static int getWeekMinHeight() {
        return 24 * 10080;
    }

    public static void setWeekMinHeight(int weekMinHeight) {
        Common.weekMinHeight = weekMinHeight;
    }

    public static int getWeekMaxHeight() {
        return 25 * 10080;
    }

    public static void setWeekMaxHeight(int weekMaxHeight) {
        Common.weekMaxHeight = weekMaxHeight;
    }
}
