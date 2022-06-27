package net.bncloud.bis.service.bis;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import net.bncloud.bis.constant.DatasourceConstants;
import net.bncloud.bis.dao.UfHzhbxxbDt3Dao;
import net.bncloud.bis.model.oa.UfHzhbxxbDt3;
import net.bncloud.bis.service.UfHzhbxxbDt3Service;
import org.springframework.stereotype.Service;

/**
 * desc: d
 *
 * @author Rao
 * @Date 2022/01/24
 **/
@DS( DatasourceConstants.BIS )
@Service("bisUfHzhbxxbDt3ServiceImpl")
@Slf4j
public class UfHzhbxxbDt3ServiceImpl extends ServiceImpl<UfHzhbxxbDt3Dao, UfHzhbxxbDt3> implements UfHzhbxxbDt3Service {



}
