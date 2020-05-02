package com.daxiang.model.request;

import com.daxiang.mbg.po.Action;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * Created by jiangyitao.
 */
@Data
public class ActionDebugRequest {

    @NotNull(message = "action不能为空")
    @Valid
    private Action action;
    @Valid
    @NotNull(message = "缺少调试信息")
    private DebugInfo debugInfo;

    @Data
    public static class DebugInfo {
        @NotNull(message = "platform不能为空")
        private Integer platform;
        @NotEmpty(message = "agentIp不能为空")
        private String agentIp;
        @NotNull(message = "agentPort不能为空")
        private Integer agentPort;
        @NotNull(message = "环境不能为空")
        private Integer env;
        @NotBlank(message = "deviceId不能为空")
        private String deviceId;
    }
}
