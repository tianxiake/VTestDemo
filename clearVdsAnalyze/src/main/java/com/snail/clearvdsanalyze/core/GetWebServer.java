package com.snail.clearvdsanalyze.core;

import java.util.Random;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * @author yongjie created on 2019-08-10.
 */
public class GetWebServer {

    private String[] urls = new String[]{"http://www.vollar-dream.com", "http://www.vollar-dream.com/icex"
            , "http://www.vollar-dream.com/game", "http://www.vollar-dream.com/whats", "http://www.vollar-dream.com/paper",
            "http://www.vollar-dream.com/source"
    };

    private Executor executor = new ThreadPoolExecutor(100, 300, 60, TimeUnit.SECONDS, new LinkedBlockingDeque<Runnable>(1000), new RejectedExecutionHandler() {
        @Override
        public void rejectedExecution(Runnable runnable, ThreadPoolExecutor threadPoolExecutor) {

        }
    });
    private ConcurrentLinkedQueue<String> linkedList = new ConcurrentLinkedQueue<>();
    private static int productIndex = 0;
    private static int consumerIndex = 0;


    public void start() {
        startProductor();
        startConsumer();
        startCheck();
    }

    private void startCheck() {
        new Thread() {
            @Override
            public void run() {
                super.run();
                while (true) {
                    System.out.println("productIndex:" + productIndex + ",consumerIndex:" + consumerIndex);
                    try {
                        sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();
    }

    private void startConsumer() {
        new Thread() {
            @Override
            public void run() {
                Retrofit retrofit = new Retrofit.Builder().addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                        .addConverterFactory(GsonConverterFactory.create())
                        .baseUrl("https://vdsblock.io/")
                        .build();
                final GetWebService getWebService = retrofit.create(GetWebService.class);


                super.run();
                while (true) {
                    try {
                        if (linkedList.isEmpty()) {
                            try {
                                Thread.sleep(1000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            continue;
                        }
                        executor.execute(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    String poll = linkedList.poll();
                                    Call<ResponseBody> web = getWebService.getWeb(poll);
                                    Response<ResponseBody> execute = web.execute();
                                    ResponseBody body = execute.body();
                                    String string = body.string();
                                } catch (Exception e) {
                                    System.err.println(e.getClass().getSimpleName());
                                }
                                consumerIndex++;
                            }
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();
    }

    private void startProductor() {
        new Thread() {
            Random random = new Random();

            @Override
            public void run() {
                super.run();
                while (true) {
                    if (linkedList.size() > 10000) {
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    linkedList.add(urls[random.nextInt(urls.length)]);
                    productIndex++;
                }
            }
        }.start();
    }
}
