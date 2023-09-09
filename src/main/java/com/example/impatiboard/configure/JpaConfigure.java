package com.example.impatiboard.configure;

import java.util.Optional;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@Configuration
@EnableJpaAuditing
@Profile(value = {"local", "default", "prod"})
public class JpaConfigure {

    @Bean
    public AuditorAware<String> auditorAware() {
        return () -> Optional.of("impati");
    }
}
