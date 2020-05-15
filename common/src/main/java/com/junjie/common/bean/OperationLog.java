package com.junjie.common.bean;

import cn.afterturn.easypoi.excel.annotation.Excel;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
public class OperationLog extends BaseEntity {

    private Long id;
    protected static final long serialVersionUID = 1L;
    @Excel(name = "手机号", orderNum = "0", width = 15)
    private String phone;
    @Excel(name = "姓名", orderNum = "1", width = 15)
    private String operator;
    private Long terminal;
    @Excel(name = "操作内容", orderNum = "3", width = 15)
    private String operationContent;
    @Excel(name = "终端", orderNum = "2", width = 15)
    private String platform;


}
