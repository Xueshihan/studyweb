package cn.iatc.database.constants;

public class Constants {
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
