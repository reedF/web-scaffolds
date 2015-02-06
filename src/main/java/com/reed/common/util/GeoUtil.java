package com.reed.common.util;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * 经纬度工具类
 * 
 * 
 */
public class GeoUtil {
	public static double X_PI = 3.14159265358979324 * 3000.0 / 180.0;

	/**
	 * 坐标系
	 * 
	 * @author Luke
	 * 
	 */
	public enum GaussSphere {
		Beijing54, Xian80, WGS84,
	}

	private static double Rad(double d) {
		return d * Math.PI / 180.0;
	}

	/**
	 * 根据两个点的经纬度和坐标系计算距离
	 * 
	 * @param lng1
	 *            第一个点的经度
	 * @param lat1
	 *            第一个点的纬度
	 * @param lng2
	 *            第二个点的经度
	 * @param lat2
	 *            第二个点的纬度
	 * @param gs
	 *            坐标系
	 * @return
	 */
	public static double DistanceOfTwoPoints(double lng1, double lat1,
			double lng2, double lat2, GaussSphere gs) {
		double radLat1 = Rad(lat1);
		double radLat2 = Rad(lat2);
		double a = radLat1 - radLat2;
		double b = Rad(lng1) - Rad(lng2);
		double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2)
				+ Math.cos(radLat1) * Math.cos(radLat2)
				* Math.pow(Math.sin(b / 2), 2)));
		s = s
				* (gs == GaussSphere.WGS84 ? 6378137.0
						: (gs == GaussSphere.Xian80 ? 6378140.0 : 6378245.0));
		s = Math.round(s * 10000) / 10000;
		return s;
	}

	/**
	 * 根据纬度， 将距离转换为经度差, 保留五位精度
	 * 
	 * @param latitude
	 *            纬度
	 * @param distance
	 *            经度距离
	 * @return
	 */
	public static double distanceToLongitude(double latitude, double distance) {
		return Math.round(distance
				/ (Math.cos(Math.PI * latitude / 180.0) * 111000) * 100000) / 100000.00D;
	}

	/**
	 * 将距离转化为纬度差, 保留五位精度
	 * 
	 * @param distance
	 *            纬度距离
	 * @return
	 */
	public static double distanceToLatitude(double distance) {
		return Math.round(distance / 111000 * 100000) / 100000.00D;
	}

	/**
	 * 火星坐标系 (GCJ-02) 转百度坐标系 (BD-09) 高德地图使用火星坐标系
	 * 
	 * @param gg_lat
	 *            纬度
	 * @param gg_lon
	 *            经度
	 * @return 0位置为纬度，1位置为经度
	 */
	public static double[] baiduEncrypt(double gg_lat, double gg_lon)
			throws Exception {
		// if(gg_lat==null || gg_lon==null){
		// throw new Exception("纬度或经度为空");
		// }
		double x = gg_lon, y = gg_lat;
		double z = Math.sqrt(x * x + y * y) + 0.00002 * Math.sin(y * X_PI);
		double theta = Math.atan2(y, x) + 0.000003 * Math.cos(x * X_PI);
		double bd_lon = z * Math.cos(theta) + 0.0065;
		double bd_lat = z * Math.sin(theta) + 0.006;
		double[] baiduCoodinate = new double[2];
		baiduCoodinate[0] = bd_lat;
		baiduCoodinate[1] = bd_lon;
		// baiduCoodinate.add(0, BigDecimal.valueOf(bd_lat));
		// baiduCoodinate.add(1, BigDecimal.valueOf(bd_lon));
		return baiduCoodinate;
	}

	/**
	 * 百度坐标系 (BD-09) 转火星坐标系 (GCJ-02)
	 * 
	 * @param bd_lat
	 *            纬度
	 * @param bd_lon
	 *            经度
	 * @return
	 */
	public static double[] baiduDecrypt(double bd_lat, double bd_lon) {
		double x = bd_lon - 0.0065, y = bd_lat - 0.006;
		double z = Math.sqrt(x * x + y * y) - 0.00002 * Math.sin(y * X_PI);
		double theta = Math.atan2(y, x) - 0.000003 * Math.cos(x * X_PI);
		double gg_lon = z * Math.cos(theta);
		double gg_lat = z * Math.sin(theta);
		double[] marsCoodinate = new double[2];
		marsCoodinate[0] = gg_lat;
		marsCoodinate[1] = gg_lon;
		return marsCoodinate;
	}

	public static void main(String[] args) throws Exception {

		// System.out.println(DistanceOfTwoPoints(116.45475, 40.000869,
		// 127.86436,
		// 41.584709, GaussSphere.Beijing54));
		Double lat1 = 34.264648;
		Double lon1 = 108.952736;

		Double lat2 = 34.264648;
		Double lon2 = 108.953736;

		int radius = 1000;
		double dis = DistanceOfTwoPoints(lat1, lon1, lat2, lon2,
				GaussSphere.Beijing54);
		double[] bd = baiduEncrypt(lat1.doubleValue(), lon1.doubleValue());
		System.out.println(bd[0] + "  " + bd[1]);
		System.out.print(radius >= dis);
	}

	/**
	 * 限制地理坐标精度在6位以内
	 * 
	 * @param coo
	 *            坐标值
	 * @return
	 */
	public static BigDecimal coordinatePrecisionLimit(BigDecimal coo) {
		if (coo != null && coo.precision() > 6) {
			return coo.setScale(6, BigDecimal.ROUND_HALF_UP);
		} else {
			return coo;
		}
	}

	/**
	 * 判断两个点的距离是否小于半径 即判断一个点是否在以另一个点为圆心，radius为半径的圆中 做商圈验证用
	 * 
	 * @param lng1
	 * @param lat1
	 * @param lng2
	 * @param lat2
	 * @param radius
	 *            半径
	 * @return
	 */
	public static Boolean isInRadius(double lng1, double lat1, double lng2,
			double lat2, double radius) {
		double dis = DistanceOfTwoPoints(lng1, lat1, lng2, lat2,
				GaussSphere.Beijing54);
		return radius >= dis;
	}
}
