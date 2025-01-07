package kr.jsh.ecommerce.product.presentation.controller;

import kr.jsh.ecommerce.product.application.ProductService;
import kr.jsh.ecommerce.product.presentation.dto.ProductListInfoResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ProductControllerTest {

    private MockMvc mockMvc;

    @Mock
    private ProductService productService;

    @InjectMocks
    private ProductController productController;

    @BeforeEach
    void setup(){
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(productController).build();
    }

    @Test
    void testGetInStcokProducts()throws Exception{
        //Given Mock 데이터 설정
        List<ProductListInfoResponse> mockProducts = List.of(
                new ProductListInfoResponse(1L,"아디다스아디오스프로4","아디다스",299000),
                new ProductListInfoResponse(2L,"나이키알파3","나이키",329000)
                );
        Mockito.when(productService.getInStockProducts()).thenReturn(mockProducts);

        //When GET 요청 수행
        mockMvc.perform(MockMvcRequestBuilders.get("/products/in-stock")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].productName").value("아디다스아디오스프로4"))
                .andExpect(jsonPath("$[1].brand").value("나이키"));

        //Then 서비스 호출 확인
        Mockito.verify(productService,Mockito.times(1)).getInStockProducts();
    }
}
