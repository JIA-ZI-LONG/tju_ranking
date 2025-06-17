package xyz.tjucomments.tjufood.aop;

import com.alibaba.fastjson.JSON;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import xyz.tjucomments.tjufood.dto.AdminDTO;
import xyz.tjucomments.tjufood.dto.UserDTO;
import xyz.tjucomments.tjufood.interceptor.AdminHolder;
import xyz.tjucomments.tjufood.interceptor.UserHolder;

import java.lang.reflect.Method;
import java.util.Objects;

/**
 * @author Gemini
 * @description 操作日志记录AOP切面
 * @create 2025-06-16 14:15
 */
@Aspect
@Component
@Slf4j
public class LogAspect {

    /**
     * 定义切点，拦截所有被 @Log 注解标记的方法
     */
    @Pointcut("@annotation(xyz.tjucomments.tjufood.aop.Log)")
    public void logPointcut() {
    }

    /**
     * 环绕通知，在目标方法执行前后进行日志记录
     *
     * @param joinPoint 切点
     * @return 目标方法的返回值
     * @throws Throwable 异常
     */
    @Around("logPointcut()")
    public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();

        // 执行目标方法
        Object result = joinPoint.proceed();

        long timeTaken = System.currentTimeMillis() - startTime;

        // 异步记录日志，避免影响主流程性能
        // 在实际生产中，推荐使用独立的线程池来处理日志记录
        try {
            handleLog(joinPoint, result, timeTaken);
        } catch (Exception e) {
            log.error("AOP 日志记录异常", e);
        }

        return result;
    }

    /**
     * 核心日志处理方法
     *
     * @param joinPoint 切点信息
     * @param result    方法执行结果
     * @param timeTaken 方法耗时
     */
    private void handleLog(ProceedingJoinPoint joinPoint, Object result, long timeTaken) {
        // 获取请求属性
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes == null) {
            return;
        }
        HttpServletRequest request = attributes.getRequest();

        // 获取方法上的@Log注解
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        Log logAnnotation = method.getAnnotation(Log.class);

        // 构建日志信息
        StringBuilder logBuilder = new StringBuilder();
        logBuilder.append("\n===================== API Log Start =====================");
        logBuilder.append("\n| 操作模块: ").append(logAnnotation.module());
        logBuilder.append("\n| 操作描述: ").append(logAnnotation.operation());
        logBuilder.append("\n| 请求方式: ").append(request.getMethod());
        logBuilder.append("\n| 请求URI: ").append(request.getRequestURI());
        logBuilder.append("\n| 请求IP: ").append(getClientIp(request));

        // 记录操作人
        String operator = getOperator();
        logBuilder.append("\n| 操作人员: ").append(operator);

        // 记录请求参数
        try {
            logBuilder.append("\n| 请求参数: ").append(JSON.toJSONString(joinPoint.getArgs()));
        } catch (Exception e) {
            logBuilder.append("\n| 请求参数: [参数序列化失败]");
        }

        // 记录响应结果
        try {
            logBuilder.append("\n| 响应结果: ").append(JSON.toJSONString(result));
        } catch (Exception e) {
            logBuilder.append("\n| 响应结果: [结果序列化失败]");
        }

        logBuilder.append("\n| 执行耗时: ").append(timeTaken).append(" ms");
        logBuilder.append("\n===================== API Log End =======================");

        // 使用info级别打印格式化的日志
        log.info(logBuilder.toString());
    }

    /**
     * 获取当前操作员信息
     *
     * @return 操作员标识
     */
    private String getOperator() {
        AdminDTO admin = AdminHolder.getAdmin();
        if (admin != null) {
            return "Admin(ID:" + admin.getId() + ", User:" + admin.getUsername() + ")";
        }
        UserDTO user = UserHolder.getUser();
        if (user != null) {
            return "User(ID:" + user.getId() + ", Nick:" + user.getNickName() + ")";
        }
        return "Anonymous";
    }

    /**
     * 获取客户端IP地址
     *
     * @param request HttpServletRequest
     * @return IP地址
     */
    private String getClientIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }
}
