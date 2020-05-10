package com.daxiang.mbg.po;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MobileExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public MobileExample() {
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

        public Criteria andEmulatorIsNull() {
            addCriterion("emulator is null");
            return (Criteria) this;
        }

        public Criteria andEmulatorIsNotNull() {
            addCriterion("emulator is not null");
            return (Criteria) this;
        }

        public Criteria andEmulatorEqualTo(Integer value) {
            addCriterion("emulator =", value, "emulator");
            return (Criteria) this;
        }

        public Criteria andEmulatorNotEqualTo(Integer value) {
            addCriterion("emulator <>", value, "emulator");
            return (Criteria) this;
        }

        public Criteria andEmulatorGreaterThan(Integer value) {
            addCriterion("emulator >", value, "emulator");
            return (Criteria) this;
        }

        public Criteria andEmulatorGreaterThanOrEqualTo(Integer value) {
            addCriterion("emulator >=", value, "emulator");
            return (Criteria) this;
        }

        public Criteria andEmulatorLessThan(Integer value) {
            addCriterion("emulator <", value, "emulator");
            return (Criteria) this;
        }

        public Criteria andEmulatorLessThanOrEqualTo(Integer value) {
            addCriterion("emulator <=", value, "emulator");
            return (Criteria) this;
        }

        public Criteria andEmulatorIn(List<Integer> values) {
            addCriterion("emulator in", values, "emulator");
            return (Criteria) this;
        }

        public Criteria andEmulatorNotIn(List<Integer> values) {
            addCriterion("emulator not in", values, "emulator");
            return (Criteria) this;
        }

        public Criteria andEmulatorBetween(Integer value1, Integer value2) {
            addCriterion("emulator between", value1, value2, "emulator");
            return (Criteria) this;
        }

        public Criteria andEmulatorNotBetween(Integer value1, Integer value2) {
            addCriterion("emulator not between", value1, value2, "emulator");
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

        public Criteria andScreenWidthIsNull() {
            addCriterion("screen_width is null");
            return (Criteria) this;
        }

        public Criteria andScreenWidthIsNotNull() {
            addCriterion("screen_width is not null");
            return (Criteria) this;
        }

        public Criteria andScreenWidthEqualTo(Integer value) {
            addCriterion("screen_width =", value, "screenWidth");
            return (Criteria) this;
        }

        public Criteria andScreenWidthNotEqualTo(Integer value) {
            addCriterion("screen_width <>", value, "screenWidth");
            return (Criteria) this;
        }

        public Criteria andScreenWidthGreaterThan(Integer value) {
            addCriterion("screen_width >", value, "screenWidth");
            return (Criteria) this;
        }

        public Criteria andScreenWidthGreaterThanOrEqualTo(Integer value) {
            addCriterion("screen_width >=", value, "screenWidth");
            return (Criteria) this;
        }

        public Criteria andScreenWidthLessThan(Integer value) {
            addCriterion("screen_width <", value, "screenWidth");
            return (Criteria) this;
        }

        public Criteria andScreenWidthLessThanOrEqualTo(Integer value) {
            addCriterion("screen_width <=", value, "screenWidth");
            return (Criteria) this;
        }

        public Criteria andScreenWidthIn(List<Integer> values) {
            addCriterion("screen_width in", values, "screenWidth");
            return (Criteria) this;
        }

        public Criteria andScreenWidthNotIn(List<Integer> values) {
            addCriterion("screen_width not in", values, "screenWidth");
            return (Criteria) this;
        }

        public Criteria andScreenWidthBetween(Integer value1, Integer value2) {
            addCriterion("screen_width between", value1, value2, "screenWidth");
            return (Criteria) this;
        }

        public Criteria andScreenWidthNotBetween(Integer value1, Integer value2) {
            addCriterion("screen_width not between", value1, value2, "screenWidth");
            return (Criteria) this;
        }

        public Criteria andScreenHeightIsNull() {
            addCriterion("screen_height is null");
            return (Criteria) this;
        }

        public Criteria andScreenHeightIsNotNull() {
            addCriterion("screen_height is not null");
            return (Criteria) this;
        }

        public Criteria andScreenHeightEqualTo(Integer value) {
            addCriterion("screen_height =", value, "screenHeight");
            return (Criteria) this;
        }

        public Criteria andScreenHeightNotEqualTo(Integer value) {
            addCriterion("screen_height <>", value, "screenHeight");
            return (Criteria) this;
        }

        public Criteria andScreenHeightGreaterThan(Integer value) {
            addCriterion("screen_height >", value, "screenHeight");
            return (Criteria) this;
        }

        public Criteria andScreenHeightGreaterThanOrEqualTo(Integer value) {
            addCriterion("screen_height >=", value, "screenHeight");
            return (Criteria) this;
        }

        public Criteria andScreenHeightLessThan(Integer value) {
            addCriterion("screen_height <", value, "screenHeight");
            return (Criteria) this;
        }

        public Criteria andScreenHeightLessThanOrEqualTo(Integer value) {
            addCriterion("screen_height <=", value, "screenHeight");
            return (Criteria) this;
        }

        public Criteria andScreenHeightIn(List<Integer> values) {
            addCriterion("screen_height in", values, "screenHeight");
            return (Criteria) this;
        }

        public Criteria andScreenHeightNotIn(List<Integer> values) {
            addCriterion("screen_height not in", values, "screenHeight");
            return (Criteria) this;
        }

        public Criteria andScreenHeightBetween(Integer value1, Integer value2) {
            addCriterion("screen_height between", value1, value2, "screenHeight");
            return (Criteria) this;
        }

        public Criteria andScreenHeightNotBetween(Integer value1, Integer value2) {
            addCriterion("screen_height not between", value1, value2, "screenHeight");
            return (Criteria) this;
        }

        public Criteria andImgPathIsNull() {
            addCriterion("img_path is null");
            return (Criteria) this;
        }

        public Criteria andImgPathIsNotNull() {
            addCriterion("img_path is not null");
            return (Criteria) this;
        }

        public Criteria andImgPathEqualTo(String value) {
            addCriterion("img_path =", value, "imgPath");
            return (Criteria) this;
        }

        public Criteria andImgPathNotEqualTo(String value) {
            addCriterion("img_path <>", value, "imgPath");
            return (Criteria) this;
        }

        public Criteria andImgPathGreaterThan(String value) {
            addCriterion("img_path >", value, "imgPath");
            return (Criteria) this;
        }

        public Criteria andImgPathGreaterThanOrEqualTo(String value) {
            addCriterion("img_path >=", value, "imgPath");
            return (Criteria) this;
        }

        public Criteria andImgPathLessThan(String value) {
            addCriterion("img_path <", value, "imgPath");
            return (Criteria) this;
        }

        public Criteria andImgPathLessThanOrEqualTo(String value) {
            addCriterion("img_path <=", value, "imgPath");
            return (Criteria) this;
        }

        public Criteria andImgPathLike(String value) {
            addCriterion("img_path like", value, "imgPath");
            return (Criteria) this;
        }

        public Criteria andImgPathNotLike(String value) {
            addCriterion("img_path not like", value, "imgPath");
            return (Criteria) this;
        }

        public Criteria andImgPathIn(List<String> values) {
            addCriterion("img_path in", values, "imgPath");
            return (Criteria) this;
        }

        public Criteria andImgPathNotIn(List<String> values) {
            addCriterion("img_path not in", values, "imgPath");
            return (Criteria) this;
        }

        public Criteria andImgPathBetween(String value1, String value2) {
            addCriterion("img_path between", value1, value2, "imgPath");
            return (Criteria) this;
        }

        public Criteria andImgPathNotBetween(String value1, String value2) {
            addCriterion("img_path not between", value1, value2, "imgPath");
            return (Criteria) this;
        }

        public Criteria andPlatformIsNull() {
            addCriterion("platform is null");
            return (Criteria) this;
        }

        public Criteria andPlatformIsNotNull() {
            addCriterion("platform is not null");
            return (Criteria) this;
        }

        public Criteria andPlatformEqualTo(Integer value) {
            addCriterion("platform =", value, "platform");
            return (Criteria) this;
        }

        public Criteria andPlatformNotEqualTo(Integer value) {
            addCriterion("platform <>", value, "platform");
            return (Criteria) this;
        }

        public Criteria andPlatformGreaterThan(Integer value) {
            addCriterion("platform >", value, "platform");
            return (Criteria) this;
        }

        public Criteria andPlatformGreaterThanOrEqualTo(Integer value) {
            addCriterion("platform >=", value, "platform");
            return (Criteria) this;
        }

        public Criteria andPlatformLessThan(Integer value) {
            addCriterion("platform <", value, "platform");
            return (Criteria) this;
        }

        public Criteria andPlatformLessThanOrEqualTo(Integer value) {
            addCriterion("platform <=", value, "platform");
            return (Criteria) this;
        }

        public Criteria andPlatformIn(List<Integer> values) {
            addCriterion("platform in", values, "platform");
            return (Criteria) this;
        }

        public Criteria andPlatformNotIn(List<Integer> values) {
            addCriterion("platform not in", values, "platform");
            return (Criteria) this;
        }

        public Criteria andPlatformBetween(Integer value1, Integer value2) {
            addCriterion("platform between", value1, value2, "platform");
            return (Criteria) this;
        }

        public Criteria andPlatformNotBetween(Integer value1, Integer value2) {
            addCriterion("platform not between", value1, value2, "platform");
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