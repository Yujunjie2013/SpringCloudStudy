package com.central.db.mybatis.method;

import com.baomidou.mybatisplus.core.injector.AbstractMethod;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlSource;

/**
 * 查询满足条件一条数据，自带limit 1
 */
public class SelectOneLimit extends AbstractMethod {

    @Override
    public MappedStatement injectMappedStatement(Class<?> mapperClass, Class<?> modelClass, TableInfo tableInfo) {
//        SqlMethod sqlMethod = SqlMethod.SELECT_ONE;
        String sql = "<script>%s SELECT %s FROM %s %s %s limit 1\n</script>";
        SqlSource sqlSource = languageDriver.createSqlSource(configuration, String.format(sql,
                sqlFirst(), sqlSelectColumns(tableInfo, true), tableInfo.getTableName(),
                sqlWhereEntityWrapper(true, tableInfo), sqlComment()), modelClass);
        return this.addSelectMappedStatementForTable(mapperClass, "selectOneLimit", sqlSource, tableInfo);
    }
}
