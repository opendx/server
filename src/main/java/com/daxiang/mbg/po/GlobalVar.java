package com.daxiang.mbg.po;

import com.daxiang.model.environment.EnvironmentValue;
import com.daxiang.validator.annotation.JavaIdentifier;
import com.daxiang.validator.annotation.NoDuplicateEnvironment;
import com.daxiang.validator.group.UpdateGroup;
import com.fasterxml.jackson.annotation.JsonInclude;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class GlobalVar implements Serializable {
    @NotNull(message = "id不能为空", groups = {UpdateGroup.class})
    private Integer id;

    /**
     * 变量名
     *
     * @mbg.generated
     */
    @JavaIdentifier(message = "变量名不合法")
    private String name;

    /**
     * 变量类型
     *
     * @mbg.generated
     */
    @NotBlank(message = "变量类型不能为空")
    private String type;

    /**
     * 描述
     *
     * @mbg.generated
     */
    private String description;

    /**
     * 所属的项目id
     *
     * @mbg.generated
     */
    @NotNull(message = "项目id不能为空")
    private Integer projectId;

    /**
     * 所属分类
     *
     * @mbg.generated
     */
    private Integer categoryId;

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
     * 变量值
     *
     * @mbg.generated
     */
    @NotEmpty(message = "变量值不能为空")
    @NoDuplicateEnvironment(message = "环境不能重复")
    @Valid
    private java.util.List<com.daxiang.model.environment.EnvironmentValue> environmentValues;

    /**
     * 专门给agent用的
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String value;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public java.util.List<com.daxiang.model.environment.EnvironmentValue> getEnvironmentValues() {
        return environmentValues;
    }

    public void setEnvironmentValues(java.util.List<com.daxiang.model.environment.EnvironmentValue> environmentValues) {
        this.environmentValues = environmentValues;
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
        sb.append(", description=").append(description);
        sb.append(", projectId=").append(projectId);
        sb.append(", categoryId=").append(categoryId);
        sb.append(", creatorUid=").append(creatorUid);
        sb.append(", createTime=").append(createTime);
        sb.append(", environmentValues=").append(environmentValues);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}