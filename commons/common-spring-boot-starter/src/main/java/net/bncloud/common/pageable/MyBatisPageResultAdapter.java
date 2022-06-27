package net.bncloud.common.pageable;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springframework.data.domain.PageRequest;

public class MyBatisPageResultAdapter<T> extends PageResultAdapter<T> {
    private static final long serialVersionUID = -605573648892602141L;

    public MyBatisPageResultAdapter(IPage<T> page) {
        super(page.getRecords(), PageRequest.of((int) page.getCurrent() - 1, (int) page.getSize()), page.getTotal());
    }
}
