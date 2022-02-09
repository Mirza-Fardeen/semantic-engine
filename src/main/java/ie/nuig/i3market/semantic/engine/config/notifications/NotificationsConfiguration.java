package ie.nuig.i3market.semantic.engine.config.notifications;

import ie.nuig.i3market.semantic.engine.common.Notifications;
import ie.nuig.i3market.semantic.engine.config.ApplicationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author qaiser
 * @email: qaiser.mehmood@insight-centre.org
 * @project i-3-market
 */

@Configuration
public class NotificationsConfiguration {

    private final ApplicationProperties applicationProperties;

    public NotificationsConfiguration(ApplicationProperties applicationProperties) {
        this.applicationProperties = applicationProperties;
    }

    public void sendDataToNotificationService(Object object){
        Notifications.postData(object, applicationProperties.getNotificationMangerIntegration().getNotificationService_POST());
    }
}
