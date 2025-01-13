package kr.jsh.ecommerce.base.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import kr.jsh.ecommerce.base.dto.BaseErrorCode;

import java.time.LocalDateTime;

public class BaseResponse {
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS")
    private LocalDateTime timestamp = LocalDateTime.now();
    private String message = BaseErrorCode.SUCCESS.getMessage(); // 예외 메세지
    private String status = BaseErrorCode.SUCCESS.getHttpStatus().toString(); // HTTP 상태 코드

    public void setMessage(String message) {
        this.message = message;
    }
}
