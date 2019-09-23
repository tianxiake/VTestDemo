package com.snail.vds;

import com.google.gson.Gson;
import com.snail.vds.entity.ClearWebEntity;
import com.snail.vds.entity.VDSBlock;
import com.snail.vds.intef.ClearVDSWebSevice;
import com.snail.vds.util.HiLogger;
import com.snail.vds.util.MainHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
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
	BlockingQueue workQueue;
	private ThreadPoolExecutor threadPoolExecutor;
	private List<ClearWebEntity> sourceList = new ArrayList<>();
	private List<ClearWebEntity> resultList = new ArrayList<>();
	private int number;
	private int weekStart;
	private int weekEnd;
	private OnGetListener onGetListener;

	public VdsService(List<ClearWebEntity> sourceList, int number) {
		copyData(sourceList);
		workQueue = new LinkedBlockingQueue<>(1000);
		int availableProcessors = Runtime.getRuntime().availableProcessors();
		HiLogger.i(TAG, "availableProcessors:" + availableProcessors);
		threadPoolExecutor = new ThreadPoolExecutor(availableProcessors,
				availableProcessors + 2,
				60, TimeUnit.SECONDS, workQueue);
		this.number = number;
		weekStart = number * 10080;
		weekEnd = (number + 1) * 10080 - 1;
	}

	public void setOnGetListener(OnGetListener onGetListener) {
		this.onGetListener = onGetListener;
	}

	public void start() {
		if (number < 0 || this.sourceList.isEmpty()) {
			HiLogger.i(TAG, "number:%s list size:%s", number, sourceList.size());
			if (onGetListener != null) {
				onGetListener.onGetError("数据传入有误", null);
			}
			return;
		}
		startClearWeb();
	}

	private void startClearWeb() {
		Retrofit retrofit = RetrofitFactory.getInstance();
		ClearVDSWebSevice clearVDSWebSevice = retrofit.create(ClearVDSWebSevice.class);
		for (int i = 0; i < sourceList.size(); i++) {
			final int temp = i + 1;
			final ClearWebEntity clearWebEntity = sourceList.get(i);
			threadPoolExecutor.execute(new Runnable() {
				@Override
				public void run() {
					HiLogger.i(TAG, "开始处理第%s个网站%s", temp, clearWebEntity.getWebUrl());
					handleOenClearWeb(temp, clearVDSWebSevice, clearWebEntity);
					HiLogger.i(TAG, "结束处理第%s个网站%s", temp, clearWebEntity.getWebUrl());
					addList(clearWebEntity);
				}
			});
		}
	}


	private synchronized void addList(ClearWebEntity clearWebEntity) {
		resultList.add(clearWebEntity);
		if (resultList.size() >= sourceList.size()) {
			HiLogger.i(TAG, "分析完成");
			if (onGetListener != null) {
				MainHandler.getInstance().post(new Runnable() {
					@Override
					public void run() {
						onGetListener.onGetSuccess(resultList);
					}
				});
			}
		}
	}

	private void handleOenClearWeb(int index, ClearVDSWebSevice clearVDSWebSevice, ClearWebEntity clearWebEntity) {
		int subVid = 0;
		int pageIndex = 1;
		boolean query = true;
		try {
			while (query) {
				Call<ResponseBody> contentByVID = clearVDSWebSevice.getContentByVID(clearWebEntity.getVid(), pageIndex);
				Response<ResponseBody> execute = contentByVID.execute();
				boolean successful = execute.isSuccessful();
				if (!successful) {
					break;
				}
				ResponseBody body = execute.body();
				String string = body.string();
				VDSBlock vdsBlock = new Gson().fromJson(string, VDSBlock.class);
				VDSBlock.DataBean data = vdsBlock.getData();
				if (data == null || vdsBlock.getCode() == 0) {
					break;
				}
				List<VDSBlock.DataBean.ListBean> list = data.getList();
				for (int i = 0; i < list.size(); i++) {
					VDSBlock.DataBean.ListBean listBean = list.get(i);
					VDSBlock.DataBean.ListBean.TransactionBean transaction = listBean.getTransaction();
					int height = transaction.getHeight();
					if (height > weekEnd) {
						continue;
					}
					if (height < weekStart) {
						query = false;
						break;
					}
					List<VDSBlock.DataBean.ListBean.TransactionBean.OutputBean> output = transaction.getOutput();
					for (VDSBlock.DataBean.ListBean.TransactionBean.OutputBean outputBean : output) {
						String address = outputBean.getAddress();
						String amount = outputBean.getAmount();
						if (address.equals(clearWebEntity.getVid()) && "4".equals(amount)) {
							subVid++;
							clearWebEntity.setNewHeight(height);
						}
					}

				}
				pageIndex++;
			}
			clearWebEntity.setSubVid(subVid);
		} catch (Exception e) {
			clearWebEntity.setError(true);
			clearWebEntity.setSubVid(subVid);
			clearWebEntity.setMessage("分析失败:" + e.getClass().getSimpleName());
			HiLogger.e(TAG, "第" + index + "网站分析发生错误:" + clearWebEntity.getWebUrl(), e);
		}

	}

	private ClearWebEntity cloneWebEntity(ClearWebEntity sourceWebEntity) {
		ClearWebEntity clearWebEntity = new ClearWebEntity();
		clearWebEntity.setNewHeight(-1);
		clearWebEntity.setError(false);
		clearWebEntity.setMessage("");
		clearWebEntity.setSpecial(sourceWebEntity.isSpecial());
		clearWebEntity.setSubVid(0);
		clearWebEntity.setWebUrl(sourceWebEntity.getWebUrl());
		clearWebEntity.setVid(sourceWebEntity.getVid());
		return clearWebEntity;
	}

	private void copyData(List<ClearWebEntity> sourceList) {
		for (int i = 0; i < sourceList.size(); i++) {
			ClearWebEntity resultWebEntity = cloneWebEntity(sourceList.get(i));
			this.sourceList.add(resultWebEntity);
		}
	}


	public interface OnGetListener {
		void onGetError(String errorMessage, Throwable throwable);

		void onGetSuccess(List<ClearWebEntity> clearWebEntities);
	}
}
