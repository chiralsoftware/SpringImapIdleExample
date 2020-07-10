package chiralsoftware.imaptest;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.mail.ImapMailReceiver;

/**
 * Create the ImapMailReceiver object
 */
@Configuration
public class ImapReceiver {
    
    private static final String url = 
            "imaps://username:password@mail.example.com:993/inbox";
    
    /** Very important: we have to create this object as a bean, so that
     Spring will call the init method on it. If you don't do that, but instead
     create a ImapMailReceiver just by doing new ImapMailReceiver(url), 
     init will not be called and it will throw a NullPointerException in waitForNewMessages()
     * when it tries to this.scheduler.schedule(), because the scheduler object will be null.
     * See: https://github.com/spring-projects/spring-integration/blob/master/spring-integration-mail/src/main/java/org/springframework/integration/mail/ImapMailReceiver.java
     * line 193
     */
    @Bean 
    public ImapMailReceiver mailReceiver() {
        final ImapMailReceiver mailReceiver = new ImapMailReceiver(url);
        /* 
        Very important: when trying to read the contents of the message you will get:
MessageHandlingException: error occurred during processing message in 'MethodInvokingMessageProcessor' [org.springframework.integration.handler.MethodInvokingMessageProcessor@5d3f553d]; nested exception is com.sun.mail.util.FolderClosedIOException, failedMessage=GenericMessage [payload=org.springframework.integration.mail.AbstractMailReceiver$IntegrationMimeMessage@48297aae, headers={id=ec656bd1-b7f3-0ffc-9c92-c6c95d071b85, timestamp=1594336676124}]
	at org.springframework.integration.support.utils.IntegrationUtils.wrapInHandlingExceptionIfNecessary(IntegrationUtils.java:192)
unless you setSimpleContent(true) here:        
        */
        mailReceiver.setSimpleContent(true);
        return mailReceiver;
    }
    
}
