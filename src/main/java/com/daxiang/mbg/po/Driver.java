package com.daxiang.mbg.po;

import com.daxiang.model.dto.DriverFile;
import com.daxiang.validator.group.UpdateGroup;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class Driver implements Serializable {
    @NotNull(message = "id不能为空", groups = {UpdateGroup.class})
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
     * 各平台文件，1.windows 2.linux 3.macos
     *
     * @mbg.generated
     */
    private java.util.List<com.daxiang.model.dto.DriverFile> files;

    /**
     * deviceIds
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

    public java.util.List<com.daxiang.model.dto.DriverFile> getFiles() {
        return files;
    }

    public void setFiles(java.util.List<com.daxiang.model.dto.DriverFile> files) {
        this.files = files;
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
        sb.append(", files=").append(files);
        sb.append(", deviceIds=").append(deviceIds);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}