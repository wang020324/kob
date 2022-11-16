package com.kob.backend;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootTest
class BackendApplicationTests {


	@Test
	void contextLoads() {
		PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		System.out.println(passwordEncoder.encode("pass"));
		System.out.println(passwordEncoder.encode("pb"));
		System.out.println(passwordEncoder.encode("pc"));
		System.out.println(passwordEncoder.matches("pass","$2a$10$laLLAnUTdcmZ2DhMggRb5.xtGgAffKDrR2MA8bglBYCzPse4MNB7G"));

	}

}
