package net.bncloud.common.util;

import org.apache.commons.lang3.StringEscapeUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 字符串工具类, 继承org.apache.commons.lang3.StringUtils类
 * @author dr
 */
public class StringUtilsTwo extends org.apache.commons.lang3.StringUtils {
	private static Pattern htmlRegEx = Pattern.compile("<[^>]+>", 2);
	private static Pattern scriptRegex = Pattern.compile("<[\\s]*?script[^>]*?>[\\s\\S]*?<[\\s]*?\\/[\\s]*?script[\\s]*?>", 2);
	private static Pattern commentRegex = Pattern.compile("<!--.*-->", 2);
	private static Pattern styleRegex = Pattern.compile("<[\\s]*?style[^>]*?>[\\s\\S]*?<[\\s]*?\\/[\\s]*?style[\\s]*?>", 2);
	private static Pattern selectRegex = Pattern.compile("<select[^>]*>.*</select>", 2);
	private static Pattern textRegex = Pattern.compile("<textarea[^>]*>.*</textarea>", 2);
	private static Pattern inputRegex = Pattern.compile("</?input[^>]*>", 2);
	private static Pattern fontRegex = Pattern.compile("</?font[^>]*>", 2);
	private static Pattern aRegex = Pattern.compile("</?a[^>]*>", 2);
	private static Pattern spanRegex = Pattern.compile("</?span[^>]*>", 2);
	private static Pattern iframeRegex = Pattern.compile("</?iframe[^>]*>", 2);
	private static Pattern imgRegex = Pattern.compile("</?img[^>]*>", 2);
	private static Pattern selectToFromRegex = Pattern.compile("^(?i)select (?:(?!select|\\bfrom\\b)[\\s\\S])*(\\((?i)select (?:(?!\\bfrom\\b)[\\s\\S])* (?i)\\bfrom\\b [^\\)]*\\)(?:(?!select|\\bfrom\\b)[^\\(])*)*\\bfrom\\b");
	private static Pattern mobileRegex = Pattern.compile("^[1][3,4,5,7,8][0-9]{9}$");

    public static String delHTMLTag(String html){
   		Matcher m = htmlRegEx.matcher(html);
		html = m.replaceAll("");
        return html;
    }

    public static String delImgTag(String html){
   		Matcher m = imgRegex.matcher(html);
		html = m.replaceAll("");
       return html;
    }

    /**
	 * 判断sql 是否存在group by
	 */
	public static boolean hasGroupby(String sql) {
		if (isBlank(sql)){
			return false;
		}
		String regEx = "\\s+(?<GroupByClause>(?i)group\\s+(?i)by\\s+.+$)";
		Pattern p = Pattern.compile(regEx);
		Matcher m = p.matcher(sql);

		return m.find();
	}

	/**
	 * 替换掉sql order by
	 */
	public static String replaceOrderby(String sql) {
		if (isBlank(sql)){
			return "";
		}
		//String regEx = "(?:(?>'([^']+|'')*'))|\\s+(?<OrderByClause>(?i)order\\s+(?i)by\\s+.+$)";
		String regEx = "\\s+(?<OrderByClause>(?i)order\\s+(?i)by\\s+.+$)";
		Pattern p = Pattern.compile(regEx);
		Matcher m = p.matcher(sql);
		String s = m.replaceAll("");
		return s;
	}

	/**
	 * 替换掉sql select和from之间的内容为count(1)
	 */
	public static String replaceSql(String sql) {
		if (isBlank(sql)){
			return "";
		}
		sql = sql.replaceAll("\\s+", " ");
		String regEx = "^(?i)select (?:(?!select|from)[\\s\\S])*(\\((?i)select (?:(?!from)[\\s\\S])* (?i)from [^\\)]*\\)(?:(?!select|from)[^\\(])*)*from";
		Pattern p = Pattern.compile(regEx,Pattern.CASE_INSENSITIVE);
		Matcher m = p.matcher(sql);
		String s = m.replaceAll("select count(1) from");
		return s;
	}

	/**
	 * 判断sql 是否存在FROM_DAYS|FROM_UNIXTIME
	 */
	public static boolean hasFrom(String sql) {
		if (isBlank(sql)){
			return false;
		}
		String regEx = ".*FROM_DAYS|.*FROM_UNIXTIME.*";
		Pattern p = Pattern.compile(regEx,Pattern.CASE_INSENSITIVE);
		Matcher m = p.matcher(sql);

		return m.find();
	}

	public static String lowerFirst(String str){
		if(StringUtilsTwo.isBlank(str)) {
			return "";
		} else {
			return str.substring(0,1).toLowerCase() + str.substring(1);
		}
	}

	public static String upperFirst(String str){
		if(StringUtilsTwo.isBlank(str)) {
			return "";
		} else {
			return str.substring(0,1).toUpperCase() + str.substring(1);
		}
	}

	/**
	 * 替换掉HTML标签方法
	 */
	public static String replaceHtml(String html) {
		if (isBlank(html)){
			return "";
		}
		String regEx = "<.+?>";
		Pattern p = Pattern.compile(regEx);
		Matcher m = p.matcher(html);
		String s = m.replaceAll("");
		return s;
	}

	public static String htmlTrim(String html) {
		Matcher m = scriptRegex.matcher(html);
		html = m.replaceAll("");
		m = commentRegex.matcher(html);
		html = m.replaceAll("");
		m = styleRegex.matcher(html);
		html = m.replaceAll("");
		m = selectRegex.matcher(html);
		html = m.replaceAll("");
		m = textRegex.matcher(html);
		html = m.replaceAll("");
		m = inputRegex.matcher(html);
		html = m.replaceAll("");
		m = fontRegex.matcher(html);
		html = m.replaceAll("");
		m = aRegex.matcher(html);
		html = m.replaceAll("");
		m = spanRegex.matcher(html);
		html = m.replaceAll("");
		m = iframeRegex.matcher(html);
		html = m.replaceAll("");
		return html.trim();
	}

	/**
	 * 缩略字符串（不区分中英文字符）
	 * @param str 目标字符串
	 * @param length 截取长度
	 * @return
	 */
	public static String abbr(String str, int length) {
		if (str == null) {
			return "";
		}
		try {
			StringBuilder sb = new StringBuilder();
			int currentLength = 0;
			for (char c : replaceHtml(StringEscapeUtils.unescapeHtml4(str)).toCharArray()) {
				currentLength += String.valueOf(c).getBytes("GBK").length;
				if (currentLength <= length - 3) {
					sb.append(c);
				} else {
					sb.append("...");
					break;
				}
			}
			return sb.toString();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return "";
	}

	/**
	 * 缩略字符串（替换html）
	 * @param str 目标字符串
	 * @param length 截取长度
	 * @return
	 */
	public static String rabbr(String str, int length) {
        return abbr(replaceHtml(str), length);
	}

	/**
	 * 转换为Double类型
	 */
	public static String toStr(Object val){
		if (val == null){
			return null;
		}
		return val.toString();
	}

	/**
	 * 转换为Double类型
	 */
	public static Double toDouble(Object val){
		if (val == null){
			return 0D;
		}
		try {
			return Double.valueOf(trim(val.toString()));
		} catch (Exception e) {
			return 0D;
		}
	}

	/**
	 * 转换为Float类型
	 */
	public static Float toFloat(Object val){
		return toDouble(val).floatValue();
	}

	/**
	 * 转换为Long类型
	 */
	public static Long toLong(Object val){
		return toDouble(val).longValue();
	}

	/**
	 * 转换为Integer类型
	 */
	public static Integer toInteger(Object val){
		return toInteger(val).intValue();
	}

	/**
	 * @param columnName
	 * @return 返回表字段值
	 */
	public static String getColumnName(String columnName) {
		StringBuilder buf = new StringBuilder(columnName);
		for (int i = 1; i < buf.length() - 1; i++) {
			if ((!Character.isLowerCase(buf.charAt(i - 1)))
					|| (!Character.isUpperCase(buf.charAt(i)))
					|| (!Character.isLowerCase(buf.charAt(i + 1)))) {
				continue;
			}

			buf.insert(i++, '_');
		}

		return buf.toString().toLowerCase();
	}

	/**
	 * 把骆驼命名法的变量，变为大写字母变小写且之前加下划线
	 *
	 * @param str
	 * @return
	 */
	public static String toUnderline(String str) {
		str = StringUtilsTwo.uncapitalize(str);
		char[] letters = str.toCharArray();
		StringBuilder sb = new StringBuilder();
		for (char letter : letters) {
			if (Character.isUpperCase(letter)) {
				sb.append("_" + letter + "");
			} else {
				sb.append(letter + "");
			}
		}
		return StringUtilsTwo.lowerCase(sb.toString());
	}

	/**
	 * 判断字符为空或者null
	 * @param string
	 * @return
	 */
	public static boolean isNullOrBlank(String string){
		if(string==null){
			return true;
		}
		if(string.trim().length()==0){
			return true;
		}
		return false;
	}

	/**
	 * 随机获取UUID字符串(无中划线)
	 *
	 * @return UUID字符串
	 */
	public static String getUUID() {
		String uuid = UUID.randomUUID().toString();
		return uuid.substring(0, 8) + uuid.substring(9, 13);
	}

	/*
	 * 根据正则表达式截取字符串
	 */
	public static List<String> toMatcher(String str, String regex, int index){
		Pattern pat = Pattern.compile(regex);
		Matcher mat = pat.matcher(str);

		List<String> result = new ArrayList<String>();
		if (mat.find()) {
			result.add(mat.group(index));
		}

		return result;
	}

	public static String doReplace(String context,String maker, String reStr){
		reStr = reStr == null ? "" : reStr;
		return context.replaceAll(maker, reStr);
	}

	public static String replaceSelectFrom(String sql,String replaceText){
		sql = sql.replaceAll("\\s+", " ").replaceAll("\n\r", " ").toUpperCase().trim();
		Matcher m = selectToFromRegex.matcher(sql);
		String s = m.replaceAll(replaceText);
		return s;
	}

	public static String urlEncodeChinese(String url) throws UnsupportedEncodingException {
		char[] ch = url.toCharArray();
		for (char c : ch) {
			String key = (c + "");
			if(key.getBytes().length != 1) {
				url = url.replace(key, URLEncoder.encode(key, "utf-8"));
			}
		}
		return url;
	}

	public static String urlEncode(String url) throws UnsupportedEncodingException{
		String res = URLEncoder.encode( url, "UTF-8" );
		return res;
	}
	
	public static boolean isMobile(String mobile) {
		Matcher m = mobileRegex.matcher(mobile);
		return m.matches();
	}

	public static String listToString(List<String> list, String separator){
		StringBuffer buffer = new StringBuffer();
        for(String data:list){
        	buffer.append(data).append(separator);
        }
        if(buffer.length()>0)
        	return buffer.substring(0, buffer.length()-1);

        return "";
	}
}