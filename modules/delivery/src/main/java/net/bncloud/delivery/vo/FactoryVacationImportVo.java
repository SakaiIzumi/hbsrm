package net.bncloud.delivery.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.bncloud.delivery.entity.FactoryVacation;

import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FactoryVacationImportVo implements Serializable {
    /**
     * true 前端直接调用接口保存  false 前端提示后用户点击确认才进行保存
     * */
    private boolean flag;
    /**
     * 新增的假日数据
     * */
    private List<FactoryVacation> listForSave;
    /**
     * 更新的假日数据
     * */
    private List<FactoryVacation> listForUpdate;

}
