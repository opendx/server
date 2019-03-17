package com.yqhp.mbg.po;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DeviceExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public DeviceExample() {
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

        public Criteria andIdEqualTo(String value) {
            addCriterion("id =", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotEqualTo(String value) {
            addCriterion("id <>", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdGreaterThan(String value) {
            addCriterion("id >", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdGreaterThanOrEqualTo(String value) {
            addCriterion("id >=", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdLessThan(String value) {
            addCriterion("id <", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdLessThanOrEqualTo(String value) {
            addCriterion("id <=", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdLike(String value) {
            addCriterion("id like", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotLike(String value) {
            addCriterion("id not like", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdIn(List<String> values) {
            addCriterion("id in", values, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotIn(List<String> values) {
            addCriterion("id not in", values, "id");
            return (Criteria) this;
        }

        public Criteria andIdBetween(String value1, String value2) {
            addCriterion("id between", value1, value2, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotBetween(String value1, String value2) {
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

        public Criteria andIpIsNull() {
            addCriterion("ip is null");
            return (Criteria) this;
        }

        public Criteria andIpIsNotNull() {
            addCriterion("ip is not null");
            return (Criteria) this;
        }

        public Criteria andIpEqualTo(String value) {
            addCriterion("ip =", value, "ip");
            return (Criteria) this;
        }

        public Criteria andIpNotEqualTo(String value) {
            addCriterion("ip <>", value, "ip");
            return (Criteria) this;
        }

        public Criteria andIpGreaterThan(String value) {
            addCriterion("ip >", value, "ip");
            return (Criteria) this;
        }

        public Criteria andIpGreaterThanOrEqualTo(String value) {
            addCriterion("ip >=", value, "ip");
            return (Criteria) this;
        }

        public Criteria andIpLessThan(String value) {
            addCriterion("ip <", value, "ip");
            return (Criteria) this;
        }

        public Criteria andIpLessThanOrEqualTo(String value) {
            addCriterion("ip <=", value, "ip");
            return (Criteria) this;
        }

        public Criteria andIpLike(String value) {
            addCriterion("ip like", value, "ip");
            return (Criteria) this;
        }

        public Criteria andIpNotLike(String value) {
            addCriterion("ip not like", value, "ip");
            return (Criteria) this;
        }

        public Criteria andIpIn(List<String> values) {
            addCriterion("ip in", values, "ip");
            return (Criteria) this;
        }

        public Criteria andIpNotIn(List<String> values) {
            addCriterion("ip not in", values, "ip");
            return (Criteria) this;
        }

        public Criteria andIpBetween(String value1, String value2) {
            addCriterion("ip between", value1, value2, "ip");
            return (Criteria) this;
        }

        public Criteria andIpNotBetween(String value1, String value2) {
            addCriterion("ip not between", value1, value2, "ip");
            return (Criteria) this;
        }

        public Criteria andAgentIpIsNull() {
            addCriterion("agent_ip is null");
            return (Criteria) this;
        }

        public Criteria andAgentIpIsNotNull() {
            addCriterion("agent_ip is not null");
            return (Criteria) this;
        }

        public Criteria andAgentIpEqualTo(String value) {
            addCriterion("agent_ip =", value, "agentIp");
            return (Criteria) this;
        }

        public Criteria andAgentIpNotEqualTo(String value) {
            addCriterion("agent_ip <>", value, "agentIp");
            return (Criteria) this;
        }

        public Criteria andAgentIpGreaterThan(String value) {
            addCriterion("agent_ip >", value, "agentIp");
            return (Criteria) this;
        }

        public Criteria andAgentIpGreaterThanOrEqualTo(String value) {
            addCriterion("agent_ip >=", value, "agentIp");
            return (Criteria) this;
        }

        public Criteria andAgentIpLessThan(String value) {
            addCriterion("agent_ip <", value, "agentIp");
            return (Criteria) this;
        }

        public Criteria andAgentIpLessThanOrEqualTo(String value) {
            addCriterion("agent_ip <=", value, "agentIp");
            return (Criteria) this;
        }

        public Criteria andAgentIpLike(String value) {
            addCriterion("agent_ip like", value, "agentIp");
            return (Criteria) this;
        }

        public Criteria andAgentIpNotLike(String value) {
            addCriterion("agent_ip not like", value, "agentIp");
            return (Criteria) this;
        }

        public Criteria andAgentIpIn(List<String> values) {
            addCriterion("agent_ip in", values, "agentIp");
            return (Criteria) this;
        }

        public Criteria andAgentIpNotIn(List<String> values) {
            addCriterion("agent_ip not in", values, "agentIp");
            return (Criteria) this;
        }

        public Criteria andAgentIpBetween(String value1, String value2) {
            addCriterion("agent_ip between", value1, value2, "agentIp");
            return (Criteria) this;
        }

        public Criteria andAgentIpNotBetween(String value1, String value2) {
            addCriterion("agent_ip not between", value1, value2, "agentIp");
            return (Criteria) this;
        }

        public Criteria andAgentPortIsNull() {
            addCriterion("agent_port is null");
            return (Criteria) this;
        }

        public Criteria andAgentPortIsNotNull() {
            addCriterion("agent_port is not null");
            return (Criteria) this;
        }

        public Criteria andAgentPortEqualTo(Integer value) {
            addCriterion("agent_port =", value, "agentPort");
            return (Criteria) this;
        }

        public Criteria andAgentPortNotEqualTo(Integer value) {
            addCriterion("agent_port <>", value, "agentPort");
            return (Criteria) this;
        }

        public Criteria andAgentPortGreaterThan(Integer value) {
            addCriterion("agent_port >", value, "agentPort");
            return (Criteria) this;
        }

        public Criteria andAgentPortGreaterThanOrEqualTo(Integer value) {
            addCriterion("agent_port >=", value, "agentPort");
            return (Criteria) this;
        }

        public Criteria andAgentPortLessThan(Integer value) {
            addCriterion("agent_port <", value, "agentPort");
            return (Criteria) this;
        }

        public Criteria andAgentPortLessThanOrEqualTo(Integer value) {
            addCriterion("agent_port <=", value, "agentPort");
            return (Criteria) this;
        }

        public Criteria andAgentPortIn(List<Integer> values) {
            addCriterion("agent_port in", values, "agentPort");
            return (Criteria) this;
        }

        public Criteria andAgentPortNotIn(List<Integer> values) {
            addCriterion("agent_port not in", values, "agentPort");
            return (Criteria) this;
        }

        public Criteria andAgentPortBetween(Integer value1, Integer value2) {
            addCriterion("agent_port between", value1, value2, "agentPort");
            return (Criteria) this;
        }

        public Criteria andAgentPortNotBetween(Integer value1, Integer value2) {
            addCriterion("agent_port not between", value1, value2, "agentPort");
            return (Criteria) this;
        }

        public Criteria andSystemVersionIsNull() {
            addCriterion("system_version is null");
            return (Criteria) this;
        }

        public Criteria andSystemVersionIsNotNull() {
            addCriterion("system_version is not null");
            return (Criteria) this;
        }

        public Criteria andSystemVersionEqualTo(String value) {
            addCriterion("system_version =", value, "systemVersion");
            return (Criteria) this;
        }

        public Criteria andSystemVersionNotEqualTo(String value) {
            addCriterion("system_version <>", value, "systemVersion");
            return (Criteria) this;
        }

        public Criteria andSystemVersionGreaterThan(String value) {
            addCriterion("system_version >", value, "systemVersion");
            return (Criteria) this;
        }

        public Criteria andSystemVersionGreaterThanOrEqualTo(String value) {
            addCriterion("system_version >=", value, "systemVersion");
            return (Criteria) this;
        }

        public Criteria andSystemVersionLessThan(String value) {
            addCriterion("system_version <", value, "systemVersion");
            return (Criteria) this;
        }

        public Criteria andSystemVersionLessThanOrEqualTo(String value) {
            addCriterion("system_version <=", value, "systemVersion");
            return (Criteria) this;
        }

        public Criteria andSystemVersionLike(String value) {
            addCriterion("system_version like", value, "systemVersion");
            return (Criteria) this;
        }

        public Criteria andSystemVersionNotLike(String value) {
            addCriterion("system_version not like", value, "systemVersion");
            return (Criteria) this;
        }

        public Criteria andSystemVersionIn(List<String> values) {
            addCriterion("system_version in", values, "systemVersion");
            return (Criteria) this;
        }

        public Criteria andSystemVersionNotIn(List<String> values) {
            addCriterion("system_version not in", values, "systemVersion");
            return (Criteria) this;
        }

        public Criteria andSystemVersionBetween(String value1, String value2) {
            addCriterion("system_version between", value1, value2, "systemVersion");
            return (Criteria) this;
        }

        public Criteria andSystemVersionNotBetween(String value1, String value2) {
            addCriterion("system_version not between", value1, value2, "systemVersion");
            return (Criteria) this;
        }

        public Criteria andApiLevelIsNull() {
            addCriterion("api_level is null");
            return (Criteria) this;
        }

        public Criteria andApiLevelIsNotNull() {
            addCriterion("api_level is not null");
            return (Criteria) this;
        }

        public Criteria andApiLevelEqualTo(String value) {
            addCriterion("api_level =", value, "apiLevel");
            return (Criteria) this;
        }

        public Criteria andApiLevelNotEqualTo(String value) {
            addCriterion("api_level <>", value, "apiLevel");
            return (Criteria) this;
        }

        public Criteria andApiLevelGreaterThan(String value) {
            addCriterion("api_level >", value, "apiLevel");
            return (Criteria) this;
        }

        public Criteria andApiLevelGreaterThanOrEqualTo(String value) {
            addCriterion("api_level >=", value, "apiLevel");
            return (Criteria) this;
        }

        public Criteria andApiLevelLessThan(String value) {
            addCriterion("api_level <", value, "apiLevel");
            return (Criteria) this;
        }

        public Criteria andApiLevelLessThanOrEqualTo(String value) {
            addCriterion("api_level <=", value, "apiLevel");
            return (Criteria) this;
        }

        public Criteria andApiLevelLike(String value) {
            addCriterion("api_level like", value, "apiLevel");
            return (Criteria) this;
        }

        public Criteria andApiLevelNotLike(String value) {
            addCriterion("api_level not like", value, "apiLevel");
            return (Criteria) this;
        }

        public Criteria andApiLevelIn(List<String> values) {
            addCriterion("api_level in", values, "apiLevel");
            return (Criteria) this;
        }

        public Criteria andApiLevelNotIn(List<String> values) {
            addCriterion("api_level not in", values, "apiLevel");
            return (Criteria) this;
        }

        public Criteria andApiLevelBetween(String value1, String value2) {
            addCriterion("api_level between", value1, value2, "apiLevel");
            return (Criteria) this;
        }

        public Criteria andApiLevelNotBetween(String value1, String value2) {
            addCriterion("api_level not between", value1, value2, "apiLevel");
            return (Criteria) this;
        }

        public Criteria andCpuAbiIsNull() {
            addCriterion("cpu_abi is null");
            return (Criteria) this;
        }

        public Criteria andCpuAbiIsNotNull() {
            addCriterion("cpu_abi is not null");
            return (Criteria) this;
        }

        public Criteria andCpuAbiEqualTo(String value) {
            addCriterion("cpu_abi =", value, "cpuAbi");
            return (Criteria) this;
        }

        public Criteria andCpuAbiNotEqualTo(String value) {
            addCriterion("cpu_abi <>", value, "cpuAbi");
            return (Criteria) this;
        }

        public Criteria andCpuAbiGreaterThan(String value) {
            addCriterion("cpu_abi >", value, "cpuAbi");
            return (Criteria) this;
        }

        public Criteria andCpuAbiGreaterThanOrEqualTo(String value) {
            addCriterion("cpu_abi >=", value, "cpuAbi");
            return (Criteria) this;
        }

        public Criteria andCpuAbiLessThan(String value) {
            addCriterion("cpu_abi <", value, "cpuAbi");
            return (Criteria) this;
        }

        public Criteria andCpuAbiLessThanOrEqualTo(String value) {
            addCriterion("cpu_abi <=", value, "cpuAbi");
            return (Criteria) this;
        }

        public Criteria andCpuAbiLike(String value) {
            addCriterion("cpu_abi like", value, "cpuAbi");
            return (Criteria) this;
        }

        public Criteria andCpuAbiNotLike(String value) {
            addCriterion("cpu_abi not like", value, "cpuAbi");
            return (Criteria) this;
        }

        public Criteria andCpuAbiIn(List<String> values) {
            addCriterion("cpu_abi in", values, "cpuAbi");
            return (Criteria) this;
        }

        public Criteria andCpuAbiNotIn(List<String> values) {
            addCriterion("cpu_abi not in", values, "cpuAbi");
            return (Criteria) this;
        }

        public Criteria andCpuAbiBetween(String value1, String value2) {
            addCriterion("cpu_abi between", value1, value2, "cpuAbi");
            return (Criteria) this;
        }

        public Criteria andCpuAbiNotBetween(String value1, String value2) {
            addCriterion("cpu_abi not between", value1, value2, "cpuAbi");
            return (Criteria) this;
        }

        public Criteria andCpuInfoIsNull() {
            addCriterion("cpu_info is null");
            return (Criteria) this;
        }

        public Criteria andCpuInfoIsNotNull() {
            addCriterion("cpu_info is not null");
            return (Criteria) this;
        }

        public Criteria andCpuInfoEqualTo(String value) {
            addCriterion("cpu_info =", value, "cpuInfo");
            return (Criteria) this;
        }

        public Criteria andCpuInfoNotEqualTo(String value) {
            addCriterion("cpu_info <>", value, "cpuInfo");
            return (Criteria) this;
        }

        public Criteria andCpuInfoGreaterThan(String value) {
            addCriterion("cpu_info >", value, "cpuInfo");
            return (Criteria) this;
        }

        public Criteria andCpuInfoGreaterThanOrEqualTo(String value) {
            addCriterion("cpu_info >=", value, "cpuInfo");
            return (Criteria) this;
        }

        public Criteria andCpuInfoLessThan(String value) {
            addCriterion("cpu_info <", value, "cpuInfo");
            return (Criteria) this;
        }

        public Criteria andCpuInfoLessThanOrEqualTo(String value) {
            addCriterion("cpu_info <=", value, "cpuInfo");
            return (Criteria) this;
        }

        public Criteria andCpuInfoLike(String value) {
            addCriterion("cpu_info like", value, "cpuInfo");
            return (Criteria) this;
        }

        public Criteria andCpuInfoNotLike(String value) {
            addCriterion("cpu_info not like", value, "cpuInfo");
            return (Criteria) this;
        }

        public Criteria andCpuInfoIn(List<String> values) {
            addCriterion("cpu_info in", values, "cpuInfo");
            return (Criteria) this;
        }

        public Criteria andCpuInfoNotIn(List<String> values) {
            addCriterion("cpu_info not in", values, "cpuInfo");
            return (Criteria) this;
        }

        public Criteria andCpuInfoBetween(String value1, String value2) {
            addCriterion("cpu_info between", value1, value2, "cpuInfo");
            return (Criteria) this;
        }

        public Criteria andCpuInfoNotBetween(String value1, String value2) {
            addCriterion("cpu_info not between", value1, value2, "cpuInfo");
            return (Criteria) this;
        }

        public Criteria andMemSizeIsNull() {
            addCriterion("mem_size is null");
            return (Criteria) this;
        }

        public Criteria andMemSizeIsNotNull() {
            addCriterion("mem_size is not null");
            return (Criteria) this;
        }

        public Criteria andMemSizeEqualTo(String value) {
            addCriterion("mem_size =", value, "memSize");
            return (Criteria) this;
        }

        public Criteria andMemSizeNotEqualTo(String value) {
            addCriterion("mem_size <>", value, "memSize");
            return (Criteria) this;
        }

        public Criteria andMemSizeGreaterThan(String value) {
            addCriterion("mem_size >", value, "memSize");
            return (Criteria) this;
        }

        public Criteria andMemSizeGreaterThanOrEqualTo(String value) {
            addCriterion("mem_size >=", value, "memSize");
            return (Criteria) this;
        }

        public Criteria andMemSizeLessThan(String value) {
            addCriterion("mem_size <", value, "memSize");
            return (Criteria) this;
        }

        public Criteria andMemSizeLessThanOrEqualTo(String value) {
            addCriterion("mem_size <=", value, "memSize");
            return (Criteria) this;
        }

        public Criteria andMemSizeLike(String value) {
            addCriterion("mem_size like", value, "memSize");
            return (Criteria) this;
        }

        public Criteria andMemSizeNotLike(String value) {
            addCriterion("mem_size not like", value, "memSize");
            return (Criteria) this;
        }

        public Criteria andMemSizeIn(List<String> values) {
            addCriterion("mem_size in", values, "memSize");
            return (Criteria) this;
        }

        public Criteria andMemSizeNotIn(List<String> values) {
            addCriterion("mem_size not in", values, "memSize");
            return (Criteria) this;
        }

        public Criteria andMemSizeBetween(String value1, String value2) {
            addCriterion("mem_size between", value1, value2, "memSize");
            return (Criteria) this;
        }

        public Criteria andMemSizeNotBetween(String value1, String value2) {
            addCriterion("mem_size not between", value1, value2, "memSize");
            return (Criteria) this;
        }

        public Criteria andResolutionIsNull() {
            addCriterion("resolution is null");
            return (Criteria) this;
        }

        public Criteria andResolutionIsNotNull() {
            addCriterion("resolution is not null");
            return (Criteria) this;
        }

        public Criteria andResolutionEqualTo(String value) {
            addCriterion("resolution =", value, "resolution");
            return (Criteria) this;
        }

        public Criteria andResolutionNotEqualTo(String value) {
            addCriterion("resolution <>", value, "resolution");
            return (Criteria) this;
        }

        public Criteria andResolutionGreaterThan(String value) {
            addCriterion("resolution >", value, "resolution");
            return (Criteria) this;
        }

        public Criteria andResolutionGreaterThanOrEqualTo(String value) {
            addCriterion("resolution >=", value, "resolution");
            return (Criteria) this;
        }

        public Criteria andResolutionLessThan(String value) {
            addCriterion("resolution <", value, "resolution");
            return (Criteria) this;
        }

        public Criteria andResolutionLessThanOrEqualTo(String value) {
            addCriterion("resolution <=", value, "resolution");
            return (Criteria) this;
        }

        public Criteria andResolutionLike(String value) {
            addCriterion("resolution like", value, "resolution");
            return (Criteria) this;
        }

        public Criteria andResolutionNotLike(String value) {
            addCriterion("resolution not like", value, "resolution");
            return (Criteria) this;
        }

        public Criteria andResolutionIn(List<String> values) {
            addCriterion("resolution in", values, "resolution");
            return (Criteria) this;
        }

        public Criteria andResolutionNotIn(List<String> values) {
            addCriterion("resolution not in", values, "resolution");
            return (Criteria) this;
        }

        public Criteria andResolutionBetween(String value1, String value2) {
            addCriterion("resolution between", value1, value2, "resolution");
            return (Criteria) this;
        }

        public Criteria andResolutionNotBetween(String value1, String value2) {
            addCriterion("resolution not between", value1, value2, "resolution");
            return (Criteria) this;
        }

        public Criteria andImgUrlIsNull() {
            addCriterion("img_url is null");
            return (Criteria) this;
        }

        public Criteria andImgUrlIsNotNull() {
            addCriterion("img_url is not null");
            return (Criteria) this;
        }

        public Criteria andImgUrlEqualTo(String value) {
            addCriterion("img_url =", value, "imgUrl");
            return (Criteria) this;
        }

        public Criteria andImgUrlNotEqualTo(String value) {
            addCriterion("img_url <>", value, "imgUrl");
            return (Criteria) this;
        }

        public Criteria andImgUrlGreaterThan(String value) {
            addCriterion("img_url >", value, "imgUrl");
            return (Criteria) this;
        }

        public Criteria andImgUrlGreaterThanOrEqualTo(String value) {
            addCriterion("img_url >=", value, "imgUrl");
            return (Criteria) this;
        }

        public Criteria andImgUrlLessThan(String value) {
            addCriterion("img_url <", value, "imgUrl");
            return (Criteria) this;
        }

        public Criteria andImgUrlLessThanOrEqualTo(String value) {
            addCriterion("img_url <=", value, "imgUrl");
            return (Criteria) this;
        }

        public Criteria andImgUrlLike(String value) {
            addCriterion("img_url like", value, "imgUrl");
            return (Criteria) this;
        }

        public Criteria andImgUrlNotLike(String value) {
            addCriterion("img_url not like", value, "imgUrl");
            return (Criteria) this;
        }

        public Criteria andImgUrlIn(List<String> values) {
            addCriterion("img_url in", values, "imgUrl");
            return (Criteria) this;
        }

        public Criteria andImgUrlNotIn(List<String> values) {
            addCriterion("img_url not in", values, "imgUrl");
            return (Criteria) this;
        }

        public Criteria andImgUrlBetween(String value1, String value2) {
            addCriterion("img_url between", value1, value2, "imgUrl");
            return (Criteria) this;
        }

        public Criteria andImgUrlNotBetween(String value1, String value2) {
            addCriterion("img_url not between", value1, value2, "imgUrl");
            return (Criteria) this;
        }

        public Criteria andTypeIsNull() {
            addCriterion("type is null");
            return (Criteria) this;
        }

        public Criteria andTypeIsNotNull() {
            addCriterion("type is not null");
            return (Criteria) this;
        }

        public Criteria andTypeEqualTo(Integer value) {
            addCriterion("type =", value, "type");
            return (Criteria) this;
        }

        public Criteria andTypeNotEqualTo(Integer value) {
            addCriterion("type <>", value, "type");
            return (Criteria) this;
        }

        public Criteria andTypeGreaterThan(Integer value) {
            addCriterion("type >", value, "type");
            return (Criteria) this;
        }

        public Criteria andTypeGreaterThanOrEqualTo(Integer value) {
            addCriterion("type >=", value, "type");
            return (Criteria) this;
        }

        public Criteria andTypeLessThan(Integer value) {
            addCriterion("type <", value, "type");
            return (Criteria) this;
        }

        public Criteria andTypeLessThanOrEqualTo(Integer value) {
            addCriterion("type <=", value, "type");
            return (Criteria) this;
        }

        public Criteria andTypeIn(List<Integer> values) {
            addCriterion("type in", values, "type");
            return (Criteria) this;
        }

        public Criteria andTypeNotIn(List<Integer> values) {
            addCriterion("type not in", values, "type");
            return (Criteria) this;
        }

        public Criteria andTypeBetween(Integer value1, Integer value2) {
            addCriterion("type between", value1, value2, "type");
            return (Criteria) this;
        }

        public Criteria andTypeNotBetween(Integer value1, Integer value2) {
            addCriterion("type not between", value1, value2, "type");
            return (Criteria) this;
        }

        public Criteria andStatusIsNull() {
            addCriterion("status is null");
            return (Criteria) this;
        }

        public Criteria andStatusIsNotNull() {
            addCriterion("status is not null");
            return (Criteria) this;
        }

        public Criteria andStatusEqualTo(Integer value) {
            addCriterion("status =", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusNotEqualTo(Integer value) {
            addCriterion("status <>", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusGreaterThan(Integer value) {
            addCriterion("status >", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusGreaterThanOrEqualTo(Integer value) {
            addCriterion("status >=", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusLessThan(Integer value) {
            addCriterion("status <", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusLessThanOrEqualTo(Integer value) {
            addCriterion("status <=", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusIn(List<Integer> values) {
            addCriterion("status in", values, "status");
            return (Criteria) this;
        }

        public Criteria andStatusNotIn(List<Integer> values) {
            addCriterion("status not in", values, "status");
            return (Criteria) this;
        }

        public Criteria andStatusBetween(Integer value1, Integer value2) {
            addCriterion("status between", value1, value2, "status");
            return (Criteria) this;
        }

        public Criteria andStatusNotBetween(Integer value1, Integer value2) {
            addCriterion("status not between", value1, value2, "status");
            return (Criteria) this;
        }

        public Criteria andStfStatusIsNull() {
            addCriterion("stf_status is null");
            return (Criteria) this;
        }

        public Criteria andStfStatusIsNotNull() {
            addCriterion("stf_status is not null");
            return (Criteria) this;
        }

        public Criteria andStfStatusEqualTo(Integer value) {
            addCriterion("stf_status =", value, "stfStatus");
            return (Criteria) this;
        }

        public Criteria andStfStatusNotEqualTo(Integer value) {
            addCriterion("stf_status <>", value, "stfStatus");
            return (Criteria) this;
        }

        public Criteria andStfStatusGreaterThan(Integer value) {
            addCriterion("stf_status >", value, "stfStatus");
            return (Criteria) this;
        }

        public Criteria andStfStatusGreaterThanOrEqualTo(Integer value) {
            addCriterion("stf_status >=", value, "stfStatus");
            return (Criteria) this;
        }

        public Criteria andStfStatusLessThan(Integer value) {
            addCriterion("stf_status <", value, "stfStatus");
            return (Criteria) this;
        }

        public Criteria andStfStatusLessThanOrEqualTo(Integer value) {
            addCriterion("stf_status <=", value, "stfStatus");
            return (Criteria) this;
        }

        public Criteria andStfStatusIn(List<Integer> values) {
            addCriterion("stf_status in", values, "stfStatus");
            return (Criteria) this;
        }

        public Criteria andStfStatusNotIn(List<Integer> values) {
            addCriterion("stf_status not in", values, "stfStatus");
            return (Criteria) this;
        }

        public Criteria andStfStatusBetween(Integer value1, Integer value2) {
            addCriterion("stf_status between", value1, value2, "stfStatus");
            return (Criteria) this;
        }

        public Criteria andStfStatusNotBetween(Integer value1, Integer value2) {
            addCriterion("stf_status not between", value1, value2, "stfStatus");
            return (Criteria) this;
        }

        public Criteria andMacacaStatusIsNull() {
            addCriterion("macaca_status is null");
            return (Criteria) this;
        }

        public Criteria andMacacaStatusIsNotNull() {
            addCriterion("macaca_status is not null");
            return (Criteria) this;
        }

        public Criteria andMacacaStatusEqualTo(Integer value) {
            addCriterion("macaca_status =", value, "macacaStatus");
            return (Criteria) this;
        }

        public Criteria andMacacaStatusNotEqualTo(Integer value) {
            addCriterion("macaca_status <>", value, "macacaStatus");
            return (Criteria) this;
        }

        public Criteria andMacacaStatusGreaterThan(Integer value) {
            addCriterion("macaca_status >", value, "macacaStatus");
            return (Criteria) this;
        }

        public Criteria andMacacaStatusGreaterThanOrEqualTo(Integer value) {
            addCriterion("macaca_status >=", value, "macacaStatus");
            return (Criteria) this;
        }

        public Criteria andMacacaStatusLessThan(Integer value) {
            addCriterion("macaca_status <", value, "macacaStatus");
            return (Criteria) this;
        }

        public Criteria andMacacaStatusLessThanOrEqualTo(Integer value) {
            addCriterion("macaca_status <=", value, "macacaStatus");
            return (Criteria) this;
        }

        public Criteria andMacacaStatusIn(List<Integer> values) {
            addCriterion("macaca_status in", values, "macacaStatus");
            return (Criteria) this;
        }

        public Criteria andMacacaStatusNotIn(List<Integer> values) {
            addCriterion("macaca_status not in", values, "macacaStatus");
            return (Criteria) this;
        }

        public Criteria andMacacaStatusBetween(Integer value1, Integer value2) {
            addCriterion("macaca_status between", value1, value2, "macacaStatus");
            return (Criteria) this;
        }

        public Criteria andMacacaStatusNotBetween(Integer value1, Integer value2) {
            addCriterion("macaca_status not between", value1, value2, "macacaStatus");
            return (Criteria) this;
        }

        public Criteria andLastOnlineTimeIsNull() {
            addCriterion("last_online_time is null");
            return (Criteria) this;
        }

        public Criteria andLastOnlineTimeIsNotNull() {
            addCriterion("last_online_time is not null");
            return (Criteria) this;
        }

        public Criteria andLastOnlineTimeEqualTo(Date value) {
            addCriterion("last_online_time =", value, "lastOnlineTime");
            return (Criteria) this;
        }

        public Criteria andLastOnlineTimeNotEqualTo(Date value) {
            addCriterion("last_online_time <>", value, "lastOnlineTime");
            return (Criteria) this;
        }

        public Criteria andLastOnlineTimeGreaterThan(Date value) {
            addCriterion("last_online_time >", value, "lastOnlineTime");
            return (Criteria) this;
        }

        public Criteria andLastOnlineTimeGreaterThanOrEqualTo(Date value) {
            addCriterion("last_online_time >=", value, "lastOnlineTime");
            return (Criteria) this;
        }

        public Criteria andLastOnlineTimeLessThan(Date value) {
            addCriterion("last_online_time <", value, "lastOnlineTime");
            return (Criteria) this;
        }

        public Criteria andLastOnlineTimeLessThanOrEqualTo(Date value) {
            addCriterion("last_online_time <=", value, "lastOnlineTime");
            return (Criteria) this;
        }

        public Criteria andLastOnlineTimeIn(List<Date> values) {
            addCriterion("last_online_time in", values, "lastOnlineTime");
            return (Criteria) this;
        }

        public Criteria andLastOnlineTimeNotIn(List<Date> values) {
            addCriterion("last_online_time not in", values, "lastOnlineTime");
            return (Criteria) this;
        }

        public Criteria andLastOnlineTimeBetween(Date value1, Date value2) {
            addCriterion("last_online_time between", value1, value2, "lastOnlineTime");
            return (Criteria) this;
        }

        public Criteria andLastOnlineTimeNotBetween(Date value1, Date value2) {
            addCriterion("last_online_time not between", value1, value2, "lastOnlineTime");
            return (Criteria) this;
        }

        public Criteria andLastOfflineTimeIsNull() {
            addCriterion("last_offline_time is null");
            return (Criteria) this;
        }

        public Criteria andLastOfflineTimeIsNotNull() {
            addCriterion("last_offline_time is not null");
            return (Criteria) this;
        }

        public Criteria andLastOfflineTimeEqualTo(Date value) {
            addCriterion("last_offline_time =", value, "lastOfflineTime");
            return (Criteria) this;
        }

        public Criteria andLastOfflineTimeNotEqualTo(Date value) {
            addCriterion("last_offline_time <>", value, "lastOfflineTime");
            return (Criteria) this;
        }

        public Criteria andLastOfflineTimeGreaterThan(Date value) {
            addCriterion("last_offline_time >", value, "lastOfflineTime");
            return (Criteria) this;
        }

        public Criteria andLastOfflineTimeGreaterThanOrEqualTo(Date value) {
            addCriterion("last_offline_time >=", value, "lastOfflineTime");
            return (Criteria) this;
        }

        public Criteria andLastOfflineTimeLessThan(Date value) {
            addCriterion("last_offline_time <", value, "lastOfflineTime");
            return (Criteria) this;
        }

        public Criteria andLastOfflineTimeLessThanOrEqualTo(Date value) {
            addCriterion("last_offline_time <=", value, "lastOfflineTime");
            return (Criteria) this;
        }

        public Criteria andLastOfflineTimeIn(List<Date> values) {
            addCriterion("last_offline_time in", values, "lastOfflineTime");
            return (Criteria) this;
        }

        public Criteria andLastOfflineTimeNotIn(List<Date> values) {
            addCriterion("last_offline_time not in", values, "lastOfflineTime");
            return (Criteria) this;
        }

        public Criteria andLastOfflineTimeBetween(Date value1, Date value2) {
            addCriterion("last_offline_time between", value1, value2, "lastOfflineTime");
            return (Criteria) this;
        }

        public Criteria andLastOfflineTimeNotBetween(Date value1, Date value2) {
            addCriterion("last_offline_time not between", value1, value2, "lastOfflineTime");
            return (Criteria) this;
        }

        public Criteria andUsernameIsNull() {
            addCriterion("username is null");
            return (Criteria) this;
        }

        public Criteria andUsernameIsNotNull() {
            addCriterion("username is not null");
            return (Criteria) this;
        }

        public Criteria andUsernameEqualTo(String value) {
            addCriterion("username =", value, "username");
            return (Criteria) this;
        }

        public Criteria andUsernameNotEqualTo(String value) {
            addCriterion("username <>", value, "username");
            return (Criteria) this;
        }

        public Criteria andUsernameGreaterThan(String value) {
            addCriterion("username >", value, "username");
            return (Criteria) this;
        }

        public Criteria andUsernameGreaterThanOrEqualTo(String value) {
            addCriterion("username >=", value, "username");
            return (Criteria) this;
        }

        public Criteria andUsernameLessThan(String value) {
            addCriterion("username <", value, "username");
            return (Criteria) this;
        }

        public Criteria andUsernameLessThanOrEqualTo(String value) {
            addCriterion("username <=", value, "username");
            return (Criteria) this;
        }

        public Criteria andUsernameLike(String value) {
            addCriterion("username like", value, "username");
            return (Criteria) this;
        }

        public Criteria andUsernameNotLike(String value) {
            addCriterion("username not like", value, "username");
            return (Criteria) this;
        }

        public Criteria andUsernameIn(List<String> values) {
            addCriterion("username in", values, "username");
            return (Criteria) this;
        }

        public Criteria andUsernameNotIn(List<String> values) {
            addCriterion("username not in", values, "username");
            return (Criteria) this;
        }

        public Criteria andUsernameBetween(String value1, String value2) {
            addCriterion("username between", value1, value2, "username");
            return (Criteria) this;
        }

        public Criteria andUsernameNotBetween(String value1, String value2) {
            addCriterion("username not between", value1, value2, "username");
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