package com.campus.utils;

//public class Test {
//    public static void main(String[] args) {
//        // 直接用你项目里已经写好的工具类！
//        String pwd = PasswordUtil.encode("123456");
//        System.out.println("加密后的密码：" + pwd);
//    }
//}

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class Test {
    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        // 明文密码 123456 加密
        String encode = encoder.encode("123456");
        System.out.println("数据库要存的真实加密密码：");
        System.out.println(encode);
    }
}