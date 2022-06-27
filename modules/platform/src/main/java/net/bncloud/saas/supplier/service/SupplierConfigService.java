package net.bncloud.saas.supplier.service;

import lombok.AllArgsConstructor;
import net.bncloud.common.security.SecurityUtils;
import net.bncloud.saas.supplier.domain.*;
import net.bncloud.saas.supplier.repository.*;
import net.bncloud.saas.supplier.service.query.ConfigQuery;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.Predicate;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class SupplierConfigService {
    private final TagRepository tagRepository;
    private final TypeRepository typeRepository;
    private final TagConfigItemRepository tagConfigItemRepository;
    private final TypeConfigItemRepository typeConfigItemRepository;
    private final SupplierTypeItemRepository supplierTypeItemRepository;
    private final SupplierTagItemRepository supplierTagItemRepository;


    public Page<Tag> pageQueryTagConfig(Long orgId, ConfigQuery param, Pageable pageable) {
        return tagRepository.findAll((Specification<Tag>) (root, query, criteriaBuilder) -> {
            final Predicate predicate = criteriaBuilder.conjunction();
            predicate.getExpressions().add(criteriaBuilder.equal(root.get("orgId"), orgId));
            if (param == null) {
                return predicate;
            }
            if (StringUtils.isNotBlank(param.getGroup())) {
                predicate.getExpressions().add(criteriaBuilder.like(root.get("group"), "%" + param.getGroup() + "%"));
            }
            return predicate;
        }, pageable);
    }

    public Tag getTagConfigDetail(Long id) {
        return tagRepository.findById(id).orElse(null);
    }

    @Transactional
    public void saveTagConfig(Tag tag) {
        if (tag.getOrgId() == null) {
            SecurityUtils.getCurrentOrg().ifPresent(org -> {
                tag.setOrgId(org.getId()
                );
            });
        }
        if (tag.getId() != null) {
            final Optional<Tag> exist = tagRepository.findById(tag.getId());
            if (exist.isPresent()) {
                final Tag old = exist.get();
                //不维护标签,只维护组名
                old.setGroup(tag.getGroup());
                //判断是否有供应商使用
                tagRepository.save(old);
            } else {
                throw new IllegalArgumentException("修改时id必须为空");
            }

        } else {
            if (tagRepository.findByOrgIdAndGroup(tag.getOrgId(), tag.getGroup()).isPresent()) {
                throw new IllegalArgumentException("标签组[" + tag.getGroup() + "]已存在");
            }
            tag.setTagsList(null);
            tagRepository.save(tag);
        }
    }

    public void deleteTagConfig(Long id) {
        List<TagConfigItem> allByTagConfigId = tagConfigItemRepository.findAllByTag(Tag.of(id));
        if (allByTagConfigId.size() > 0) {
            throw new IllegalArgumentException("标签组存在子标签");
        }
        //判断是否有供应商使用
        tagRepository.findById(id).ifPresent(tagRepository::delete);

    }


    public Page<Type> pageQueryTypeConfig(Long orgId, ConfigQuery param, Pageable pageable) {
        return typeRepository.findAll((Specification<Type>) (root, query, criteriaBuilder) -> {
            final Predicate predicate = criteriaBuilder.conjunction();
            predicate.getExpressions().add(criteriaBuilder.equal(root.get("orgId"), orgId));
            if (param == null) {
                return predicate;
            }
            if (StringUtils.isNotBlank(param.getGroup())) {
                predicate.getExpressions().add(criteriaBuilder.like(root.get("group"), "%" + param.getGroup() + "%"));
            }
            return predicate;
        }, pageable);
    }

    public Type getTypeConfigDetail(Long id) {
        return typeRepository.findById(id).orElse(null);
    }

    public void saveTypeConfig(Type type) {
        if (type.getOrgId() == null) {
            SecurityUtils.getCurrentOrg().ifPresent(org -> {
                type.setOrgId(org.getId());
            });
        }
        if (type.getOrgId() == null) {
            SecurityUtils.getCurrentOrg().ifPresent(org -> {
                type.setOrgId(org.getId());
            });
        }
        if (type.getId() != null) {
            final Optional<Type> exist = typeRepository.findById(type.getId());
            if (exist.isPresent()) {
                final Type old = exist.get();
                old.setGroup(type.getGroup());
                typeRepository.save(old);
            } else {
                throw new IllegalArgumentException("修改时id必须为空");
            }
        } else {
            if (typeRepository.findOneByOrgIdAndGroup(type.getOrgId(), type.getGroup()).isPresent()) {
                throw new IllegalArgumentException("类型组[" + type.getGroup() + "]已存在");
            }
            typeRepository.save(type);
        }
    }

    public void deleteTypeConfig(Long id) {
        List<TypeConfigItem> allByTypeConfigId = typeConfigItemRepository.findAllByType(Type.of(id));
        if (allByTypeConfigId.size() > 0) {
            throw new IllegalArgumentException("标签组存在子标签");
        }

        typeRepository.findById(id).ifPresent(typeRepository::delete);
    }

    @Transactional
    public void saveTagConfig(TagConfigItem tagConfigItem) {

        List<SupplierTagItem> supplierTagItem = supplierTagItemRepository.findAllByTagId(tagConfigItem.getId());

        //修改
        if (tagConfigItem.getId() != null) {
            TagConfigItem item = tagConfigItemRepository.findById(tagConfigItem.getId()).get();
            //修改标签item
            List<TagConfigItem> tagConfigItemList = tagConfigItemRepository.findAllByTagAndItemAndIdNot(item.getTag(), tagConfigItem.getItem(), tagConfigItem.getId());
            if (tagConfigItemList.size() > 0) {
                throw new IllegalArgumentException(tagConfigItem.getItem() + "已经存在此标签组");
            }

            for (SupplierTagItem tagItem : supplierTagItem) {

                tagItem.setTags(tagConfigItem.getItem());

                supplierTagItemRepository.save(tagItem);
            }
            item.setItem(tagConfigItem.getItem());

            tagConfigItemRepository.saveAndFlush(item);
        }//新增
        else if (tagConfigItem.getId() == null && tagConfigItem.getTagConfigId() != null) {
            List<TagConfigItem> tagConfigItemList = tagConfigItemRepository.findAllByTagAndItem(Tag.of(tagConfigItem.getTagConfigId()), tagConfigItem.getItem());
            if (tagConfigItemList.size() > 0) {
                throw new IllegalArgumentException(tagConfigItem.getItem() + "已经存在此标签组");
            }
            Tag tag = tagRepository.getOne(tagConfigItem.getTagConfigId());
            tagConfigItem.setTag(tag);
            tagConfigItemRepository.saveAndFlush(tagConfigItem);
        } else if (tagConfigItem.getTagConfigId() == null) {
            throw new IllegalArgumentException("添加未选择供应商标签组");
        }


    }

    @Transactional
    public void saveTypeConfigItem(TypeConfigItem typeConfigItem) {
        List<SupplierTypeItem> supplierTypeItem = supplierTypeItemRepository.findAllByTypeId(typeConfigItem.getId());

        if (typeConfigItem.getId() != null) {
            TypeConfigItem item = typeConfigItemRepository.findById(typeConfigItem.getId()).get();


            List<TypeConfigItem> typeConfigItemList = typeConfigItemRepository.findAllByTypeAndItemAndIdNot(Type.of(item.getTypeConfigId()), typeConfigItem.getItem(), typeConfigItem.getId());
            if (typeConfigItemList.size() > 0) {
                throw new IllegalArgumentException(typeConfigItem.getItem() + "已经存在此标签组");
            }
            for (SupplierTypeItem typeItem : supplierTypeItem) {
                typeItem.setTypes(typeConfigItem.getItem());
                supplierTypeItemRepository.save(typeItem);
            }
            item.setItem(typeConfigItem.getItem());
            typeConfigItemRepository.save(item);
        } else if (typeConfigItem.getTypeConfigId() != null) {
            List<TypeConfigItem> tagConfigItemList = typeConfigItemRepository.findAllByTypeAndItem(Type.of(typeConfigItem.getTypeConfigId()), typeConfigItem.getItem());
            if (tagConfigItemList.size() > 0) {
                throw new IllegalArgumentException(typeConfigItem.getItem() + "已经存在此标签组");
            }
            Type type = typeRepository.getOne(typeConfigItem.getTypeConfigId());
            typeConfigItem.setType(type);
            typeConfigItemRepository.saveAndFlush(typeConfigItem);
        } else {
            throw new IllegalArgumentException("添加未选择供应商标签组");
        }

    }

    public void deleteTagItem(Long id) {
        List<SupplierTagItem> supplierTagItem = supplierTagItemRepository.findAllByTagId(id);
        if (supplierTagItem.size() > 0) {
            throw new IllegalArgumentException("标签被使用");
        }

        Optional<TagConfigItem> tagConfigItem = tagConfigItemRepository.findById(id);
        if (!tagConfigItem.isPresent()) {
            throw new IllegalArgumentException("标签不存在");
        }
        tagConfigItemRepository.deleteById(id);
    }

    public void deleteTypeItem(Long id) {
        List<SupplierTypeItem> supplierTypeItem = supplierTypeItemRepository.findAllByTypeId(id);
        if (supplierTypeItem.size() > 0) {
            throw new IllegalArgumentException("类型被使用");
        }
        Optional<TypeConfigItem> typeConfigItem = typeConfigItemRepository.findById(id);
        if (!typeConfigItem.isPresent()) {
            throw new IllegalArgumentException("类型不存在");
        }
        typeConfigItemRepository.deleteById(id);
    }
}
