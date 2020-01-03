package com.ctsaing.mapdemo;

import android.app.Application;

import com.baidu.mapapi.CoordType;
import com.baidu.mapapi.SDKInitializer;

public class MapApplication extends Application {

	//权限
	public static final String INTERNET_PERMISSION = "android.permission.INTERNET";
	public static final String NETWORK_STATE_PERMISSION = "android.permission.ACCESS_NETWORK_STATE";
	public static final String WIFI_STATE_PERMISSION = "android.permission.ACCESS_WIFI_STATE";
	public static final String COARSE_LOCATION_PERMISSION = "android.permission.ACCESS_COARSE_LOCATION";
	public static final String FINE_LOCATION_PERMISSION = "android.permission.ACCESS_FINE_LOCATION";

	public static String[] permissions = new String[]{INTERNET_PERMISSION, NETWORK_STATE_PERMISSION, WIFI_STATE_PERMISSION, COARSE_LOCATION_PERMISSION, FINE_LOCATION_PERMISSION};

	@Override
	public void onCreate() {
		super.onCreate();
		//在使用sdk各组件之前初始化context信息
		SDKInitializer.initialize(this);

		//自4.3.0起，百度地图sdk所有接口均支持百度坐标和国测局坐标，用此方法设置需要的坐标类型
		//包括BD09LL和GCJ02,默认是BD09LL
		SDKInitializer.setCoordType(CoordType.BD09LL);
	}
}
