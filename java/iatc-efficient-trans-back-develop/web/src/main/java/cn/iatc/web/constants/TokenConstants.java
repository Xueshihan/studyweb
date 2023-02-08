package cn.iatc.web.constants;

public final class TokenConstants {

    private TokenConstants() {
        throw new IllegalStateException("Cannot create instance of static constant class");
    }

    /**
     * JWT签名密钥，这里使用 HS512 算法的签名密钥
     * <p>
     * 注意：最好使用环境变量或 .properties 文件的方式将密钥传入程序
     * 密钥生成地址：http://www.allkeysgenerator.com/
     */
    public static final String JWT_SECRET_KEY = "mZq4t7w!z%C*F-JaNdRfUjXn2r5u8x/A?D(G+KbPeShVkYp3s6v9y$B&E)H@McQf";

    public static final String TOKEN_TYPE = "JWT";

    public static final String TOKEN_DATA_CLAIMS = "data";

    public static final long ACCESS_EXPIRATION = 1000L * 60 * 60 * 24 * 7;
    public static final long REFRESH_EXPIRATION = 1000L * 60 * 60 * 24 * 100;


}
