package net.bncloud.common.pageable;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.stream.Collectors;

public class PageUtils {

    public static <T> IPage<T> toPage(Pageable pageable) {
        Page<T> page = new Page<>(pageable.getPageNumber() + 1, pageable.getPageSize());
        Sort sort = pageable.getSort();
        if (sort.isSorted()) {
            sort.forEach(order -> {
                if (order.isDescending()) {
                    page.addOrder(OrderItem.desc(order.getProperty()));
                } else {
                    page.addOrder(OrderItem.asc(order.getProperty()));
                }
            });
        }
        return page;
    }

    public static Pageable toPageable(IPage<?> page) {
        List<OrderItem> orders = page.orders();
        Sort sort = Sort.unsorted();
        if (orders != null && !orders.isEmpty()) {
            List<Sort.Order> sortOrders = orders.stream().map(orderItem -> {
                if (orderItem.isAsc()) {
                    return Sort.Order.asc(orderItem.getColumn());
                } else {
                    return Sort.Order.desc(orderItem.getColumn());
                }
            }).collect(Collectors.toList());
            sort = Sort.by(sortOrders);
        }
        long current = page.getCurrent();
        int pageIndex = 0;
        if (current > 1L) {
            pageIndex = (int) current - 1;
        }

        return PageRequest.of(pageIndex, (int) page.getSize(), sort);
    }

    public static <T> PageImpl<T> result(IPage<T> page) {
        return new MyBatisPageResultAdapter<>(page);
    }

    /**
     * 支持业务转换
     * @param iPage
     * @param records
     * @param <T>
     * @return
     */
    public static <T> IPage<T> toPage(IPage iPage,List<T> records) {
        Page<T> page = new Page<>(iPage.getCurrent(), iPage.getSize(),iPage.getTotal());
        page.setRecords( records );
        return page;
    }

}
