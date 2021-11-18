package com.securityserviceprovider.util;

import java.util.regex.Pattern;

/**
 * @Author:SCBC_LiYongJie
 * @time:2021/11/16
 */
public class ValidatorUtils {

    public static Boolean checkMobile(String mobile) {
        String regex = "(\\+\\d+)?1[3458]\\d{9}$";
        return Pattern.matches(regex,mobile);
    }

}
