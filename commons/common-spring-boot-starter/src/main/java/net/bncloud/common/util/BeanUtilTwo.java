package net.bncloud.common.util;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
//import sun.misc.BASE64Encoder;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 实体相关常用方法
 * @author dr
 */
public class BeanUtilTwo {
	static Pattern p = Pattern
			.compile("^(0)?(13)[0-9][0-9]{8}$|^(0)?(14)[5,7][0-9]{8}$|^(0)?(15)[0-3][0-9]{8}$|^(0)?(15)[5-9][0-9]{8}$|^(0)?(17)[6-8][0-9]{8}$|^(0)?(18)[0-9][0-9]{8}$");
	private static final String EMAIL_REGEX = "^\\s*?(.+)@(.+?)\\s*$";
    private static final Pattern EMAIL_PATTERN = Pattern.compile(EMAIL_REGEX);

	/**
	 * 判断参数是否为空或Undefined
	 */
	public static boolean isAnyUndefined(String... str) {
		boolean flag = false;
		for (String s : str) {
			if (org.apache.commons.lang3.StringUtils.isBlank(s) || "undefined".equalsIgnoreCase(s)) {
				flag = true;
				break;
			}
		}
		return flag;
	}

	/**
	 * 是否为空
	 * @param obj
	 * @return
	 */
	public static boolean isEmpty(Object obj) {
		if (obj == null)
			return true;
		// 字符串
		if (obj instanceof String) {
			String str = (String) obj;
			if (str == null || str.equals("")
					|| str.equalsIgnoreCase("undefined"))
				return true;
			return false;
		}
		// int
		if (obj instanceof Integer) {
			// Integer i=(Integer)obj;
			// if(i==null)return true;
			return false;
		}
		// List
		if (obj instanceof List<?>) {
			List<?> l = (List<?>) obj;
			if (l == null || l.size() == 0)
				return true;
			return false;
		}
		// 未知类型
		return false;
	}

	//是否为非空
	public static boolean isNotEmpty(Object obj) {
		return (!isEmpty(obj));
	}

	public static List<String> asList(String values) {
		String[] arr = values.split(",");
		List<String> list = Arrays.asList(arr);
		return list;
	}

	/**
	 * list转为逗号隔开的String
	 */
	public static String listToString(List<String> stringList) {
		if (stringList == null) {
			return null;
		}
		StringBuilder result = new StringBuilder();
		boolean flag = false;
		for (Object string : stringList) {
			if (flag) {
				result.append(",");
			} else {
				flag = true;
			}
			result.append(string);
		}
		return result.toString();
	}

	/**
	 * 检查对象中是否含有非空值的属性
	 * @param obj 对象
	 * @param exclude 需要排除的属性
	 * @return
	 */
	public static boolean containsNonNullValues(Object obj, String... exclude) {
		try {
			// 得到类对象
			Class<?> c = (Class<?>) obj.getClass();
			// 得到类中的所有属性集合
			Field[] fs = c.getDeclaredFields();
			for (Field f : fs) {
				// 设置些属性是可以访问的
				f.setAccessible(true);
				// 得到此属性的值
				Object val = f.get(obj);
				// 是否有需要排除某些属性值，如果有，则判断当前属性是否在其中
				boolean state = false;
				if (exclude.length > 0)
					state = exclude[0].contains(f.getName());
				if (!state && val != null && !"".equals(val))
					return true;
			}
			// 父类
			Class<?> superClass = c.getSuperclass();
			fs = superClass.getDeclaredFields();
			for (Field f : fs) {
				// 设置些属性是可以访问的
				f.setAccessible(true);
				// 得到此属性的值
				Object val = f.get(obj);
				// 是否有需要排除某些属性值，如果有，则判断当前属性是否在其中
				boolean state = false;
				if (exclude.length > 0)
					state = exclude[0].contains(f.getName());
				if (!state && val != null && !"".equals(val))
					return true;
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * 检查对象中是否含有空值的属性
	 * @param obj 对象
	 * @param exclude 需要排除的属性
	 * @return
	 */
	public static boolean containsNullValues(Object obj, String... exclude) {
		try {
			// 得到类对象
			Class<?> c = (Class<?>) obj.getClass();
			// 得到类中的所有属性集合
			Field[] fs = c.getDeclaredFields();
			for (Field f : fs) {
				// 设置些属性是可以访问的
				f.setAccessible(true);
				// 得到此属性的值
				Object val = f.get(obj);
				// 是否有需要排除某些属性值，如果有，则判断当前属性是否在其中
				boolean state = false;
				if (exclude.length > 0)
					state = exclude[0].contains(f.getName());
				if (!state && (val == null || "".equals(val)))
					return true;
				return true;
			}
			// 父类
			Class<?> superClass = c.getSuperclass();
			fs = superClass.getDeclaredFields();
			for (Field f : fs) {
				// 设置些属性是可以访问的
				f.setAccessible(true);
				// 得到此属性的值
				Object val = f.get(obj);
				// 是否有需要排除某些属性值，如果有，则判断当前属性是否在其中
				boolean state = false;
				if (exclude.length > 0)
					state = exclude[0].contains(f.getName());
				if (!state && (val == null || "".equals(val)))
					return true;
				return true;
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * 生成随机数
	 * @param length 随机数长度
	 * @return
	 */
	public static String randomString(int length) {
		Random random = new Random();
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < length; ++i) {
			int number = random.nextInt(3);
			long result = 0;
			switch (number) {
			case 0:
				result = Math.round(Math.random() * 25 + 65);
				sb.append(String.valueOf((char) result));
				break;
			case 1:
				result = Math.round(Math.random() * 25 + 97);
				sb.append(String.valueOf((char) result));
				break;
			case 2:
				sb.append(String.valueOf(new Random().nextInt(10)));
				break;
			}
		}
		return sb.toString();
	}

	/**
	 * 获取N年前的今天-时间戳
	 * @param year
	 * @return
	 */
	public static Integer getTime(Integer year) {
		Integer re_time = null;
		Calendar cd = Calendar.getInstance();
		cd.add(Calendar.YEAR, -year);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date dat = cd.getTime();
		String user_time = sdf.format(dat);
		Date d;
		try {
			d = sdf.parse(user_time);
			String str = String.valueOf(d.getTime() / 1000L);
			re_time = Integer.parseInt(str);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return re_time;
	}

	/**
	 * 过滤掉特殊字符
	 * @param str
	 * @return
	 */
	public static String StringFilter(String str){
		//清除掉所有特殊字符 只保留中文，数字，字母
		str=str.replaceAll("[^(a-zA-Z0-9\\u4e00-\\u9fa5)]","").trim();
		return str;
	}

	/**
	 * 验证手机号码
	 * @param mobiles
	 * @return [0-9]{5,9}
	 */
	public static boolean isMobile(String mobiles) {
		boolean flag = false;
		try {
			Matcher m = p.matcher(mobiles);
			flag = m.matches();
		} catch (Exception e) {
			flag = false;
		}
		return flag;
	}

	/**
	 * 判断邮箱是否合法
	 * @param email
	 * @return
	 */
	public static boolean isEmail(String email) {
        if (email == null) {
            return false;
        }
        if (email.endsWith(".")) { // check this first - it's cheap!
            return false;
        }
        // Check the whole email address structure
        Matcher emailMatcher = EMAIL_PATTERN.matcher(email);
        if (!emailMatcher.matches()) {
            return false;
        }
        return true;
    }

	/**
	 * 将内容用符号连接
	 * @param ids 连接后的内容
	 * @param id 待连接的内容
	 * @param mark 连接符号
	 * @return
	 */
	public static String mergeValue(String ids, String id,String mark) {
		if (isEmpty(id)) {
			return ids;
		}
		if (isEmpty(ids)) {
			ids = id;
		} else {
			if (isEmpty(mark)) {
				ids = ids + id;
			} else {
				ids = ids + mark + id;
			}
		}
		return ids;
	}

	public static String getClientIp(HttpServletRequest request) {
		/*if(request == null)
			return "";

		String ip = request.getHeader("X-Forwarded-For");
		if (isNotEmpty(ip) && !"unKnown".equalsIgnoreCase(ip)) {
			// 多次反向代理后会有多个ip值，第一个ip才是真实ip
			int index = ip.indexOf(",");
			if (index != -1) {
				return ip.substring(0, index);
			} else {
				return ip;
			}
		}
		ip = request.getHeader("X-Real-IP");
		if (isNotEmpty(ip) && !"unKnown".equalsIgnoreCase(ip)) {
			return ip;
		}*/
		return getFullIp(request);
	}

	/**
	 * 取完整ip，包含伪装ip,真实ip,代理ip
	 * @param request
	 * @return
	 */
	public static String getFullIp(HttpServletRequest request) {
		String ip = request.getHeader("X-Forwarded-For");
		if (isNotEmpty(ip) && !"unKnown".equalsIgnoreCase(ip)) {
			return  StringUtilsTwo.doReplace(ip, " ", "");
		}
		ip = request.getHeader("X-Real-IP");
		if (isNotEmpty(ip) && !"unKnown".equalsIgnoreCase(ip)) {
			return   StringUtilsTwo.doReplace(ip, " ", "");
		}
		return  StringUtilsTwo.doReplace(request.getRemoteAddr(), " ", "");
	}

	/**
	 * 生成随机数
	 * @param length 随机数长度
	 * @param type 随机数类型，0-大写字母，1-小写字母，2-数字，不传该参数则为大小写字母数字组合
	 * @return
	 */
	public static String randomString(int length, int...type){
		Random random = new Random();
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < length; ++i){
			int number = type.length>0?type[0]:random.nextInt(3);
			long result = 0;
			switch (number){
				case 0:
					result = Math.round(Math.random() * 25 + 65);
					sb.append(String.valueOf((char)result));
					break;
				case 1:
					result = Math.round(Math.random() * 25 + 97);
					sb.append(String.valueOf((char)result));
					break;
				case 2:
					sb.append(String.valueOf(new Random().nextInt(10)));
					break;
			}
		}
		return sb.toString();
	}

//	/**
//	 * TODO : 根据url获取图片并进行base64编码
//	 * @param urlPath
//	 * @return
//	 */
//	@SuppressWarnings("restriction")
//	public static String getUrlPhoto(String urlPath) throws Exception{
//		ByteArrayOutputStream outputStream = null;
//    	BufferedImage bufferedImage = ImageIO.read(new URL(urlPath));
//    	outputStream = new ByteArrayOutputStream();
//    	ImageIO.write(bufferedImage, "jpg", outputStream);
//	    // 对字节数组Base64编码
//	    BASE64Encoder encoder = new BASE64Encoder();
//	    return encoder.encode(outputStream.toByteArray());// 返回Base64编码过的字节数组字符串
//	}

	public static String getRequestUrl(HttpServletRequest request) {
		String url = request.getScheme() + "://"; //请求协议 http 或 https    
		url += request.getHeader("host");  // 请求服务器    
		url += request.getRequestURI();     // 工程名      
		if(request.getQueryString() != null) //判断请求参数是否为空  
			url += "?" + request.getQueryString();   // 参数   
		return url;
	}
	
	/**
	 * bigList 去掉 smaList里面重复元素
	 */
	public static List<String> disList(List<String> bigList,List<String> smaList){
		if(smaList.size()==0){
			return bigList;
		}
		List<String> list=new ArrayList<String>();
		
 		for(int i=0;i<bigList.size();i++){
			if(!smaList.contains(bigList.get(i))){
				list.add(bigList.get(i));
			}
		} 
		return list; 
	}

	/**
	 * 计算百分比 divisor/dividend 结果70% 计算成70
	 * @param divisor 分子，dividend 分母
	 */
	public static String toPercentNumber(int divisor,int dividend){
		DecimalFormat df=new DecimalFormat("0.000");
		BigDecimal result_str=new  BigDecimal(df.format((float)divisor/dividend));
		String result=result_str.multiply(new BigDecimal("100")).setScale(1,BigDecimal.ROUND_HALF_UP).toString();
		return result;
	}

	/**
	 * 生成指定位数的随机数
	 * @param length
	 * @return
	 */
	public static String getRandom(int length){
		String val = "";
		Random random = new Random();
		for (int i = 0; i < length; i++) {
			val += String.valueOf(random.nextInt(10));
		}
		return val;
	}

	public static String[] getNullPropertyNames (Object source) {
		final BeanWrapper src = new BeanWrapperImpl(source);
		java.beans.PropertyDescriptor[] pds = src.getPropertyDescriptors();

		Set<String> emptyNames = new HashSet<String>();
		for(java.beans.PropertyDescriptor pd : pds) {
			Object srcValue = src.getPropertyValue(pd.getName());
			if (srcValue == null) {
				emptyNames.add(pd.getName());
			}
		}
		String[] result = new String[emptyNames.size()];
		return emptyNames.toArray(result);
	}
	//配合Spring的BeanUtils使用，使用在两类之间复制的时候排除null属性复制
	public static void copyPropertiesIgnoreNull(Object src, Object target){
		BeanUtils.copyProperties(src, target, getNullPropertyNames(src));
	}

	//测试用
	public static void main(String[] args) throws Exception{
		//System.out.println(randomString(8));
//		List<String> months = DateUtils.strToListInTime("2020-11-01", "2021-03-02");
//		for(String m:months){
//			System.out.println(m);
//		}
//		String a="2020-11-03";
//		System.out.println(a.substring(0,8)+"02");

//		String a="<div>\n<div><span style=\"font-size: 14pt;\"><strong>&nbsp;星樾山畔┃理想置业科学城 靠山自有一套</strong></span></div>\n</div>\n<div>&nbsp;</div>\n<div>\n<div>\n<div><span style=\"color: #999999;\">悦秀会&nbsp;11月19日</span></div>\n</div>\n</div>\n<div>&nbsp;</div>\n<div><span style=\"color: #999999;\"><img class=\"wscnph\" style=\"width: 350px; display: block; margin: -1px auto;\" src=\"https://yhbtest.yuexiuproperty.cn/file/17?access_token=438fc909-f20f-429f-badd-56c4ed1c90b0\" /><img class=\"wscnph\" style=\"width: 350px; display: block; margin: -1px auto;\" src=\"https://yhbtest.yuexiuproperty.cn/file/18?access_token=438fc909-f20f-429f-badd-56c4ed1c90b0\" /><img class=\"wscnph\" style=\"width: 350px; display: block; margin: -1px auto;\" src=\"https://yhbtest.yuexiuproperty.cn/file/19?access_token=438fc909-f20f-429f-badd-56c4ed1c90b0\" /><img class=\"wscnph\" style=\"width: 350px; display: block; margin: -1px auto;\" src=\"https://yhbtest.yuexiuproperty.cn/file/20?access_token=438fc909-f20f-429f-badd-56c4ed1c90b0\" /><img class=\"wscnph\" style=\"width: 350px; display: block; margin: -1px auto;\" src=\"https://yhbtest.yuexiuproperty.cn/file/21?access_token=438fc909-f20f-429f-badd-56c4ed1c90b0\" /><img class=\"wscnph\" style=\"width: 350px; display: block; margin: -1px auto;\" src=\"https://yhbtest.yuexiuproperty.cn/file/22?access_token=438fc909-f20f-429f-badd-56c4ed1c90b0\" /></span></div>";
//		a=a.replaceAll("\\w{8}-\\w{4}-\\w{4}-\\w{4}-\\w{12}","");
//		a=a.replaceAll("\\?access_token=","");
//		System.out.println(a);

//		double d = 5123446*1.0 / 10000;
//		System.out.println(d);
//		System.out.println((Double.parseDouble(String.format("%.2f", d))));

//		double d = 1.0 * (0.8 - 0.6) / 0.6;
//		System.out.println((Double.parseDouble(String.format("%.2f", d))));

//		Map<String, Object> map1 = new HashMap<>();
//		Map<String, Object> map2 = new HashMap<>();
//		map1.put("111","jjj");
//		map2.put("iii","iii");
//		List<Map<String, Object>> li = new ArrayList<>();
//		li.add(map1);
//		li.add(map2);

//		List<String> ss = new ArrayList<>();
//		ss.add("jjj");
//		ss.add("iii");
//		if(ss.contains("iii")){
//			System.out.println("888");
//		}
		String a="";
		System.out.println(BeanUtilTwo.isNotEmpty(a));
	}
}
