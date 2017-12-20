package main.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * Created by fanzhe on 2017/12/12.
 */
@Configuration
@ConfigurationProperties()
@PropertySource("classpath:main/config/project.properties")
@Component
public class ProjectConfig {
    public Boolean getSprintInitInitListener() {
        return sprintInitInitListener;
    }
    @Value("${spring.init.InitListener}")
    private Boolean sprintInitInitListener;

}
