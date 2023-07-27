//package vip.penint.cloud.server.system.aspect;
//
//import com.alibaba.fastjson.JSONObject;
//import vip.penint.cloud.common.core.entity.system.SysOperationLog;
//import vip.penint.cloud.common.core.exception.PenintException;
//import vip.penint.cloud.common.core.utils.PenintUtil;
//import vip.penint.cloud.server.system.annotation.Log;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.apache.commons.lang3.StringUtils;
//import org.aspectj.lang.ProceedingJoinPoint;
//import org.aspectj.lang.annotation.Around;
//import org.aspectj.lang.annotation.Aspect;
//import org.aspectj.lang.annotation.Pointcut;
//import org.springframework.stereotype.Component;
//import vip.penint.cloud.server.system.service.ISysOperationLogService;
//
//import java.lang.reflect.Method;
//
///**
// *
// */
//@Aspect
//@Slf4j
//@Component
//@RequiredArgsConstructor
//public class LogAspect extends BaseAspectSupport {
//
//    private final ISysOperationLogService logService;
//
//    @Pointcut("execution(* vip.penint.cloud.server.system.controller.*.*(..)) && @annotation(vip.penint.cloud.server.system.annotation.Log)")
//    public void pointcut() {
//    }
//
//    @Around("pointcut()")
//    public Object around(ProceedingJoinPoint point) throws PenintException {
//        Object result;
//        Method targetMethod = resolveMethod(point);
//        Log annotation = targetMethod.getAnnotation(Log.class);
//        String operation = annotation.operation();
//        long start = System.currentTimeMillis();
//        try {
//            result = point.proceed();
//            if (StringUtils.isNotBlank(operation)) {
//                String username = PenintUtil.getCurrentUsername();
//                String ip = PenintUtil.getHttpServletRequestIpAddress();
//                // 执行时长(毫秒)
//                long time = System.currentTimeMillis() - start;
//                SysOperationLog operationLog = new SysOperationLog();
//                String resultStr = JSONObject.toJSONString(result);
//                operationLog.setUsername(username);
//                operationLog.setIp(ip);
//                operationLog.setTime(time);
//                operationLog.setResult(resultStr);
//
//                if (JSONObject.parseObject(resultStr).getInteger("status") == 200) {
//                    operationLog.setIsSuccess(true);
//                } else {
//                    operationLog.setIsSuccess(false);
//                }
//                logService.saveLog(point, operationLog);
//            }
//            return result;
//        } catch (Throwable throwable) {
//            log.error(throwable.getMessage(), throwable);
//            String exceptionMessage = annotation.exceptionMessage();
//            String message = throwable.getMessage();
//            String error = PenintUtil.containChinese(message) ? exceptionMessage + "，" + message : exceptionMessage;
//            throw new PenintException(error);
//        }
//    }
//}
//
//
//
