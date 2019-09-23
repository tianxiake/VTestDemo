package com.snail.vds;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.snail.vds.adapter.VDSAdapter;
import com.snail.vds.entity.ClearWebEntity;
import com.snail.vds.intef.AuthListener;
import com.snail.vds.util.HiLogger;
import com.snail.vds.util.ToastUtil;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

	private static final String TAG = "MainActivity";
	private static final int READ_REQUEST_CODE = 10;
	private static final int WRITE_REQUEST_CODE = 12;
	private static final int PHONE_STATE = 13;
	@BindView(R.id.tv_current_week)
	TextView tvCurrentWeek;
	@BindView(R.id.rv_content)
	RecyclerView rvContent;
	@BindView(R.id.fl_home_page)
	FrameLayout flHomePage;
	@BindView(R.id.btn_query)
	Button btnQuery;
	@BindView(R.id.btn_reinput)
	Button btnReinput;
	@BindView(R.id.ll_bottom)
	LinearLayout llBottom;
	private List<ClearWebEntity> clearWebEntities = new ArrayList<>();
	private static int weekNumber = -1;
	private List<Boolean> booleans = new ArrayList<>();
	private VDSAdapter vdsAdapter;
	private long analyseTime = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		ButterKnife.bind(this);
		clearWebEntities = readClearWeb();
		//权限检查
		handlePermission();
		rvContent.setLayoutManager(new LinearLayoutManager(MainActivity.this, LinearLayoutManager.VERTICAL, false));
		vdsAdapter = new VDSAdapter();
		rvContent.setAdapter(vdsAdapter);
	}

	private void checkAuth(AuthListener authListener) {

	}

	@OnClick({R.id.btn_query, R.id.btn_reinput})
	public void onClick(View view) {
		int id = view.getId();
		switch (id) {
			case R.id.btn_query:
				if (weekNumber == -1) {
					ToastUtil.showToast(this, "请输入赛季周");
					return;
				}
				setWeekText(weekNumber);
				startAnlayise(weekNumber);
				break;
			case R.id.btn_reinput:
				showInputDialog();
				break;
			default:
				break;
		}

	}

	private void handlePermission() {
		int readPermission = ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
		if (readPermission != PackageManager.PERMISSION_GRANTED) {
			ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, READ_REQUEST_CODE);
		} else {
			HiLogger.i(TAG, "readPermission grant");
			booleans.add(true);
		}
		int writePermission = ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
		if (writePermission != PackageManager.PERMISSION_GRANTED) {
			ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, WRITE_REQUEST_CODE);
		} else {
			HiLogger.i(TAG, "writePermission grant");
			booleans.add(true);
		}

		int phoneState = ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE);
		if (phoneState != PackageManager.PERMISSION_GRANTED) {
			ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE}, PHONE_STATE);
		} else {
			HiLogger.i(TAG, "phoneState grant");
			booleans.add(true);
		}
		if (booleans.size() >= 3) {
			//请求认证信息
			requestAuth();
		}
	}

	@Override
	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
		super.onRequestPermissionsResult(requestCode, permissions, grantResults);
		HiLogger.i(TAG, "onRequestPermissionsResult: requestCode %s, permissions:%s grantResults:%s", requestCode, Arrays.toString(permissions), Arrays.toString(grantResults));
		if (requestCode == READ_REQUEST_CODE) {
			if (grantResults.length > 0) {
				int grantResult = grantResults[0];
				if (grantResult != PackageManager.PERMISSION_GRANTED) {
					ToastUtil.showToast(this, "请授予读权限,否则无法使用app");
				} else {
					booleans.add(true);
				}
			}

		} else if (requestCode == WRITE_REQUEST_CODE) {
			if (grantResults.length > 0) {
				int grantResult = grantResults[0];
				if (grantResult != PackageManager.PERMISSION_GRANTED) {
					ToastUtil.showToast(this, "请授予写权限,否则无法使用app");
				} else {
					booleans.add(true);
				}
			}
		} else if (requestCode == PHONE_STATE) {
			if (grantResults.length > 0) {
				int grantResult = grantResults[0];
				if (grantResult != PackageManager.PERMISSION_GRANTED) {
					ToastUtil.showToast(this, "请授予读写联系人权限,否则无法使用app");
				} else {
					booleans.add(true);
				}
			}
		}
		if (booleans.size() >= 3) {
			//请求认证信息
			requestAuth();
		}
	}


	private void requestAuth() {
		HiLogger.i(TAG, "requestAuth");
		flHomePage.setVisibility(View.GONE);
		if (clearWebEntities.isEmpty()) {
			ToastUtil.showToast(this, "缺少配置文件");
		} else {
			HiLogger.i(TAG, "clearWebEntities size:" + clearWebEntities.size());
			showInputDialog();
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	private void showInputDialog() {
		HiLogger.i(TAG, "showInputDialog");
		View inflate = LayoutInflater.from(this).inflate(R.layout.layout_dialog, null);
		EditText editText = inflate.findViewById(R.id.et_input);
		AlertDialog alertDialog = new AlertDialog.Builder(this).setPositiveButton("确定", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				String content = editText.getText().toString();
				if (TextUtils.isEmpty(content)) {
					ToastUtil.showToast(MainActivity.this, "赛季周信息输入有误");
				} else {
					int number = Integer.parseInt(content.trim());
					weekNumber = number;
					setWeekText(weekNumber);
					dialog.dismiss();
				}
			}
		}).setTitle("请输入赛季周").setCancelable(false).setView(inflate).create();
		alertDialog.show();
	}

	private void setWeekText(int weekNumber) {
		StringBuilder builder = new StringBuilder();
		builder.append("第").append(weekNumber).append("周")
				.append("(")
				.append(weekNumber * 10080)
				.append("-")
				.append((weekNumber + 1) * 10080 - 1)
				.append(")");
		tvCurrentWeek.setText(builder.toString());
	}

	private void startAnlayise(int number) {
		analyseTime = System.currentTimeMillis();
		btnQuery.setEnabled(false);
		btnReinput.setEnabled(false);
		btnQuery.setText("正在查询中,请稍后...");
		VdsService vdsService = new VdsService(clearWebEntities, number);
		vdsService.setOnGetListener(new VdsService.OnGetListener() {
			@Override
			public void onGetError(String errorMessage, Throwable throwable) {
				btnQuery.setEnabled(false);
				btnReinput.setEnabled(false);
				btnQuery.setText("查询");
				long timeSencond = (System.currentTimeMillis() - analyseTime) / 1000;
				String s = tvCurrentWeek.getText().toString() + "分析耗时:" + timeSencond + "秒";
				tvCurrentWeek.setText(s);
				HiLogger.w(TAG, "errorMessage:%s throwable:%s", errorMessage, throwable);
				ToastUtil.showToast(MainActivity.this, "查询失败,请稍后再试");
			}

			@Override
			public void onGetSuccess(List<ClearWebEntity> clearWebEntities) {
				ToastUtil.showToast(MainActivity.this, "分析完成");
				long timeSencond = (System.currentTimeMillis() - analyseTime) / 1000;
				String s = tvCurrentWeek.getText().toString() + "耗时:" + timeSencond + "秒";
				tvCurrentWeek.setText(s);
				btnQuery.setEnabled(true);
				btnReinput.setEnabled(true);
				btnQuery.setText("查询");
				refreshList(clearWebEntities);
			}
		});
		vdsService.start();
	}

	private void refreshList(List<ClearWebEntity> clearWebEntities) {
		Observable.just(clearWebEntities).subscribeOn(Schedulers.io())
				.doOnNext(new Consumer<List<ClearWebEntity>>() {
					@Override
					public void accept(List<ClearWebEntity> clearWebEntities) throws Exception {
						List<ClearWebEntity> errorList = new ArrayList<>();
						for (int i = 0; i < clearWebEntities.size(); i++) {
							ClearWebEntity clearWebEntity = clearWebEntities.get(i);
							if (clearWebEntity.isError()) {
								errorList.add(clearWebEntity);
							}
						}
						for (int i = 0; i < errorList.size(); i++) {
							clearWebEntities.remove(errorList.get(i));
						}
						//排序
						Collections.sort(clearWebEntities, new Comparator<ClearWebEntity>() {
							@Override
							public int compare(ClearWebEntity o1, ClearWebEntity o2) {
								if (o1.getSubVid() < o2.getSubVid()) {
									return -1;
								} else if (o1.getSubVid() > o2.getSubVid()) {
									return 1;
								}
								return 0;
							}
						});
						Collections.reverse(clearWebEntities);
						clearWebEntities.addAll(0, errorList);
					}
				}).observeOn(AndroidSchedulers.mainThread())
				.subscribe(new Consumer<List<ClearWebEntity>>() {
					@Override
					public void accept(List<ClearWebEntity> clearWebEntities) throws Exception {
						vdsAdapter.resetAllData(clearWebEntities);
					}
				});
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			Intent home = new Intent(Intent.ACTION_MAIN);
			home.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			home.addCategory(Intent.CATEGORY_HOME);
			startActivity(home);
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	private List<ClearWebEntity> readClearWeb() {
		AssetManager assets = this.getAssets();
		try {
			List<ClearWebEntity> clearWebEntities = new ArrayList<>();
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(assets.open("clearWebAddress")));
			String lineContent = "";
			while ((lineContent = bufferedReader.readLine()) != null) {
				HiLogger.i(TAG, "content:%s", lineContent);
				String[] split = lineContent.split(",");
				ClearWebEntity clearWebEntity = new ClearWebEntity();
				if (split[0].contains("vdsvds.online") || split[0].contains("vdsm.top")) {
					clearWebEntity.setSpecial(true);
				}
				clearWebEntity.setWebUrl(split[0]);
				clearWebEntity.setVid(split[1]);
				clearWebEntities.add(clearWebEntity);
			}
			bufferedReader.close();
			return clearWebEntities;
		} catch (Exception e) {
			HiLogger.e(TAG, "配置文件读取错误", e);
		} finally {

		}
		return new ArrayList<>();

	}


}
