package com.daikuan.platform.ma.pojo;

public class ADictTypeEntity {
    /** 编号（自增id） id **/
    private Integer id;

    /** 字典类型 type **/
    private String type;

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
        return "ADictTypeEntity [id=" + id + ", type=" + type + ", description=" + description + "]";
    }
}
