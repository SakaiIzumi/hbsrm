package net.bncloud.baidu.util;

import lombok.extern.slf4j.Slf4j;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Map;

/**
 * desc: sn签名工具
 *
 *
 * @author Rao
 * @Date 2022/05/24
 **/
@Slf4j
@Deprecated
public class SnUtils {

    public static void main(String[] args) throws UnsupportedEncodingException {

        // 这里的顺序要求和接口传参的顺序一致。

        System.out.println(URLDecoder.decode("%E4%B8%AD%E5%9B%BD"));

//        System.out.println(URLEncoder.encode("中国"));
//        Map<String,String> paramsMap = new LinkedHashMap<String, String>();
//        paramsMap.put("address", "百度大厦");
//        paramsMap.put("output", "json");
//        paramsMap.put("ak", "");
//
//        String sn = SnUtils.demo(paramsMap);
//        System.out.println(sn);
    }

    /**
     * Get 请求下的签名生成
     * @param wholeStr
     * @return
     * @throws UnsupportedEncodingException
     */
    public static String snWithGet(String wholeStr) throws UnsupportedEncodingException {
        return SnUtils.md5( URLEncoder.encode(wholeStr, "UTF-8") );
    }

    /**
     * demo
     * https://lbsyun.baidu.com/index.php?title=webapi/appendix
     */
    protected static String demo(Map<String,String> params) throws UnsupportedEncodingException {
        String paramsStr = SnUtils.toQueryString(params);
        String wholeStr = "/geocoding/v3/?" + paramsStr + "";
        // 对上面wholeStr再作utf8编码
        String tempStr = URLEncoder.encode(wholeStr, "UTF-8");
        return SnUtils.md5( tempStr );
    }


    /**
     *
     * @param data
     * @return
     */
    protected static String toQueryString( Map<String, String> data) throws UnsupportedEncodingException {
        StringBuilder queryString = new StringBuilder();
        for (Map.Entry<String, String> pair : data.entrySet()) {
            queryString.append( pair.getKey()).append("=");
            queryString.append( URLEncoder.encode( pair.getValue(), "UTF-8")).append("&");
        }
        if (queryString.length() > 0) {
            queryString.deleteCharAt(queryString.length() - 1);
        }
        return queryString.toString();

    }

    /**
     * md5
     * @param str
     * @return
     */
    protected static String md5(String str){
        try {
            java.security.MessageDigest md = java.security.MessageDigest.getInstance("MD5");
            byte[] array = md.digest(str.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : array) {
                sb.append( Integer.toHexString((b & 0xFF) | 0x100), 1, 3);
            }
            return sb.toString();
        } catch (java.security.NoSuchAlgorithmException ex) {
            log.error("md5 error!",ex);
            throw new RuntimeException(ex.getMessage());
        }
    }

}
