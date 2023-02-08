package cn.iatc.web.utils.jwt;

import cn.iatc.web.constants.TokenConstants;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.AlgorithmMismatchException;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * JWT token 工具类 有无限期token
 * 注意：jwt加解密的私钥使用配置的字符串而不使用用户登录密码的好处是防止用户密码被其他人修改后，
 * 用户操作系统此时再去校验token会发生token解析对不上的情况。
 */
@Slf4j
public class JWTUtil {

    //过期时间
    private static Long expire= TokenConstants.ACCESS_EXPIRATION;

    private static Long expireLongTime= TokenConstants.REFRESH_EXPIRATION;

    // 秘钥
    private static String secret= TokenConstants.JWT_SECRET_KEY;

    /**
     * 校验token是否正确
     * @param token  密钥
     * @return 是否正确
     */
    public static boolean verify(String token) {
        boolean result = true;
        try {
            //根据密码生成JWT效验器
            Algorithm algorithm = Algorithm.HMAC256(secret);
            JWTVerifier verifier = JWT.require(algorithm).build();
            //效验TOKEN
            verifier.verify(token);
        } catch (UnsupportedEncodingException e) {
            result = false;
        } catch (SignatureVerificationException | AlgorithmMismatchException e) {
            // 签名不一致异常, 算法不匹配异常
            result = false;
        } catch (TokenExpiredException e) {
            // 令牌过期异常
            log.info("===== TokenExpiredException 令牌过期");
//            result = false;
        } catch (Exception e) {
            result = false;
        } finally {
            return result;
        }
    }

    /**
     * 加密生成token
     * @param jwtAccessData
     * @param rememberMe
     * @return
     */
    public static String sign(JWTAccessData jwtAccessData, boolean rememberMe) {
        try {
            Long expiresAt = -1L;
            if(rememberMe){
                expiresAt = System.currentTimeMillis() + expireLongTime;
            }else{
                expiresAt = System.currentTimeMillis() + expire;
            }
//            Date date = new Date(expiresAt);
            Algorithm algorithm = Algorithm.HMAC256(secret);
            // 附带username信息
            return JWT.create()
                    .withAudience("12345")
                    .withClaim(JWTAccessData.KEY_USER_ID, jwtAccessData.getUserId())
                    .withClaim(JWTAccessData.KEY_EXPIRE_AT, expiresAt)
                    .sign(algorithm);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 生成无期限的token
     * @param jwtAccessData
     * @return
     */
    public static String signInfinite(JWTAccessData jwtAccessData) {
        try {
            Long expiresAt = -1L;
            Algorithm algorithm = Algorithm.HMAC256(secret);
            log.info("====== getUserId:{}", jwtAccessData.getUserId());
            // 附带username信息
            return JWT.create()
                    .withAudience("12345")
                    .withClaim(JWTAccessData.KEY_USER_ID, jwtAccessData.getUserId())
                    .withClaim(JWTAccessData.KEY_EXPIRE_AT, expiresAt)
                    .sign(algorithm);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 解密token
     * @param accessToken
     * @return
     */
    public static JWTAccessData parseAccessToken(String accessToken) {
        JWTAccessData accessData = null;
        try {
            if (accessToken != null){
                DecodedJWT jwt = JWT.decode(accessToken);
                Map<String, Object> map = new HashMap<>();
                map.put(JWTAccessData.KEY_USER_ID, jwt.getClaim(JWTAccessData.KEY_USER_ID).asLong());
                map.put(JWTAccessData.KEY_EXPIRE_AT, jwt.getClaim(JWTAccessData.KEY_EXPIRE_AT).asLong());
                accessData = JWTAccessData.fromMap(map);
            }
        } catch (JWTDecodeException e) {
            log.warn("Request to JWTDecodeException e:", e);
        } catch (Exception e) {
            log.warn("Request to parse Exception e : {}", e);
        }
        return accessData;
    }
}
