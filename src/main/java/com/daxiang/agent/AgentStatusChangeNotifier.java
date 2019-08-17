package com.daxiang.agent;

import com.daxiang.mbg.po.Device;
import com.daxiang.service.DeviceService;
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
 */
@Component
@Slf4j
public class AgentStatusChangeNotifier extends AbstractStatusChangeNotifier {

    @Autowired
    private DeviceService deviceService;

    public AgentStatusChangeNotifier(InstanceRepository repository) {
        super(repository);
    }

    @Override
    protected Mono<Void> doNotify(InstanceEvent event, Instance instance) {
        return Mono.fromRunnable(() -> {
            if (event instanceof InstanceStatusChangedEvent) {
                String status = ((InstanceStatusChangedEvent) event).getStatusInfo().getStatus();
                if (!StatusInfo.STATUS_UP.equals(status)) { // 非在线
                    String agentUrl = instance.getRegistration().getServiceUrl();// http://xx.xx.xx.xx:xxx/
                    log.info("检测到agent {} 处于非在线状态", agentUrl);
                    String agentIp = agentUrl.split("//")[1].split(":")[0];// xx.xx.xx.xx

                    // 把该agent下的设备都改为离线
                    Device device = new Device();
                    device.setStatus(Device.OFFLINE_STATUS);
                    device.setLastOfflineTime(new Date());
                    int updateRow = deviceService.updateByAgentIp(device, agentIp);
                    log.info("agent({})下的{}个设备已成功离线", agentIp, updateRow);
                }
            }
        });
    }
}
