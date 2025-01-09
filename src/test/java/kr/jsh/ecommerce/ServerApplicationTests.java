package kr.jsh.ecommerce;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootTest
@EnableJpaRepositories(basePackages = "kr.jsh.ecommerce.product.infrastructure")
class ServerApplicationTests {

	@Test
	void contextLoads() {
	}

}
