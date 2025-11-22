package com.example.product.service;

import com.example.product.model.ExcelProduct;
import com.example.product.util.ExcelUtil;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Excel导出服务类
 * 实现将产品数据导出到Excel文件的功能
 */
public class ExcelExportService {
    
    private ProductService productService;
    
    public ExcelExportService(ProductService productService) {
        this.productService = productService;
    }
    
    /**
     * 导出所有产品到Excel文件
     * @param filePath 输出文件路径
     * @return 导出是否成功
     */
    public boolean exportAllProducts(String filePath) {
        try {
            List<ExcelProduct> products = productService.getAllProducts().stream()
                    .map(this::convertToExcelProduct)
                    .collect(Collectors.toList());
            
            ExcelUtil.writeProductsToExcel(products, filePath);
            return true;
        } catch (IOException e) {
            System.err.println("导出Excel文件失败: " + e.getMessage());
            return false;
        } catch (Exception e) {
            System.err.println("导出过程中发生未知错误: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * 导出筛选后的产品到Excel文件
     * @param filePath 输出文件路径
     * @param name 产品名称（模糊匹配）
     * @param category 产品分类
     * @param minPrice 最低价格
     * @param maxPrice 最高价格
     * @return 导出是否成功
     */
    public boolean exportFilteredProducts(String filePath, String name, String category, 
                                         Double minPrice, Double maxPrice) {
        try {
            List<ExcelProduct> products = productService.searchProducts(name, category, minPrice, maxPrice)
                    .stream()
                    .map(this::convertToExcelProduct)
                    .collect(Collectors.toList());
            
            ExcelUtil.writeProductsToExcel(products, filePath);
            return true;
        } catch (IOException e) {
            System.err.println("导出Excel文件失败: " + e.getMessage());
            return false;
        } catch (Exception e) {
            System.err.println("导出过程中发生未知错误: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * 将Product对象转换为ExcelProduct对象
     * @param product Product对象
     * @return ExcelProduct对象
     */
    private ExcelProduct convertToExcelProduct(com.example.product.model.Product product) {
        ExcelProduct excelProduct = new ExcelProduct();
        excelProduct.setId(product.getId());
        excelProduct.setName(product.getName());
        excelProduct.setDescription(product.getDescription());
        excelProduct.setPrice(product.getPrice());
        excelProduct.setStock(product.getStock());
        excelProduct.setCategory(product.getCategory());
        return excelProduct;
    }
}