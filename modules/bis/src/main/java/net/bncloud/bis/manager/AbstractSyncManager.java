package net.bncloud.bis.manager;

import net.bncloud.bis.constant.BisSyncConstants;
import net.bncloud.bis.properties.ApplicationProperties;
import net.bncloud.common.base.globallock.DistributedLock;
import net.bncloud.msk3cloud.kingdee.K3cloudRemoteService;
import org.redisson.api.RMap;
import org.redisson.api.RedissonClient;
import org.redisson.codec.JsonJacksonCodec;

import javax.annotation.Resource;
import java.util.Date;

/**
 * desc:
 *
 * @author Rao
 * @Date 2022/05/30
 **/
public abstract class AbstractSyncManager {

    @Resource
    protected RedissonClient redissonClient;
    @Resource
    protected DistributedLock distributedLock;
    @Resource
    protected ApplicationProperties applicationProperties;
    @Resource
    protected SegmentedQueryManager segmentedQueryManager;
    @Resource
    protected K3cloudRemoteService k3cloudRemoteService;

    /**
     * 获取上次同步时间
     * @param syncDateTimeKey
     * @return
     */
    protected Date getLastSyncDateTime(String syncDateTimeKey,String supplierCode){
        RMap<String, Date> syncDateTimeMap = redissonClient.getMap( BisSyncConstants.TASK_SYNC_DATA_TIME_MAP_KEY, JsonJacksonCodec.INSTANCE);
        return syncDateTimeMap.getOrDefault( syncDateTimeKey, applicationProperties.getSchedulingTask().getDefaultSyncDateTime( supplierCode ) );
    }

    /**
     * 更新回最后同步时间
     * @param syncDateTimeKey
     * @param syncDate
     */
    protected void updateLastSyncDateTime( String syncDateTimeKey,Date syncDate ){
        RMap<String, Date> syncDataTimeMap = redissonClient.getMap( BisSyncConstants.TASK_SYNC_DATA_TIME_MAP_KEY, JsonJacksonCodec.INSTANCE);
        syncDataTimeMap.fastPut( syncDateTimeKey  ,syncDate );
    }


}
