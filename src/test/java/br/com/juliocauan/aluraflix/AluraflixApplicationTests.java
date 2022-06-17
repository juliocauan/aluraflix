package br.com.juliocauan.aluraflix;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Order(0)
class AluraflixApplicationTests {

	@Test
	@DisplayName("Testa estado da aplicação")
	void contextLoads() {
	}

}
