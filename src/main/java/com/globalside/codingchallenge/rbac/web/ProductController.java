package com.globalside.codingchallenge.rbac.web;

import com.globalside.codingchallenge.rbac.api.ProductApi;
import com.globalside.codingchallenge.rbac.model.ProductDto;
import com.globalside.codingchallenge.rbac.service.ProductService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ProductController implements ProductApi {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @Override
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public List<ProductDto> getAllProducts() {
        return productService.getAllProducts();
    }

    @Override
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ProductDto getProductById(Integer id) {
        return productService.getProductById(id);
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public ProductDto createProduct(ProductDto productDto) {
        return productService.createProduct(productDto);
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public ProductDto updateProduct(Integer id, ProductDto productDto) {
        return productService.updateProduct(id, productDto);
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteProduct(Integer id) {
        productService.deleteProduct(id);
    }
}
