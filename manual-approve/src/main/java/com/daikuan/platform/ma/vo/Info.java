package com.daikuan.platform.ma.vo;

import java.io.Serializable;

/** 页面上显示的信息 */
public class Info implements Serializable {
    private String name;
    private String value;
    private String color;

    public Info() {
    };

    public Info(String name, String value, String color) {
        super();
        this.name = name;
        this.value = value;
        this.color = color;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }
}
