package ta.bot.fit.restSender.dto;

/**
 * Created by Joey on 4/12/17.
 */
public class FbMsgSendRequestDto {
    private RecipientDto recipient;
    private MessageDto message;

    public FbMsgSendRequestDto() {
    }

    public FbMsgSendRequestDto(RecipientDto recipient, MessageDto message) {
        this.recipient = recipient;
        this.message = message;
    }

    public RecipientDto getRecipient() {
        return recipient;
    }

    public void setRecipient(RecipientDto recipient) {
        this.recipient = recipient;
    }

    public MessageDto getMessage() {
        return message;
    }

    public void setMessage(MessageDto message) {
        this.message = message;
    }
}
