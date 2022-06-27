package net.bncloud.quotation.event.material.listener;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import net.bncloud.common.util.BeanUtil;
import net.bncloud.quotation.entity.MaterialForm;
import net.bncloud.quotation.entity.MaterialFormExt;
import net.bncloud.quotation.event.material.MaterialFormChangeEvent;
import net.bncloud.quotation.service.MaterialFormExtService;
import net.bncloud.quotation.service.MaterialFormService;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Toby
 */
@Component
@Slf4j
public class MaterialFormChangeEventListener {

    private final MaterialFormService materialFormService;

    private final MaterialFormExtService materialFormExtService;

    public MaterialFormChangeEventListener(MaterialFormService materialFormService, MaterialFormExtService materialFormExtService) {
        this.materialFormService = materialFormService;
        this.materialFormExtService = materialFormExtService;
    }

    @EventListener(MaterialFormChangeEvent.class)
    public void syncExtContent(MaterialFormChangeEvent materialFormChangeEvent){

    }

}
