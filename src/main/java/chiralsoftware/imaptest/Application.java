package chiralsoftware.imaptest;

import java.util.logging.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.integration.annotation.IntegrationComponentScan;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.scheduling.annotation.EnableScheduling;

/** 
Initialize the Spring Boot application
*/
@SpringBootApplication
@EnableIntegration
@EnableScheduling
@IntegrationComponentScan(basePackageClasses = Application.class)
public class Application {

    private static final Logger LOG = Logger.getLogger(Application.class.getName());

    public static void main(String[] args) throws Exception {
    
        LOG.info("Ok I'm in a Spring Boot application");
        SpringApplication.run(Application.class, args);
    }    
}
