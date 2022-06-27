package net.bncloud.contract.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import net.bncloud.common.api.R;
import net.bncloud.common.base.domain.QueryParam;
import net.bncloud.contract.entity.Contract;
import net.bncloud.base.BaseService;
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
    R<ContractSaveParam> saveContract(ContractSaveParam contract);

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
     * @param contract 合同
     * @return
     */
    boolean send(ContractSaveParam contract);

    /**
     * 撤回合同
     * @param contractId 合同ID
     * @return
     */
    boolean withdraw(String contractId);

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
     * 作废合同
     * @param contractId
     * @return
     */
    boolean invalid(Long contractId);

    /**
     * 批量设置可操作按钮
     * @param records
     */
    void buildPermissionButtonBatch(List<ContractVo> records);

    /**
     * 确认合同
     * @param contractId
     * @return
     */
    boolean confirm(Long contractId);

    /**
     * 拒绝合同
     * @param contractId
     * @return
     */
    boolean reject(Long contractId);

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

    IPage<Contract> selectPage(IPage<Contract> page, QueryParam<ContractQueryParam> queryParam);

}
