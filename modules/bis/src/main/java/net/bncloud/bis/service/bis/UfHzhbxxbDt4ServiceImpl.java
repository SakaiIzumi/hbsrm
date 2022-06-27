package net.bncloud.bis.service.bis;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import net.bncloud.bis.constant.DatasourceConstants;
import net.bncloud.bis.dao.UfHzhbxxbDt4Dao;
import net.bncloud.bis.model.oa.UfHzhbxxbDt4;
import net.bncloud.bis.service.UfHzhbxxbDt4Service;
import org.springframework.stereotype.Service;

/**
 * desc: d
 *
 * @author Rao
 * @Date 2022/01/24
 **/
@DS( DatasourceConstants.BIS )
@Service("bisUfHzhbxxbDt4ServiceImpl")
@Slf4j
public class UfHzhbxxbDt4ServiceImpl extends ServiceImpl<UfHzhbxxbDt4Dao, UfHzhbxxbDt4> implements UfHzhbxxbDt4Service {



}
