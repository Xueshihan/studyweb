package cn.iatc.web.utils.jwt;

import cn.iatc.web.common.data.Status;
import cn.iatc.web.common.data.exception.BaseException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Date;

@Slf4j
@Component
public class TokenVerify {

//    @Autowired
//    private UserService autoUserService;
//
//    @Autowired
//    private CommonConfig autoCommonConfig;
//
//    private static UserService userService;
//
//    private static CommonConfig commonConfig;
//
//    @PostConstruct
//    public void initialize() {
//        userService = autoUserService;
//        commonConfig = autoCommonConfig;
//    }

    public static JWTAccessData verifyToken(String token) throws BaseException {
        JWTAccessData jwtAccessData = JWTUtil.parseAccessToken(token);
        // 验证是否为空
        if (jwtAccessData == null) {
            // 抛出异常
            throw new BaseException(Status.TOKEN_INVALID);
        }

        // 验证是否超时
        Date expiresTime = jwtAccessData.getExpiresTime();
        log.info("===== expiresAt:{}", jwtAccessData);
        if (expiresTime != null) {
            Date curDate = new Date();
            if (expiresTime.getTime() < curDate.getTime()) {
                // token过时， 抛异常
                throw new BaseException(Status.TOKEN_EXPIRED);
            }
        }

        // 验证token是否有效，是否用的是自己的加密算法，自己的密钥
        if (!JWTUtil.verify(token)) {
            // token是否正确， 抛异常
            throw new BaseException(Status.TOKEN_INVALID);
        }

        Long userId = jwtAccessData.getUserId();
        if (userId == null) {
            throw new BaseException(Status.TOKEN_INVALID);
        }

        //根据需求，1.若一个设备只能一个登陆，需要查看redis是否有一样的token记录。2.查看用户数据库，用户是否存在

        // 查看是否是我们自己的数据库
//        User user = userService.findById(userId);
//
//        if (user == null) {
//            throw new BaseException(Status.TOKEN_PARSE_ERROR);
//        }
        return jwtAccessData;
    }
}
