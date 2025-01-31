package kr.jsh.ecommerce.base.exception;

import kr.jsh.ecommerce.base.dto.BaseErrorCode;
import lombok.Getter;

import java.text.MessageFormat;

@Getter
public class BaseCustomException extends RuntimeException {
    private final BaseErrorCode baseErrorCode;
    private String[] msgArgs;

    public BaseCustomException(BaseErrorCode baseErrorCode) {
        super(baseErrorCode.getMessage());
        this.baseErrorCode = baseErrorCode;
    }

    public BaseCustomException(BaseErrorCode baseErrorCode, String[] msgArgs) {
        super(MessageFormat.format(baseErrorCode.getMessage(), (Object[]) msgArgs));
        this.baseErrorCode = baseErrorCode;
        this.msgArgs = msgArgs;
    }

    public BaseCustomException(BaseErrorCode baseErrorCode, Throwable throwable) {
        super(baseErrorCode.getMessage(), throwable);
        this.baseErrorCode = baseErrorCode;
    }

    public BaseCustomException(BaseErrorCode baseErrorCode, String[] msgArgs, Throwable throwable) {
        super(baseErrorCode.getMessage(), throwable);
        this.baseErrorCode = baseErrorCode;
        this.msgArgs = msgArgs;
    }

    /**
     * 동적으로 메시지를 형식화하여 반환합니다.
     * BaseErrorCode에 정의된 메시지의 "{0}", "{1}"와 같은 포맷을
     * msgArgs 배열의 값으로 대체합니다.
     *
     * 예시:
     * - BaseErrorCode 메시지: "Invalid parameter: {0}"
     * - msgArgs: {"itemId"}
     * - 결과: "Invalid parameter: itemId"
     */
    public String formatMessage() {
        String message = baseErrorCode.getMessage();
        if (msgArgs != null && msgArgs.length > 0) {
            return MessageFormat.format(message, (Object[]) msgArgs); // MessageFormat 사용
        }
        return message; // msgArgs가 없으면 기본 메시지 반환
    }
}
