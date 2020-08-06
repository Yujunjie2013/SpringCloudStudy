package com.junjie.product.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.junjie.common.bean.BaseEntity;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;

/**
 * 商品实体类
 */
@Getter
@Setter
@TableName("tb_product")
@ToString
public class TbProduct extends BaseEntity {
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 商品名称
     */
    @TableField(value = "product_name")
    private String productName;

    /**
     * 状态
     */
    @TableField(value = "status")
    private Long status;

    /**
     * 单价
     */
    @TableField(value = "price")
    private BigDecimal price;

    /**
     * 类型、1小程序、2、web登记
     */
    @TableField(value = "type")
    private Integer type;

    /**
     * 描述
     */
    @TableField(value = "product_desc")
    private String productDesc;

    /**
     * 标题
     */
    @TableField(value = "caption")
    private String caption;

    @TableField(value = "inventory")
    private Integer inventory;

    private static final long serialVersionUID = 1L;

    public static final String COL_ID = "id";

    public static final String COL_PRODUCT_NAME = "product_name";

    public static final String COL_STATUS = "status";

    public static final String COL_PRICE = "price";

    public static final String COL_TYPE = "type";

    public static final String COL_PRODUCT_DESC = "product_desc";

    public static final String COL_CAPTION = "caption";

    public static final String COL_INVENTORY = "inventory";
}
