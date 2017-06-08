package ta.bot.fit.restController;

import com.restfb.DefaultJsonMapper;
import com.restfb.JsonMapper;
import com.restfb.types.webhook.WebhookEntry;
import com.restfb.types.webhook.WebhookObject;
import com.restfb.types.webhook.messaging.MessageItem;
import com.restfb.types.webhook.messaging.MessagingAttachment;
import com.restfb.types.webhook.messaging.MessagingItem;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ta.bot.fit.restSender.FbMsgSender;

import javax.annotation.PostConstruct;
import java.util.Date;
import java.util.List;

/**
 * Created by Joey on 4/9/17.
 */
@RestController
@RequestMapping(value = "/chatBot")
@PropertySource("classpath:config.properties")
public class RestFbChatBotController {

    static Logger log = Logger.getLogger(RestFbChatBotController.class.getName());

    private String VERIFY_TOKEN;
    private String MODE_SUBSCRIBE;

    /*********************************
     * Spring Beans Inject
     *********************************/
    @Autowired
    private Environment env;


    private FbMsgSender fbMsgSender;
    @Autowired
    public void setFbMsgSender(FbMsgSender fbMsgSender){
        this.fbMsgSender = fbMsgSender;
    }

    /*********************************
     *              Init             *
     ********************************/
    @PostConstruct
    public void initIt() throws Exception {
        VERIFY_TOKEN = env.getProperty("VERIFY_TOKEN");
        MODE_SUBSCRIBE = env.getProperty("MODE_SUBSCRIBE");
//        log.debug("$$$$$$ VERIFY_TOKEN="+VERIFY_TOKEN);
//        log.debug("$$$$$$ MODE_SUBSCRIBE="+MODE_SUBSCRIBE);
    }

    /*********************************
     *              API             *
     ********************************/

    @RequestMapping(value = "/webhook", method= RequestMethod.GET)
    public String verify(
            @RequestParam("hub.mode") String mode,
            @RequestParam("hub.challenge") String challenge,
            @RequestParam("hub.verify_token") String verifyToken) {
        if(!verifyToken.equals(VERIFY_TOKEN)) {
            log.debug("############################ Wrong token: "+verifyToken);
            return null;
        }else if(!mode.equals(MODE_SUBSCRIBE)){
            log.debug("############################ Wrong mode: "+mode);
            return null;
        }
        return challenge;
    }

    @RequestMapping(value = "/webhook", method= RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public void verify(@RequestBody String pushedJsonAsString) {
        JsonMapper mapper = new DefaultJsonMapper();
        WebhookObject webhookObject =
                mapper.toJavaObject(pushedJsonAsString, WebhookObject.class);
        if(webhookObject.getObject().equals("page")){
            for(WebhookEntry entry : webhookObject.getEntryList()){
                if(entry!=null){
                    String pageId = entry.getId();
                    Date timeOfEvent = entry.getTime();

                    for(MessagingItem messaging : entry.getMessaging()){
                        if(messaging.getMessage()!=null){
                            receivedMessage(messaging);
                        }
                    }
                }
            }
        }
    }

    private void receivedMessage(MessagingItem messaging){
        String senderId = messaging.getSender().getId();
        String recipientId = messaging.getRecipient().getId();
        Date timeOfMessage = messaging.getTimestamp();
        MessageItem message = messaging.getMessage();

        String messageId = message.getMid();
        String messageText = message.getText();
        List<MessagingAttachment> messageAttachments = message.getAttachments();

        if(messageText != null){
            if(messageText.length()>0) fbMsgSender.sendTextMessage(senderId, messageText);
            else fbMsgSender.sendTextMessage(senderId, "Blank message...");
        }else if(messageAttachments!=null){
            fbMsgSender.sendTextMessage(senderId, "Message with attachment received");
        }
    }

//    @RequestMapping(value = "/webhook", method= RequestMethod.POST)
//    @ResponseStatus(HttpStatus.OK)
//    public void verify(@RequestBody RcvMsgDto dto) {
//        if(dto!=null && dto.getObject()!=null && dto.getEntry()!=null && dto.getObject().equals("page")){
//            for(EntryDto entry : dto.getEntry()){
//                if(entry!=null){
//                    String pageId = entry.getId();
//                    String timeOfEvent = entry.getTime();
//
//                    for(MessagingDto messaging : entry.getMessaging()){
//                        if(messaging!=null && messaging.getMessage()!=null){
//                            receivedMessage(messaging);
//                        }
//                    }
//                }
//            }
//        }
//    }
//
//    private void receivedMessage(MessagingDto messaging){
//        if(messaging.getSender()==null) return;
//        String senderId = messaging.getSender().getId();
//        String recipientId = messaging.getRecipient().getId();
//        String timeOfMessage = messaging.getTimestamp();
//        MessageDto message = messaging.getMessage();
//
//        String messageId = message.getMid();
//        String messageText = message.getText();
//        List<AttachmentDto> messageAttachments = message.getAttachments();
//
//        if(messageAttachments!=null){
//            fbMsgSender.sendTextMessage(senderId, "Message with attachment received");
//        }else if(messageText != null){
//            fbMsgSender.sendTextMessage(senderId, messageText);
//        }
//    }
}
