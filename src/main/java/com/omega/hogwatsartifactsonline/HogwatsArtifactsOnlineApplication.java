package com.omega.hogwatsartifactsonline;

import de.mkammerer.snowflakeid.SnowflakeIdGenerator;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class HogwatsArtifactsOnlineApplication {

    public static void main(String[] args) {
        SpringApplication.run(HogwatsArtifactsOnlineApplication.class, args);
    }

    @Bean
    public SnowflakeIdGenerator idGenerator() {
        return SnowflakeIdGenerator.createDefault(1);
    }
}
