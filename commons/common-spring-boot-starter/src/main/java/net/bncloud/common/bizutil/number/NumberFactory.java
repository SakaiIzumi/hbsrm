package net.bncloud.common.bizutil.number;


import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

/**
 * 单号工厂
 * @author Rao
 * @Date 2021/12/24
 **/
@Component
public class NumberFactory {

    private final StringRedisTemplate stringRedisTemplate;
    public NumberFactory(StringRedisTemplate stringRedisTemplate) {
        this.stringRedisTemplate = stringRedisTemplate;
    }

    /**
     * 生成 单号
     * 规则：不同单据前缀+年月日+10位由redis生成的递增数
     */
    public String buildNumber(NumberType numberType) {
        return generate(numberType, 10);
    }

    public String generate(NumberType numberType, Integer digit) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        String date = LocalDateTime.now(ZoneOffset.of("+8")).format(formatter);
        String key = "generate_no:" + numberType + ":" + date;
        Long increment = stringRedisTemplate.opsForValue().increment(key);
        return numberType.getPrefix() + date + String.format("%0" + digit + "d", increment);
    }


}
