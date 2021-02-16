package com.daxiang.mbg.po;

import java.io.Serializable;
import java.util.Date;

public class AgentExtJar implements Serializable {
    private Integer id;

    /**
     * jar name
     *
     * @mbg.generated
     */
    private String name;

    /**
     * 版本
     *
     * @mbg.generated
     */
    private String version;

    /**
     * jar文件 md5
     *
     * @mbg.generated
     */
    private String md5;

    /**
     * 服务端保存的文件路径
     *
     * @mbg.generated
     */
    private String filePath;

    /**
     * 文件大小
     *
     * @mbg.generated
     */
    private Long fileSize;

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

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getMd5() {
        return md5;
    }

    public void setMd5(String md5) {
        this.md5 = md5;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public Long getFileSize() {
        return fileSize;
    }

    public void setFileSize(Long fileSize) {
        this.fileSize = fileSize;
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

    public String getFilename() {
        return name + "-" + version + ".jar";
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", name=").append(name);
        sb.append(", version=").append(version);
        sb.append(", md5=").append(md5);
        sb.append(", filePath=").append(filePath);
        sb.append(", fileSize=").append(fileSize);
        sb.append(", uploadTime=").append(uploadTime);
        sb.append(", uploadorUid=").append(uploadorUid);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}