package ie.nuig.i3market.semantic.engine.domain.notifications;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * @author qaiser
 * @email: qaiser.mehmood@insight-centre.org
 * @project i-3-market
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class NotificationModel {
    String receiver_id;
    String message;

    public NotificationModel(String receiver_id, String message) {
        this.receiver_id = receiver_id;
        this.message = message;
    }

    public String getReceiver_id() {
        return receiver_id;
    }

    public void setReceiver_id(String receiver_id) {
        this.receiver_id = receiver_id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "NotificationModel{" +
                "receiver_id='" + receiver_id + '\'' +
                ", message='" + message + '\'' +
                '}';
    }
}
