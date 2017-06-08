package ta.bot.fit.restSender.dto;

/**
 * Created by Joey on 4/12/17.
 */
public class MessageDto {
    private String text;

    public MessageDto() {
    }

    public MessageDto(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
