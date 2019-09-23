package com.snail.clearvdsanalyze.core;

import com.google.gson.Gson;
import com.snail.clearvdsanalyze.entity.ClearWebEntity;
import com.snail.clearvdsanalyze.entity.Common;
import com.snail.clearvdsanalyze.entity.VDSBlock;
import com.snail.clearvdsanalyze.entity.VDSBlockMarket;
import com.snail.clearvdsanalyze.entity.VDSMarket;
import com.snail.clearvdsanalyze.util.FileUtil;
import com.snail.clearvdsanalyze.util.Log;

import java.io.File;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * @author yongjie created on 2019-08-04.
 */
public class VdsService {
    private static final String TAG = "VdsService";


    private ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(3);
    private List<ClearWebEntity> clearWebEntityList;

    public VdsService(List<ClearWebEntity> clearWebEntityList) {
        this.clearWebEntityList = clearWebEntityList;
    }


    public void start() {
        Log.log(TAG, "启动明网VID统计服务");
        startGetCurrentHeightService();
        Log.log(TAG, "启动明网区块高度服务");
        startClearWeb();
    }

    private void startClearWeb() {
        scheduledExecutorService.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                Log.log(TAG, "开始明网数据统计分析");
                Log.toAppendFile("开始明网数据统计分析");
                try {
                    Retrofit retrofit = RetrofitFactory.createRetrofit();
                    ClearVDSWebSevice clearVDSWebSevice = retrofit.create(ClearVDSWebSevice.class);
                    for (int i = 0; i < clearWebEntityList.size(); i++) {
                        ClearWebEntity clearWebEntity = clearWebEntityList.get(i);
                        Log.log(TAG, "开始分析第" + i + "个vid:" + clearWebEntity.getVid());
                        handleOenClearWeb(clearVDSWebSevice, clearWebEntity);
                        Log.log(TAG, "完成分析第" + i + "个vid:" + clearWebEntity.getVid() + ",subVid:" + clearWebEntity.getSubVid() + ",网址:" + clearWebEntity.getWebUrl());
                    }
                    Log.log(TAG, "所有明网数据分析完成,开始写入文件");
                    boolean writeResult = writeToFile();
                    Log.log(TAG, "所有明网数据分析完成,写入文件结果:" + writeResult);
                    Log.log(TAG, "明网数据统计分析成功");
                    Log.toAppendFile("明网数据统计分析成功");
                } catch (Exception e) {
                    Log.log(TAG, "统计明网数据出现异常");
                    Log.toAppendFile("统计明网数据出现异常");
                }
            }

            private boolean writeToFile() {
                File file = new File("vid");
//               clearWebEntityList.sor

//                JsonArray jsonArray = new JsonArray();
//                for (int i = 0; i < clearWebEntityList.size(); i++) {
//                    Log.log(TAG, "分析结果:" + clearWebEntityList.get(i).toString());
//                    jsonArray.add(new Gson().toJson(clearWebEntityList.get(i)));
//                }
//                String s = jsonArray.toString();
//                System.out.println("文件byte:" + s.getBytes().length);
//                return FileUtil.writeToFile(file, s, false);
                return true;
            }

            private void handleOenClearWeb(ClearVDSWebSevice clearVDSWebSevice, ClearWebEntity clearWebEntity) {
                int subVid = 0;
                int pageIndex = 1;
                boolean query = true;
                try {
                    while (query) {
                        Call<ResponseBody> contentByVID = clearVDSWebSevice.getContentByVID(clearWebEntity.getVid(), pageIndex);
                        Response<ResponseBody> execute = contentByVID.execute();
                        boolean successful = execute.isSuccessful();
                        if (!successful) {
                            StringBuilder stringBuilder = new StringBuilder();
                            stringBuilder.append("查询失败:").append(clearWebEntity.getVid()).append(",进行下一个查找。错误信息: errorCode=")
                                    .append(execute.code());
                            Log.toAppendFile(stringBuilder.toString());
                            break;
                        }
                        ResponseBody body = execute.body();
                        String string = body.string();
                        VDSBlock vdsBlock = new Gson().fromJson(string, VDSBlock.class);
                        VDSBlock.DataBean data = vdsBlock.getData();
                        if (data == null || vdsBlock.getCode() == 0) {
                            Log.toAppendFile("本周没有可查询数据了,结束" + clearWebEntity.getVid() + "的分析");
                            break;
                        }

                        List<VDSBlock.DataBean.ListBean> list = data.getList();
                        for (int i = 0; i < list.size(); i++) {
                            VDSBlock.DataBean.ListBean listBean = list.get(i);
                            VDSBlock.DataBean.ListBean.TransactionBean transaction = listBean.getTransaction();
                            int height = transaction.getHeight();
                            if (height > Common.getWeekMaxHeight()) {
                                continue;
                            }
                            if (height < Common.getWeekMinHeight()) {
                                query = false;
                                break;
                            }
                            List<VDSBlock.DataBean.ListBean.TransactionBean.OutputBean> output = transaction.getOutput();
                            for (VDSBlock.DataBean.ListBean.TransactionBean.OutputBean outputBean : output) {
                                String address = outputBean.getAddress();
                                String amount = outputBean.getAmount();
                                if (address.equals(clearWebEntity.getVid()) && "4".equals(amount)) {
                                    Log.log(TAG, "==address:" + outputBean.getAddress() + "," + outputBean.getAmount());
                                    subVid++;
                                }
                            }

                        }
                        pageIndex++;
                    }
                } catch (Exception e) {
                    clearWebEntity.setMessage("分析失败");
                    Log.toAppendFile("", e);
                }
                clearWebEntity.setSubVid(subVid);
            }
        }, 20, 200, TimeUnit.SECONDS);

    }


    /**
     * 获取最新高度的任务
     */
    private void startGetCurrentHeightService() {
        scheduledExecutorService.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                try {
                    Log.log(TAG, "开始获取最新的高度信息");
                    Log.toAppendFile("开始获取最新的高度信息");
                    Retrofit retrofit = RetrofitFactory.createRetrofit();
                    ClearVDSWebSevice clearVDSWebSevice = retrofit.create(ClearVDSWebSevice.class);
                    Call<ResponseBody> currentBlockMarket = clearVDSWebSevice.getCurrentBlockMarket();
                    Response<ResponseBody> execute = currentBlockMarket.execute();
                    boolean successful = execute.isSuccessful();
                    if (successful) {
                        Log.log(TAG, "获取最新的高度信息成功");
                        VDSMarket vdsMarket = new VDSMarket();
                        String string = execute.body().string();
                        Log.log(TAG, string);
                        VDSBlockMarket body = new Gson().fromJson(string, VDSBlockMarket.class);
                        VDSBlockMarket.DataBean.BlockBean block = body.getData().getBlock();
                        vdsMarket.setCurrentVdsHeight(block.getHeight());
                        boolean result = FileUtil.writeToFile(new File(Constant.BLOCK_HEIGHT_FILE_NAME), new Gson().toJson(vdsMarket), false);
                        Log.log(TAG, "写入最新的高度信息成功:" + result);
                        Log.toAppendFile("写入区块高度信息结果:" + result);
                    } else {
                        StringBuilder stringBuilder = new StringBuilder();
                        stringBuilder.append("errorCode:").append(execute.code());
                        Log.log(TAG, "获取最新的高度信息失败");
                        Log.toAppendFile("获取最新的高度信息失败 " + stringBuilder.toString());
                    }
                } catch (Exception e) {
                    Log.log(TAG, "获取最新的高度信息出现异常");
                    Log.toAppendFile("获取最新的高度信息出现异常", e);
                }
            }
        }, 0, 20, TimeUnit.SECONDS);

    }
}
