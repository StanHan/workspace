package com.daikuan.platform.ma.pojo;

public class ADictTypeDtlEntity {
    /** 编号（自增id） id **/
    private Integer id;

    /** 字典类型 type **/
    private String type;

    /** 字典子项编码 code **/
    private String code;

    /** 描述 description **/
    private String description;

    /** 编号（自增id） id **/
    public Integer getId() {
        return id;
    }

    /** 编号（自增id） id **/
    public void setId(Integer id) {
        this.id = id;
    }

    /** 字典类型 type **/
    public String getType() {
        return type;
    }

    /** 字典类型 type **/
    public void setType(String type) {
        this.type = type == null ? null : type.trim();
    }

    /** 字典子项编码 code **/
    public String getCode() {
        return code;
    }

    /** 字典子项编码 code **/
    public void setCode(String code) {
        this.code = code == null ? null : code.trim();
    }

    /** 描述 description **/
    public String getDescription() {
        return description;
    }

    /** 描述 description **/
    public void setDescription(String description) {
        this.description = description == null ? null : description.trim();
    }

    @Override
    public String toString() {
        return "ADictTypeDtlEntity [id=" + id + ", type=" + type + ", code=" + code + ", description=" + description
                + "]";
    }
}
