package kr.jsh.ecommerce.base.dto.response;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
public class BaseResponseContent<T> extends BaseResponse {

    private T content;

    public BaseResponseContent(T content) {
        this.content = content;
    }
}
