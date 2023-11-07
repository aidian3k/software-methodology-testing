package aidian3k.pw.softwaremethodologytesting.infrastructure.clock;

import java.time.Clock;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ClockConfiguration {

	@Bean
	public Clock handleClockConfiguration() {
		return Clock.systemUTC();
	}
}
