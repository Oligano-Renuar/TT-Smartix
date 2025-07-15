package com.example.smartixtest.service.impl;

import com.example.smartixtest.dto.PageResponse;
import com.example.smartixtest.dto.ProductDto;
import com.example.smartixtest.exception.ResourceNotFoundException;
import com.example.smartixtest.mapper.ProductMapper;
import com.example.smartixtest.model.Category;
import com.example.smartixtest.model.Product;
import com.example.smartixtest.repository.CategoryRepository;
import com.example.smartixtest.repository.ProductRepository;
import com.example.smartixtest.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ProductMapper productMapper;
    private final RestTemplate restTemplate;
    
    @Value("${external.api.url:https://fakestoreapi.com/products}")
    private String externalApiUrl;
    
    @Override
    @Transactional
    public List<ProductDto> importProductsFromExternalApi() {
        log.info("Импортируем товары из внешнего API: {}", externalApiUrl);
        
        ResponseEntity<List<ProductDto>> response = restTemplate.exchange(
                externalApiUrl,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<ProductDto>>() {}
        );
        
        List<ProductDto> productDtos = response.getBody();
        if (productDtos == null || productDtos.isEmpty()) {
            log.warn("Внешний API вернул пустой список товаров");
            return List.of();
        }
        
        List<Product> products = productDtos.stream()
                .map(dto -> {
                    Product product = productMapper.toEntity(dto);
                    if (product.getCategory() != null) {
                        // Ищем категорию в БД или создаем новую
                        Category category = categoryRepository
                                .findByName(product.getCategory().getName())
                                .orElse(product.getCategory());
                        product.setCategory(category);
                    }
                    return product;
                })
                .collect(Collectors.toList());
        
        products = productRepository.saveAll(products);
        log.info("Импортировано {} товаров из внешнего API", products.size());
        
        return products.stream()
                .map(productMapper::toDto)
                .collect(Collectors.toList());
    }
    
    @Override
    @Transactional
    public ProductDto createProduct(ProductDto productDto) {
        log.info("Создаем новый товар: {}", productDto.getTitle());
        
        Product product = productMapper.toEntity(productDto);
        
        // Обрабатываем категорию
        if (product.getCategory() != null) {
            String categoryName = product.getCategory().getName();
            Category category = categoryRepository
                    .findByName(categoryName)
                    .orElse(new Category(null, categoryName));
            product.setCategory(category);
        }
        
        Product savedProduct = productRepository.save(product);
        return productMapper.toDto(savedProduct);
    }
    
    @Override
    public ProductDto getProductById(Long id) {
        log.info("Получаем товар по ID: {}", id);
        
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Товар", "id", id));
        
        return productMapper.toDto(product);
    }
    
    @Override
    public PageResponse<ProductDto> getAllProducts(int page, int size) {
        log.info("Получаем список всех товаров, страница: {}, размер: {}", page, size);
        
        Pageable pageable = PageRequest.of(page, size);
        Page<Product> productPage = productRepository.findAll(pageable);
        
        return createPageResponse(productPage);
    }
    
    @Override
    @Transactional
    public ProductDto updateProduct(Long id, ProductDto productDto) {
        log.info("Обновляем товар с ID: {}", id);
        
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Товар", "id", id));
        
        // Обновляем поля товара
        productMapper.updateEntityFromDto(productDto, product);
        
        // Обрабатываем категорию, если она была изменена
        if (productDto.getCategory() != null) {
            String newCategoryName = productDto.getCategory();
            
            // Если текущая категория отличается от новой
            if (product.getCategory() == null || 
                    !product.getCategory().getName().equals(newCategoryName)) {
                
                Category category = categoryRepository
                        .findByName(newCategoryName)
                        .orElse(new Category(null, newCategoryName));
                
                product.setCategory(category);
            }
        }
        
        Product updatedProduct = productRepository.save(product);
        return productMapper.toDto(updatedProduct);
    }
    
    @Override
    @Transactional
    public void deleteProduct(Long id) {
        log.info("Удаляем товар с ID: {}", id);
        
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Товар", "id", id));
        
        productRepository.delete(product);
    }
    
    @Override
    public PageResponse<ProductDto> getProductsByPriceRange(BigDecimal minPrice, BigDecimal maxPrice, int page, int size) {
        log.info("Получаем товары в ценовом диапазоне от {} до {}, страница: {}, размер: {}", 
                minPrice, maxPrice, page, size);
        
        Pageable pageable = PageRequest.of(page, size);
        Page<Product> productPage = productRepository.findByPriceBetween(minPrice, maxPrice, pageable);
        
        return createPageResponse(productPage);
    }
    
    @Override
    public List<String> getAllUniqueCategories() {
        log.info("Получаем список уникальных категорий");
        return productRepository.findAllUniqueCategories();
    }
    
    private PageResponse<ProductDto> createPageResponse(Page<Product> productPage) {
        List<ProductDto> productDtos = productPage.getContent().stream()
                .map(productMapper::toDto)
                .collect(Collectors.toList());
        
        return PageResponse.<ProductDto>builder()
                .content(productDtos)
                .pageNo(productPage.getNumber())
                .pageSize(productPage.getSize())
                .totalElements(productPage.getTotalElements())
                .totalPages(productPage.getTotalPages())
                .last(productPage.isLast())
                .build();
    }
} 