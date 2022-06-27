package net.bncloud.contract.job;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.extern.slf4j.Slf4j;
import net.bncloud.common.util.DateUtil;
import net.bncloud.contract.entity.Contract;
import net.bncloud.contract.enums.ContractStatus;
import net.bncloud.contract.service.ContractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @ClassName ContractExpiredJob
 * @Description: 合同过期，定时任务
 * @Author Administrator
 * @Date 2021/3/26
 * @Version V1.0
 **/
@Component
@Slf4j
public class ContractExpiredJob {

    @Autowired
    private ContractService contractService;


    /*@Scheduled(cron = "0 1 * * * ?")
    public void expired(){
        log.info("ContractExpiredJob-expired 启动");

        //修改合同状态
//        contractService.update(Wrappers.<Contract>update().lambda()
//                .set(Contract::getContractStatusCode,ContractStatus.INVALID.getCode())
//                .eq(Contract::getContractStatusCode,ContractStatus.VALID.getCode())
//                .le(Contract::getExpiryDate,DateUtil.now())
//        );

//        //修改合同状态
        contractService.update(Wrappers.<Contract>update().lambda()
                .set(Contract::getContractStatusCode,ContractStatus.EXPIRED.getCode())
                .eq(Contract::getContractStatusCode,ContractStatus.VALID.getCode())
                .le(Contract::getExpiryDate,DateUtil.now())
        );



        log.info("ContractExpiredJob-expired 执行完成");
    }*/

}
