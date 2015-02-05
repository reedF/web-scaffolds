package com.reed.pg.special.domain.handler;

import java.math.BigDecimal;
import java.sql.Array;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

/**
 * Decimal类型处理器 MyBatis使用类型处理器（type handler）将数据从数据库特定的数据类型转换为应用程序中的数据类型
 * 
 * @author reed
 * 
 */
public class DecimalArrayTypeHandler extends BaseTypeHandler<BigDecimal[]> {

	public DecimalArrayTypeHandler() {
		super();
	}

	@Override
	public void setNonNullParameter(PreparedStatement ps, int i,
			BigDecimal[] parameter, JdbcType jdbcType) throws SQLException {
		Array arr = ps.getConnection().createArrayOf("numeric", parameter);
		ps.setArray(i, arr);
	}

	@Override
	public BigDecimal[] getNullableResult(ResultSet rs, String columnName)
			throws SQLException {
		Array array = rs.getArray(columnName);
		if (array == null || array.getArray() == null) {
			return null;
		}
		return (BigDecimal[]) array.getArray();
	}

	@Override
	public BigDecimal[] getNullableResult(ResultSet rs, int columnIndex)
			throws SQLException {
		Array array = rs.getArray(columnIndex);
		if (array == null || array.getArray() == null) {
			return null;
		}
		return (BigDecimal[]) array.getArray();
	}

	@Override
	public BigDecimal[] getNullableResult(CallableStatement cs, int columnIndex)
			throws SQLException {
		Array array = cs.getArray(columnIndex);
		if (array == null || array.getArray() == null) {
			return null;
		}
		return (BigDecimal[]) array.getArray();
	}
}