package com.daxiang.typehandler;

import com.alibaba.fastjson.JSONObject;
import com.daxiang.mbg.po.TestPlan;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by jiangyitao.
 */
public class TestPlanTypeHandler extends BaseTypeHandler<TestPlan> {
    @Override
    public void setNonNullParameter(PreparedStatement preparedStatement, int i, TestPlan testPlan, JdbcType jdbcType) throws SQLException {
        preparedStatement.setString(i, JSONObject.toJSONString(testPlan));
    }

    @Override
    public TestPlan getNullableResult(ResultSet resultSet, String s) throws SQLException {
        return JSONObject.parseObject(resultSet.getString(s), TestPlan.class);
    }

    @Override
    public TestPlan getNullableResult(ResultSet resultSet, int i) throws SQLException {
        return JSONObject.parseObject(resultSet.getString(i), TestPlan.class);
    }

    @Override
    public TestPlan getNullableResult(CallableStatement callableStatement, int i) throws SQLException {
        return JSONObject.parseObject(callableStatement.getString(i), TestPlan.class);
    }
}
