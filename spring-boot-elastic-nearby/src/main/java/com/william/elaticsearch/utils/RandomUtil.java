package com.william.elaticsearch.utils;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import org.omg.CORBA.PRIVATE_MEMBER;

/**
 * 生成随机数
 * @author Tom
 *
 */
public class RandomUtil {

	private static Random random = new Random();
	private static final char [] wxNo = "abcdefghijklmnopqrstuvwxyz0123456789".toLowerCase().toCharArray();
	private static final char [] firstName = "赵钱孙李周吴郑王冯陈卫蒋沈韩杨朱秦许何吕施张孔曹严金魏陶姜谢邹窦章苏潘葛范彭谭夏胡".toCharArray();
	private static final String [] province = {"北京","安徽","福建","甘肃","广东","广西","贵州","海南","河北","河南","黑龙江","湖北",
			"湖南","吉林","江苏","江西","辽宁","内蒙古","宁夏","青海","山东","山西","陕西","上海","四川","天津","西藏","新疆","云南","浙江","重庆"};
	
	//确保车牌号不重复，声明一个缓存区
	private static Set<String> wechatNoCache;
	
	
	/**
	 * 随机生成性别
	 * @return
	 */
	public static short randomSex(){
		return (short) random.nextInt(2);
	}
	
	/**
	 * 随机生成车牌号
	 * @return
	 */
	public static String randomWechatNo(){
		//初始化缓冲区
		openCache();
		
		//微信号自动生成规则，wx_开头加上10位数字字组合
		StringBuffer sb = new StringBuffer();
		for(int c = 0;c < 10;c ++){
			int i = random.nextInt(wxNo.length);
			sb.append(wxNo[i]);
		}
		String carNum = ("wx_" + sb.toString());
		//为了防止微信号重复，生成以后检查一下
		//如果重复，递归重新生成，直到不重复为止
		if(wechatNoCache.contains(carNum)){
			return randomWechatNo();
		}
		wechatNoCache.add(carNum);
		return carNum;
	}
	
	/**
	 * 随机生成坐标
	 */
	public static double[] randomPoint(double myLat,double myLon){
		double min = 0.000001;//坐标范围，最小1米
		double max = 0.00002; //坐标范围，最大1000米
		
		//随机生成一组深圳附近的坐标
		double s = random.nextDouble() % (max - min + 1) + max;
		//格式化保留6位小数
		DecimalFormat df = new DecimalFormat("######0.000000");
		String slon = df.format(s + myLon);
		String slat = df.format(s + myLat);
		Double dlon = Double.valueOf(slon);
		Double dlat = Double.valueOf(slat);
		return new double []{dlat,dlon};
	}
	

	
	/**
	 * 随机生成司机姓名
	 * @return
	 */
	public static String randomNickName(short gender){
		int i = random.nextInt(firstName.length);
		return firstName[i] + (gender==1 ? "帅哥" : "美女");
	}
	
	/**
	 * @Title: randomBirthday
	 * @Description: 随机生成出生
	 * @return
	 */
	public static String randomBirthday() {
		int num = random.nextInt(4000)-2000;		//生成区间[-2000,2000]的数字
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date date = null;
		try {
			date = sdf.parse("1990-01-01");
		} catch (ParseException e) {
			e.printStackTrace();
		}
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.DAY_OF_MONTH, num);
		Date newdate = calendar.getTime();
		calendar.setTime(newdate);
		sdf = new SimpleDateFormat("yyyy-MM-dd");
		return sdf.format(newdate);
	}
	
	/**
	 * @Title: randomProvince
	 * @Description: 生成随机省份
	 * @return
	 */
	public static String randomProvince() {
		return province[random.nextInt(31)];
	}
	
	/**
	 * 开启缓存区
	 */
	public static void openCache(){
		if(wechatNoCache == null){
			wechatNoCache = new HashSet<String>();
		}
	}
	
	/**
	 * 清空缓存区
	 */
	public static void clearCache(){
		wechatNoCache = null;
	}
	
}
