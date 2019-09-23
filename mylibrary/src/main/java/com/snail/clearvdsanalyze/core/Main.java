package com.snail.clearvdsanalyze.core;

import com.snail.clearvdsanalyze.entity.ClearWebEntity;
import com.snail.clearvdsanalyze.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * 主类
 */
public class Main {

    private static final String TAG = "Main";
    public static List<ClearWebEntity> clearWebEntityList;

    public static void main(String[] args) {
        Log.log(TAG, "读取明网数据");
        clearWebEntityList = readClearWeb();
        Log.log(TAG, "准备启动服务");
        VdsService vdsService = new VdsService(clearWebEntityList);
        vdsService.start();
    }

    private static List<ClearWebEntity> readClearWeb() {
        File file = new File(Constant.CLEAR_WEB_FILE_NAME);
        if (!file.exists()) {
            throw new IllegalStateException("缺少clearWeb配置文件");
        } else {
            List<ClearWebEntity> clearWebEntities = new ArrayList<>();
            try {
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
                String lineContent = "";
                while ((lineContent = bufferedReader.readLine()) != null) {
                    System.out.println(lineContent);
                    String[] split = lineContent.split(",");
                    ClearWebEntity clearWebEntity = new ClearWebEntity();
                    clearWebEntity.setWebUrl(split[0]);
                    clearWebEntity.setVid(split[1]);
                    System.out.println("读取到的clearWebMessage:" + clearWebEntity.toString());
                    clearWebEntities.add(clearWebEntity);
                }
                bufferedReader.close();
            } catch (Exception e) {
                throw new IllegalStateException("clearWeb配置文件有问题,请检查格式");
            }
            return clearWebEntities;
        }
    }
}
