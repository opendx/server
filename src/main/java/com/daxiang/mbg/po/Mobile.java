package com.daxiang.mbg.po;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.Date;

public class Mobile implements Serializable {

    public static final int OFFLINE_STATUS = 0;
    public static final int USING_STATUS = 1;
    public static final int IDLE_STATUS = 2;

    /**
     * mobile id
     *
     * @mbg.generated
     */
    @NotBlank(message = "mobile id不能为空")
    private String id;

    /**
     * mobile名
     *
     * @mbg.generated
     */
    private String name;

    /**
     * 0: 真机 1: 模拟器
     *
     * @mbg.generated
     */
    private Integer emulator;

    /**
     * 所在的agent的ip
     *
     * @mbg.generated
     */
    private String agentIp;

    /**
     * 所在的agent的端口
     *
     * @mbg.generated
     */
    private Integer agentPort;

    /**
     * 系统版本
     *
     * @mbg.generated
     */
    private String systemVersion;

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
     * 屏幕宽（像素）
     *
     * @mbg.generated
     */
    private Integer screenWidth;

    /**
     * 屏幕高（像素）
     *
     * @mbg.generated
     */
    private Integer screenHeight;

    /**
     * 服务端保存的图片路径
     *
     * @mbg.generated
     */
    private String imgPath;

    /**
     * 平台：1.android  2.ios
     *
     * @mbg.generated
     */
    private Integer platform;

    /**
     * 状态：0.离线 1.使用中 2.空闲
     *
     * @mbg.generated
     */
    private Integer status;

    /**
     * 最近一次在线时间
     *
     * @mbg.generated
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date lastOnlineTime;

    /**
     * 最近一次使用人
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

    public Integer getEmulator() {
        return emulator;
    }

    public void setEmulator(Integer emulator) {
        this.emulator = emulator;
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

    public Integer getScreenWidth() {
        return screenWidth;
    }

    public void setScreenWidth(Integer screenWidth) {
        this.screenWidth = screenWidth;
    }

    public Integer getScreenHeight() {
        return screenHeight;
    }

    public void setScreenHeight(Integer screenHeight) {
        this.screenHeight = screenHeight;
    }

    public String getImgPath() {
        return imgPath;
    }

    public void setImgPath(String imgPath) {
        this.imgPath = imgPath;
    }

    public Integer getPlatform() {
        return platform;
    }

    public void setPlatform(Integer platform) {
        this.platform = platform;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Date getLastOnlineTime() {
        return lastOnlineTime;
    }

    public void setLastOnlineTime(Date lastOnlineTime) {
        this.lastOnlineTime = lastOnlineTime;
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
        sb.append(", emulator=").append(emulator);
        sb.append(", agentIp=").append(agentIp);
        sb.append(", agentPort=").append(agentPort);
        sb.append(", systemVersion=").append(systemVersion);
        sb.append(", cpuInfo=").append(cpuInfo);
        sb.append(", memSize=").append(memSize);
        sb.append(", screenWidth=").append(screenWidth);
        sb.append(", screenHeight=").append(screenHeight);
        sb.append(", imgPath=").append(imgPath);
        sb.append(", platform=").append(platform);
        sb.append(", status=").append(status);
        sb.append(", lastOnlineTime=").append(lastOnlineTime);
        sb.append(", username=").append(username);
        sb.append(", createTime=").append(createTime);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}