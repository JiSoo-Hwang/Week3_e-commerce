package kr.jsh.ecommerce.order.presentation;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testCreateOrder() throws Exception {
        String requestBody = """
        {
          "products": [
            {
              "productId": 1,
              "quantity": 2
            },
            {
              "productId": 2,
              "quantity": 1
            }
          ],
          "customerId": 123
        }
        """;

        mockMvc.perform(post("/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.orderId").value(1001))
                .andExpect(jsonPath("$.totalAmount").value(35000));
    }
}
