package com.ao.banca;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class BancaApplicationTests {

	@Test
	void contextLoads() {
		int number=1;
		assertEquals(1,number,0.3);
	}

}
