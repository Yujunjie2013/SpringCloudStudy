package com.junjie.common.bean;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;


/**
 * 数据库实体类父类，声明通用字段
 */
@Getter
@Setter
public class BaseEntity implements Serializable {

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    protected Date createTime;

    /**
     * 更新时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    protected Date updateTime;

    /**
     * 逻辑删除
     */
    @JsonIgnore
    @TableField(select = false)
    protected Boolean logicDelete;
    /**
     * 乐观锁
     */
    @TableField(select = false)
    private Integer version;
}
