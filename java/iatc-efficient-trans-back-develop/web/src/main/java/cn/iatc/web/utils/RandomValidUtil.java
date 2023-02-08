package cn.iatc.web.utils;

import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.LineCaptcha;
import org.springframework.util.DigestUtils;

import java.util.HashMap;
import java.util.Map;

public class RandomValidUtil {

    // 验证码图片生成器
    public static Map<String, String> getValidCode() {
        Map<String, String> hashMap = new HashMap<>();
        // 生成图片验证码类
        LineCaptcha captcha = CaptchaUtil.createLineCaptcha(111, 36, 4, 10);
//        GifCaptcha captcha = CaptchaUtil.createGifCaptcha(111, 36, 4);
//        ShearCaptcha captcha = CaptchaUtil.createShearCaptcha(111, 36, 4, 1);
        // 获取图片验证码上的字符串(小写) 随机
        String lowerCaseCode = captcha.getCode().toLowerCase();
        // 转化为base64f给前端
        String imageBase = captcha.getImageBase64();
        // 将字符串用md5加密当作key保存到redis中
        String md5Image = DigestUtils.md5DigestAsHex(lowerCaseCode.getBytes());
        // 组装数据返回前端
        hashMap.put("imageBase64", imageBase);
        // 下次前端携带此参数我们从redis中查询
        hashMap.put("md5Image", md5Image);
        // 此数据在此只是展示，不要返回给前端
        hashMap.put("code", lowerCaseCode);

        return hashMap;
    }
}
