package ta.bot.fit.restSender;

import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ta.bot.fit.restSender.dto.FbMsgSendRequestDto;
import ta.bot.fit.restSender.dto.FbMsgSendRequestResponseDto;
import ta.bot.fit.restSender.dto.MessageDto;
import ta.bot.fit.restSender.dto.RecipientDto;

import javax.annotation.PostConstruct;
import javax.net.ssl.SSLContext;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by Joey on 6/12/16.
 */
@Service
public class FbMsgSender {
    static Logger log = Logger.getLogger(FbMsgSender.class.getName());

    /*********************************
     * Spring Beans Inject
     *********************************/

    @Autowired
    private Environment env;

    /*********************************
     *            Member             *
     ********************************/

    private String FB_MSG_REQUEST_URL;

    private HttpComponentsClientHttpRequestFactory requestFactory;

    /*********************************
     *              Init             *
     ********************************/
    @PostConstruct
    public void initIt() throws Exception {

        FB_MSG_REQUEST_URL = "https://graph.facebook.com/v2.6/me/messages?access_token=" + env.getProperty("PAGE_ACCESS_TOKEN");

        try {
            SSLContext sslContext = org.apache.http.ssl.SSLContexts.custom().
                    loadTrustMaterial(null, new TrustStrategy() {
                        @Override
                        public boolean isTrusted(X509Certificate[] chain,
                                                 String authType)
                                throws CertificateException {
                            return true;
                        }
                    })
                    .build();
            SSLConnectionSocketFactory csf = new SSLConnectionSocketFactory(sslContext);

            CloseableHttpClient httpClient = HttpClients.custom()
                    .setSSLSocketFactory(csf)
                    .build();

            HttpComponentsClientHttpRequestFactory requestFactory =
                    new HttpComponentsClientHttpRequestFactory();

            requestFactory.setHttpClient(httpClient);
            this.requestFactory = requestFactory;
        }catch(Exception e){
            e.printStackTrace();
            log.debug("EXCEPTION:"+e.toString());
        }
    }

    public void sendTextMessage(String id, String msg){


        MessageDto messageDto = new MessageDto(msg);
        RecipientDto recipientDto = new RecipientDto(id);

        FbMsgSendRequestDto request = new FbMsgSendRequestDto(recipientDto, messageDto);

        RestTemplate restTemplate = new RestTemplate(this.requestFactory);

        FbMsgSendRequestResponseDto response = restTemplate.postForObject(FB_MSG_REQUEST_URL, request, FbMsgSendRequestResponseDto.class);

        //log.debug("################################ TESTING ################################");
        //log.debug("################ DEBUG: return status = "+psGroup.getStatus());
        //log.debug("################ DEBUG: return data size = "+psGroup.getPsDtoList().size());

    }

}
