package net.bncloud.bis.service.oa;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import net.bncloud.bis.constant.DatasourceConstants;
import net.bncloud.bis.dao.UfHzhbxxbDao;
import net.bncloud.bis.model.oa.UfHzhbxxb;
import net.bncloud.bis.service.UfHzhbxxbService;
import org.springframework.stereotype.Service;

/**
 * desc: d
 *
 * @author Rao
 * @Date 2022/01/24
 **/
@DS( DatasourceConstants.MS_OA )
@Service("oaUfHzhbxxbServiceImpl")
@Slf4j
public class UfHzhbxxbServiceImpl extends ServiceImpl<UfHzhbxxbDao, UfHzhbxxb> implements UfHzhbxxbService {

}
