package cn.iatc.web.config;

import cn.iatc.web.utils.aop.TokenAopUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * token切面认证功能
 */
@Slf4j
@Aspect
@Configuration
public class AspectPointTokenConfig {

    @Bean("tokenAopUtil")
    public TokenAopUtil initTokenAopUtil() {
        log.info("=====initTokenAopUtil ");
        return new TokenAopUtil();
    }

    // 非token认证列表
//    private static final String[] noIdentificationList = new String[]{
//            "cn.net.iatc.storagebattery.controller.LoginController",
//    };

    /**
     * 不验证token的路径
     */
    private static final String[] noServletPathList = new String[]{
            "/login", "/login/code",
            "/swagger-ui.html",
            "/swagger-ui/",
            "/v3/api-docs"
    };

    /**
     * 1.0版本 只针对路径限制，目前先不用
     * 切入点 针对controller下面的所有方法
     */
    @Pointcut("execution(* cn.iatc.web.controller..*.*(..))")
    private void tokenAop(){

    }

    /**
     * 2.0版本，加入了方法注解和路径双重限制
     * 切入点，针对 controller文件下,并且注解是@GetMapping，@PostMapping，@DeleteMapping, @PutMapping, @RequestMapping下的所有公共方法，进行切面管理
     * @annotation 用于拦截所有被该注解标注的方法
     */
    @Pointcut("execution(public * cn.iatc.web.controller..*.*(..)) &&" +
            "(@annotation(org.springframework.web.bind.annotation.GetMapping) || " +
            "@annotation(org.springframework.web.bind.annotation.PostMapping) ||" +
            "@annotation(org.springframework.web.bind.annotation.DeleteMapping) ||" +
            "@annotation(org.springframework.web.bind.annotation.PutMapping) ||" +
            "@annotation(org.springframework.web.bind.annotation.RequestMapping))")
    public void checkTokenAopMethod() {

    }

    /**
     * 2.1版本 加入了类(对象上)注解和路径双重限制
     * 切入点，针对 controller文件下，并且类注解是RestController的
     * @within  用于拦截被所有该注解标注的类
     */
    @Pointcut("execution(public * cn.iatc.web.controller..*.*(..)) &&" +
            "@within(org.springframework.web.bind.annotation.RestController)")
    public void checkTokenAopClass() {

    }

    @Around(value = "checkTokenAopClass()")
    public Object tokenVerify(ProceedingJoinPoint joinPoint) throws InterruptedException {
        TokenAopUtil tokenAopUtil = initTokenAopUtil();
        return tokenAopUtil.tokenVerify(joinPoint, noServletPathList);
    }
}
