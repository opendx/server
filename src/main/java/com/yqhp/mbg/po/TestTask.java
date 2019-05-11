package com.yqhp.mbg.po;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

public class TestTask implements Serializable {

    /** 未完成 */
    public static final Integer UNFINISHED_STATUS = 0;
    /** 完成*/
    public static final Integer FINISHED_STATUS = 1;
    /** 兼容模式 */
    public static final Integer RUN_MODE_COMPATIBLE = 1;
    /** 高效模式 */
    public static final Integer RUN_MODE_EFFICIENCY = 2;

    /**
     * 主键id
     *
     * @mbg.generated
     */
    private Integer id;

    /**
     * 所属项目
     *
     * @mbg.generated
     */
    @NotNull(message = "项目id不能为空")
    private Integer projectId;

    /**
     * 所属测试计划
     *
     * @mbg.generated
     */
    @NotNull(message = "测试计划不能为空")
    private Integer testPlanId;

    /**
     * 任务名
     *
     * @mbg.generated
     */
    @NotBlank(message = "测试任务名不能为空")
    private String name;

    /**
     * 任务描述
     *
     * @mbg.generated
     */
    private String description;

    /**
     * 运行模式 1:兼容模式 2:高效模式
     *
     * @mbg.generated
     */
    @NotNull(message = "用例分发策划不能为空")
    private Integer runMode;

    /**
     * 任务状态 0:未完成 1:已完成
     *
     * @mbg.generated
     */
    private Integer status;

    /**
     * 任务创建人
     *
     * @mbg.generated
     */
    private Integer creatorUid;

    /**
     * 测试通过用例数
     *
     * @mbg.generated
     */
    private Integer passCaseCount;

    /**
     * 测试失败用例数
     *
     * @mbg.generated
     */
    private Integer failCaseCount;

    /**
     * 测试跳过用例数
     *
     * @mbg.generated
     */
    private Integer skipCaseCount;

    /**
     * 任务提交时间
     *
     * @mbg.generated
     */
    private Date commitTime;

    /**
     * 任务完成时间
     *
     * @mbg.generated
     */
    private Date finishTime;

    private static final long serialVersionUID = 1L;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getProjectId() {
        return projectId;
    }

    public void setProjectId(Integer projectId) {
        this.projectId = projectId;
    }

    public Integer getTestPlanId() {
        return testPlanId;
    }

    public void setTestPlanId(Integer testPlanId) {
        this.testPlanId = testPlanId;
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

    public Integer getRunMode() {
        return runMode;
    }

    public void setRunMode(Integer runMode) {
        this.runMode = runMode;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getCreatorUid() {
        return creatorUid;
    }

    public void setCreatorUid(Integer creatorUid) {
        this.creatorUid = creatorUid;
    }

    public Integer getPassCaseCount() {
        return passCaseCount;
    }

    public void setPassCaseCount(Integer passCaseCount) {
        this.passCaseCount = passCaseCount;
    }

    public Integer getFailCaseCount() {
        return failCaseCount;
    }

    public void setFailCaseCount(Integer failCaseCount) {
        this.failCaseCount = failCaseCount;
    }

    public Integer getSkipCaseCount() {
        return skipCaseCount;
    }

    public void setSkipCaseCount(Integer skipCaseCount) {
        this.skipCaseCount = skipCaseCount;
    }

    public Date getCommitTime() {
        return commitTime;
    }

    public void setCommitTime(Date commitTime) {
        this.commitTime = commitTime;
    }

    public Date getFinishTime() {
        return finishTime;
    }

    public void setFinishTime(Date finishTime) {
        this.finishTime = finishTime;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", projectId=").append(projectId);
        sb.append(", testPlanId=").append(testPlanId);
        sb.append(", name=").append(name);
        sb.append(", description=").append(description);
        sb.append(", runMode=").append(runMode);
        sb.append(", status=").append(status);
        sb.append(", creatorUid=").append(creatorUid);
        sb.append(", passCaseCount=").append(passCaseCount);
        sb.append(", failCaseCount=").append(failCaseCount);
        sb.append(", skipCaseCount=").append(skipCaseCount);
        sb.append(", commitTime=").append(commitTime);
        sb.append(", finishTime=").append(finishTime);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}