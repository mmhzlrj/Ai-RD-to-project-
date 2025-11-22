package com.example.product.config;

import com.example.product.service.ExcelExportService;
import com.example.product.service.ExcelImportService;
import com.example.product.service.ProductService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 应用配置类
 * 配置服务层Bean
 */
@Configuration
public class AppConfig {
    
    /**
     * 配置ProductService Bean
     * @return ProductService实例
     */
    @Bean
    public ProductService productService() {
        return new ProductService();
    }
    
    /**
     * 配置ExcelImportService Bean
     * @return ExcelImportService实例
     */
    @Bean
    public ExcelImportService excelImportService() {
        return new ExcelImportService(productService());
    }
    
    /**
     * 配置ExcelExportService Bean
     * @return ExcelExportService实例
     */
    @Bean
    public ExcelExportService excelExportService() {
        return new ExcelExportService(productService());
    }
}