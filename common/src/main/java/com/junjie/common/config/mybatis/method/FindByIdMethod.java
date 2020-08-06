//package com.junjie.common.config.mybatis.method;
//
//import com.baomidou.mybatisplus.core.injector.AbstractMethod;
//import com.baomidou.mybatisplus.core.metadata.TableInfo;
//import org.apache.ibatis.mapping.MappedStatement;
//import org.apache.ibatis.mapping.SqlSource;
//import org.apache.ibatis.scripting.defaults.RawSqlSource;
//
///**
// * 自定义查询语句，查询一个无状态对象(无视逻辑删除)
// * @author yujunjie
// */
//public class FindByIdMethod extends AbstractMethod {
//    @Override
//    public MappedStatement injectMappedStatement(Class<?> mapperClass, Class<?> modelClass, TableInfo tableInfo) {
//        String preSql = "SELECT %s FROM %s WHERE %s=#{%s}";
////        SqlMethod sqlMethod = SqlMethod.LOGIC_SELECT_BY_ID;
//        String methodName = "findById2";
//        SqlSource sqlSource = new RawSqlSource(configuration,
//                String.format(preSql,
//                        sqlSelectColumns(tableInfo, false),
//                        tableInfo.getTableName(),
//                        tableInfo.getKeyColumn(),
//                        tableInfo.getKeyProperty()
//                ), Object.class);
//
//        return this.addSelectMappedStatementForTable(mapperClass, methodName, sqlSource, tableInfo);
////
////        String preSql = "SELECT %s FROM %s LIMIT 1";
////        String methodName = "findById";
////        String sql = String.format(preSql, sqlSelectColumns(tableInfo, false), tableInfo.getTableName());
////        SqlSource sqlSource = languageDriver.createSqlSource(configuration, sql, modelClass);
////        return addSelectMappedStatementForTable(modelClass, methodName, sqlSource, tableInfo);
//    }
//}
