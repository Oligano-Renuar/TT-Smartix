package com.example.smartixtest.controller;

import com.example.smartixtest.dto.PageResponse;
import com.example.smartixtest.dto.ProductDto;
import com.example.smartixtest.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
@Tag(name = "Товары", description = "Операции с товарами")
public class ProductController {
    
    private final ProductService productService;
    
    @PostMapping("/import")
    @Operation(summary = "Импорт товаров из внешнего API")
    public ResponseEntity<List<ProductDto>> importProducts() {
        List<ProductDto> importedProducts = productService.importProductsFromExternalApi();
        return ResponseEntity.status(HttpStatus.CREATED).body(importedProducts);
    }
    
    @PostMapping
    @Operation(summary = "Создание нового товара")
    public ResponseEntity<ProductDto> createProduct(@Valid @RequestBody ProductDto productDto) {
        ProductDto createdProduct = productService.createProduct(productDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdProduct);
    }
    
    @GetMapping("/{id}")
    @Operation(summary = "Получение товара по ID")
    public ResponseEntity<ProductDto> getProductById(
            @Parameter(description = "ID товара") 
            @PathVariable Long id) {
        ProductDto productDto = productService.getProductById(id);
        return ResponseEntity.ok(productDto);
    }
    
    @GetMapping
    @Operation(summary = "Получение списка всех товаров с пагинацией")
    public ResponseEntity<PageResponse<ProductDto>> getAllProducts(
            @Parameter(description = "Номер страницы (от 0)") 
            @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Размер страницы") 
            @RequestParam(defaultValue = "10") int size) {
        PageResponse<ProductDto> pageResponse = productService.getAllProducts(page, size);
        return ResponseEntity.ok(pageResponse);
    }
    
    @PutMapping("/{id}")
    @Operation(summary = "Обновление существующего товара")
    public ResponseEntity<ProductDto> updateProduct(
            @Parameter(description = "ID товара") 
            @PathVariable Long id,
            @Valid @RequestBody ProductDto productDto) {
        ProductDto updatedProduct = productService.updateProduct(id, productDto);
        return ResponseEntity.ok(updatedProduct);
    }
    
    @DeleteMapping("/{id}")
    @Operation(summary = "Удаление товара")
    public ResponseEntity<Void> deleteProduct(
            @Parameter(description = "ID товара") 
            @PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }
    
    @GetMapping("/filter")
    @Operation(summary = "Фильтрация товаров по диапазону цен")
    public ResponseEntity<PageResponse<ProductDto>> getProductsByPriceRange(
            @Parameter(description = "Минимальная цена") 
            @RequestParam BigDecimal minPrice,
            @Parameter(description = "Максимальная цена") 
            @RequestParam BigDecimal maxPrice,
            @Parameter(description = "Номер страницы (от 0)") 
            @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Размер страницы") 
            @RequestParam(defaultValue = "10") int size) {
        PageResponse<ProductDto> pageResponse = 
                productService.getProductsByPriceRange(minPrice, maxPrice, page, size);
        return ResponseEntity.ok(pageResponse);
    }
    
    @GetMapping("/categories")
    @Operation(summary = "Получение списка уникальных категорий товаров")
    public ResponseEntity<List<String>> getAllUniqueCategories() {
        List<String> categories = productService.getAllUniqueCategories();
        return ResponseEntity.ok(categories);
    }
} 