package cn.iatc.database.constants;

public class SqlConstants {

    public static final String TABLE_BASE_DEVICE = "t_base_device";
    public static final String TABLE_BASE_ELEC = "t_base_elec";
    public static final String TABLE_BASE_PROPERTY = "t_base_property";
    public static final String TABLE_DEVICE = "t_device";
    public static final String TABLE_DEVICE_DATA_FORMAT = "t_device_data_format";
    public static final String TABLE_DEVICE_POINT = "t_device_point";
    public static final String TABLE_DEVICE_PROPERTY_MODEL = "t_device_property_model";
    public static final String TABLE_DEVICE_STATION = "t_device_station";
    public static final String TABLE_DEVICE_TYPE = "t_device_type";
    public static final String TABLE_FACTORY = "t_factory";
    public static final String TABLE_GENERAL_TYPE = "t_general_type";
    public static final String TABLE_MENU = "t_menu";
    public static final String TABLE_REGION = "t_region";
    public static final String TABLE_ROLE = "t_role";
    public static final String TABLE_ROLE_MENU = "t_role_menu";
    public static final String TABLE_STATION = "t_station";
    public static final String TABLE_STATION_RELATION = "t_station_relation";
    public static final String TABLE_STATION_TYPE = "t_station_type";
    public static final String TABLE_STATION_VIDEO = "t_station_video";
    public static final String TABLE_SYSTEM_CONFIGURATION = "t_system_configuration";
    public static final String TABLE_USER = "t_user";
    public static final String TABLE_TRANSFORMER_INFO = "t_energy_transformer_info";
    public static final String TABLE_OIL_TRANSFORMER_EFFICIENCY = "t_oil_transformer_efficiency";
    public static final String TABLE_ORIGINAL_POWER = "t_original_power";
    public static final String TABLE_REALTIME_POWER = "t_realtime_power";
    public static final String TABLE_STATISTIC_HOUR_POWER = "t_statistic_hour_power";
    public static final String TABLE_STATISTIC_DAY_POWER = "t_statistic_day_power";
    public static final String TABLE_STATISTIC_MONTH_POWER = "t_statistic_month_power";


    // 变压器相位
    public enum TransPhase {
        A(1),
        B(2),
        C(3);
        private int value;

        TransPhase(int value) {
            this.value = value;
        }

        public int getValue() {
            return this.value;
        }
    }
}
