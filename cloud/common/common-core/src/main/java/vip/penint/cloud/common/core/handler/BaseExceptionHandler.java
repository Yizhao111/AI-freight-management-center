package vip.penint.cloud.common.core.handler;

import com.netflix.hystrix.exception.HystrixRuntimeException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import vip.penint.cloud.common.core.entity.AjaxResult;
import vip.penint.cloud.common.core.entity.constant.StringConstant;
import vip.penint.cloud.common.core.exception.FileDownloadException;
import vip.penint.cloud.common.core.exception.PenintException;
import vip.penint.cloud.common.core.utils.PenintUtil;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Path;
import java.util.List;
import java.util.Set;

/**
 *
 */
@Slf4j
public class BaseExceptionHandler {


    @ExceptionHandler({HystrixRuntimeException.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public AjaxResult handleFeignException(HystrixRuntimeException e) {
        return AjaxResult.error("服务繁忙，请稍后再试");
    }

    @ExceptionHandler(value = Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public AjaxResult handleException(Exception e) {
        String message = PenintUtil.containChinese(e.getMessage()) ? e.getMessage() : "系统内部异常";
        log.error(message, e);
        return AjaxResult.error(message);
    }

    @ExceptionHandler(value = PenintException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public AjaxResult handlePenintException(PenintException e) {
        log.error("Penint-Cloud系统异常", e);
        return AjaxResult.error(e.getMessage());
    }

    /**
     * 统一处理请求参数校验(实体对象传参)
     *
     * @param e BindException
     * @return AjaxResult
     */
    @ExceptionHandler(BindException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public AjaxResult handleBindException(BindException e) {
        StringBuilder message = new StringBuilder();
        List<FieldError> fieldErrors = e.getBindingResult().getFieldErrors();
        for (FieldError error : fieldErrors) {
            message.append(error.getField()).append(error.getDefaultMessage()).append(StringConstant.COMMA);
        }
        message = new StringBuilder(message.substring(0, message.length() - 1));
        log.error(message.toString());
        return AjaxResult.error(message.toString());
    }

    /**
     * 统一处理请求参数校验(普通传参)
     *
     * @param e ConstraintViolationException
     * @return AjaxResult
     */
    @ExceptionHandler(value = ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public AjaxResult handleConstraintViolationException(ConstraintViolationException e) {
        StringBuilder message = new StringBuilder();
        Set<ConstraintViolation<?>> violations = e.getConstraintViolations();
        for (ConstraintViolation<?> violation : violations) {
            Path path = violation.getPropertyPath();
            String[] pathArr = StringUtils.splitByWholeSeparatorPreserveAllTokens(path.toString(), ".");
            message.append(pathArr[1]).append(violation.getMessage()).append(StringConstant.COMMA);
        }
        message = new StringBuilder(message.substring(0, message.length() - 1));
        log.error(message.toString());
        return AjaxResult.error(message.toString());
    }

    /**
     * 统一处理请求参数校验(json)
     *
     * @param e ConstraintViolationException
     * @return AjaxResult
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public AjaxResult handlerMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        StringBuilder message = new StringBuilder();
        for (FieldError error : e.getBindingResult().getFieldErrors()) {
            message.append(error.getField()).append(error.getDefaultMessage()).append(StringConstant.COMMA);
        }
        message = new StringBuilder(message.substring(0, message.length() - 1));
        log.error(message.toString());
        return AjaxResult.error(message.toString());
    }

    @ExceptionHandler(value = FileDownloadException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public void handleFileDownloadException(FileDownloadException e) {
        log.error("FileDownloadException", e);
    }

    @ExceptionHandler(value = AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public AjaxResult handleAccessDeniedException() {
        return AjaxResult.error("没有权限访问该资源");
    }

    @ExceptionHandler(value = HttpMediaTypeNotSupportedException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public AjaxResult handleHttpMediaTypeNotSupportedException(HttpMediaTypeNotSupportedException e) {
        String message = "该方法不支持" + StringUtils.substringBetween(e.getMessage(), "'", "'") + "媒体类型";
        log.error(message);
        return AjaxResult.error(message);
    }

    @ExceptionHandler(value = HttpRequestMethodNotSupportedException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public AjaxResult handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e) {
        String message = "该方法不支持" + StringUtils.substringBetween(e.getMessage(), "'", "'") + "请求方法";
        log.error(message);
        return AjaxResult.error(message);
    }

}
