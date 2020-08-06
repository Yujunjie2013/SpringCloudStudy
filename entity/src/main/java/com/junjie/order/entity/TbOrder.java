package com.junjie.order.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.junjie.common.bean.BaseEntity;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@TableName(value = "tb_order")
@ToString
public class TbOrder extends BaseEntity {
    @TableId(value = "id", type = IdType.INPUT)
    private Integer id;

    @TableField(value = "product_id")
    private Integer productId;

    @TableField(value = "order_number")
    private String orderNumber;

    private static final long serialVersionUID = 1L;

    public static final String COL_ID = "id";

    public static final String COL_PRODUCT_ID = "product_id";

    public static final String COL_ORDER_NUMBER = "order_number";
}