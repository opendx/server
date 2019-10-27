package com.daxiang.mbg.po;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

public class Category implements Serializable {

    public static final int PAGE = 1;
    public static final int ACTION = 2;

    /**
     * 分类id
     *
     * @mbg.generated
     */
    private Integer id;

    /**
     * 分类名字
     *
     * @mbg.generated
     */
    @NotBlank(message = "分类名不能为空")
    private String name;

    /**
     * 类型：1.Page
     *
     * @mbg.generated
     */
    @NotNull(message = "分类类型不能为空")
    private Integer type;

    /**
     * 所属项目的id
     *
     * @mbg.generated
     */
    @NotNull(message = "项目Id不能为空")
    private Integer projectId;

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

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getProjectId() {
        return projectId;
    }

    public void setProjectId(Integer projectId) {
        this.projectId = projectId;
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

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", name=").append(name);
        sb.append(", type=").append(type);
        sb.append(", projectId=").append(projectId);
        sb.append(", createTime=").append(createTime);
        sb.append(", creatorUid=").append(creatorUid);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}