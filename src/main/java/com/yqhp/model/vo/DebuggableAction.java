package com.yqhp.model.vo;

import com.yqhp.mbg.po.Action;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * Created by jiangyitao.
 * 可调试的Action
 */
@Data
public class DebuggableAction {

    @NotNull(message = "action不能为空")
    private Action action;
    @Valid
    @NotNull(message = "缺少调试信息")
    private DebugInfo debugInfo;

    @Data
    public static class DebugInfo {
        @NotEmpty(message = "agentIp不能为空")
        private String agentIp;
        @NotNull(message = "agentPort不能为空")
        private Integer agentPort;
        @NotNull(message = "调试端口不能为空")
        private Integer port;
        /**
         * 移动端需要设备id
         */
        private String deviceId;
    }
}
