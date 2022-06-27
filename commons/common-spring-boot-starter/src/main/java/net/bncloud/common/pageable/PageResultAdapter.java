package net.bncloud.common.pageable;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;

public class PageResultAdapter<T> extends PageImpl<T> {
    private static final long serialVersionUID = 4208477587330385315L;

    public PageResultAdapter(Page<T> page) {
        this(page.getContent(), page.getPageable(), page.getTotalElements());
    }

    public PageResultAdapter(List<T> content, Pageable pageable, long total) {
        super(content, pageable, total);
    }

    @JsonIgnore
    @Override
    public Pageable getPageable() {
        return super.getPageable();
    }
    @JsonIgnore
    @Override
    public int getNumber() {
        return super.getNumber();
    }
}
