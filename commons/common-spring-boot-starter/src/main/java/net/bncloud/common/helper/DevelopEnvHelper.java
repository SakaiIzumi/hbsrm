package net.bncloud.common.helper;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.bncloud.common.exception.ApiException;
import net.bncloud.common.exception.Asserts;
import net.bncloud.common.util.CollectionUtil;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * desc: 开发环境助手
 *
 * @author Rao
 * @Date 2022/03/26
 **/
@Slf4j
@AllArgsConstructor
@Component
@ConditionalOnBean(value = {DevelopConfiguration.class,EnvHelper.class})
public class DevelopEnvHelper{

    private final DevelopConfiguration developConfiguration;
    private final EnvHelper envHelper;

    /**
     * 非生产运行
     */
    public void nonProdIsRun( Runnable runnable ){
        if (envHelper.nonPro()) {
            runnable.run();
        }
    }

    /**
     * 转换手机号
     * @param phone
     * @return
     */
    public String transferSmsPhone( String phone){
        if ( envHelper.nonPro()) {
            String msg = "未配置开发环境的手机号.see DevelopConfiguration.class";
            ApiException apiException = new ApiException(msg);
            DevelopConfiguration.SmsEnv smsEnv = Optional.ofNullable( developConfiguration.getSmsEnv() ).orElseThrow(() -> apiException);
            List<String> devPhones = smsEnv.getDevPhones();
            if( CollectionUtil.isNotEmpty( devPhones ) && StrUtil.isNotBlank( phone)){
                if( devPhones.contains( phone.trim() ) ){
                    log.info("[DevelopEnvHelper] transferSmsPhone 手机号属于测试人员手机号（{}），符合测试环境发送规则!",phone );
                    return phone;
                }
            }
            String smsPhone = Optional.ofNullable( smsEnv.getPhone()).orElseThrow(() -> apiException);
            Asserts.isTrue( StrUtil.isNotBlank(smsPhone),msg );
            log.info("[DevelopEnvHelper] transferSmsPhone 手机号不属于测试人员手机号（{}），转换成配置的手机号（{}）!",phone,smsPhone );
            return smsPhone;
        }
        return phone;
    }
    /**
     * 转换手机号
     * @param phone
     * @return
     */
    public List<String> transferSmsPhoneV2( String phone){
        List<String> phoneList=new ArrayList<>();
        if ( envHelper.nonPro()) {
            log.info("==========注意:非生产环境================");
            log.info("非生产环境,开始校验或转换手机号:{}", JSON.toJSONString(phone));
            String msg = "未配置开发环境的手机号.see DevelopConfiguration.class";
            ApiException apiException = new ApiException(msg);


            DevelopConfiguration.SmsEnv smsEnv = Optional.ofNullable( developConfiguration.getSmsEnv() ).orElseThrow(() -> apiException);

            List<String> devPhones = smsEnv.getDevPhones();

            if( CollectionUtil.isNotEmpty( devPhones ) && StrUtil.isNotBlank( phone)){
                if( devPhones.contains( phone.trim() ) ){
                    log.info("[DevelopEnvHelper] transferSmsPhone 手机号属于测试人员手机号（{}），符合测试环境发送规则!",phone );
                    phoneList.add(phone);
                    return phoneList;
                }
            }
            String smsPhone = Optional.ofNullable( smsEnv.getPhone()).orElseThrow(() -> apiException);
            Asserts.isTrue( StrUtil.isNotBlank(smsPhone),msg );

            phoneList.add(smsPhone);
            if(CollectionUtil.isNotEmpty( devPhones )){
                phoneList.addAll(devPhones);
            }
            log.info("[DevelopEnvHelper] transferSmsPhone 手机号不属于测试人员手机号（{}），转换成配置的手机号（{}）!",phone,smsPhone );
            log.info("发送手机号的集合为:{}", JSON.toJSONString(phoneList));
            return phoneList;
        }
        log.info("生产环境,发送短信的手机号的集合为:{}", JSON.toJSONString(phoneList));
        phoneList.add(phone);
        return phoneList;
    }


}
