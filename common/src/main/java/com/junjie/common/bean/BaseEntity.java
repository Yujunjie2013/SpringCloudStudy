package com.junjie.common.bean;

import cn.afterturn.easypoi.excel.annotation.Excel;
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
    @Excel(name = "创建时间", width = 30d, orderNum = "20", format = "yyyy/MM/dd HH:mm:ss")
    protected Date createTime;

    /**
     * 更新时间
     */
    @Excel(name = "修改时间", width = 30d, orderNum = "30", format = "yyyy/MM/dd HH:mm:ss")
    protected Date updateTime;

    /**
     * 逻辑删除
     */
    @JsonIgnore
    protected Boolean logicDelete;
    private Integer version;
}
