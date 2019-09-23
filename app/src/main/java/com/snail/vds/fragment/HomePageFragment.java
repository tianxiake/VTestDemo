package com.snail.vds.fragment;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.snail.vds.R;
import com.snail.vds.util.HiLogger;
import com.snail.vds.util.ToastUtil;

import java.util.Arrays;

/**
 * @author yongjie created on 2019-08-06.
 */
public class HomePageFragment extends Fragment {

    private static final String TAG = "HomePageFragment";
    public static final int READ_REQUEST_CODE = 10;
    public static final int WRITE_REQUEST_CODE = 20;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(container.getContext()).inflate(R.layout.layout_home_page_fragment, null);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        HiLogger.i(TAG, "onResume request permission");
        int readPermission = ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE);
        if (readPermission != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, READ_REQUEST_CODE);
        }
        int writePermission = ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (writePermission != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, WRITE_REQUEST_CODE);
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
                    ToastUtil.showToast(getContext(), "请授予读权限,否则无法使用app");
                }
            }

        } else if (requestCode == WRITE_REQUEST_CODE) {
            if (grantResults.length > 0) {
                int grantResult = grantResults[0];
                if (grantResult != PackageManager.PERMISSION_GRANTED) {
                    ToastUtil.showToast(getContext(), "请授予写权限,否则无法使用app");
                }
            }
        }
    }
}

