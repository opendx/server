package com.daxiang.mbg.po;

import com.daxiang.model.action.LocalVar;
import com.daxiang.model.action.Param;
import com.daxiang.model.action.Step;
import com.daxiang.validator.annotation.HasLengthIfNotEmpty;
import com.daxiang.validator.group.SaveActionGroup;
import com.daxiang.validator.group.UpdateGroup;
import com.fasterxml.jackson.annotation.JsonInclude;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class Action implements Serializable {

    /** 禁用 */
    public static final int DISABLE_STATE = 0;
    /** 草稿 */
    public static final int DRAFT_STATE = 1;
    /** 发布 */
    public static final int RELEASE_STATE = 2;

    /**
     * 基础action(代码形式的)
     */
    public static final int TYPE_BASE = 1;
    /**
     * 用户在网页前端封装的action
     */
    public static final int TYPE_ENCAPSULATION = 2;
    /**
     * 测试用例action
     */
    public static final int TYPE_TESTCASE = 3;

    /**
     * 主键id
     *
     * @mbg.generated
     */
    @NotNull(message = "id不能为空", groups = {UpdateGroup.class})
    private Integer id;

    /**
     * action名
     *
     * @mbg.generated
     */
    @NotBlank(message = "action名不能为空", groups = {SaveActionGroup.class})
    private String name;

    /**
     * 描述
     *
     * @mbg.generated
     */
    private String description;

    /**
     * 类型：1.基础action（代码形式的） 2.用户在网页前端封装的action 3.测试用例
     *
     * @mbg.generated
     */
    @NotNull(message = "ActionType不能为空")
    private Integer type;

    /**
     * 基础action专用：调用
     *
     * @mbg.generated
     */
    private String invoke;

    /**
     * 返回值类型: void / 其他
     *
     * @mbg.generated
     */
    @NotBlank(message = "返回值类型不能为空")
    private String returnValueType;

    /**
     * 返回值描述
     *
     * @mbg.generated
     */
    private String returnValueDesc;

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
     * 所属的page id
     *
     * @mbg.generated
     */
    private Integer pageId;

    /**
     * 所属的分类id
     *
     * @mbg.generated
     */
    private Integer categoryId;

    /**
     * 所属的项目id
     *
     * @mbg.generated
     */
    private Integer projectId;

    /**
     * 禁用: 0  草稿: 1  发布: 2
     *
     * @mbg.generated
     */
    @NotNull(message = "status不能为空", groups = {SaveActionGroup.class})
    private Integer state;

    /**
     * 方法参数
     *
     * @mbg.generated
     */
    @Valid
    private java.util.List<com.daxiang.model.action.Param> params;

    /**
     * 局部变量
     *
     * @mbg.generated
     */
    @Valid
    private java.util.List<com.daxiang.model.action.LocalVar> localVars;

    /**
     * 前置步骤
     *
     * @mbg.generated
     */
    @Valid
    private java.util.List<com.daxiang.model.action.Step> setUp;

    /**
     * 步骤
     *
     * @mbg.generated
     */
    @Valid
    @NotEmpty(message = "步骤不能为空", groups = {SaveActionGroup.class})
    private java.util.List<com.daxiang.model.action.Step> steps;

    /**
     * 后置步骤
     *
     * @mbg.generated
     */
    @Valid
    private java.util.List<com.daxiang.model.action.Step> tearDown;

    /**
     * java imports
     *
     * @mbg.generated
     */
    @HasLengthIfNotEmpty(message = "javaImport不能为空")
    private java.util.List<String> javaImports;

    /**
     * action imports
     *
     * @mbg.generated
     */
    private java.util.List<Integer> actionImports;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<Action> importActions;

    public List<Action> getImportActions() {
        return importActions;
    }

    public void setImportActions(List<Action> importActions) {
        this.importActions = importActions;
    }

    /**
     * 1.android 2.ios 3.pc web empty.通用
     *
     * @mbg.generated
     */
    private java.util.List<Integer> platforms;

    /**
     * 依赖的测试用例id
     *
     * @mbg.generated
     */
    private java.util.List<Integer> depends;

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

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getInvoke() {
        return invoke;
    }

    public void setInvoke(String invoke) {
        this.invoke = invoke;
    }

    public String getReturnValueType() {
        return returnValueType;
    }

    public void setReturnValueType(String returnValueType) {
        this.returnValueType = returnValueType;
    }

    public String getReturnValueDesc() {
        return returnValueDesc;
    }

    public void setReturnValueDesc(String returnValueDesc) {
        this.returnValueDesc = returnValueDesc;
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

    public Integer getPageId() {
        return pageId;
    }

    public void setPageId(Integer pageId) {
        this.pageId = pageId;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public Integer getProjectId() {
        return projectId;
    }

    public void setProjectId(Integer projectId) {
        this.projectId = projectId;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public java.util.List<com.daxiang.model.action.Param> getParams() {
        return params;
    }

    public void setParams(java.util.List<com.daxiang.model.action.Param> params) {
        this.params = params;
    }

    public java.util.List<com.daxiang.model.action.LocalVar> getLocalVars() {
        return localVars;
    }

    public void setLocalVars(java.util.List<com.daxiang.model.action.LocalVar> localVars) {
        this.localVars = localVars;
    }

    public java.util.List<com.daxiang.model.action.Step> getSetUp() {
        return setUp;
    }

    public void setSetUp(java.util.List<com.daxiang.model.action.Step> setUp) {
        this.setUp = setUp;
    }

    public java.util.List<com.daxiang.model.action.Step> getSteps() {
        return steps;
    }

    public void setSteps(java.util.List<com.daxiang.model.action.Step> steps) {
        this.steps = steps;
    }

    public java.util.List<com.daxiang.model.action.Step> getTearDown() {
        return tearDown;
    }

    public void setTearDown(java.util.List<com.daxiang.model.action.Step> tearDown) {
        this.tearDown = tearDown;
    }

    public java.util.List<String> getJavaImports() {
        return javaImports;
    }

    public void setJavaImports(java.util.List<String> javaImports) {
        this.javaImports = javaImports;
    }

    public java.util.List<Integer> getActionImports() {
        return actionImports;
    }

    public void setActionImports(java.util.List<Integer> actionImports) {
        this.actionImports = actionImports;
    }

    public java.util.List<Integer> getPlatforms() {
        return platforms;
    }

    public void setPlatforms(java.util.List<Integer> platforms) {
        this.platforms = platforms;
    }

    public java.util.List<Integer> getDepends() {
        return depends;
    }

    public void setDepends(java.util.List<Integer> depends) {
        this.depends = depends;
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
        sb.append(", type=").append(type);
        sb.append(", invoke=").append(invoke);
        sb.append(", returnValueType=").append(returnValueType);
        sb.append(", returnValueDesc=").append(returnValueDesc);
        sb.append(", creatorUid=").append(creatorUid);
        sb.append(", createTime=").append(createTime);
        sb.append(", updatorUid=").append(updatorUid);
        sb.append(", updateTime=").append(updateTime);
        sb.append(", pageId=").append(pageId);
        sb.append(", categoryId=").append(categoryId);
        sb.append(", projectId=").append(projectId);
        sb.append(", state=").append(state);
        sb.append(", params=").append(params);
        sb.append(", localVars=").append(localVars);
        sb.append(", setUp=").append(setUp);
        sb.append(", steps=").append(steps);
        sb.append(", tearDown=").append(tearDown);
        sb.append(", javaImports=").append(javaImports);
        sb.append(", actionImports=").append(actionImports);
        sb.append(", platforms=").append(platforms);
        sb.append(", depends=").append(depends);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}