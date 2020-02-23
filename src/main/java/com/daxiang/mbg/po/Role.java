package com.daxiang.mbg.po;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

public class Role implements Serializable {
    private Integer id;

    /**
     * 角色名
     *
     * @mbg.generated
     */
    @NotBlank(message = "角色名不能为空")
    private String name;

    /**
     * 别名
     *
     * @mbg.generated
     */
    @NotBlank(message = "别名不能为空")
    private String alias;

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

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", name=").append(name);
        sb.append(", alias=").append(alias);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}