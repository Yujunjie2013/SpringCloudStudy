package com.junjie.common.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.junjie.common.bean.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author zlt
 * 角色
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("sys_role")
public class SysRole extends BaseEntity {
    private static final long serialVersionUID = 4497149010220586111L;
    private String code;
    private String name;
    @TableField(exist = false)
    private Long userId;
}
