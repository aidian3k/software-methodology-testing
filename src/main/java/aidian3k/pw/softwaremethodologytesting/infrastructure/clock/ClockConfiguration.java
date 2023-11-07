package aidian3k.pw.softwaremethodologytesting.infrastructure.clock;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Clock;

@Configuration
public class ClockConfiguration {
    @Bean
    public Clock handleClockConfiguration() {
        return Clock.systemUTC();
    }
}
