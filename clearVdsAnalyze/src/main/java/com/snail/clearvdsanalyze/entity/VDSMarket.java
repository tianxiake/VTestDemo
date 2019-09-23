package com.snail.clearvdsanalyze.entity;

/**
 * @author yongjie created on 2019-08-04.
 */
public class VDSMarket {

    public static final int WEEK_BLOCK_HEIGHT = 10080;
    private int currentVdsHeight;
    private int currentWeek;
    private int weekMinHeight;
    private int weekMaxHeight;


    public int getCurrentVdsHeight() {
        return currentVdsHeight;
    }

    public void setCurrentVdsHeight(int currentVdsHeight) {
        this.currentVdsHeight = currentVdsHeight;
        int index = currentVdsHeight / WEEK_BLOCK_HEIGHT;
        int extra = currentVdsHeight % WEEK_BLOCK_HEIGHT;
        weekMinHeight = index * WEEK_BLOCK_HEIGHT - 1;
        if (extra != 0) {
            currentWeek = index + 1;
            weekMaxHeight = (index + 1) * WEEK_BLOCK_HEIGHT - 1;
        } else {
            currentWeek = index;
            weekMaxHeight = (index + 1) * WEEK_BLOCK_HEIGHT;
        }
        Common.setWeekMinHeight(weekMinHeight);
        Common.setWeekMaxHeight(weekMaxHeight);
    }

    public int getWeekMinHeight() {
        return weekMinHeight;
    }


    public int getWeekMaxHeight() {
        return weekMaxHeight;
    }


}
