package net.bncloud.delivery.vo;

import lombok.Data;
import net.bncloud.delivery.entity.FactoryInfo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


/**
 * @author liyh
 * @description 工厂主数据dto
 * @since 2022/5/16
 */
@Data
public class FactoryInfoVo extends FactoryInfo implements Serializable {
    /**
     *
     *前端要求,需啊哟这个字段
     */
    private List<String> areas=new ArrayList();
}
