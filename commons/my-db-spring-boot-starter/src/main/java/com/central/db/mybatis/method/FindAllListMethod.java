//package com.central.db.config.mybatis.method;
//
//import com.baomidou.mybatisplus.core.enums.SqlMethod;
//import com.baomidou.mybatisplus.core.injector.AbstractMethod;
//import com.baomidou.mybatisplus.core.metadata.TableInfo;
//import org.apache.ibatis.mapping.MappedStatement;
//import org.apache.ibatis.mapping.SqlSource;
//
///**
// * 自定义查询所有，返回一个list，包含无状态数据(无视逻辑删除)
// * @author yujunjie
// */
//public class FindAllListMethod extends AbstractMethod {
//    @Override
//    public MappedStatement injectMappedStatement(Class<?> mapperClass, Class<?> modelClass, TableInfo tableInfo) {
////        SqlMethod sqlMethod = SqlMethod.SELECT_LIST;
////        String sql = String.format(sqlMethod.getSql(), sqlSelectColumns(tableInfo, true),
////                tableInfo.getTableName(), sqlWhereEntityWrapper(true, tableInfo),
////                sqlComment());
////        SqlSource sqlSource = languageDriver.createSqlSource(configuration, sql, modelClass);
////        return this.addSelectMappedStatementForTable(mapperClass, sqlMethod.getMethod(), sqlSource, tableInfo);
//        String preSql = "SELECT %s FROM %s";
//        String methodName = "findAllList";
//        String sql = String.format(preSql, sqlSelectColumns(tableInfo, false), tableInfo.getTableName());
//        SqlSource sqlSource = languageDriver.createSqlSource(configuration, sql, modelClass);
//        return addSelectMappedStatementForTable(modelClass, methodName, sqlSource, tableInfo);
//    }
//
//    @Override
//    public String getMethod(SqlMethod sqlMethod) {
//        return super.getMethod(sqlMethod);
//    }
//}
