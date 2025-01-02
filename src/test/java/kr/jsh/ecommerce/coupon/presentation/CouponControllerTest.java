package kr.jsh.ecommerce.coupon.presentation;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class CouponControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testIssueCoupon() throws Exception {
        mockMvc.perform(post("/customers/{customerId}/coupons", 123))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.couponId").value(1))
                .andExpect(jsonPath("$.status").value("ISSUED"));
    }

    @Test
    public void testGetCouponList() throws Exception {
        mockMvc.perform(get("/customers/{customerId}/coupons", 123))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].couponId").value(1))
                .andExpect(jsonPath("$[0].name").value("FIRST_COME"));
    }
}

