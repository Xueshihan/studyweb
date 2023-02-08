package cn.iatc.web.utils.aop;

import cn.iatc.web.common.data.RestResponse;
import cn.iatc.web.common.data.Status;
import cn.iatc.web.common.data.exception.BaseException;
import cn.iatc.web.utils.jwt.TokenVerify;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Arrays;

@Slf4j
public class TokenAopUtil {

    public Object tokenVerify(ProceedingJoinPoint joinPoint, String[] noServletPathList) {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Method method = methodSignature.getMethod();
        String methodName = method.getName();
        
        // 根据业务可以放返回响应
        RestResponse<Object> restResponse = new RestResponse<>();
        //最终返回结果
        Object result = restResponse;

        long startTime = System.currentTimeMillis();;
        long endTime;

        Object object = joinPoint.getTarget();
        String controllerName = object.getClass().getName();
        try {
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            HttpServletRequest servletRequest = attributes.getRequest();
            log.info("=====request method:{}, contextPath:{}, servletPath:{}, ", servletRequest.getMethod(), servletRequest.getContextPath(), servletRequest.getServletPath());
            /**
             * request.getRequestURL() 返回全路径
             * request.getRequestURI() 返回除去host（域名或者ip）部分的路径
             * request.getContextPath() 返回工程名部分，如果工程映射为/，此处返回则为空
             * request.getServletPath() 返回除去host和工程名部分的路径
             */
            // 通过servletPath判断
            if (!Arrays.asList(noServletPathList).contains(servletRequest.getServletPath())) {
                String token = servletRequest.getHeader("token");
                if (StringUtils.isBlank(token)) {
                    throw new BaseException(Status.TOKEN_EMPTY);
                }
                // 验证token有效性
                TokenVerify.verifyToken(token);
            }
            result = joinPoint.proceed();
        } catch (BaseException baseException) {
            restResponse.setFail(baseException.getCode(), baseException.getMessage());
        } catch (Exception e) {
            log.info("===TokenAop e:{}", e);
            restResponse.setFail(Status.EXCEPTION.getCode(), Status.EXCEPTION.getMessage());
        } catch (Throwable e) {
            restResponse.setFail(Status.EXCEPTION.getCode(), Status.EXCEPTION.getMessage());
            throw new RuntimeException(e);
        }
        endTime = System.currentTimeMillis();
        log.info("controllerName:{}, method:{}, diff time:[{}]ms", controllerName, methodName, (endTime - startTime));
        return result;
    }

}
