package kr.jsh.ecommerce.base.dto;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum BaseErrorCode {
    // General Error
    SUCCESS(HttpStatus.OK, "Success"),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "Internal server error"),
    BAD_REQUEST(HttpStatus.BAD_REQUEST, "Bad request"),
    INVALID_PARAMETER(HttpStatus.BAD_REQUEST, "Invalid parameter : {0}"),
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "Unauthorized"),
    FORBIDDEN(HttpStatus.FORBIDDEN, "Forbidden"),
    NOT_FOUND(HttpStatus.NOT_FOUND, "Resource not found : {0}"),
    METHOD_NOT_ALLOWED(HttpStatus.METHOD_NOT_ALLOWED, "Method not allowed"),
    CONFLICT(HttpStatus.CONFLICT, "Conflict"),
    UNSUPPORTED_MEDIA_TYPE(HttpStatus.UNSUPPORTED_MEDIA_TYPE, "Unsupported media type"),

    // Custom Error
    OUT_OF_STOCK(HttpStatus.CONFLICT, "{0} 의 재고가 부족합니다."),
    INSUFFICIENT_BALANCE(HttpStatus.CONFLICT, "{0} 님의 잔액이 부족합니다."),
    COUPON_EXPIRED(HttpStatus.GONE,"쿠폰이 만료되었습니다. 만료일 : {0}"),
    ALREADY_USED_COUPON(HttpStatus.CONFLICT,"이미 사용된 쿠폰입니다. 사용일 : {0}")
    ;

    private final HttpStatus httpStatus;
    private final String message;

    BaseErrorCode(HttpStatus httpStatus, String message) {
        this.httpStatus = httpStatus;
        this.message = message;
    }
}
