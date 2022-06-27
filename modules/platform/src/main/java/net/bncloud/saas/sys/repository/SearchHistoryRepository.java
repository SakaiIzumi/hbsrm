package net.bncloud.saas.sys.repository;

import net.bncloud.saas.sys.domain.SearchHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

/**
 * @ClassName SearchHistoryRepository
 * @Description: 搜索历史repository
 * @Author Administrator
 * @Date 2021/5/10
 * @Version V1.0
 **/
@Repository
public interface SearchHistoryRepository extends JpaRepository<SearchHistory,Long>, JpaSpecificationExecutor<SearchHistory> {

}
