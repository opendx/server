package com.daxiang.mbg.po;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

public class Page implements Serializable {
    /**
     * page id
     *
     * @mbg.generated
     */
    private Integer id;

    /**
     * page名
     *
     * @mbg.generated
     */
    @NotBlank(message = "page名不能为空")
    private String name;

    /**
     * page所属项目
     *
     * @mbg.generated
     */
    @NotNull(message = "所属项目不能为空")
    private Integer projectId;

    /**
     * page所属分类
     *
     * @mbg.generated
     */
    private Integer categoryId;

    /**
     * page描述
     *
     * @mbg.generated
     */
    private String description;

    /**
     * 截图下载地址
     *
     * @mbg.generated
     */
    private String imgUrl;

    /**
     * 截图高度
     *
     * @mbg.generated
     */
    private Integer imgHeight;

    /**
     * 截图宽度
     *
     * @mbg.generated
     */
    private Integer imgWidth;

    /**
     * 图片所属的设备id
     *
     * @mbg.generated
     */
    private String deviceId;

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
     * 页面布局
     *
     * @mbg.generated
     */
    private String windowHierarchy;

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

    public Integer getProjectId() {
        return projectId;
    }

    public void setProjectId(Integer projectId) {
        this.projectId = projectId;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public Integer getImgHeight() {
        return imgHeight;
    }

    public void setImgHeight(Integer imgHeight) {
        this.imgHeight = imgHeight;
    }

    public Integer getImgWidth() {
        return imgWidth;
    }

    public void setImgWidth(Integer imgWidth) {
        this.imgWidth = imgWidth;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
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

    public String getWindowHierarchy() {
        return windowHierarchy;
    }

    public void setWindowHierarchy(String windowHierarchy) {
        this.windowHierarchy = windowHierarchy;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", name=").append(name);
        sb.append(", projectId=").append(projectId);
        sb.append(", categoryId=").append(categoryId);
        sb.append(", description=").append(description);
        sb.append(", imgUrl=").append(imgUrl);
        sb.append(", imgHeight=").append(imgHeight);
        sb.append(", imgWidth=").append(imgWidth);
        sb.append(", deviceId=").append(deviceId);
        sb.append(", creatorUid=").append(creatorUid);
        sb.append(", createTime=").append(createTime);
        sb.append(", windowHierarchy=").append(windowHierarchy);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}