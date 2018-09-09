package ua.com.alistratenko.ReverseSideTestApp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration for Beans
 *
 * @author Nikita
 * @since 08.09.2018
 */
@Configuration
public class ApplicationBeansConfiguration {

    @Bean
    public Logger logger(){
        return LoggerFactory.getLogger("application");
    }

}
