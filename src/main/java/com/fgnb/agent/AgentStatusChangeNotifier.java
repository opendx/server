package com.fgnb.agent;

import com.fgnb.mbg.mapper.DeviceMapper;
import com.fgnb.mbg.po.Device;
import com.fgnb.mbg.po.DeviceExample;
import de.codecentric.boot.admin.server.domain.entities.Instance;
import de.codecentric.boot.admin.server.domain.entities.InstanceRepository;
import de.codecentric.boot.admin.server.domain.events.InstanceEvent;
import de.codecentric.boot.admin.server.domain.events.InstanceStatusChangedEvent;
import de.codecentric.boot.admin.server.domain.values.StatusInfo;
import de.codecentric.boot.admin.server.notify.AbstractStatusChangeNotifier;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.Date;

/**
 * Created by jiangyitao.
 * Agent状态变化通知器
 */
@Component
@Slf4j
public class AgentStatusChangeNotifier extends AbstractStatusChangeNotifier {

    @Autowired
    private DeviceMapper deviceMapper;

    public AgentStatusChangeNotifier(InstanceRepository repositpry) {
        super(repositpry);
    }

    @Override
    protected Mono<Void> doNotify(InstanceEvent event, Instance instance) {
        return Mono.fromRunnable(() -> {
            if (event instanceof InstanceStatusChangedEvent) {
                String status = ((InstanceStatusChangedEvent) event).getStatusInfo().getStatus();
                if (!StatusInfo.STATUS_UP.equals(status)) {
                    //非在线
                    String agentURL = instance.getRegistration().getServiceUrl();// http://xx.xx.xx.xx:xxx/
                    log.info("检测到agent {} 处于非在线状态", agentURL);
                    String agentIp = agentURL.split("//")[1].split(":")[0];// xx.xx.xx.xx

                    //把该agent下的设备都改为离线
                    Device device = new Device();
                    device.setStatus(Device.OFFLINE_STATUS);
                    device.setLastOfflineTime(new Date());

                    DeviceExample deviceExample = new DeviceExample();
                    deviceExample.createCriteria().andAgentIpEqualTo(agentIp);

                    int updateRow = deviceMapper.updateByExampleSelective(device, deviceExample);
                    log.info("agent({})下的{}个设备已成功离线",agentIp,updateRow);
                }
            }
        });
    }
}
