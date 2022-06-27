package net.bncloud.saas.tenant.repository;

import net.bncloud.saas.tenant.domain.OrgDepartment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrgDepartmentRepository extends JpaRepository<OrgDepartment, Long> {
    Optional<OrgDepartment> findOneByOrgIdAndSupplierCode(Long orgId, String supplierCode);

    /**
     * 根据组织idList查询部门列表
     */
    List<OrgDepartment> findAllByOrgIdIn(List orgIdList);

    /**
     * 查询根部门
     * @return 部门列表
     */
    List<OrgDepartment> findAllByCompanyIdAndParentIsNullAndDeleted(Long companyId,Boolean isDeleted);

    /**
     * 根据公司租户ID，父部门id查询部门列表
     * @param parent 父部门
     * @return 部门列表
     */
    List<OrgDepartment> findAllByParentAndDeleted(OrgDepartment parent, Boolean isDeleted);

    Optional<OrgDepartment> findOneByParentAndName(OrgDepartment parent, String name); // TODO company/org

    Optional<OrgDepartment> findOneByParentIdAndName(Long parentId, String name); // TODO company/org

    List<OrgDepartment> findAllByParentId(Long parentId); // TODO company/org

    //根据最小组织ID和最小主键ID查所属该组织的二级部门
    @Query(value = "SELECT d.* FROM ss_tenant_org_department d WHERE \n" +
            "d.parent_id = (select o.root_dept_id from ss_tenant_organization o where o.is_deleted=0 order by o.id asc limit 1) \n" +
            "order by d.id asc \n" +
            "limit 1", nativeQuery = true)
    OrgDepartment findSmallDepart();

    //根据组织ID和主键ID查刚刚大于该主键ID且所属该组织的二级部门
    @Query(value = "SELECT d.* FROM ss_tenant_org_department d WHERE d.org_id = ?1 and d.parent_id = ?2 and d.id > ?3 order by d.id asc limit 1", nativeQuery = true)
    OrgDepartment findGtById(Long orgId, Long parentId, Long id);

    //根据组织ID查刚刚大于该组织ID的组织（全表循环-即传入表中最大组织ID又查回最小组织ID）-且根据前置条件查出所属该组织的最小主键ID的二级部门
    @Query(value = "select p.* from ss_tenant_org_department p\n" +
            ",\n" +
            "(select o.id as oid, o.root_dept_id as root_dept_id from ss_tenant_organization o where o.is_deleted=0 and o.id=\n" +
            "(select d.org_id from ss_tenant_org_department d where \n" +
            "d.org_id > (select if((select max(dd.org_id) from ss_tenant_org_department dd) > ?1 , ?1 , 0)) \n" +
            "order by d.org_id asc \n" +
            "limit 1)) a\n" +
            "where p.org_id=a.oid and p.parent_id=a.root_dept_id order by p.id asc limit 1", nativeQuery = true)
    OrgDepartment findGtByOrgId(Long orgId);

    //根据父ID递归查询旗下所有子节点（包含本身）
    @Query(value = "SELECT rd.*\n" +
            "FROM (SELECT * FROM ss_tenant_org_department WHERE parent_id IS NOT NULL) rd,\n" +
            "     (SELECT @pid \\:= ?1) pd \n" +
            "WHERE FIND_IN_SET(parent_id, @pid) > 0 \n" +
            "AND @pid \\:=concat(@pid, ',', id) \n" +
            "union select * from ss_tenant_org_department where id = @pid", nativeQuery = true)
    List<OrgDepartment> findAndAllChildByPid(Long pid);

    //根据orgId查顶级部门
    @Query(value = "SELECT d.* FROM ss_tenant_org_department d WHERE d.org_id = ?1 and (d.parent_id is null or d.parent_id='') order by d.id asc limit 1", nativeQuery = true)
    OrgDepartment findTopDeptByOrgId(Long orgId);

    //根据员工查部门
    @Query(value = "select d.* from ss_tenant_org_department d\n" +
    "left join ss_tenant_org_employee e on d.id=e.dept_id and e.enabled=1 and e.is_deleted=0\n" +
    "where e.user_id=?1 and d.org_id = ?2 and d.supplier_id > 0 group by d.supplier_id", nativeQuery = true)
    List<OrgDepartment> findByEmployee(Long uid, Long orgId);

    List<OrgDepartment> findAllBySupplierId(Long supplierId);
}
