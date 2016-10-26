package cn.tiup.models;

import java.util.List;

/**
 * Created by leenanxi on 2016/10/26.
 *
 * 用户信息实体类
 */
public class UserInfo {
    public String uid;
    public String name;
    public String email;
    public String phone;
    public String sex;
    public String birthday;
    public List<Linkedaccount> linkedaccounts;

    public class Linkedaccount {
        public String host;
        public String username;
    }
}
