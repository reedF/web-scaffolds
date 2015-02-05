package com.reed.pg.special.domain.handler;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.postgresql.util.PGobject;

import com.reed.common.util.JsonUtil;

/**
 * Json类型处理器 继承自BaseTypeHandler<Object>
 * 
 * @author reed
 * 
 */
public class JsonTypeHandler extends BaseTypeHandler<Object> {

	@Override
	public void setNonNullParameter(PreparedStatement ps, int i,
			Object parameter, JdbcType jdbcType) throws SQLException {
		PGobject jsonObject = new PGobject();
		jsonObject.setType("json");
		jsonObject.setValue((String) parameter);
		ps.setObject(i, jsonObject);
	}

	@Override
	public Object getNullableResult(ResultSet rs, String columnName)
			throws SQLException {
		return JsonUtil.json2Object(rs.getString(columnName), Object.class);
	}

	@Override
	public Object getNullableResult(ResultSet rs, int columnIndex)
			throws SQLException {
		return JsonUtil.json2Object(rs.getString(columnIndex), Object.class);
	}

	@Override
	public Object getNullableResult(CallableStatement cs, int columnIndex)
			throws SQLException {
		return JsonUtil.json2Object(cs.getString(columnIndex), Object.class);
	}

}
