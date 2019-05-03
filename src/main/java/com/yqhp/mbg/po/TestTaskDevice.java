package com.yqhp.mbg.po;

import com.yqhp.mbg.po.Action;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class TestTaskDevice implements Serializable {
    /**
     * 主键id
     *
     * @mbg.generated
     */
    private Integer id;

    /**
     * 测试任务id
     *
     * @mbg.generated
     */
    private Integer testTaskId;

    /**
     * 设备id
     *
     * @mbg.generated
     */
    private String deviceId;

    /**
     * 执行所有用例前执行的
     *
     * @mbg.generated
     */
    private String beforeSuite;

    /**
     * 状态： 0:未开始 1:运行中 2:已完成
     *
     * @mbg.generated
     */
    private Integer status;

    /**
     * 开始测试时间
     *
     * @mbg.generated
     */
    private Date startTime;

    /**
     * 结束测试时间
     *
     * @mbg.generated
     */
    private Date endTime;

    /**
     * 执行的测试用例
     *
     * @mbg.generated
     */
    private java.util.List<Action> testcases;

    private static final long serialVersionUID = 1L;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getTestTaskId() {
        return testTaskId;
    }

    public void setTestTaskId(Integer testTaskId) {
        this.testTaskId = testTaskId;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getBeforeSuite() {
        return beforeSuite;
    }

    public void setBeforeSuite(String beforeSuite) {
        this.beforeSuite = beforeSuite;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public java.util.List<Action> getTestcases() {
        return testcases;
    }

    public void setTestcases(java.util.List<Action> testcases) {
        this.testcases = testcases;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", testTaskId=").append(testTaskId);
        sb.append(", deviceId=").append(deviceId);
        sb.append(", beforeSuite=").append(beforeSuite);
        sb.append(", status=").append(status);
        sb.append(", startTime=").append(startTime);
        sb.append(", endTime=").append(endTime);
        sb.append(", testcases=").append(testcases);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}