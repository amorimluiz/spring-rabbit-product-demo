package br.com.felipe.producer_rabbit.controllers;

import br.com.felipe.producer_rabbit.services.ProductService;
import br.com.felipe.producer_rabbit.services.StringService;
import dtos.ProductDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/product")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;

    @PostMapping
    public ResponseEntity<String> produce(@RequestBody ProductDTO product) {
        productService.createProduct(product);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
