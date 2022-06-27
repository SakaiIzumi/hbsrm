package net.bncloud.bis.manager;

import com.kingdee.bos.webapi.sdk.QueryParam;
import lombok.extern.slf4j.Slf4j;
import net.bncloud.bis.constant.BisSyncConstants;
import net.bncloud.bis.properties.ApplicationProperties;
import net.bncloud.msk3cloud.core.condition.QueryCondition;
import net.bncloud.msk3cloud.kingdee.K3cloudRemoteService;
import org.apache.commons.lang3.StringUtils;
import org.redisson.api.RMap;
import org.redisson.api.RedissonClient;
import org.redisson.codec.JsonJacksonCodec;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.Date;
import java.util.List;
import java.util.function.Consumer;

/**
 * desc: 分段查询
 *
 * @author Rao
 * @Date 2022/01/25
 **/
@Component
@Slf4j
public class SegmentedQueryManager {

    @Autowired
    private K3cloudRemoteService k3cloudRemoteService;

    @Autowired
    private RedissonClient redissonClient;

    @Autowired
    private ApplicationProperties applicationProperties;

    /**
     * 循环同步
     * @param queryCondition 条件值
     * @param syncDateTimeKey 同步时间key
     * @param lastSyncDateField 最后更新时间字段
     * @param limit 限制数
     * @param cls 类
     * @param consumer 消费
     * @param <T> 类型
     */
    public <T> void doWhile( QueryCondition queryCondition,String syncDateTimeKey,String lastSyncDateField,int limit, Class<T> cls, Consumer<List<T>> consumer ){
        // 获取上一次同步的时间蹉
        RMap<String, Date> syncDataTimeMap = redissonClient.getMap( BisSyncConstants.TASK_SYNC_DATA_TIME_MAP_KEY, JsonJacksonCodec.INSTANCE);
        Date lastSyncDataTime = StringUtils.isEmpty(syncDateTimeKey) ? null : syncDataTimeMap.getOrDefault(syncDateTimeKey, applicationProperties.getSchedulingTask().getDefaultSyncDateTime(syncDateTimeKey));
        // 统一处理 日期问题
        queryCondition.gt( lastSyncDataTime != null && lastSyncDateField != null,lastSyncDateField,lastSyncDataTime );
        this.whileHandle(queryCondition, limit, cls, consumer);

    }

    /**
     * 循环获取所有的数据 （仅用于获取少量字段）
     * @param queryCondition
     * @param limit
     * @param cls
     * @param consumer
     * @param <T>
     */
    public  <T> void whileHandle(QueryCondition queryCondition, int limit, Class<T> cls, Consumer<List<T>> consumer) {
        boolean continueNext = false;
        int page = 1;
        do {
            queryCondition.page( page, limit);
            QueryParam queryParam = queryCondition.queryParam();
            try {
                List<T> recordList = k3cloudRemoteService.documentQueryWithClass(queryParam, cls);
                if (! CollectionUtils.isEmpty( recordList)) {
                    // 消费数据
                    consumer.accept( recordList );

                    continueNext = true;
                    page++;
                } else {
                    continueNext = false;
                }

            } catch (Exception exception) {
                log.error("[SegmentedQueryManager] sync startRow:{},endRow:{} data error!",queryParam.getStartRow(),queryParam.getTopRowCount(), exception);
                continueNext = false;
            }

        } while (continueNext);
    }

}
