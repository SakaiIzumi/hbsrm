package net.bncloud.contract.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import net.bncloud.common.api.IResultCode;
import net.bncloud.common.api.R;
import net.bncloud.common.base.domain.QueryParam;
import net.bncloud.contract.entity.Contract;
import net.bncloud.base.BaseService;
import net.bncloud.contract.param.ContractExistValidateParam;
import net.bncloud.contract.param.ContractQueryParam;
import net.bncloud.contract.param.ContractSaveParam;
import net.bncloud.contract.vo.ContractStatisticsVo;
import net.bncloud.contract.vo.ContractVo;

import java.util.List;

/**
 * <p>
 * 合同信息表 服务类
 * </p>
 *
 * @author huangtao
 * @since 2021-03-12
 */
public interface ContractService extends BaseService<Contract> {

    /**
     * 保存合同
     * @param contract
     */
    R<ContractSaveParam> saveContract(ContractSaveParam contract,boolean confSaveParam);

    /**
     * 更新合同
     * @param contract
     */
    R<ContractSaveParam> updateContract(ContractSaveParam contract);

    /**
     * 获取合同数量统计信息
     * @return
     */
    ContractStatisticsVo getStatisticsInfo();

    /**
     * 发送合同
     * @param
     * @return
     */
    boolean send(Long id);

    /**
     * 撤回合同
     * @param contractId 合同ID
     * @return
     */
    boolean withdraw(Long contractId);

    /**
     * 重新签约
     * @param contractId
     * @return
     */
    boolean resign(String contractId);


    /**
     * 复制生成新合同
     * @param historyContractId 原合同ID
     * @return 合同
     */
    Contract copy(Long historyContractId, ContractSaveParam contract);

    /**
     * 提送
     * @param contractId 合同ID
     * @return
     */
    boolean remind(String contractId);

    /**
     * 获取合同详情
     * @param id
     * @return
     */
    ContractVo getContractInfo(Long id);

    /**
     * 批量设置可操作按钮
     * @param records
     */
    void buildPermissionButtonBatch(List<ContractVo> records);

    /**
     * 作废合同
     * @param contractId
     * @return
     */
    boolean invalid(Long contractId);

    /**
     * 发送合同（列表操作）
     * @param contractId 合同ID
     * @return
     */
    boolean sendByContractId(Long contractId,boolean confSaveParam);

    /**
     * 设置合同附件
     * @param record
     */
    void buildAttachment(ContractVo record);

    /**
     * 批量设置合同附件
     * @param records
     */
    void buildAttachmentBatch(List<ContractVo> records);

    /**
     * 合同参数，唯一校验
     */
    void existValidate(ContractExistValidateParam param);

    /**
     * 自定义分页
     * @param page
     * @param queryParam
     * @return
     */
    IPage<Contract>  selectPage(IPage<Contract> page, QueryParam<ContractQueryParam> queryParam);

//    Contract getByContractCode(String contractCode);
    Contract getByContractCode(Integer contractCode);
}
