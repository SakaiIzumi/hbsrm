package net.bncloud.bis.controller;


import io.swagger.annotations.ApiOperation;
import net.bncloud.common.api.R;
import net.bncloud.service.api.contract.dto.ContractDTO;
import net.bncloud.service.api.contract.feign.ContractFeignClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class ContractSyncDataControllerForRemoteTest {

    @Autowired
    private ContractFeignClient contractFeignClient;

    @GetMapping("/bis/test")
    @ApiOperation(value = "bis调用合同远程接口", notes = "")
    public R<Object> testContract(){
        ContractDTO contractDTO = new ContractDTO();
        List<ContractDTO> contractList = new ArrayList<>();
        R<Object> syncdata = contractFeignClient.syncdata(contractList);
        return R.success(syncdata.getMsg());
    }


}
