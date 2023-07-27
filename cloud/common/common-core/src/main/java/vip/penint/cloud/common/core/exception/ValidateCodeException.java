package vip.penint.cloud.common.core.exception;

/**
 * 验证码类型异常
 *
 * 
 */
public class ValidateCodeException extends RuntimeException {

    private static final long serialVersionUID = 7514854456967620043L;

    public ValidateCodeException(String message) {
        super(message);
    }
}
