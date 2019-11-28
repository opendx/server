package com.daxiang.mbg.po;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TestPlanExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public TestPlanExample() {
        oredCriteria = new ArrayList<Criteria>();
    }

    public void setOrderByClause(String orderByClause) {
        this.orderByClause = orderByClause;
    }

    public String getOrderByClause() {
        return orderByClause;
    }

    public void setDistinct(boolean distinct) {
        this.distinct = distinct;
    }

    public boolean isDistinct() {
        return distinct;
    }

    public List<Criteria> getOredCriteria() {
        return oredCriteria;
    }

    public void or(Criteria criteria) {
        oredCriteria.add(criteria);
    }

    public Criteria or() {
        Criteria criteria = createCriteriaInternal();
        oredCriteria.add(criteria);
        return criteria;
    }

    public Criteria createCriteria() {
        Criteria criteria = createCriteriaInternal();
        if (oredCriteria.size() == 0) {
            oredCriteria.add(criteria);
        }
        return criteria;
    }

    protected Criteria createCriteriaInternal() {
        Criteria criteria = new Criteria();
        return criteria;
    }

    public void clear() {
        oredCriteria.clear();
        orderByClause = null;
        distinct = false;
    }

    protected abstract static class GeneratedCriteria {
        protected List<Criterion> criteria;

        protected GeneratedCriteria() {
            super();
            criteria = new ArrayList<Criterion>();
        }

        public boolean isValid() {
            return criteria.size() > 0;
        }

        public List<Criterion> getAllCriteria() {
            return criteria;
        }

        public List<Criterion> getCriteria() {
            return criteria;
        }

        protected void addCriterion(String condition) {
            if (condition == null) {
                throw new RuntimeException("Value for condition cannot be null");
            }
            criteria.add(new Criterion(condition));
        }

        protected void addCriterion(String condition, Object value, String property) {
            if (value == null) {
                throw new RuntimeException("Value for " + property + " cannot be null");
            }
            criteria.add(new Criterion(condition, value));
        }

        protected void addCriterion(String condition, Object value1, Object value2, String property) {
            if (value1 == null || value2 == null) {
                throw new RuntimeException("Between values for " + property + " cannot be null");
            }
            criteria.add(new Criterion(condition, value1, value2));
        }

        public Criteria andIdIsNull() {
            addCriterion("id is null");
            return (Criteria) this;
        }

        public Criteria andIdIsNotNull() {
            addCriterion("id is not null");
            return (Criteria) this;
        }

        public Criteria andIdEqualTo(Integer value) {
            addCriterion("id =", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotEqualTo(Integer value) {
            addCriterion("id <>", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdGreaterThan(Integer value) {
            addCriterion("id >", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdGreaterThanOrEqualTo(Integer value) {
            addCriterion("id >=", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdLessThan(Integer value) {
            addCriterion("id <", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdLessThanOrEqualTo(Integer value) {
            addCriterion("id <=", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdIn(List<Integer> values) {
            addCriterion("id in", values, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotIn(List<Integer> values) {
            addCriterion("id not in", values, "id");
            return (Criteria) this;
        }

        public Criteria andIdBetween(Integer value1, Integer value2) {
            addCriterion("id between", value1, value2, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotBetween(Integer value1, Integer value2) {
            addCriterion("id not between", value1, value2, "id");
            return (Criteria) this;
        }

        public Criteria andNameIsNull() {
            addCriterion("name is null");
            return (Criteria) this;
        }

        public Criteria andNameIsNotNull() {
            addCriterion("name is not null");
            return (Criteria) this;
        }

        public Criteria andNameEqualTo(String value) {
            addCriterion("name =", value, "name");
            return (Criteria) this;
        }

        public Criteria andNameNotEqualTo(String value) {
            addCriterion("name <>", value, "name");
            return (Criteria) this;
        }

        public Criteria andNameGreaterThan(String value) {
            addCriterion("name >", value, "name");
            return (Criteria) this;
        }

        public Criteria andNameGreaterThanOrEqualTo(String value) {
            addCriterion("name >=", value, "name");
            return (Criteria) this;
        }

        public Criteria andNameLessThan(String value) {
            addCriterion("name <", value, "name");
            return (Criteria) this;
        }

        public Criteria andNameLessThanOrEqualTo(String value) {
            addCriterion("name <=", value, "name");
            return (Criteria) this;
        }

        public Criteria andNameLike(String value) {
            addCriterion("name like", value, "name");
            return (Criteria) this;
        }

        public Criteria andNameNotLike(String value) {
            addCriterion("name not like", value, "name");
            return (Criteria) this;
        }

        public Criteria andNameIn(List<String> values) {
            addCriterion("name in", values, "name");
            return (Criteria) this;
        }

        public Criteria andNameNotIn(List<String> values) {
            addCriterion("name not in", values, "name");
            return (Criteria) this;
        }

        public Criteria andNameBetween(String value1, String value2) {
            addCriterion("name between", value1, value2, "name");
            return (Criteria) this;
        }

        public Criteria andNameNotBetween(String value1, String value2) {
            addCriterion("name not between", value1, value2, "name");
            return (Criteria) this;
        }

        public Criteria andDescriptionIsNull() {
            addCriterion("description is null");
            return (Criteria) this;
        }

        public Criteria andDescriptionIsNotNull() {
            addCriterion("description is not null");
            return (Criteria) this;
        }

        public Criteria andDescriptionEqualTo(String value) {
            addCriterion("description =", value, "description");
            return (Criteria) this;
        }

        public Criteria andDescriptionNotEqualTo(String value) {
            addCriterion("description <>", value, "description");
            return (Criteria) this;
        }

        public Criteria andDescriptionGreaterThan(String value) {
            addCriterion("description >", value, "description");
            return (Criteria) this;
        }

        public Criteria andDescriptionGreaterThanOrEqualTo(String value) {
            addCriterion("description >=", value, "description");
            return (Criteria) this;
        }

        public Criteria andDescriptionLessThan(String value) {
            addCriterion("description <", value, "description");
            return (Criteria) this;
        }

        public Criteria andDescriptionLessThanOrEqualTo(String value) {
            addCriterion("description <=", value, "description");
            return (Criteria) this;
        }

        public Criteria andDescriptionLike(String value) {
            addCriterion("description like", value, "description");
            return (Criteria) this;
        }

        public Criteria andDescriptionNotLike(String value) {
            addCriterion("description not like", value, "description");
            return (Criteria) this;
        }

        public Criteria andDescriptionIn(List<String> values) {
            addCriterion("description in", values, "description");
            return (Criteria) this;
        }

        public Criteria andDescriptionNotIn(List<String> values) {
            addCriterion("description not in", values, "description");
            return (Criteria) this;
        }

        public Criteria andDescriptionBetween(String value1, String value2) {
            addCriterion("description between", value1, value2, "description");
            return (Criteria) this;
        }

        public Criteria andDescriptionNotBetween(String value1, String value2) {
            addCriterion("description not between", value1, value2, "description");
            return (Criteria) this;
        }

        public Criteria andProjectIdIsNull() {
            addCriterion("project_id is null");
            return (Criteria) this;
        }

        public Criteria andProjectIdIsNotNull() {
            addCriterion("project_id is not null");
            return (Criteria) this;
        }

        public Criteria andProjectIdEqualTo(Integer value) {
            addCriterion("project_id =", value, "projectId");
            return (Criteria) this;
        }

        public Criteria andProjectIdNotEqualTo(Integer value) {
            addCriterion("project_id <>", value, "projectId");
            return (Criteria) this;
        }

        public Criteria andProjectIdGreaterThan(Integer value) {
            addCriterion("project_id >", value, "projectId");
            return (Criteria) this;
        }

        public Criteria andProjectIdGreaterThanOrEqualTo(Integer value) {
            addCriterion("project_id >=", value, "projectId");
            return (Criteria) this;
        }

        public Criteria andProjectIdLessThan(Integer value) {
            addCriterion("project_id <", value, "projectId");
            return (Criteria) this;
        }

        public Criteria andProjectIdLessThanOrEqualTo(Integer value) {
            addCriterion("project_id <=", value, "projectId");
            return (Criteria) this;
        }

        public Criteria andProjectIdIn(List<Integer> values) {
            addCriterion("project_id in", values, "projectId");
            return (Criteria) this;
        }

        public Criteria andProjectIdNotIn(List<Integer> values) {
            addCriterion("project_id not in", values, "projectId");
            return (Criteria) this;
        }

        public Criteria andProjectIdBetween(Integer value1, Integer value2) {
            addCriterion("project_id between", value1, value2, "projectId");
            return (Criteria) this;
        }

        public Criteria andProjectIdNotBetween(Integer value1, Integer value2) {
            addCriterion("project_id not between", value1, value2, "projectId");
            return (Criteria) this;
        }

        public Criteria andEnvironmentIdIsNull() {
            addCriterion("environment_id is null");
            return (Criteria) this;
        }

        public Criteria andEnvironmentIdIsNotNull() {
            addCriterion("environment_id is not null");
            return (Criteria) this;
        }

        public Criteria andEnvironmentIdEqualTo(Integer value) {
            addCriterion("environment_id =", value, "environmentId");
            return (Criteria) this;
        }

        public Criteria andEnvironmentIdNotEqualTo(Integer value) {
            addCriterion("environment_id <>", value, "environmentId");
            return (Criteria) this;
        }

        public Criteria andEnvironmentIdGreaterThan(Integer value) {
            addCriterion("environment_id >", value, "environmentId");
            return (Criteria) this;
        }

        public Criteria andEnvironmentIdGreaterThanOrEqualTo(Integer value) {
            addCriterion("environment_id >=", value, "environmentId");
            return (Criteria) this;
        }

        public Criteria andEnvironmentIdLessThan(Integer value) {
            addCriterion("environment_id <", value, "environmentId");
            return (Criteria) this;
        }

        public Criteria andEnvironmentIdLessThanOrEqualTo(Integer value) {
            addCriterion("environment_id <=", value, "environmentId");
            return (Criteria) this;
        }

        public Criteria andEnvironmentIdIn(List<Integer> values) {
            addCriterion("environment_id in", values, "environmentId");
            return (Criteria) this;
        }

        public Criteria andEnvironmentIdNotIn(List<Integer> values) {
            addCriterion("environment_id not in", values, "environmentId");
            return (Criteria) this;
        }

        public Criteria andEnvironmentIdBetween(Integer value1, Integer value2) {
            addCriterion("environment_id between", value1, value2, "environmentId");
            return (Criteria) this;
        }

        public Criteria andEnvironmentIdNotBetween(Integer value1, Integer value2) {
            addCriterion("environment_id not between", value1, value2, "environmentId");
            return (Criteria) this;
        }

        public Criteria andBeforeClassIsNull() {
            addCriterion("before_class is null");
            return (Criteria) this;
        }

        public Criteria andBeforeClassIsNotNull() {
            addCriterion("before_class is not null");
            return (Criteria) this;
        }

        public Criteria andBeforeClassEqualTo(Integer value) {
            addCriterion("before_class =", value, "beforeClass");
            return (Criteria) this;
        }

        public Criteria andBeforeClassNotEqualTo(Integer value) {
            addCriterion("before_class <>", value, "beforeClass");
            return (Criteria) this;
        }

        public Criteria andBeforeClassGreaterThan(Integer value) {
            addCriterion("before_class >", value, "beforeClass");
            return (Criteria) this;
        }

        public Criteria andBeforeClassGreaterThanOrEqualTo(Integer value) {
            addCriterion("before_class >=", value, "beforeClass");
            return (Criteria) this;
        }

        public Criteria andBeforeClassLessThan(Integer value) {
            addCriterion("before_class <", value, "beforeClass");
            return (Criteria) this;
        }

        public Criteria andBeforeClassLessThanOrEqualTo(Integer value) {
            addCriterion("before_class <=", value, "beforeClass");
            return (Criteria) this;
        }

        public Criteria andBeforeClassIn(List<Integer> values) {
            addCriterion("before_class in", values, "beforeClass");
            return (Criteria) this;
        }

        public Criteria andBeforeClassNotIn(List<Integer> values) {
            addCriterion("before_class not in", values, "beforeClass");
            return (Criteria) this;
        }

        public Criteria andBeforeClassBetween(Integer value1, Integer value2) {
            addCriterion("before_class between", value1, value2, "beforeClass");
            return (Criteria) this;
        }

        public Criteria andBeforeClassNotBetween(Integer value1, Integer value2) {
            addCriterion("before_class not between", value1, value2, "beforeClass");
            return (Criteria) this;
        }

        public Criteria andBeforeMethodIsNull() {
            addCriterion("before_method is null");
            return (Criteria) this;
        }

        public Criteria andBeforeMethodIsNotNull() {
            addCriterion("before_method is not null");
            return (Criteria) this;
        }

        public Criteria andBeforeMethodEqualTo(Integer value) {
            addCriterion("before_method =", value, "beforeMethod");
            return (Criteria) this;
        }

        public Criteria andBeforeMethodNotEqualTo(Integer value) {
            addCriterion("before_method <>", value, "beforeMethod");
            return (Criteria) this;
        }

        public Criteria andBeforeMethodGreaterThan(Integer value) {
            addCriterion("before_method >", value, "beforeMethod");
            return (Criteria) this;
        }

        public Criteria andBeforeMethodGreaterThanOrEqualTo(Integer value) {
            addCriterion("before_method >=", value, "beforeMethod");
            return (Criteria) this;
        }

        public Criteria andBeforeMethodLessThan(Integer value) {
            addCriterion("before_method <", value, "beforeMethod");
            return (Criteria) this;
        }

        public Criteria andBeforeMethodLessThanOrEqualTo(Integer value) {
            addCriterion("before_method <=", value, "beforeMethod");
            return (Criteria) this;
        }

        public Criteria andBeforeMethodIn(List<Integer> values) {
            addCriterion("before_method in", values, "beforeMethod");
            return (Criteria) this;
        }

        public Criteria andBeforeMethodNotIn(List<Integer> values) {
            addCriterion("before_method not in", values, "beforeMethod");
            return (Criteria) this;
        }

        public Criteria andBeforeMethodBetween(Integer value1, Integer value2) {
            addCriterion("before_method between", value1, value2, "beforeMethod");
            return (Criteria) this;
        }

        public Criteria andBeforeMethodNotBetween(Integer value1, Integer value2) {
            addCriterion("before_method not between", value1, value2, "beforeMethod");
            return (Criteria) this;
        }

        public Criteria andAfterClassIsNull() {
            addCriterion("after_class is null");
            return (Criteria) this;
        }

        public Criteria andAfterClassIsNotNull() {
            addCriterion("after_class is not null");
            return (Criteria) this;
        }

        public Criteria andAfterClassEqualTo(Integer value) {
            addCriterion("after_class =", value, "afterClass");
            return (Criteria) this;
        }

        public Criteria andAfterClassNotEqualTo(Integer value) {
            addCriterion("after_class <>", value, "afterClass");
            return (Criteria) this;
        }

        public Criteria andAfterClassGreaterThan(Integer value) {
            addCriterion("after_class >", value, "afterClass");
            return (Criteria) this;
        }

        public Criteria andAfterClassGreaterThanOrEqualTo(Integer value) {
            addCriterion("after_class >=", value, "afterClass");
            return (Criteria) this;
        }

        public Criteria andAfterClassLessThan(Integer value) {
            addCriterion("after_class <", value, "afterClass");
            return (Criteria) this;
        }

        public Criteria andAfterClassLessThanOrEqualTo(Integer value) {
            addCriterion("after_class <=", value, "afterClass");
            return (Criteria) this;
        }

        public Criteria andAfterClassIn(List<Integer> values) {
            addCriterion("after_class in", values, "afterClass");
            return (Criteria) this;
        }

        public Criteria andAfterClassNotIn(List<Integer> values) {
            addCriterion("after_class not in", values, "afterClass");
            return (Criteria) this;
        }

        public Criteria andAfterClassBetween(Integer value1, Integer value2) {
            addCriterion("after_class between", value1, value2, "afterClass");
            return (Criteria) this;
        }

        public Criteria andAfterClassNotBetween(Integer value1, Integer value2) {
            addCriterion("after_class not between", value1, value2, "afterClass");
            return (Criteria) this;
        }

        public Criteria andAfterMethodIsNull() {
            addCriterion("after_method is null");
            return (Criteria) this;
        }

        public Criteria andAfterMethodIsNotNull() {
            addCriterion("after_method is not null");
            return (Criteria) this;
        }

        public Criteria andAfterMethodEqualTo(Integer value) {
            addCriterion("after_method =", value, "afterMethod");
            return (Criteria) this;
        }

        public Criteria andAfterMethodNotEqualTo(Integer value) {
            addCriterion("after_method <>", value, "afterMethod");
            return (Criteria) this;
        }

        public Criteria andAfterMethodGreaterThan(Integer value) {
            addCriterion("after_method >", value, "afterMethod");
            return (Criteria) this;
        }

        public Criteria andAfterMethodGreaterThanOrEqualTo(Integer value) {
            addCriterion("after_method >=", value, "afterMethod");
            return (Criteria) this;
        }

        public Criteria andAfterMethodLessThan(Integer value) {
            addCriterion("after_method <", value, "afterMethod");
            return (Criteria) this;
        }

        public Criteria andAfterMethodLessThanOrEqualTo(Integer value) {
            addCriterion("after_method <=", value, "afterMethod");
            return (Criteria) this;
        }

        public Criteria andAfterMethodIn(List<Integer> values) {
            addCriterion("after_method in", values, "afterMethod");
            return (Criteria) this;
        }

        public Criteria andAfterMethodNotIn(List<Integer> values) {
            addCriterion("after_method not in", values, "afterMethod");
            return (Criteria) this;
        }

        public Criteria andAfterMethodBetween(Integer value1, Integer value2) {
            addCriterion("after_method between", value1, value2, "afterMethod");
            return (Criteria) this;
        }

        public Criteria andAfterMethodNotBetween(Integer value1, Integer value2) {
            addCriterion("after_method not between", value1, value2, "afterMethod");
            return (Criteria) this;
        }

        public Criteria andRunModeIsNull() {
            addCriterion("run_mode is null");
            return (Criteria) this;
        }

        public Criteria andRunModeIsNotNull() {
            addCriterion("run_mode is not null");
            return (Criteria) this;
        }

        public Criteria andRunModeEqualTo(Integer value) {
            addCriterion("run_mode =", value, "runMode");
            return (Criteria) this;
        }

        public Criteria andRunModeNotEqualTo(Integer value) {
            addCriterion("run_mode <>", value, "runMode");
            return (Criteria) this;
        }

        public Criteria andRunModeGreaterThan(Integer value) {
            addCriterion("run_mode >", value, "runMode");
            return (Criteria) this;
        }

        public Criteria andRunModeGreaterThanOrEqualTo(Integer value) {
            addCriterion("run_mode >=", value, "runMode");
            return (Criteria) this;
        }

        public Criteria andRunModeLessThan(Integer value) {
            addCriterion("run_mode <", value, "runMode");
            return (Criteria) this;
        }

        public Criteria andRunModeLessThanOrEqualTo(Integer value) {
            addCriterion("run_mode <=", value, "runMode");
            return (Criteria) this;
        }

        public Criteria andRunModeIn(List<Integer> values) {
            addCriterion("run_mode in", values, "runMode");
            return (Criteria) this;
        }

        public Criteria andRunModeNotIn(List<Integer> values) {
            addCriterion("run_mode not in", values, "runMode");
            return (Criteria) this;
        }

        public Criteria andRunModeBetween(Integer value1, Integer value2) {
            addCriterion("run_mode between", value1, value2, "runMode");
            return (Criteria) this;
        }

        public Criteria andRunModeNotBetween(Integer value1, Integer value2) {
            addCriterion("run_mode not between", value1, value2, "runMode");
            return (Criteria) this;
        }

        public Criteria andCronExpressionIsNull() {
            addCriterion("cron_expression is null");
            return (Criteria) this;
        }

        public Criteria andCronExpressionIsNotNull() {
            addCriterion("cron_expression is not null");
            return (Criteria) this;
        }

        public Criteria andCronExpressionEqualTo(String value) {
            addCriterion("cron_expression =", value, "cronExpression");
            return (Criteria) this;
        }

        public Criteria andCronExpressionNotEqualTo(String value) {
            addCriterion("cron_expression <>", value, "cronExpression");
            return (Criteria) this;
        }

        public Criteria andCronExpressionGreaterThan(String value) {
            addCriterion("cron_expression >", value, "cronExpression");
            return (Criteria) this;
        }

        public Criteria andCronExpressionGreaterThanOrEqualTo(String value) {
            addCriterion("cron_expression >=", value, "cronExpression");
            return (Criteria) this;
        }

        public Criteria andCronExpressionLessThan(String value) {
            addCriterion("cron_expression <", value, "cronExpression");
            return (Criteria) this;
        }

        public Criteria andCronExpressionLessThanOrEqualTo(String value) {
            addCriterion("cron_expression <=", value, "cronExpression");
            return (Criteria) this;
        }

        public Criteria andCronExpressionLike(String value) {
            addCriterion("cron_expression like", value, "cronExpression");
            return (Criteria) this;
        }

        public Criteria andCronExpressionNotLike(String value) {
            addCriterion("cron_expression not like", value, "cronExpression");
            return (Criteria) this;
        }

        public Criteria andCronExpressionIn(List<String> values) {
            addCriterion("cron_expression in", values, "cronExpression");
            return (Criteria) this;
        }

        public Criteria andCronExpressionNotIn(List<String> values) {
            addCriterion("cron_expression not in", values, "cronExpression");
            return (Criteria) this;
        }

        public Criteria andCronExpressionBetween(String value1, String value2) {
            addCriterion("cron_expression between", value1, value2, "cronExpression");
            return (Criteria) this;
        }

        public Criteria andCronExpressionNotBetween(String value1, String value2) {
            addCriterion("cron_expression not between", value1, value2, "cronExpression");
            return (Criteria) this;
        }

        public Criteria andEnableScheduleIsNull() {
            addCriterion("enable_schedule is null");
            return (Criteria) this;
        }

        public Criteria andEnableScheduleIsNotNull() {
            addCriterion("enable_schedule is not null");
            return (Criteria) this;
        }

        public Criteria andEnableScheduleEqualTo(Integer value) {
            addCriterion("enable_schedule =", value, "enableSchedule");
            return (Criteria) this;
        }

        public Criteria andEnableScheduleNotEqualTo(Integer value) {
            addCriterion("enable_schedule <>", value, "enableSchedule");
            return (Criteria) this;
        }

        public Criteria andEnableScheduleGreaterThan(Integer value) {
            addCriterion("enable_schedule >", value, "enableSchedule");
            return (Criteria) this;
        }

        public Criteria andEnableScheduleGreaterThanOrEqualTo(Integer value) {
            addCriterion("enable_schedule >=", value, "enableSchedule");
            return (Criteria) this;
        }

        public Criteria andEnableScheduleLessThan(Integer value) {
            addCriterion("enable_schedule <", value, "enableSchedule");
            return (Criteria) this;
        }

        public Criteria andEnableScheduleLessThanOrEqualTo(Integer value) {
            addCriterion("enable_schedule <=", value, "enableSchedule");
            return (Criteria) this;
        }

        public Criteria andEnableScheduleIn(List<Integer> values) {
            addCriterion("enable_schedule in", values, "enableSchedule");
            return (Criteria) this;
        }

        public Criteria andEnableScheduleNotIn(List<Integer> values) {
            addCriterion("enable_schedule not in", values, "enableSchedule");
            return (Criteria) this;
        }

        public Criteria andEnableScheduleBetween(Integer value1, Integer value2) {
            addCriterion("enable_schedule between", value1, value2, "enableSchedule");
            return (Criteria) this;
        }

        public Criteria andEnableScheduleNotBetween(Integer value1, Integer value2) {
            addCriterion("enable_schedule not between", value1, value2, "enableSchedule");
            return (Criteria) this;
        }

        public Criteria andEnableRecordVideoIsNull() {
            addCriterion("enable_record_video is null");
            return (Criteria) this;
        }

        public Criteria andEnableRecordVideoIsNotNull() {
            addCriterion("enable_record_video is not null");
            return (Criteria) this;
        }

        public Criteria andEnableRecordVideoEqualTo(Integer value) {
            addCriterion("enable_record_video =", value, "enableRecordVideo");
            return (Criteria) this;
        }

        public Criteria andEnableRecordVideoNotEqualTo(Integer value) {
            addCriterion("enable_record_video <>", value, "enableRecordVideo");
            return (Criteria) this;
        }

        public Criteria andEnableRecordVideoGreaterThan(Integer value) {
            addCriterion("enable_record_video >", value, "enableRecordVideo");
            return (Criteria) this;
        }

        public Criteria andEnableRecordVideoGreaterThanOrEqualTo(Integer value) {
            addCriterion("enable_record_video >=", value, "enableRecordVideo");
            return (Criteria) this;
        }

        public Criteria andEnableRecordVideoLessThan(Integer value) {
            addCriterion("enable_record_video <", value, "enableRecordVideo");
            return (Criteria) this;
        }

        public Criteria andEnableRecordVideoLessThanOrEqualTo(Integer value) {
            addCriterion("enable_record_video <=", value, "enableRecordVideo");
            return (Criteria) this;
        }

        public Criteria andEnableRecordVideoIn(List<Integer> values) {
            addCriterion("enable_record_video in", values, "enableRecordVideo");
            return (Criteria) this;
        }

        public Criteria andEnableRecordVideoNotIn(List<Integer> values) {
            addCriterion("enable_record_video not in", values, "enableRecordVideo");
            return (Criteria) this;
        }

        public Criteria andEnableRecordVideoBetween(Integer value1, Integer value2) {
            addCriterion("enable_record_video between", value1, value2, "enableRecordVideo");
            return (Criteria) this;
        }

        public Criteria andEnableRecordVideoNotBetween(Integer value1, Integer value2) {
            addCriterion("enable_record_video not between", value1, value2, "enableRecordVideo");
            return (Criteria) this;
        }

        public Criteria andFailRetryCountIsNull() {
            addCriterion("fail_retry_count is null");
            return (Criteria) this;
        }

        public Criteria andFailRetryCountIsNotNull() {
            addCriterion("fail_retry_count is not null");
            return (Criteria) this;
        }

        public Criteria andFailRetryCountEqualTo(Integer value) {
            addCriterion("fail_retry_count =", value, "failRetryCount");
            return (Criteria) this;
        }

        public Criteria andFailRetryCountNotEqualTo(Integer value) {
            addCriterion("fail_retry_count <>", value, "failRetryCount");
            return (Criteria) this;
        }

        public Criteria andFailRetryCountGreaterThan(Integer value) {
            addCriterion("fail_retry_count >", value, "failRetryCount");
            return (Criteria) this;
        }

        public Criteria andFailRetryCountGreaterThanOrEqualTo(Integer value) {
            addCriterion("fail_retry_count >=", value, "failRetryCount");
            return (Criteria) this;
        }

        public Criteria andFailRetryCountLessThan(Integer value) {
            addCriterion("fail_retry_count <", value, "failRetryCount");
            return (Criteria) this;
        }

        public Criteria andFailRetryCountLessThanOrEqualTo(Integer value) {
            addCriterion("fail_retry_count <=", value, "failRetryCount");
            return (Criteria) this;
        }

        public Criteria andFailRetryCountIn(List<Integer> values) {
            addCriterion("fail_retry_count in", values, "failRetryCount");
            return (Criteria) this;
        }

        public Criteria andFailRetryCountNotIn(List<Integer> values) {
            addCriterion("fail_retry_count not in", values, "failRetryCount");
            return (Criteria) this;
        }

        public Criteria andFailRetryCountBetween(Integer value1, Integer value2) {
            addCriterion("fail_retry_count between", value1, value2, "failRetryCount");
            return (Criteria) this;
        }

        public Criteria andFailRetryCountNotBetween(Integer value1, Integer value2) {
            addCriterion("fail_retry_count not between", value1, value2, "failRetryCount");
            return (Criteria) this;
        }

        public Criteria andCreatorUidIsNull() {
            addCriterion("creator_uid is null");
            return (Criteria) this;
        }

        public Criteria andCreatorUidIsNotNull() {
            addCriterion("creator_uid is not null");
            return (Criteria) this;
        }

        public Criteria andCreatorUidEqualTo(Integer value) {
            addCriterion("creator_uid =", value, "creatorUid");
            return (Criteria) this;
        }

        public Criteria andCreatorUidNotEqualTo(Integer value) {
            addCriterion("creator_uid <>", value, "creatorUid");
            return (Criteria) this;
        }

        public Criteria andCreatorUidGreaterThan(Integer value) {
            addCriterion("creator_uid >", value, "creatorUid");
            return (Criteria) this;
        }

        public Criteria andCreatorUidGreaterThanOrEqualTo(Integer value) {
            addCriterion("creator_uid >=", value, "creatorUid");
            return (Criteria) this;
        }

        public Criteria andCreatorUidLessThan(Integer value) {
            addCriterion("creator_uid <", value, "creatorUid");
            return (Criteria) this;
        }

        public Criteria andCreatorUidLessThanOrEqualTo(Integer value) {
            addCriterion("creator_uid <=", value, "creatorUid");
            return (Criteria) this;
        }

        public Criteria andCreatorUidIn(List<Integer> values) {
            addCriterion("creator_uid in", values, "creatorUid");
            return (Criteria) this;
        }

        public Criteria andCreatorUidNotIn(List<Integer> values) {
            addCriterion("creator_uid not in", values, "creatorUid");
            return (Criteria) this;
        }

        public Criteria andCreatorUidBetween(Integer value1, Integer value2) {
            addCriterion("creator_uid between", value1, value2, "creatorUid");
            return (Criteria) this;
        }

        public Criteria andCreatorUidNotBetween(Integer value1, Integer value2) {
            addCriterion("creator_uid not between", value1, value2, "creatorUid");
            return (Criteria) this;
        }

        public Criteria andCreateTimeIsNull() {
            addCriterion("create_time is null");
            return (Criteria) this;
        }

        public Criteria andCreateTimeIsNotNull() {
            addCriterion("create_time is not null");
            return (Criteria) this;
        }

        public Criteria andCreateTimeEqualTo(Date value) {
            addCriterion("create_time =", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeNotEqualTo(Date value) {
            addCriterion("create_time <>", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeGreaterThan(Date value) {
            addCriterion("create_time >", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeGreaterThanOrEqualTo(Date value) {
            addCriterion("create_time >=", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeLessThan(Date value) {
            addCriterion("create_time <", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeLessThanOrEqualTo(Date value) {
            addCriterion("create_time <=", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeIn(List<Date> values) {
            addCriterion("create_time in", values, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeNotIn(List<Date> values) {
            addCriterion("create_time not in", values, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeBetween(Date value1, Date value2) {
            addCriterion("create_time between", value1, value2, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeNotBetween(Date value1, Date value2) {
            addCriterion("create_time not between", value1, value2, "createTime");
            return (Criteria) this;
        }
    }

    public static class Criteria extends GeneratedCriteria {

        protected Criteria() {
            super();
        }
    }

    public static class Criterion {
        private String condition;

        private Object value;

        private Object secondValue;

        private boolean noValue;

        private boolean singleValue;

        private boolean betweenValue;

        private boolean listValue;

        private String typeHandler;

        public String getCondition() {
            return condition;
        }

        public Object getValue() {
            return value;
        }

        public Object getSecondValue() {
            return secondValue;
        }

        public boolean isNoValue() {
            return noValue;
        }

        public boolean isSingleValue() {
            return singleValue;
        }

        public boolean isBetweenValue() {
            return betweenValue;
        }

        public boolean isListValue() {
            return listValue;
        }

        public String getTypeHandler() {
            return typeHandler;
        }

        protected Criterion(String condition) {
            super();
            this.condition = condition;
            this.typeHandler = null;
            this.noValue = true;
        }

        protected Criterion(String condition, Object value, String typeHandler) {
            super();
            this.condition = condition;
            this.value = value;
            this.typeHandler = typeHandler;
            if (value instanceof List<?>) {
                this.listValue = true;
            } else {
                this.singleValue = true;
            }
        }

        protected Criterion(String condition, Object value) {
            this(condition, value, null);
        }

        protected Criterion(String condition, Object value, Object secondValue, String typeHandler) {
            super();
            this.condition = condition;
            this.value = value;
            this.secondValue = secondValue;
            this.typeHandler = typeHandler;
            this.betweenValue = true;
        }

        protected Criterion(String condition, Object value, Object secondValue) {
            this(condition, value, secondValue, null);
        }
    }
}