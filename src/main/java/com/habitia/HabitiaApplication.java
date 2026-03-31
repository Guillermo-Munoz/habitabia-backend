package com.habitia;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import com.habitia.shared.infrastructure.security.JwtProperties;
import com.habitia.shared.infrastructure.storage.StorageProperties;

@SpringBootApplication
@EnableConfigurationProperties({JwtProperties.class, StorageProperties.class})
public class HabitiaApplication {
	public static void main(String[] args) {
		SpringApplication.run(HabitiaApplication.class, args);
	}
}