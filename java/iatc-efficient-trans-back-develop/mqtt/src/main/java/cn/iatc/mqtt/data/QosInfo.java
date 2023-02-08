package cn.iatc.mqtt.data;

/**
 * qos信息
 */
public class QosInfo {

    public enum Type {
        Qos0(0),
        Qos1(1),
        Qos2(2);

        private int value;
        Type(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }
}
