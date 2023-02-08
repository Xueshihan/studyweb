package cn.iatc.web.utils.jwt;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Slf4j
public class JWTAccessData {

    public static final String KEY_USER_ID = "userId";

    public static final String KEY_EXPIRE_AT = "expiresAt";

    private Long userId;
    private Long expiresAt;

    // 可空，空表示无限期
    private Date expiresTime;

    public Map<String, Object> parseMap() {
        Map<String, Object> map = new HashMap<>();
        map.put(KEY_USER_ID, userId);
        map.put(KEY_EXPIRE_AT, expiresAt);
        return map;
    }

    public static JWTAccessData fromMap(Map<String, Object> map) {
        Long userId = (Long) map.get(KEY_USER_ID);
        Long expiresAt = (Long) map.get(KEY_EXPIRE_AT);
        Date expiresTime = null;
        if (expiresAt != -1) {
            expiresTime = new Date(expiresAt);
        }
        return new JWTAccessData(userId, expiresAt, expiresTime);
    }
}
