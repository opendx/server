package com.daxiang.mbg.po;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class TestPlan implements Serializable {
    /**
     * 主键
     *
     * @mbg.generated
     */
    private Integer id;

    /**
     * 测试计划名
     *
     * @mbg.generated
     */
    @NotBlank(message = "测试计划名不能为空")
    private String name;

    /**
     * 描述
     *
     * @mbg.generated
     */
    private String description;

    /**
     * 所属项目
     *
     * @mbg.generated
     */
    @NotNull(message = "项目id不能为空")
    private Integer projectId;

    /**
     * BeforeClass
     *
     * @mbg.generated
     */
    private Integer beforeClass;

    /**
     * BeforeMehtod
     *
     * @mbg.generated
     */
    private Integer beforeMethod;

    /**
     * AfterClass
     *
     * @mbg.generated
     */
    private Integer afterClass;

    /**
     * AfterMethod
     *
     * @mbg.generated
     */
    private Integer afterMethod;

    /**
     * 创建人
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
     * 测试集
     *
     * @mbg.generated
     */
    @NotEmpty(message = "至少有要一个测试集")
    private java.util.List<Integer> testSuites;

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

    public Integer getBeforeClass() {
        return beforeClass;
    }

    public void setBeforeClass(Integer beforeClass) {
        this.beforeClass = beforeClass;
    }

    public Integer getBeforeMethod() {
        return beforeMethod;
    }

    public void setBeforeMethod(Integer beforeMethod) {
        this.beforeMethod = beforeMethod;
    }

    public Integer getAfterClass() {
        return afterClass;
    }

    public void setAfterClass(Integer afterClass) {
        this.afterClass = afterClass;
    }

    public Integer getAfterMethod() {
        return afterMethod;
    }

    public void setAfterMethod(Integer afterMethod) {
        this.afterMethod = afterMethod;
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

    public java.util.List<Integer> getTestSuites() {
        return testSuites;
    }

    public void setTestSuites(java.util.List<Integer> testSuites) {
        this.testSuites = testSuites;
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
        sb.append(", beforeClass=").append(beforeClass);
        sb.append(", beforeMethod=").append(beforeMethod);
        sb.append(", afterClass=").append(afterClass);
        sb.append(", afterMethod=").append(afterMethod);
        sb.append(", creatorUid=").append(creatorUid);
        sb.append(", createTime=").append(createTime);
        sb.append(", testSuites=").append(testSuites);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}