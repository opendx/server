package com.daxiang.typehandler;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by jiangyitao.
 */
public abstract class ListTypeHandler extends BaseTypeHandler<List> {

    public abstract Class getTypeClass();

    @Override
    public void setNonNullParameter(PreparedStatement preparedStatement, int i, List list, JdbcType jdbcType) throws SQLException {
        preparedStatement.setString(i, JSONArray.toJSONString(list));
    }

    @Override
    public List getNullableResult(ResultSet resultSet, String s) throws SQLException {
        return JSONObject.parseArray(resultSet.getString(s), getTypeClass());
    }

    @Override
    public List getNullableResult(ResultSet resultSet, int i) throws SQLException {
        return JSONObject.parseArray(resultSet.getString(i), getTypeClass());
    }

    @Override
    public List getNullableResult(CallableStatement callableStatement, int i) throws SQLException {
        return JSONObject.parseArray(callableStatement.getString(i), getTypeClass());
    }
}
