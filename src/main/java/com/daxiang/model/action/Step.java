package com.daxiang.model.action;

import com.daxiang.mbg.po.Action;
import com.daxiang.validator.group.SaveActionGroup;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

/**
 * Created by jiangyitao.
 * action步骤
 */
@Data
public class Step {

    public static final int DISABLE_STATUS = 0;
    public static final int ENABLE_STATUS = 1;

    /**
     * 调用的action id
     */
    @NotNull(message = "步骤Action不能为空")
    private Integer actionId;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Action action;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    private Date startTime;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    private Date endTime;

    @NotBlank(message = "步骤名不能为空", groups = {SaveActionGroup.class})
    private String name;
    /**
     * 步骤赋值
     */
    private String evaluation;
    /**
     * 异常处理： null.中断执行 0.忽略，继续执行 1.抛出跳过异常
     */
    private Integer handleException;
    @NotNull(message = "步骤号不能为空")
    private Integer number;
    /**
     * 调用action传入的值
     */
    private List<String> args;
    @NotNull(message = "步骤status不能为空")
    private Integer status;
}
