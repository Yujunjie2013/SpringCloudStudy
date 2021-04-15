package com.central.db.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;

@Getter
@Setter
@RefreshScope
@ConfigurationProperties(prefix = "com.mydb.mybatis-plus.auto-fill")
public class MybatisPlusAutoFillProperties {
    /**
     * 默认删除值
     */
    private Boolean logicDelete = false;
    /**
     * 是否开启了插入填充
     */
    private Boolean enableInsertFill = true;
    /**
     * 是否开启了更新填充
     */
    private Boolean enableUpdateFill = true;
    /**
     * 创建时间字段名
     */
    private String createTimeField = "createTime";
    /**
     * 更新时间字段名
     */
    private String updateTimeField = "updateTime";
    /**
     * 更新逻辑删除
     */
    private String logicDeleteField = "logicDelete";
}
