package net.bncloud.utils;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class SortUtil {

    private static Pattern linePattern = Pattern.compile("_(\\w)");

    /** 下划线转驼峰 */
    public static String lineToHump(String str) {
        str = str.toLowerCase();
        Matcher matcher = linePattern.matcher(str);
        StringBuffer sb = new StringBuffer();
        while (matcher.find()) {
            matcher.appendReplacement(sb, matcher.group(1).toUpperCase());
        }
        matcher.appendTail(sb);
        return sb.toString();
    }

    /** 驼峰转下划线(简单写法，效率低于{@link #humpToLine2(String)}) */
    public static String humpToLine(String str) {
        return str.replaceAll("[A-Z]", "_$0").toLowerCase();
    }

    private static Pattern humpPattern = Pattern.compile("[A-Z]");

    /** 驼峰转下划线,效率比上面高 */
    public static String humpToLine2(String str) {
        Matcher matcher = humpPattern.matcher(str);
        StringBuffer sb = new StringBuffer();
        while (matcher.find()) {
            matcher.appendReplacement(sb, "_" + matcher.group(0).toLowerCase());
        }
        matcher.appendTail(sb);
        return sb.toString();
    }

    public  static Pageable sortMybatis(Pageable pageable){
        List<Sort.Order> sortOrders =  pageable.getSort().get().map(orderItem->{
            if (orderItem.getDirection()== Sort.Direction.DESC) {
                return Sort.Order.desc(humpToLine2(orderItem.getProperty()));
            } else {
                return Sort.Order.asc(humpToLine2(orderItem.getProperty()));
            }
        }).collect(Collectors.toList());

        Sort sort = Sort.by(sortOrders);

        return PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(),sort);

    }

}


