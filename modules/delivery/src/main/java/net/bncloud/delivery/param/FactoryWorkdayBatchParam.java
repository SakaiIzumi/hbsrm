package net.bncloud.delivery.param;

import lombok.Data;
import net.bncloud.delivery.entity.FactoryWorkday;

import java.io.Serializable;
import java.util.List;

/**
 * desc: 工作日信息维护 (批量)
 *
 * @author liyh
 * @Date 2022/05/16
 **/
@Data
public class FactoryWorkdayBatchParam  implements Serializable {
    /**
     *保存或者更新的列表
     **/
    private List<FactoryWorkdayParam> workdayList;
    /**
     *新增/编辑
     **/
    private String saveOrUpdate;

}
