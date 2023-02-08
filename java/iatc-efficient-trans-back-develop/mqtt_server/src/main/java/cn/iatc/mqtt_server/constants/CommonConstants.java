package cn.iatc.mqtt_server.constants;

public class CommonConstants {

    //登陆错误次数
    public static final long LOGIN_ERR_NUM = 5;

    //登陆锁定时间，5分钟，毫秒级别
    public static final long LOGIN_LOCK_TIME = 5 * 60 * 1000;

    // 采集周期 15分钟
    public static final long COLLECT_BASE_PERIOD = 15 * 60 * 1000;

    // 采集倍数
    public static final long COLLECT_NUM = 2;

    // 针对ognl方式处理，只能是静态变量或静态方法
    public static int ENABLED = Status.ENABLED.getValue();

    // 针对ognl方式处理，只能是静态变量或静态方法
    public static int DISABLED = Status.DISABLED.getValue();

    public enum Status {

        DISABLED(0),
        ENABLED(1);

        private int value;

        Status(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }
}
