package ta.bot.fit.restSender.dto;

/**
 * Created by Joey on 4/12/17.
 */
public class FbMsgSendRequestResponseDto {
    private String recipient_id;
    private String message_id;

    public String getRecipient_id() {
        return recipient_id;
    }

    public void setRecipient_id(String recipient_id) {
        this.recipient_id = recipient_id;
    }

    public String getMessage_id() {
        return message_id;
    }

    public void setMessage_id(String message_id) {
        this.message_id = message_id;
    }
}
