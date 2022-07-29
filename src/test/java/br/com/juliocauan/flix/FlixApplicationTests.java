package br.com.juliocauan.flix;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Order(1)
class FlixApplicationTests {

	@Test
	@DisplayName("Testa estado da aplicação")
	void contextLoads() {
	}

}
