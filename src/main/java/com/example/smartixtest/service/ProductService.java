package com.example.smartixtest.service;

import com.example.smartixtest.dto.PageResponse;
import com.example.smartixtest.dto.ProductDto;
import java.math.BigDecimal;
import java.util.List;

public interface ProductService {
    
    // Импорт продуктов из внешнего API
    List<ProductDto> importProductsFromExternalApi();
    
    // CRUD операции
    ProductDto createProduct(ProductDto productDto);
    
    ProductDto getProductById(Long id);
    
    PageResponse<ProductDto> getAllProducts(int page, int size);
    
    ProductDto updateProduct(Long id, ProductDto productDto);
    
    void deleteProduct(Long id);
    
    // Получение продуктов с фильтрацией по цене
    PageResponse<ProductDto> getProductsByPriceRange(BigDecimal minPrice, BigDecimal maxPrice, int page, int size);
    
    // Получение списка уникальных категорий
    List<String> getAllUniqueCategories();
} 