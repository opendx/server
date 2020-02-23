package com.daxiang.mbg.po;

import com.daxiang.validator.group.SaveUserGroup;
import com.daxiang.validator.group.UpdateGroup;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

public class User implements Serializable {

    public static final int ENABLE_STATUS = 1;

    @NotNull(message = "用户id不能为空", groups = {UpdateGroup.class})
    private Integer id;

    /**
     * 用户名
     *
     * @mbg.generated
     */
    @NotBlank(message = "用户名不能为空")
    private String username;

    /**
     * 用户密码
     *
     * @mbg.generated
     */
    @NotBlank(message = "密码不能为空")
    private String password;

    /**
     * 用户昵称
     *
     * @mbg.generated
     */
    @NotBlank(message = "昵称不能为空", groups = {SaveUserGroup.class})
    private String nickName;

    /**
     * 状态，0:禁用 1:正常
     *
     * @mbg.generated
     */
    @NotNull(message = "账户状态不能为空", groups = {SaveUserGroup.class})
    private Integer status;

    /**
     * 创建时间
     *
     * @mbg.generated
     */
    private Date createTime;

    private static final long serialVersionUID = 1L;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
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
        sb.append(", username=").append(username);
        sb.append(", password=").append(password);
        sb.append(", nickName=").append(nickName);
        sb.append(", status=").append(status);
        sb.append(", createTime=").append(createTime);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}