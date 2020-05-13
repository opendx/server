package com.daxiang.agent;

import com.daxiang.service.BrowserService;
import com.daxiang.service.MobileService;
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

import java.net.URI;
import java.net.URISyntaxException;

/**
 * Created by jiangyitao.
 */
@Component
@Slf4j
public class AgentStatusChangeNotifier extends AbstractStatusChangeNotifier {

    @Autowired
    private MobileService mobileService;
    @Autowired
    private BrowserService browserService;

    public AgentStatusChangeNotifier(InstanceRepository repository) {
        super(repository);
    }

    @Override
    protected Mono<Void> doNotify(InstanceEvent event, Instance instance) {
        return Mono.fromRunnable(() -> {
            if (event instanceof InstanceStatusChangedEvent) {
                String agentUrl = instance.getRegistration().getServiceUrl();
                String status = ((InstanceStatusChangedEvent) event).getStatusInfo().getStatus();
                log.info("agent: {}, status: {}", agentUrl, status);

                URI agentUri;
                try {
                    agentUri = new URI(agentUrl);
                } catch (URISyntaxException e) {
                    log.error("非法的agentUrl: {}", agentUrl, e);
                    return;
                }

                if (!StatusInfo.STATUS_UP.equals(status)) {
                    // agent离线，把该agent下device变为离线
                    mobileService.agentOffline(agentUri.getHost());
                    browserService.agentOffline(agentUri.getHost());
                }
            }
        });
    }
}
