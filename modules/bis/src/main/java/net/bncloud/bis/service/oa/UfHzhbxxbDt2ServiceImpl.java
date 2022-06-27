package net.bncloud.bis.service.oa;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import net.bncloud.bis.constant.DatasourceConstants;
import net.bncloud.bis.dao.UfHzhbxxbDt2Dao;
import net.bncloud.bis.model.oa.UfHzhbxxbDt2;
import net.bncloud.bis.service.UfHzhbxxbDt2Service;
import org.springframework.stereotype.Service;

/**
 * desc: d
 *
 * @author Rao
 * @Date 2022/01/24
 **/
@DS( DatasourceConstants.MS_OA )
@Service(value = "oaUfHzhbxxbDt2ServiceImpl")
@Slf4j
public class UfHzhbxxbDt2ServiceImpl extends ServiceImpl<UfHzhbxxbDt2Dao, UfHzhbxxbDt2> implements UfHzhbxxbDt2Service {

}
