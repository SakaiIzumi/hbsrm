package net.bncloud.bis.properties;

import lombok.Data;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Map;
import java.util.Optional;

/**
 * desc: 应用参数
 *
 * @author Rao
 * @Date 2022/02/12
 **/
@Slf4j
@Data
@Configuration
@ConfigurationProperties(prefix = "application")
public class ApplicationProperties {

    /**
     * 调用任务
     */
    private SchedulingTask schedulingTask;

    /**
     * 调度任务配置
     */
    public static class SchedulingTask {

        /**
         * 调度任务默认同步日期map
         */
        private Map<String, String> taskDefaultSyncDateTimeMap;

        /**
         * 默认的时间
         */
        private String defaultDateTime = "2022-01-01 00:00:00";

        public void setTaskDefaultSyncDateTimeMap(Map<String, String> taskDefaultSyncDateTimeMap) {
            this.taskDefaultSyncDateTimeMap = taskDefaultSyncDateTimeMap;
        }

        public void setDefaultDateTime(String defaultDateTime) {
            this.defaultDateTime = defaultDateTime;
        }

        /**
         * 获取默认的同步日期
         * @param key
         * @return
         */
        @SneakyThrows
        public Date getDefaultSyncDateTime(String key){
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String dateStr = Optional.ofNullable( taskDefaultSyncDateTimeMap.get(key)).orElse( defaultDateTime);
            try {
                return dateFormat.parse(dateStr);
            } catch (ParseException e) {
                log.warn("[ApplicationProperties] schedulingTask 的 taskDefaultSyncDateTimeMap 获取 {} 的值({})反序列化失败 ",key,dateStr);
                return dateFormat.parse( defaultDateTime);
            }
        }
    }

}
