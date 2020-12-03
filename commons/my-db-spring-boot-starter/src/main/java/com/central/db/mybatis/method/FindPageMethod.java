//package com.central.db.config.mybatis.method;
//
//import com.baomidou.mybatisplus.core.enums.SqlMethod;
//import com.baomidou.mybatisplus.core.injector.AbstractMethod;
//import com.baomidou.mybatisplus.core.metadata.TableInfo;
//import com.baomidou.mybatisplus.core.toolkit.sql.SqlScriptUtils;
//import org.apache.ibatis.mapping.MappedStatement;
//import org.apache.ibatis.mapping.SqlSource;
//
///**
// * 自定义通用（分页查询）满足条件的所有数据（无视逻辑删除状态）
// * @author yujunjie
// */
//public class FindPageMethod extends AbstractMethod {
//    @Override
//    public MappedStatement injectMappedStatement(Class<?> mapperClass, Class<?> modelClass, TableInfo tableInfo) {
////        String preSql = "<script>\nSELECT %s FROM %s %s %s\n</script>";
//        String methodName = "findPage";
//        SqlMethod sqlMethod = SqlMethod.SELECT_PAGE;
//        String sql = String.format(sqlMethod.getSql(),
//                sqlSelectColumns(tableInfo, true),
//                tableInfo.getTableName(),
//                sqlWhereEntityWrapper(true, tableInfo),
//                sqlComment());
//        SqlSource sqlSource = languageDriver.createSqlSource(configuration, sql, modelClass);
//        return this.addSelectMappedStatementForTable(mapperClass, methodName, sqlSource, tableInfo);
//    }
//
//    /**
//     * 重写父类方法，只执行无删除逻辑的代码
//     *
//     * @param newLine 是否提到下一行
//     * @param table   表信息
//     * @return String
//     */
//    @Override
//    protected String sqlWhereEntityWrapper(boolean newLine, TableInfo table) {
//        String sqlScript = table.getAllSqlWhere(false, true, WRAPPER_ENTITY_DOT);
//        sqlScript = SqlScriptUtils.convertIf(sqlScript, String.format("%s != null", WRAPPER_ENTITY), true);
//        sqlScript += NEWLINE;
//        sqlScript += SqlScriptUtils.convertIf(String.format(SqlScriptUtils.convertIf(" AND", String.format("%s and %s", WRAPPER_NONEMPTYOFENTITY, WRAPPER_NONEMPTYOFNORMAL), false) + " ${%s}", WRAPPER_SQLSEGMENT),
//                String.format("%s != null and %s != '' and %s", WRAPPER_SQLSEGMENT, WRAPPER_SQLSEGMENT,
//                        WRAPPER_NONEMPTYOFWHERE), true);
//        sqlScript = SqlScriptUtils.convertWhere(sqlScript) + NEWLINE;
//        sqlScript += SqlScriptUtils.convertIf(String.format(" ${%s}", WRAPPER_SQLSEGMENT),
//                String.format("%s != null and %s != '' and %s", WRAPPER_SQLSEGMENT, WRAPPER_SQLSEGMENT,
//                        WRAPPER_EMPTYOFWHERE), true);
//        sqlScript = SqlScriptUtils.convertIf(sqlScript, String.format("%s != null", WRAPPER), true);
//        return newLine ? NEWLINE + sqlScript : sqlScript;
//    }
//}
