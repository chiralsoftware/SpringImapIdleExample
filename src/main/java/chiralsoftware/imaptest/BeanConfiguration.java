package chiralsoftware.imaptest;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.mail.BodyPart;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.mail.ImapIdleChannelAdapter;
import org.springframework.integration.mail.ImapMailReceiver;
import org.springframework.messaging.Message;
import static org.springframework.util.StringUtils.startsWithIgnoreCase;

/**
 * Configure some beans
 */
@Configuration
public class BeanConfiguration {

    private static final Logger LOG = Logger.getLogger(BeanConfiguration.class.getName());

    @Bean
    public ImapIdleChannelAdapter imapMailReceiver(@Autowired ImapMailReceiver mailReceiver) {
        final ImapIdleChannelAdapter result = new ImapIdleChannelAdapter(mailReceiver);
        LOG.info("ok i created this imapidlechanneladapter: " + result);
        result.setOutputChannelName("emailOutputChannel");
        return result;
    }
        
    @ServiceActivator(inputChannel = "emailOutputChannel")
    public void processEmailMessage(Message m) throws MessagingException, IOException {
        LOG.info("========= Service activator  ========== " + m);
        final Object payload = m.getPayload();
        if(payload == null) {
            LOG.warning("Message payload was null");
            return;
        }
        if(! (payload instanceof MimeMessage)) {
            LOG.info("The payload should have been a " + MimeMessage.class + " but instead was: " + payload.getClass());
            return;
        }
        final MimeMessage mm = (MimeMessage) payload;
        LOG.info("Received a Mime message with content type: " + mm.getContentType() + " and subject: " + mm.getSubject());
        if(! startsWithIgnoreCase(mm.getContentType(), "multipart/mixed")) {
            LOG.info("The content type is not multipart/mixed so I'm not set up for this.");
            return;
        }
        final Object contentObject = mm.getContent();
        if(contentObject == null) {
            LOG.info("Content object was null!");
            return;
        }
        if(! (contentObject instanceof MimeMultipart)) {
            LOG.info("The content type was multipart/mixed but the content object was class: " + contentObject.getClass() + 
                    " instead of " + MimeMultipart.class);
            return;
        }
        final MimeMultipart multipart = (MimeMultipart) contentObject;
        LOG.info("Got this many parts: " + multipart.getCount());
        for(int i = 0; i < multipart.getCount(); i++) {
            final BodyPart part = multipart.getBodyPart(i);
            LOG.info("Looking at part: " + i + " which has type: " + part.getContentType());
            if(startsWithIgnoreCase(part.getContentType(), "image/jpeg")) {
                LOG.info("The content type is a JPEG!");
                final BufferedImage bim = ImageIO.read(part.getInputStream());
                LOG.info("The image size is: " + bim.getWidth() + "x" + bim.getHeight());
            }
        }
    }

}
