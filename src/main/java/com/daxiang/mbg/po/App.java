package com.daxiang.mbg.po;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

public class App implements Serializable {
    /**
     * 主键
     *
     * @mbg.generated
     */
    private Integer id;

    /**
     * 平台：1.android 2.iOS
     *
     * @mbg.generated
     */
    @NotNull(message = "平台不能为空")
    private Integer platform;

    /**
     * app名
     *
     * @mbg.generated
     */
    @NotEmpty(message = "app名不能为空")
    private String name;

    /**
     * 版本号
     *
     * @mbg.generated
     */
    private String version;

    /**
     * android: 包名
     *
     * @mbg.generated
     */
    private String packageName;

    /**
     * android: 启动activity
     *
     * @mbg.generated
     */
    private String launchActivity;

    /**
     * 下载地址
     *
     * @mbg.generated
     */
    private String downloadUrl;

    /**
     * 上传时间
     *
     * @mbg.generated
     */
    private Date uploadTime;

    /**
     * 上传人
     *
     * @mbg.generated
     */
    private Integer uploadorUid;

    /**
     * 所属项目
     *
     * @mbg.generated
     */
    @NotNull(message = "项目id不能为空")
    private Integer projectId;

    private static final long serialVersionUID = 1L;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getPlatform() {
        return platform;
    }

    public void setPlatform(Integer platform) {
        this.platform = platform;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getLaunchActivity() {
        return launchActivity;
    }

    public void setLaunchActivity(String launchActivity) {
        this.launchActivity = launchActivity;
    }

    public String getDownloadUrl() {
        return downloadUrl;
    }

    public void setDownloadUrl(String downloadUrl) {
        this.downloadUrl = downloadUrl;
    }

    public Date getUploadTime() {
        return uploadTime;
    }

    public void setUploadTime(Date uploadTime) {
        this.uploadTime = uploadTime;
    }

    public Integer getUploadorUid() {
        return uploadorUid;
    }

    public void setUploadorUid(Integer uploadorUid) {
        this.uploadorUid = uploadorUid;
    }

    public Integer getProjectId() {
        return projectId;
    }

    public void setProjectId(Integer projectId) {
        this.projectId = projectId;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", platform=").append(platform);
        sb.append(", name=").append(name);
        sb.append(", version=").append(version);
        sb.append(", packageName=").append(packageName);
        sb.append(", launchActivity=").append(launchActivity);
        sb.append(", downloadUrl=").append(downloadUrl);
        sb.append(", uploadTime=").append(uploadTime);
        sb.append(", uploadorUid=").append(uploadorUid);
        sb.append(", projectId=").append(projectId);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}