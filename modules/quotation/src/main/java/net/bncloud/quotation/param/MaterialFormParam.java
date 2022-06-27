package net.bncloud.quotation.param;


import com.fasterxml.jackson.annotation.JsonFormat;
import net.bncloud.common.util.DateUtil;
import net.bncloud.quotation.entity.MaterialForm;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;


/**
 * <p>
 * 物料表单信息表
 * </p>
 *
 * @author Auto-generator
 * @since 2022-02-14
 */
@Data
public class MaterialFormParam extends MaterialForm implements Serializable {

    private static final long serialVersionUID = 1L;

    private String purchaseTime;

    private String product;



}
