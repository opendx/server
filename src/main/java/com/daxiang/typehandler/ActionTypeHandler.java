package com.daxiang.typehandler;

import com.alibaba.fastjson.JSONObject;
import com.daxiang.mbg.po.Action;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by jiangyitao.
 */
public class ActionTypeHandler extends BaseTypeHandler<Action> {
    @Override
    public void setNonNullParameter(PreparedStatement preparedStatement, int i, Action action, JdbcType jdbcType) throws SQLException {
        preparedStatement.setString(i, JSONObject.toJSONString(action));
    }

    @Override
    public Action getNullableResult(ResultSet resultSet, String s) throws SQLException {
        return JSONObject.parseObject(resultSet.getString(s), Action.class);
    }

    @Override
    public Action getNullableResult(ResultSet resultSet, int i) throws SQLException {
        return JSONObject.parseObject(resultSet.getString(i), Action.class);
    }

    @Override
    public Action getNullableResult(CallableStatement callableStatement, int i) throws SQLException {
        return JSONObject.parseObject(callableStatement.getString(i), Action.class);
    }
}
