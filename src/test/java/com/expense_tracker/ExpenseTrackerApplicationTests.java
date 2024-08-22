package com.expense_tracker;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@SpringBootTest(properties = { "spring.datasource.url=jdbc:h2:mem:testdb",
		"spring.datasource.driverClassName=org.h2.Driver", "spring.datasource.username=sa",
		"spring.datasource.password=", "spring.jpa.database-platform=org.hibernate.dialect.H2Dialect",
		"jwt.secret=xxxxx", "logging.level.org.springframework=DEBUG" })

class ExpenseTrackerApplicationTests {

	@Autowired
	private Environment env;

	@Test
	void contextLoads() {
		System.out.println("Active profiles: " + String.join(", ", env.getActiveProfiles()));
		System.out.println("DataSource URL: " + env.getProperty("spring.datasource.url"));
		System.out.println("DataSource username: " + env.getProperty("spring.datasource.username"));
		System.out.println("Hibernate dialect: " + env.getProperty("spring.jpa.database-platform"));

	}

}