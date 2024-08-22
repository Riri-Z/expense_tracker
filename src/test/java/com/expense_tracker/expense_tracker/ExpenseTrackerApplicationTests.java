package com.expense_tracker.expense_tracker;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(properties = { "spring.datasource.url=jdbc:h2:mem:testdb",
		"spring.datasource.driverClassName=org.h2.Driver", "spring.datasource.username=sa",
		"spring.datasource.password=", "spring.jpa.database-platform=org.hibernate.dialect.H2Dialect",
		"jwt.secret=xxxxx", "logging.level.org.springframework=DEBUG" })
@ActiveProfiles("test")
class ExpenseTrackerApplicationTests {

	@Test
	void contextLoads() {
	}

}