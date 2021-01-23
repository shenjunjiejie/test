package com.lirongyuns.happyrupees.utils;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.text.TextUtils;

import androidx.core.content.ContextCompat;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

/**
 * tip :格式验证工具类，并做了判空验证，如果为空直接返回false 包括:电话号码,座机号码，Email邮箱,邮编,网址,汉字,密码格式,身份证号,等验证
 */
@SuppressLint("NewApi")
public class CheckUtil {
    /**
     * 检测是否是合法手机号
     *
     * @param mobileNumber
     * @return
     */
    public static boolean isMobileNO(String mobileNumber) {
        if (TextUtils.isEmpty(mobileNumber)) {
            return false;
        }
        Pattern p = Pattern.compile("[1]\\d{10}");
        Matcher m = p.matcher(mobileNumber);
        return m.matches();
    }


    /**
     * 验证手机格式
     */
    public static boolean isMobile(String number) {
    /*
    移动：134、135、136、137、138、139、150、151、157(TD)、158、159、187、188
    联通：130、131、132、152、155、156、185、186
    电信：133、153、180、189、（1349卫通）
    167 开发的手机号不正常，第二位，添加 6
    总结起来就是第一位必定为1，第二位必定为3或5或8，其他位置的可以为0-9
    */
        String num = "[1][358679]\\d{9}";//"[1]"代表第1位为数字1，"[358]"代表第二位可以为3、5、8中的一个，"\\d{9}"代表后面是可以是0～9的数字，有9位。
        if (TextUtils.isEmpty(number)) {
            return false;
        } else {
            //matches():字符串是否在给定的正则表达式匹配
            return number.matches(num);
        }
    }

    /**
     * 检测是否为合法的邮箱格式
     *
     * @param email
     * @return
     */
    public static boolean isEmail(String email) {
        if (TextUtils.isEmpty(email)) {
            return false;
        }

        // String str =
        // "^([a-zA-Z0-9]*[-_]?[a-zA-Z0-9]+)*@([a-zA-Z0-9]*[-_]?[a-zA-Z0-9]+)+[\\.][A-Za-z]{2,3}([\\.][A-Za-z]{2})?$";
        String str = "^[a-zA-Z0-9][\\w\\.-]*[a-zA-Z0-9]@[a-zA-Z0-9][\\w\\.-]*[a-zA-Z0-9]\\.[a-zA-Z][a-zA-Z\\.]*[a-zA-Z]$";
        Pattern p = Pattern.compile(str);
        Matcher m = p.matcher(email);
        return m.matches();
    }

    /**
     * 检测是否为合法的的汉字
     *
//     * @param email
     * @return
     */
    public static boolean isChinese(String cnword) {
        if (TextUtils.isEmpty(cnword)) {
            return false;
        }

        String str = "^[\u4e00-\u9fa5]{0,}$";
        Pattern p = Pattern.compile(str);
        Matcher m = p.matcher(cnword);
        return m.matches();
    }

    /**
     * 检查座机号码 可输入的格式: "XXX-XXXXXXX"、"XXXX-XXXXXXXX"、
     * "XXX-XXXXXXX"、"XXX-XXXXXXXX"、 "XXXXXXX"和"XXXXXXXX"
     *
     * @param machineNumber 座机号码
     * @return true 号码格式正确 false 格式有误
     */
    public static boolean isMachineNumber(String machineNumber) {
        if (TextUtils.isEmpty(machineNumber)) {
            return false;
        }
        try {
            Pattern p = Pattern.compile("([0-9]{7,8})|[0-9]{4}|[0-9]{3}-[0-9]{7,8}");
            Matcher m = p.matcher(machineNumber);
            return m.matches();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 检测字符串是否为url网址 如:http://www.
     *
     * @param url
     * @return true URl地址合法 false 不合法的url地址
     */
    public static boolean isInternetUrl(String url) {
        if (TextUtils.isEmpty(url)) {
            return false;
        }
        // 定义正则字符串
        String strPatten = "^http://([\\w-]+\\.)+[\\w-]+(/[\\w-./?%&=]*)?$";
        Pattern p = Pattern.compile(strPatten);
        Matcher m = p.matcher(url);
        return m.matches();
    }


    /**
     * 用户名格式验证 必须以字母开头, 密码只能由数字,字母,下划线组成(可以纯字母，数字)
     *
//     * @param passWord 密码
//     * @param min      最小位数
//     * @param max      最大位数
     * @return true 密码符合规范 false 密码不符合规范
     */
    public static boolean isUserName(String username) {
        if (TextUtils.isEmpty(username)) {
            return false;
        }
        // 定义正则字符串
        String strPatten = "^[a-zA-Z0-9_]{1,}$";
        Pattern p = Pattern.compile(strPatten);
        Matcher m = p.matcher(username);
        return m.matches();
    }

    /**
     * 密码格式验证 必须以字母开头, 密码只能由数字,字母,下划线组成(可以纯字母，数字)
     *
     * @param passWord 密码
     * @param min      最小位数
     * @param max      最大位数
     * @return true 密码符合规范 false 密码不符合规范
     */
    public static boolean isPassWord(String passWord, int min, int max) {
        if (TextUtils.isEmpty(passWord) || min < 0 || max < min + 1) {
            return false;
        }
        // 定义正则字符串
        String strPatten = "^[a-zA-Z\\w]{" + min + "," + max + "}$";
        Pattern p = Pattern.compile(strPatten);
        Matcher m = p.matcher(passWord);
        return m.matches();
    }

    /**
     * 密码格式验证 必须以字母开头, 密码只能由数字,字母,下划线组成(可以纯字母，数字)
     *
     * @param passWord 密码
     * @param length   最小位数
     * @return true 密码符合规范 false 密码不符合规范
     */
    public static boolean isPassWord(String passWord, int length) {
        if (TextUtils.isEmpty(passWord) || length < 0) {
            return false;
        }
        // 定义正则字符串
        String strPatten = "^[a-zA-Z\\w]{" + length + "}$";
        Pattern p = Pattern.compile(strPatten);
        Matcher m = p.matcher(passWord);
        return m.matches();
    }

    /**
     * 验证身份证号码
     *
//     * @param id_number 身份证号码
     * @return true 身份证符合规范 false 身份证有误
     */
    public static Boolean isNID(String number) {
        if (TextUtils.isEmpty(number)) {
            return false;
        }
        String pattern = "((11|12|13|14|15|21|22|23|31|32|33|34|35|36|37|41|42|43|44|45|46|50|51|52|53|54|61|62|63|64|65|71|81|82|91)\\d{4})((((19|20)(([02468][048])|([13579][26]))0229))|((20[0-9][0-9])|(19[0-9][0-9]))((((0[1-9])|(1[0-2]))((0[1-9])|(1\\d)|(2[0-8])))|((((0[1,3-9])|(1[0-2]))(29|30))|(((0[13578])|(1[02]))31))))((\\d{3}(x|X))|(\\d{4}))";
        Pattern p = Pattern.compile(pattern);
        Matcher m = p.matcher(number);
        return m.matches();
    }

    /**
     * 验证邮编是否合法
     *
     * @param postCode 邮编
     * @return true 邮编符合规范 false 邮编不符合规范
     */
    public static boolean isPostCode(String postCode) {
        if (TextUtils.isEmpty(postCode)) {
            return false;
        }
        return postCode.matches("[1-9]\\d{5}(?!\\d)");
    }

    /**
     * 判断输入的字符串是否是纯数字
     *
     * @param string 输入的字符串
     * @author liuzheng
     */
    public static boolean isAllnum(String string) {
        if (TextUtils.isEmpty(string)) {
            return false;
        }

        return !string.isEmpty() && string.matches("[0-9]*");
    }

    /**
     * 过滤字符串中的符号
     *
     * @param str 过滤的字符串对象
     * @return
     * @throws PatternSyntaxException
     */
    public static String stringFilter(String str) throws PatternSyntaxException {

//      String regEx = "[/\\:*#?<>{}|\'\"\n\t]";
        String regEx = "[^\u4e00-\u9fa5\\w]+";
        Pattern p = Pattern.compile(regEx);

        Matcher m = p.matcher(str);

        return m.replaceAll("");

    }

    /**
     * 对QQ号码进行校验
     * 要求5~15位，不能以0开头，只能是数字
     *
     * @param qq
     * @return
     */
    public static boolean isQq(String qq) {
        if (TextUtils.isEmpty(qq)) {
            return false;
        }
        String regex = "[1-9][0-9]{4,14}";//第一位1-9之间的数字，第二位0-9之间的数字，数字范围4-14个之间
        //String regex2 = "[1-9]\\d{4,14}";//此句也可以
        return qq.matches(regex);
    }



    /**
     * 判断权限集合
     * permissions 权限数组
     * return true-表示没有改权限  false-表示权限已开启
     */
    public static boolean lacksPermissions(Context mContexts, String[] permissions) {
        for (String permission : permissions) {
            if (lacksPermission(mContexts,permission)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 判断是否缺少权限
     */
    private static boolean lacksPermission(Context mContexts, String permission) {
        return ContextCompat.checkSelfPermission(mContexts, permission) ==
                PackageManager.PERMISSION_DENIED;
    }
}
