package net.bncloud.bis.controller;

import com.alibaba.fastjson.JSON;
import com.baomidou.dynamic.datasource.toolkit.DynamicDataSourceContextHolder;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.bncloud.bis.constant.DatasourceConstants;
import net.bncloud.bis.manager.SyncMaterialManager;
import net.bncloud.bis.service.api.feign.SyncContractDataFeignClient;
import net.bncloud.bis.service.api.feign.SyncMaterialFeignClient;
import net.bncloud.bis.srm.doc.manager.ContractFileManager;
import net.bncloud.common.api.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;


@RestController
@AllArgsConstructor
@Slf4j
public class SyncContractDataControl implements SyncContractDataFeignClient {

    @Autowired
    private ContractFileManager contractFileManager;

    @Override
    public R<Object> syncContractData() {
        try{
            DynamicDataSourceContextHolder.push( DatasourceConstants.MS_OA );
            contractFileManager.syncContractAndContractFile();
        }catch (Exception e){
            log.error("同步合同数据出现问题，{}", JSON.toJSONString(e.getMessage()),e);
            throw new RuntimeException("同步合同数据出现问题",e);
        }finally {
            DynamicDataSourceContextHolder.poll();
        }
        return R.success("调用成功");
    }
}
