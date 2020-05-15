package com.junjie.common.bean;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Data;

@Data
@TableName(value = "test_char_varchar_text")
public class TestCharVarcharText implements Serializable {
    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 测试char
     */
    @TableField(value = "mychar")
    private String mychar;

    /**
     * 测试varchar
     */
    @TableField(value = "myvarchar")
    private String myvarchar;

    /**
     * 测试text
     */
    @TableField(value = "mytext")
    private String mytext;

    private static final long serialVersionUID = 1L;

    public static final String COL_ID = "id";

    public static final String COL_MYCHAR = "mychar";

    public static final String COL_MYVARCHAR = "myvarchar";

    public static final String COL_MYTEXT = "mytext";
}