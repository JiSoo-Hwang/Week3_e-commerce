package kr.jsh.ecommerce.base.dto.response;

import org.springframework.data.domain.Page;

public class BaseResponsePage<T> extends BaseResponseContent {

    private int page;
    private int size;
    private long total;

    public BaseResponsePage(Page<T> page) {
        super(page.getContent());
        this.page = page.getNumber() + 1;
        this.size = page.getSize();
        this.total = page.getTotalElements();
    }
}
