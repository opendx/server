package com.fgnb.model.vo;

import com.fgnb.mbg.po.Device;
import lombok.Data;

import java.util.List;


/**
 * Created by jiangyitao.
 */
@Data
public class AgentVo {
    private String agentIp;
    private Integer agentPort;
    private List<Driver> drivers;
    private List<Device> devices;

    @Data
    public static class Driver {
        public static final Integer CHROME_TYPE = 1;
        public static final String CHROME_NAME = "chrome";

        private Integer type;
        private String name;
        private Integer port;
    }
}
