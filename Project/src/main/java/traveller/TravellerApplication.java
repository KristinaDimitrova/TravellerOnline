package traveller;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.security.reactive.ReactiveSecurityAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration //test if redundant
@SpringBootApplication(exclude = {ReactiveSecurityAutoConfiguration.class })
@EnableScheduling
@ConditionalOnProperty(name = "scheduling.enabled", matchIfMissing = true) //can be turned off during testing
public class TravellerApplication  {

    public static void main(String[] args) {
        SpringApplication.run(TravellerApplication.class, args);
    }



}


