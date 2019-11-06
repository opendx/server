package com.daxiang.mbg.po;

import com.daxiang.model.vo.DriverUrl;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class Driver implements Serializable {
    private Integer id;

    /**
     * 版本号
     *
     * @mbg.generated
     */
    @NotBlank(message = "version不能为空")
    private String version;

    /**
     * 1. chromedriver
     *
     * @mbg.generated
     */
    @NotNull(message = "type不能为空")
    private Integer type;

    /**
     * 创建时间
     *
     * @mbg.generated
     */
    private Date createTime;

    /**
     * 创建人
     *
     * @mbg.generated
     */
    private Integer creatorUid;

    /**
     * 各平台下载地址，1.windows 2.linux 3.macos
     *
     * @mbg.generated
     */
    private java.util.List<com.daxiang.model.vo.DriverUrl> urls;

    /**
     * 设备ids
     *
     * @mbg.generated
     */
    private java.util.List<String> deviceIds;

    private static final long serialVersionUID = 1L;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Integer getCreatorUid() {
        return creatorUid;
    }

    public void setCreatorUid(Integer creatorUid) {
        this.creatorUid = creatorUid;
    }

    public java.util.List<com.daxiang.model.vo.DriverUrl> getUrls() {
        return urls;
    }

    public void setUrls(java.util.List<com.daxiang.model.vo.DriverUrl> urls) {
        this.urls = urls;
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
        sb.append(", version=").append(version);
        sb.append(", type=").append(type);
        sb.append(", createTime=").append(createTime);
        sb.append(", creatorUid=").append(creatorUid);
        sb.append(", urls=").append(urls);
        sb.append(", deviceIds=").append(deviceIds);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}