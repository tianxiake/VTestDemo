package com.snail.clearvdsanalyze.entity;

/**
 * @author yongjie created on 2019-08-04.
 */
public class Common {

    private static int weekMinHeight;
    private static int weekMaxHeight;

    public static int getWeekMinHeight() {
        return weekMinHeight;
    }

    public static void setWeekMinHeight(int weekMinHeight) {
        Common.weekMinHeight = weekMinHeight;
    }

    public static int getWeekMaxHeight() {
        return weekMaxHeight;
    }

    public static void setWeekMaxHeight(int weekMaxHeight) {
        Common.weekMaxHeight = weekMaxHeight;
    }
}
