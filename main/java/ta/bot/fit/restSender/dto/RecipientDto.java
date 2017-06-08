package ta.bot.fit.restSender.dto;

/**
 * Created by Joey on 4/12/17.
 */
public class RecipientDto {
    private String id;

    public RecipientDto() {
    }

    public RecipientDto(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
