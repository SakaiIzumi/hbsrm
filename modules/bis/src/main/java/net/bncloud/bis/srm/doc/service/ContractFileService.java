package net.bncloud.bis.srm.doc.service;

import com.baomidou.mybatisplus.extension.service.IService;
import net.bncloud.bis.srm.doc.model.ContractFile;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface ContractFileService extends IService<ContractFile> {
}
