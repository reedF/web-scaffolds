package com.reed.pg.special.domain.handler;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import java.sql.*;

/**
 * Integer类型处理器
 * MyBatis使用类型处理器（type handler）将数据从数据库特定的数据类型转换为应用程序中的数据类型，
 * 这样你就可以创建一个以一种尽可能透明的方式来使用数据库的应用程序。
 * 类型处理器本质上就是一个翻译器（translator）--他将数据库返回的结果集合中的列“翻译”为相应的JavaBean中的字段。
 * @author Luke
 *
 */
public class IntArrayTypeHandler extends BaseTypeHandler<Integer[]> {

    public IntArrayTypeHandler() {
        super();
    }

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i,
    		Integer[] parameter, JdbcType jdbcType) throws SQLException {
    	Array arr = ps.getConnection().createArrayOf("int4", parameter);
        ps.setArray(i, arr);
    }

    @Override
    public Integer[] getNullableResult(ResultSet rs, String columnName) 
            throws SQLException {
        Array array = rs.getArray(columnName);
        if(array == null) {
        	return null;
        }
        return (Integer[])array.getArray();
    }

    @Override
    public Integer[] getNullableResult(ResultSet rs, int columnIndex)
            throws SQLException {
        Array array = rs.getArray(columnIndex);
        if(array == null) {
        	return null;
        }
        return (Integer[])array.getArray();
    }

    @Override
    public Integer[] getNullableResult(CallableStatement cs, int columnIndex)
            throws SQLException {
        Array array = cs.getArray(columnIndex);
        if(array == null) {
        	return null;
        }
        return (Integer[])array.getArray();
    }
}