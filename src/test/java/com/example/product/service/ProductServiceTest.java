package com.example.product.service;

import com.example.product.model.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

/**
 * ProductService单元测试类
 */
class ProductServiceTest {
    
    private ProductService productService;
    
    @BeforeEach
    void setUp() {
        productService = new ProductService();
    }
    
    @Test
    void testAddProduct() {
        Product product = new Product("1", "测试产品", "测试产品描述", 100.0, 10, "测试分类");
        boolean result = productService.addProduct(product);
        assertTrue(result);
    }
    
    @Test
    void testAddProductWithInvalidData() {
        Product product = new Product("1", "", "测试产品描述", 100.0, 10, "测试分类");
        boolean result = productService.addProduct(product);
        assertFalse(result);
    }
    
    @Test
    void testGetProductById() {
        Product product = new Product("1", "测试产品", "测试产品描述", 100.0, 10, "测试分类");
        productService.addProduct(product);
        
        Product result = productService.getProductById("1");
        assertNotNull(result);
        assertEquals("测试产品", result.getName());
    }
    
    @Test
    void testUpdateProduct() {
        Product product = new Product("1", "测试产品", "测试产品描述", 100.0, 10, "测试分类");
        productService.addProduct(product);
        
        product.setName("更新后的产品名称");
        boolean result = productService.updateProduct(product);
        assertTrue(result);
        
        Product updatedProduct = productService.getProductById("1");
        assertEquals("更新后的产品名称", updatedProduct.getName());
    }
    
    @Test
    void testDeleteProduct() {
        Product product = new Product("1", "测试产品", "测试产品描述", 100.0, 10, "测试分类");
        productService.addProduct(product);
        
        boolean result = productService.deleteProduct("1");
        assertTrue(result);
        
        Product deletedProduct = productService.getProductById("1");
        assertNull(deletedProduct);
    }
    
    @Test
    void testGetAllProducts() {
        Product product1 = new Product("1", "测试产品1", "测试产品描述1", 100.0, 10, "测试分类");
        Product product2 = new Product("2", "测试产品2", "测试产品描述2", 200.0, 20, "测试分类");
        productService.addProduct(product1);
        productService.addProduct(product2);
        
        List<Product> products = productService.getAllProducts();
        assertEquals(2, products.size());
    }
    
    @Test
    void testSearchProducts() {
        Product product1 = new Product("1", "苹果手机", "苹果手机描述", 100.0, 10, "手机");
        Product product2 = new Product("2", "华为手机", "华为手机描述", 200.0, 20, "手机");
        Product product3 = new Product("3", "苹果电脑", "苹果电脑描述", 300.0, 30, "电脑");
        productService.addProduct(product1);
        productService.addProduct(product2);
        productService.addProduct(product3);
        
        List<Product> results = productService.searchProducts("苹果", null, null, null);
        assertEquals(2, results.size());
        
        results = productService.searchProducts(null, "手机", null, null);
        assertEquals(2, results.size());
        
        results = productService.searchProducts(null, null, 150.0, 250.0);
        assertEquals(1, results.size());
    }
    
    @Test
    void testBatchUpdatePrice() {
        Product product1 = new Product("1", "测试产品1", "测试产品描述1", 100.0, 10, "测试分类");
        Product product2 = new Product("2", "测试产品2", "测试产品描述2", 200.0, 20, "测试分类");
        productService.addProduct(product1);
        productService.addProduct(product2);
        
        new java.util.ArrayList<String>(java.util.Arrays.asList("1", "2"));
        int count = productService.batchUpdatePrice(java.util.Arrays.asList("1", "2"), 50.0);
        assertEquals(2, count);
        
        Product updatedProduct1 = productService.getProductById("1");
        Product updatedProduct2 = productService.getProductById("2");
        assertEquals(150.0, updatedProduct1.getPrice());
        assertEquals(250.0, updatedProduct2.getPrice());
    }
    
    @Test
    void testBatchUpdateStock() {
        Product product1 = new Product("1", "测试产品1", "测试产品描述1", 100.0, 10, "测试分类");
        Product product2 = new Product("2", "测试产品2", "测试产品描述2", 200.0, 20, "测试分类");
        productService.addProduct(product1);
        productService.addProduct(product2);
        
        int count = productService.batchUpdateStock(java.util.Arrays.asList("1", "2"), 50);
        assertEquals(2, count);
        
        Product updatedProduct1 = productService.getProductById("1");
        Product updatedProduct2 = productService.getProductById("2");
        assertEquals(50, updatedProduct1.getStock());
        assertEquals(50, updatedProduct2.getStock());
    }
}