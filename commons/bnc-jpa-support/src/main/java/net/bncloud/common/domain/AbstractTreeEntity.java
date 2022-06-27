package net.bncloud.common.domain;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.hibernate.annotations.Where;

import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import javax.persistence.OneToMany;
import javax.persistence.Transient;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@MappedSuperclass
public abstract class AbstractTreeEntity<T extends AbstractTreeEntity<?>> extends AbstractOrderEntity {

    private static final long serialVersionUID = 4910356855976770283L;

    @Column(name = "parent_id")
    private Long parentId;

    /** 父级对象 */
    @JsonIgnoreProperties("children")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id", insertable = false, updatable = false)
    @NotFound(action = NotFoundAction.IGNORE)
    protected T parent;

    /** 子集 */
    @JsonIgnoreProperties("parent")
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "parent")
    @Where(clause = "is_deleted=false")
    protected List<T> children = new ArrayList<>(0);


    /**
     * 获得节点列表。从父节点到自身。
     *
     */
    @SuppressWarnings("unchecked")
    @Transient
    @JSONField(serialize = false)
    public List<T> getNodeList() {
        LinkedList<T> list = new LinkedList<T>();
        T node = (T) this;
        while (node != null) {
            list.addFirst(node);
            if (node.getParent() != null) {
                node = (T) node.getParent();
            } else {
                break;
            }
        }
        return list;
    }


    /**
     * 获得深度
     *
     * @return 第一层为0，第二层为1，以此类推。
     */
    @SuppressWarnings("unchecked")
    @Transient
    public int getDeep() {
        int deep = 0;
        T parent = getParent();
        while (parent != null) {
            deep++;
            parent = (T) parent.getParent();
        }
        return deep;
    }



    public T getParent() {
        return parent;
    }

    public void setParent(T parent) {
        this.parent = parent;
    }

    public Long getParentId(){
        return parentId;
    }

    public void setParentId(Long parentId){
        this.parentId = parentId;
    }

    public List<T> getChildren() {
        return children;
    }

    public void setChildren(List<T> children) {
        this.children = children;
    }
}
