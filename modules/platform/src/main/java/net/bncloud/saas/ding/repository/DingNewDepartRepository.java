package net.bncloud.saas.ding.repository;

import net.bncloud.saas.ding.domain.DingNewDepart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DingNewDepartRepository extends JpaRepository<DingNewDepart, Long> {
    @Query(value = "SELECT d.* FROM ss_ding_new_depart d WHERE d.depart_id = ?1 limit 1", nativeQuery = true)
    DingNewDepart findByDepartId(Long departId);

    @Query(value = "SELECT d.* FROM ss_ding_new_depart d WHERE d.parent_id = ?1 limit 1", nativeQuery = true)
    DingNewDepart findByParentId(Long parentId);

    //根据父ID递归查询旗下所有子节点（包含本身）
    @Query(value = "SELECT rd.* \n" +
            "FROM (SELECT * FROM ss_ding_new_depart WHERE parent_id IS NOT NULL) rd,\n" +
            "     (SELECT @pid \\:= ?1) pd \n" +
            "WHERE FIND_IN_SET(parent_id, @pid) > 0 \n" +
            "AND @pid \\:= concat(@pid, ',', id) \n" +
            "union select * from ss_ding_new_depart where id = @pid order by level desc", nativeQuery = true)
    List<DingNewDepart> findAndAllChildByPid(Long pid);

    @Query("select du.ddId from DingNewDepart du where du.departId in ?1")
    List<Long> findDdIdByDepartIdIn(List<Long> departIds);
}