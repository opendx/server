package com.daxiang.mbg.po;

import com.daxiang.validator.group.UpdateGroup;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class TestPlan implements Serializable {

    /** 兼容模式 */
    public static final int RUN_MODE_COMPATIBLE = 1;
    /** 高效模式 */
    public static final int RUN_MODE_EFFICIENCY = 2;

    public static final int ENABLE_SCHEDULE = 1;

    public static final int ENABLE_RECORD_VIDEO = 1;

    @NotNull(message = "id不能为空", groups = {UpdateGroup.class})
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
     * 环境，默认-1
     *
     * @mbg.generated
     */
    @NotNull(message = "环境不能为空")
    private Integer environmentId;

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
     * 运行模式 1:兼容模式 2:高效模式
     *
     * @mbg.generated
     */
    @NotNull(message = "运行模式不能为空")
    private Integer runMode;

    /**
     * cron表达式
     *
     * @mbg.generated
     */
    private String cronExpression;

    /**
     * 是否开启定时任务，0: 关闭 1: 开启
     *
     * @mbg.generated
     */
    @NotNull(message = "是否开启定时任务不能为空")
    private Integer enableSchedule;

    /**
     * 是否录制视频，0: 不录制 1: 录制
     *
     * @mbg.generated
     */
    @NotNull(message = "是否录制视频不能为空")
    private Integer enableRecordVideo;

    /**
     * 失败重试次数
     *
     * @mbg.generated
     */
    @Min(value = 0, message = "失败重试次数必须>=0")
    private Integer failRetryCount;

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
    @NotEmpty(message = "测试集不能为空")
    private java.util.List<Integer> testSuites;

    /**
     * deviceIds
     *
     * @mbg.generated
     */
    @NotEmpty(message = "device不能为空")
    private java.util.List<String> deviceIds;

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

    public Integer getEnvironmentId() {
        return environmentId;
    }

    public void setEnvironmentId(Integer environmentId) {
        this.environmentId = environmentId;
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

    public Integer getRunMode() {
        return runMode;
    }

    public void setRunMode(Integer runMode) {
        this.runMode = runMode;
    }

    public String getCronExpression() {
        return cronExpression;
    }

    public void setCronExpression(String cronExpression) {
        this.cronExpression = cronExpression;
    }

    public Integer getEnableSchedule() {
        return enableSchedule;
    }

    public void setEnableSchedule(Integer enableSchedule) {
        this.enableSchedule = enableSchedule;
    }

    public Integer getEnableRecordVideo() {
        return enableRecordVideo;
    }

    public void setEnableRecordVideo(Integer enableRecordVideo) {
        this.enableRecordVideo = enableRecordVideo;
    }

    public Integer getFailRetryCount() {
        return failRetryCount;
    }

    public void setFailRetryCount(Integer failRetryCount) {
        this.failRetryCount = failRetryCount;
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

    public java.util.List<String> getDeviceIds() {
        return deviceIds;
    }

    public void setDeviceIds(java.util.List<String> deviceIds) {
        this.deviceIds = deviceIds;
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
        sb.append(", environmentId=").append(environmentId);
        sb.append(", beforeClass=").append(beforeClass);
        sb.append(", beforeMethod=").append(beforeMethod);
        sb.append(", afterClass=").append(afterClass);
        sb.append(", afterMethod=").append(afterMethod);
        sb.append(", runMode=").append(runMode);
        sb.append(", cronExpression=").append(cronExpression);
        sb.append(", enableSchedule=").append(enableSchedule);
        sb.append(", enableRecordVideo=").append(enableRecordVideo);
        sb.append(", failRetryCount=").append(failRetryCount);
        sb.append(", creatorUid=").append(creatorUid);
        sb.append(", createTime=").append(createTime);
        sb.append(", testSuites=").append(testSuites);
        sb.append(", deviceIds=").append(deviceIds);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}