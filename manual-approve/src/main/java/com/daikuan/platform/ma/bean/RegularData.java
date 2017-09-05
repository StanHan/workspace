/**
 * @Copyright: 2005-2017 www.2345.com. All rights reserved.
 */
package com.daikuan.platform.ma.bean;

/**
 * @author feizy
 * @version RegularData, v0.1 2017/3/16 9:28
 */
public class RegularData {
    public static final int PASS_1 = 1;
    public static final int REJECT_0 = 0;
    public static final int NO_JUDEGE_NEGATIVE1 = -1;

    private int type;// 进行正则判断的类型（比如联系人，单位名称，家庭地址等）
    private String key;// 进行正则判断的字符串
    private int pass = -1; // 1 通过， 0 决绝， -1 未判断出
    private int regExType;// 具体通过或拒绝的正则code
    private String regEx;// 具体通过或拒绝的正则表达式

    public RegularData(int type, String key) {
        this.type = type;
        this.key = key;
    }

    public RegularData() {
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getPass() {
        return pass;
    }

    public void setPass(int pass) {
        this.pass = pass;
    }

    public int getRegExType() {
        return regExType;
    }

    public void setRegExType(int regExType) {
        this.regExType = regExType;
    }

    public String getRegEx() {
        return regEx;
    }

    public void setRegEx(String regEx) {
        this.regEx = regEx;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    @Override
    public String toString() {
        return "RegularData{" + "type=" + type + ", key='" + key + '\'' + ", pass=" + pass + ", regExType=" + regExType
                + ", regEx='" + regEx + '\'' + '}';
    }
}
