package com.daxiang.mbg.po;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

public class Browser implements Serializable {

    public static final int OFFLINE_STATUS = 0;
    public static final int USING_STATUS = 1;
    public static final int IDLE_STATUS = 2;

    /**
     * 浏览器id
     *
     * @mbg.generated
     */
    @NotBlank(message = "浏览器id不能为空")
    private String id;

    /**
     * 类型: chrome firefox ...
     *
     * @mbg.generated
     */
    @NotBlank(message = "浏览器type不能为空")
    private String type;

    /**
     * 版本号
     *
     * @mbg.generated
     */
    @NotBlank(message = "浏览器version不能为空")
    private String version;

    /**
     * 平台: 1. windows 2.linux 3.macos
     *
     * @mbg.generated
     */
    @NotNull(message = "浏览器所在platform不能为空")
    private Integer platform;

    /**
     * 状态：0.离线 1.使用中 2.空闲
     *
     * @mbg.generated
     */
    private Integer status;

    /**
     * 浏览器所在的agent的ip
     *
     * @mbg.generated
     */
    private String agentIp;

    /**
     * 浏览器所在的agent的端口
     *
     * @mbg.generated
     */
    private Integer agentPort;

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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
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
        sb.append(", type=").append(type);
        sb.append(", version=").append(version);
        sb.append(", platform=").append(platform);
        sb.append(", status=").append(status);
        sb.append(", agentIp=").append(agentIp);
        sb.append(", agentPort=").append(agentPort);
        sb.append(", lastOnlineTime=").append(lastOnlineTime);
        sb.append(", username=").append(username);
        sb.append(", createTime=").append(createTime);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}