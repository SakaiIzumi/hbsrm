package net.bncloud.saas.utils.pageable;

import com.querydsl.core.QueryResults;
import org.springframework.data.domain.*;

import java.util.List;


public class PageUtils {

    public static <T> Page<T> of(QueryResults<T> queryResults, Pageable pageable) {
        return new <T>PageImpl(queryResults.getResults(), pageable, queryResults.getTotal());
    }

    public static <T> Page<T> of(List<T> result, Pageable pageable, Long total) {
        return new <T>PageImpl(result, pageable, total);
    }

    public static Page<?> empty() {
        return new PageImpl(QueryResults.emptyResults().getResults(), PageRequest.of(1, 10), 0);
    }
}
