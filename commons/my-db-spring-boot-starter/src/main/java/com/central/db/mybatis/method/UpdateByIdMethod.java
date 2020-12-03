//package com.central.db.config.mybatis.method;
//
//import com.baomidou.mybatisplus.core.enums.SqlMethod;
//import com.baomidou.mybatisplus.core.injector.AbstractMethod;
//import com.baomidou.mybatisplus.core.metadata.TableInfo;
//import org.apache.ibatis.mapping.MappedStatement;
//import org.apache.ibatis.mapping.SqlSource;
//
///**
// * 自定义更新操作，不带逻辑删除的更新(无视逻辑删除)
// * @author yujunjie
// */
//public class UpdateByIdMethod extends AbstractMethod {
//    @Override
//    public MappedStatement injectMappedStatement(Class<?> mapperClass, Class<?> modelClass, TableInfo tableInfo) {
//        SqlMethod sqlMethod = SqlMethod.UPDATE_BY_ID;
//        String methodName = "updateById2Entity";
//        final String additional = optlockVersion(tableInfo);
//        String sql = String.format(sqlMethod.getSql(), tableInfo.getTableName(),
//                sqlSet(false, false, tableInfo, false, ENTITY, ENTITY_DOT),
//                tableInfo.getKeyColumn(), ENTITY_DOT + tableInfo.getKeyProperty(), additional);
//        SqlSource sqlSource = languageDriver.createSqlSource(configuration, sql, modelClass);
//        return addUpdateMappedStatement(mapperClass, modelClass, methodName, sqlSource);
//    }
//}
