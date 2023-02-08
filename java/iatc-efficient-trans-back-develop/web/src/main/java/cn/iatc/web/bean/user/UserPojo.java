package cn.iatc.web.bean.user;

import cn.iatc.database.entity.Role;
import cn.iatc.database.entity.Station;
import cn.iatc.database.entity.User;
import lombok.Data;

@Data
public class UserPojo extends User {

    private Role role;

    private Station station;

}
