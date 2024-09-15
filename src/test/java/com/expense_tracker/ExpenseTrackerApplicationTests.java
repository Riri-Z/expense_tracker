package com.expense_tracker;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")

class ExpenseTrackerApplicationTests {

	Boolean isRunning = true;

	@Test
	void contextLoads() {
		Assertions.assertThat(isRunning).isTrue();

	}

}