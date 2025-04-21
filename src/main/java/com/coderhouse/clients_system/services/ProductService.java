package com.coderhouse.clients_system.services;

import com.coderhouse.clients_system.entities.Product;
import com.coderhouse.clients_system.repositories.ProductRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {
    private static final Logger log = LoggerFactory.getLogger(InvoiceService.class);

    @Autowired
    private ProductRepository productRepository;

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Optional<Product> getProductById(Integer id) {
        return productRepository.findById(id);
    }

    public Product saveProduct(Product product) {
        log.info("Saving product: {}");
        Product savedProduct = productRepository.save(product);
        log.info("Product saved with ID: {}");
        return savedProduct;
    }

    public Product updateProduct(Integer id, Product updatedProduct) {
        Optional<Product> existingProduct = productRepository.findById(id);

        if (existingProduct.isPresent()) {
            Product product = existingProduct.get();
            product.setDescription(updatedProduct.getDescription());
            product.setCode(updatedProduct.getCode());
            product.setStock(updatedProduct.getStock());
            product.setPrice(updatedProduct.getPrice());
            return productRepository.save(product);
        } else {
            throw new RuntimeException("Product not found with ID: " + id);
        }
    }

    public void deleteProduct(Integer id) {
        productRepository.deleteById(id);
    }

    public boolean reduceStock(Integer productId, int quantity) {
        Optional<Product> optionalProduct = productRepository.findById(productId);
        if (optionalProduct.isPresent()) {
            Product product = optionalProduct.get();
            if (product.getStock() >= quantity) {
                product.setStock(product.getStock() - quantity);
                productRepository.save(product);
                return true;
            }
        }
        return false;
    }

}
