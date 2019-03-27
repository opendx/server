package com.yqhp.mbg.po;

import com.yqhp.model.action.LocalVar;
import com.yqhp.model.action.Param;
import com.yqhp.model.action.Step;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class Action implements Serializable {

    /**
     * 基础action
     */
    public static final Integer TYPE_BASE = 1;
    /**
     * 自定义action
     */
    public static final Integer TYPE_CUSTOM = 2;
    /**
     * 测试用例action
     */
    public static final Integer TYPE_TESTCASE = 3;
    /**
     * 测试计划前置action
     */
    public static final Integer TYPE_TEST_PLAN_BEFORE = 4;

    /**
     * 有返回值
     */
    public static final Integer HAS_RETURN_VALUE = 1;

    /**
     * 是否需要在代码模板里传client/driver
     */
    public static final Integer NEED_DRIVER = 1;

    /**
     * 主键id
     *
     * @mbg.generated
     */
    private Integer id;

    /**
     * action名
     *
     * @mbg.generated
     */
    @NotBlank(message = "actionName不能为空")
    private String name;

    /**
     * 描述
     *
     * @mbg.generated
     */
    private String description;

    /**
     * 所属的项目id
     *
     * @mbg.generated
     */
    private Integer projectId;

    /**
     * 所属的page id
     *
     * @mbg.generated
     */
    private Integer pageId;

    /**
     * 类型：1.基础的action 2.用户自定义的action 3.测试用例 2.测试计划的前置action
     *
     * @mbg.generated
     */
    @NotNull(message = "actionType不能为空")
    private Integer type;

    /**
     * 基础action专用：1.android 2.ios 3.web
     *
     * @mbg.generated
     */
    private Integer projectType;

    /**
     * 基础action专用：类名
     *
     * @mbg.generated
     */
    private String className;

    /**
     * 基础action专用：是否需要传入driver
     *
     * @mbg.generated
     */
    private Integer needDriver;

    /**
     * 由于基础action是不会在表中记录returnValue的，为了区分是否有返回值，加了这个字段
     *
     * @mbg.generated
     */
    private Integer hasReturnValue;

    /**
     * action的返回值，返回值都是用局部变量保存的，这里保存的局部变量的名字
     *
     * @mbg.generated
     */
    private String returnValue;

    /**
     * 创建人id
     *
     * @mbg.generated
     */
    private Integer creatorUid;

    /**
     * 创建时间
     *
     * @mbg.generated
     */
    private Date createTime;

    /**
     * 更新人id
     *
     * @mbg.generated
     */
    private Integer updatorUid;

    /**
     * 更新时间
     *
     * @mbg.generated
     */
    private Date updateTime;

    /**
     * 所属的分类
     *
     * @mbg.generated
     */
    private Integer categoryId;

    /**
     * 方法参数
     *
     * @mbg.generated
     */
    @Valid
    private java.util.List<com.yqhp.model.action.Param> params;

    /**
     * 局部变量
     *
     * @mbg.generated
     */
    @Valid
    private java.util.List<com.yqhp.model.action.LocalVar> localVars;

    /**
     * 步骤
     *
     * @mbg.generated
     */
    @Valid
    @NotEmpty(message = "步骤不能为空")
    private java.util.List<com.yqhp.model.action.Step> steps;

    private static final long serialVersionUID = 1L;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getProjectId() {
        return projectId;
    }

    public void setProjectId(Integer projectId) {
        this.projectId = projectId;
    }

    public Integer getPageId() {
        return pageId;
    }

    public void setPageId(Integer pageId) {
        this.pageId = pageId;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getProjectType() {
        return projectType;
    }

    public void setProjectType(Integer projectType) {
        this.projectType = projectType;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public Integer getNeedDriver() {
        return needDriver;
    }

    public void setNeedDriver(Integer needDriver) {
        this.needDriver = needDriver;
    }

    public Integer getHasReturnValue() {
        return hasReturnValue;
    }

    public void setHasReturnValue(Integer hasReturnValue) {
        this.hasReturnValue = hasReturnValue;
    }

    public String getReturnValue() {
        return returnValue;
    }

    public void setReturnValue(String returnValue) {
        this.returnValue = returnValue;
    }

    public Integer getCreatorUid() {
        return creatorUid;
    }

    public void setCreatorUid(Integer creatorUid) {
        this.creatorUid = creatorUid;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Integer getUpdatorUid() {
        return updatorUid;
    }

    public void setUpdatorUid(Integer updatorUid) {
        this.updatorUid = updatorUid;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public java.util.List<com.yqhp.model.action.Param> getParams() {
        return params;
    }

    public void setParams(java.util.List<com.yqhp.model.action.Param> params) {
        this.params = params;
    }

    public java.util.List<com.yqhp.model.action.LocalVar> getLocalVars() {
        return localVars;
    }

    public void setLocalVars(java.util.List<com.yqhp.model.action.LocalVar> localVars) {
        this.localVars = localVars;
    }

    public java.util.List<com.yqhp.model.action.Step> getSteps() {
        return steps;
    }

    public void setSteps(java.util.List<com.yqhp.model.action.Step> steps) {
        this.steps = steps;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", name=").append(name);
        sb.append(", description=").append(description);
        sb.append(", projectId=").append(projectId);
        sb.append(", pageId=").append(pageId);
        sb.append(", type=").append(type);
        sb.append(", projectType=").append(projectType);
        sb.append(", className=").append(className);
        sb.append(", needDriver=").append(needDriver);
        sb.append(", hasReturnValue=").append(hasReturnValue);
        sb.append(", returnValue=").append(returnValue);
        sb.append(", creatorUid=").append(creatorUid);
        sb.append(", createTime=").append(createTime);
        sb.append(", updatorUid=").append(updatorUid);
        sb.append(", updateTime=").append(updateTime);
        sb.append(", categoryId=").append(categoryId);
        sb.append(", params=").append(params);
        sb.append(", localVars=").append(localVars);
        sb.append(", steps=").append(steps);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}