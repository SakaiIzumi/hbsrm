package net.bncloud.bis.service.api.feign;

import net.bncloud.bis.service.api.fallbackfactory.SupplierFeignClientFallbackFactory;
import net.bncloud.common.api.R;
import net.bncloud.security.client.AuthorizedFeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * desc: 供应商信息同步feign
 *
 * @author Rao
 * @Date 2022/01/20
 **/
@AuthorizedFeignClient(name = "bis",path = "bis", contextId = "bisSupplierClient", fallbackFactory = SupplierFeignClientFallbackFactory.class)
public interface SupplierFeignClient {

    /**
     * 同步供应商信息
     * @param ids
     * @return
     */
    @PostMapping("/syncSupplierInfo")
    R<Object> syncSupplierInfo(@RequestBody List<Integer> ids);

    /**
     * 同步供应商信息和表
     * @param ids
     * @return
     */
    @PostMapping("/syncSupplierInfoWithTable")
    R<String> syncSupplierInfoWithTable( @RequestBody List<Integer> ids );

}
