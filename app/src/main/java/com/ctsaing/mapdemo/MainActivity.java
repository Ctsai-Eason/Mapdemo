package com.ctsaing.mapdemo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.os.Bundle;
import android.widget.Toast;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMapOptions;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationData;

/**
 * 重点要管理mapView的生命周期
 */
public class MainActivity extends AppCompatActivity {


	private MapView mapView;
	private BaiduMap mBaiduMap;
	private LocationClient mLocationClient;

	private MyLocationListener myLocationListener;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		getPermissions(MapApplication.permissions);
		mapView = findViewById(R.id.main_map);
		mBaiduMap = mapView.getMap();
		initMap();
	}

	private void getPermissions(String[] permissions) {
		ActivityCompat.requestPermissions(this, permissions, 1);
	}

	//初始化Map
	private void initMap() {
		//开启定位图层
		mBaiduMap.setMyLocationEnabled(true);
		initLocation();
	}

	//初始化起始位置
	private void initLocation() {
		//定位初始化
		myLocationListener = new MyLocationListener();
		mLocationClient = new LocationClient(this);
		LocationClientOption locationClientOption = new LocationClientOption();
		//设置
		locationClientOption.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
		locationClientOption.setCoorType("BD09LL");
		locationClientOption.setOpenGps(true);
		locationClientOption.setScanSpan(1000);
		mLocationClient.setLocOption(locationClientOption);

		mLocationClient.registerLocationListener(myLocationListener);
		mLocationClient.start();
	}

	//切换至卫星地图
	private void checkToSatelliteMap() {
		mBaiduMap.setMapType(BaiduMap.MAP_TYPE_SATELLITE);
	}

	//切换至正常地图
	private void checkToNormalMap() {
		mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
	}

	//切换到公交路况地图
	private void checkToTrafficMap() {
		//支持公交路况
		mBaiduMap.setTrafficEnabled(true);
		//设置路况的颜色区分
		//按顺序分别是严重拥堵，拥堵，缓行，畅通
		mBaiduMap.setCustomTrafficColor("#ffba0101", "#fff33131", "#ffff9e19", "#00000000");
		//对地图状态进行更新，否则可能不能渲染，造成样式定义无法立即生效
		MapStatusUpdate update = MapStatusUpdateFactory.zoomBy(13);
		mBaiduMap.animateMapStatus(update);
	}

	//切换到热力图
	private void checkToHeatMap() {
		mBaiduMap.setBaiduHeatMapEnabled(true);
	}

	@Override
	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
		super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//		if (requestCode == 1) {
//			for (int i : grantResults) {
//				if (i != RESULT_OK) {
//					Toast.makeText(MainActivity.this, "需要获取指定的权限才能够正常使用!", Toast.LENGTH_SHORT).show();
//				}
//			}
//			initLocation();
//		}

	}

	@Override
	protected void onResume() {
		super.onResume();
		mapView.onResume();
	}

	@Override
	protected void onPause() {
		super.onPause();
		mapView.onPause();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		mapView.onDestroy();
	}

	/**
	 * 继承抽象类BDAbstractLocationListener并重写方法获取定位数据并回传给mapView
	 */
	public class MyLocationListener extends BDAbstractLocationListener{

		@Override
		public void onReceiveLocation(BDLocation bdLocation) {

			//mapView销毁之后不在处理新接收的位置
			if (bdLocation == null || mapView == null){
				return;
			}

			MyLocationData myLocationData = new MyLocationData.Builder()
					.accuracy(bdLocation.getRadius())
					//获取位置的方向和经纬度，顺时针0-360
					.direction(bdLocation.getDirection())
					.latitude(bdLocation.getLatitude())
					.longitude(bdLocation.getLongitude())
					.build();
			mBaiduMap.setMyLocationData(myLocationData);
		}
	}
}
