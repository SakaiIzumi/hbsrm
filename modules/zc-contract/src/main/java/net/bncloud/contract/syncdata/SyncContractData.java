package net.bncloud.contract.syncdata;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import net.bncloud.bis.service.api.feign.SyncContractDataFeignClient;
import net.bncloud.common.api.R;
import net.bncloud.contract.vo.ContractVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/zc/contract/sync")
@Api(tags = "合同手动同步数据接口")
@Slf4j
public class SyncContractData {

    @Autowired
    private SyncContractDataFeignClient syncContractDataFeignClient;



    @PostMapping("/saveData")
    @ApiOperation(value = "手动同步合同数据接口", notes = "")
    public R<Object> saveContractData() {
        System.out.println("准备开始同步");
//        ContractVo contractVo = contractService.getContractInfo(id);
        //调用bis服务进行同步
        R<Object> objectR = syncContractDataFeignClient.syncContractData();
        return R.data(objectR.getData());
    }



}
