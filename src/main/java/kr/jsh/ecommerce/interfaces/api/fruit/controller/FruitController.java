package kr.jsh.ecommerce.interfaces.api.fruit.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.jsh.ecommerce.application.fruit.FindFruitByIdUseCase;
import kr.jsh.ecommerce.application.fruit.GetFruitsUseCase;
import kr.jsh.ecommerce.base.dto.response.BaseResponsePage;
import kr.jsh.ecommerce.domain.fruit.Fruit;
import kr.jsh.ecommerce.interfaces.api.fruit.dto.FruitResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Item API",description = "상품 API")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/items")
public class FruitController {

    private final GetFruitsUseCase getFruitsUseCase;
    private final FindFruitByIdUseCase findFruitByIdUseCase;

    @Operation
    @GetMapping
    public ResponseEntity<BaseResponsePage> getItems(
            @PageableDefault(page = 0, size = 9) Pageable pageable
    ){
        Page<FruitResponse> fruits = getFruitsUseCase.getFruits(pageable);

        return ResponseEntity.ok(new BaseResponsePage(fruits));
    }

    @Operation(summary = "ID로 특정 과일 조회")
    @GetMapping("/{fruitId}")
    public ResponseEntity<FruitResponse> getFruitById(@PathVariable Long fruitId) {
        FruitResponse fruitResponse = findFruitByIdUseCase.findFruitById(fruitId);
        return ResponseEntity.ok(fruitResponse);
    }

}
