package net.bncloud.msk3cloud.client;

import com.kingdee.bos.webapi.sdk.QueryParam;
import com.kingdee.bos.webapi.sdk.SaveParam;
import net.bncloud.msk3cloud.kingdee.entity.common.EntityResult;
import net.bncloud.msk3cloud.kingdee.request.GroupQueryParam;
import net.bncloud.msk3cloud.kingdee.response.ResponseResult;
import net.bncloud.msk3cloud.kingdee.response.Result;

import java.util.List;

/**
 * @author Rao
 * @Date 2022/01/07
 **/
public interface ApiClient {

    /**
     * 提交
     * @param formId
     * @param data
     * @return
     * @throws Exception
     */
    @Deprecated
    String submit(String formId, String data) throws Exception;

    /**
     * view
     * @param formId
     * @param data
     * @return
     * @throws Exception
     */
    @Deprecated
    String view(String formId, String data) throws Exception;

    /**
     * 查询物料]]分组信息
     * @param groupQueryParam 查询参数
     * @param
     * @return
     * @throws Exception
     */
    public <T> ResponseResult<T> queryGroupInfo(GroupQueryParam groupQueryParam) throws Exception;
    /**
     * 单据查询
     * @param queryParam
     * @return
     */
    List<List<Object>> documentQuery(QueryParam queryParam) throws Exception;

    /**
     * 保存或更新
     * @param <P>
     * @param formId
     * @param saveParam
     * @return
     */
    <P> EntityResult saveOrUpdate(String formId, SaveParam<P> saveParam) throws Exception;


    /**
     * 带返回对象的 保存or更新
     *   k3cloud 实际调用 is save
     * @return
     */
    <P> String saveOrUpdateWithReturnObj(String formId, SaveParam<P> saveParam) throws Exception;

    /**
     * 带返回对象的 保存or更新
     *   k3cloud 实际调用 is save
     * @return
     */
    <P> String saveOrUpdateWithReturnObj(String formId, String params) throws Exception;
}
