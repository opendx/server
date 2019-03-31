package com.yqhp.mbg.po;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.Date;

public class Device implements Serializable {

    public static final Integer ANDROID_TYPE = 1;
    public static final Integer IOS_TYPE = 2;

    public static final Integer OFFLINE_STATUS = 0;
    public static final Integer IDLE_STATUS = 1;
    public static final Integer USING_STATUS = 2;

    /**
     * 设备id
     *
     * @mbg.generated
     */
    @NotBlank(message = "设备id不能为空")
    private String id;

    /**
     * 设备名
     *
     * @mbg.generated
     */
    private String name;

    /**
     * 设备ip
     *
     * @mbg.generated
     */
    private String ip;

    /**
     * 设备所在的agent的ip
     *
     * @mbg.generated
     */
    private String agentIp;

    /**
     * 设备所在的agent的端口
     *
     * @mbg.generated
     */
    private Integer agentPort;

    /**
     * 设备系统版本
     *
     * @mbg.generated
     */
    private String systemVersion;

    /**
     * 设备api版本
     *
     * @mbg.generated
     */
    private String apiLevel;

    /**
     * 设备cpu架构
     *
     * @mbg.generated
     */
    private String cpuAbi;

    /**
     * cpu信息
     *
     * @mbg.generated
     */
    private String cpuInfo;

    /**
     * 内存大小：GB
     *
     * @mbg.generated
     */
    private String memSize;

    /**
     * 分辨率
     *
     * @mbg.generated
     */
    private String resolution;

    /**
     * 图片地址，用于在前端展示
     *
     * @mbg.generated
     */
    private String imgUrl;

    /**
     * 设备类型：1.android  2.ios
     *
     * @mbg.generated
     */
    private Integer type;

    /**
     * 设备状态：0.离线 1.空闲 2.使用中
     *
     * @mbg.generated
     */
    private Integer status;

    /**
     * stf初始化状态：0.失败 1.成功
     *
     * @mbg.generated
     */
    private Integer stfStatus;

    /**
     * macaca初始化状态：0.失败 1.成功
     *
     * @mbg.generated
     */
    private Integer macacaStatus;

    /**
     * 最近一次在线时间
     *
     * @mbg.generated
     */
    private Date lastOnlineTime;

    /**
     * 最近一次离线时间
     *
     * @mbg.generated
     */
    private Date lastOfflineTime;

    /**
     * 使用人
     *
     * @mbg.generated
     */
    private String username;

    /**
     * 创建时间
     *
     * @mbg.generated
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    private static final long serialVersionUID = 1L;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getAgentIp() {
        return agentIp;
    }

    public void setAgentIp(String agentIp) {
        this.agentIp = agentIp;
    }

    public Integer getAgentPort() {
        return agentPort;
    }

    public void setAgentPort(Integer agentPort) {
        this.agentPort = agentPort;
    }

    public String getSystemVersion() {
        return systemVersion;
    }

    public void setSystemVersion(String systemVersion) {
        this.systemVersion = systemVersion;
    }

    public String getApiLevel() {
        return apiLevel;
    }

    public void setApiLevel(String apiLevel) {
        this.apiLevel = apiLevel;
    }

    public String getCpuAbi() {
        return cpuAbi;
    }

    public void setCpuAbi(String cpuAbi) {
        this.cpuAbi = cpuAbi;
    }

    public String getCpuInfo() {
        return cpuInfo;
    }

    public void setCpuInfo(String cpuInfo) {
        this.cpuInfo = cpuInfo;
    }

    public String getMemSize() {
        return memSize;
    }

    public void setMemSize(String memSize) {
        this.memSize = memSize;
    }

    public String getResolution() {
        return resolution;
    }

    public void setResolution(String resolution) {
        this.resolution = resolution;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getStfStatus() {
        return stfStatus;
    }

    public void setStfStatus(Integer stfStatus) {
        this.stfStatus = stfStatus;
    }

    public Integer getMacacaStatus() {
        return macacaStatus;
    }

    public void setMacacaStatus(Integer macacaStatus) {
        this.macacaStatus = macacaStatus;
    }

    public Date getLastOnlineTime() {
        return lastOnlineTime;
    }

    public void setLastOnlineTime(Date lastOnlineTime) {
        this.lastOnlineTime = lastOnlineTime;
    }

    public Date getLastOfflineTime() {
        return lastOfflineTime;
    }

    public void setLastOfflineTime(Date lastOfflineTime) {
        this.lastOfflineTime = lastOfflineTime;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", name=").append(name);
        sb.append(", ip=").append(ip);
        sb.append(", agentIp=").append(agentIp);
        sb.append(", agentPort=").append(agentPort);
        sb.append(", systemVersion=").append(systemVersion);
        sb.append(", apiLevel=").append(apiLevel);
        sb.append(", cpuAbi=").append(cpuAbi);
        sb.append(", cpuInfo=").append(cpuInfo);
        sb.append(", memSize=").append(memSize);
        sb.append(", resolution=").append(resolution);
        sb.append(", imgUrl=").append(imgUrl);
        sb.append(", type=").append(type);
        sb.append(", status=").append(status);
        sb.append(", stfStatus=").append(stfStatus);
        sb.append(", macacaStatus=").append(macacaStatus);
        sb.append(", lastOnlineTime=").append(lastOnlineTime);
        sb.append(", lastOfflineTime=").append(lastOfflineTime);
        sb.append(", username=").append(username);
        sb.append(", createTime=").append(createTime);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}