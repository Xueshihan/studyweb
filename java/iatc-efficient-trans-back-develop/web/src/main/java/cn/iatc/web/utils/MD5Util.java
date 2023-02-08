package cn.iatc.web.utils;

import cn.hutool.crypto.digest.DigestUtil;

public class MD5Util {

    public static void main(String[] args) {
        System.out.println(encodeMd5("Iatc@2022#C!A^U"));
    }

    public static String encodeMd5(String code) {
        return DigestUtil.md5Hex(code);
    }
}
