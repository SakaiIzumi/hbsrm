package net.bncloud.quotation.xxljobtask;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import net.bncloud.common.api.R;
import net.bncloud.common.base.globallock.DistributedLock;
import net.bncloud.common.base.globallock.lock.LockWrapper;
import net.bncloud.quotation.constant.QuotationSyncConstants;
import net.bncloud.quotation.manager.QuotationSupplierAlert;
import net.bncloud.quotation.service.QuotationBaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;


/**
 * desc: 预警任务处理
 *    XxlJobHelper.log("xxxx");
 * @author YangYu
 * @Date 2022/03/03
 **/
@Component
@Slf4j
public class EarlyWarningTaskHandler {

    @Resource
    QuotationSupplierAlert quotationSupplierAlert;

    @Resource
    QuotationBaseService quotationBaseService;

    @Autowired
    private  DistributedLock distributedLock;

    /**
     * 供应商预警
     */
    @XxlJob(value = "supplierAlert")
    public void supplierAlert(){
        log.info("询价单->供应商预警方法调用开始");
        quotationSupplierAlert.earlyWarningMethod();
        log.info("询价单->供应商预警方法调用结束");
    }

    /**
     * 定时任务检查询价单报价是否到达截止时间并修改状态
     */
    @XxlJob(value = "modifyQuotationStatus")
    public R<Object> modifyQuotationStatus(){
        LockWrapper lockWrapper = new LockWrapper().setKey(QuotationSyncConstants.SYNC_LOCK_PREFIX_KEY + QuotationSyncConstants.TRY_LOCK_UPDATE_QUOTATION_STATUS).setWaitTime(0).setLeaseTime(15).setUnit(TimeUnit.MINUTES);
        return distributedLock.tryLock(lockWrapper, () -> {
            log.info("询价单->扫描截止时间并更新状态");

            quotationBaseService.modifyQuotationStatus();

            log.info("询价单->扫描更新完毕");
            return R.success("更新成功！");
        }, () -> R.fail("已经有更新任务在进行了."));
    }


    public static void main(String[] args) {
        String str = "{\"type\":[\"1\"],\"hour\":\"10\",\"quotationSwitch\":true}";
        JSONObject jsonObject = JSON.parseObject(str);  // result数据源：JSON格式字符串
        // 获取值
        jsonObject.getLong("hour");
      jsonObject.getBoolean("quotationSwitch");
        JSONArray data = jsonObject.getJSONArray("type");
        int size = data.size();
        List<Integer> integerList = new ArrayList<>();
        for (int i = 0; i <size ; i++) {
            String s=(String) data.get(i);
            integerList.add(Integer.valueOf(s));

        }
        System.out.println(integerList.toString());
    }

}
