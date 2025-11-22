package com.example.product.controller;

import com.example.product.model.Product;
import com.example.product.service.ProductService;
import com.example.product.service.ExcelImportService;
import com.example.product.service.ExcelExportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 产品管理控制器
 * 提供REST API接口用于产品管理
 */
@RestController
@RequestMapping("/api/products")
public class ProductController {
    
    @Autowired
    private ProductService productService;
    
    @Autowired
    private ExcelImportService excelImportService;
    
    @Autowired
    private ExcelExportService excelExportService;
    
    /**
     * 添加产品
     * @param product 产品对象
     * @return 添加结果
     */
    @PostMapping
    public ResponseEntity<String> addProduct(@RequestBody Product product) {
        boolean result = productService.addProduct(product);
        if (result) {
            return ResponseEntity.ok("产品添加成功");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("产品添加失败");
        }
    }
    
    /**
     * 根据ID获取产品
     * @param id 产品ID
     * @return 产品对象
     */
    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable String id) {
        Product product = productService.getProductById(id);
        if (product != null) {
            return ResponseEntity.ok(product);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    
    /**
     * 更新产品
     * @param product 产品对象
     * @return 更新结果
     */
    @PutMapping
    public ResponseEntity<String> updateProduct(@RequestBody Product product) {
        boolean result = productService.updateProduct(product);
        if (result) {
            return ResponseEntity.ok("产品更新成功");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("产品更新失败");
        }
    }
    
    /**
     * 删除产品
     * @param id 产品ID
     * @return 删除结果
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteProduct(@PathVariable String id) {
        boolean result = productService.deleteProduct(id);
        if (result) {
            return ResponseEntity.ok("产品删除成功");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("产品删除失败，产品不存在");
        }
    }
    
    /**
     * 获取所有产品
     * @return 产品列表
     */
    @GetMapping
    public ResponseEntity<List<Product>> getAllProducts() {
        List<Product> products = productService.getAllProducts();
        return ResponseEntity.ok(products);
    }
    
    /**
     * 根据条件查询产品
     * @param name 产品名称（模糊匹配）
     * @param category 产品分类
     * @param minPrice 最低价格
     * @param maxPrice 最高价格
     * @return 符合条件的产品列表
     */
    @GetMapping("/search")
    public ResponseEntity<List<Product>> searchProducts(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) Double minPrice,
            @RequestParam(required = false) Double maxPrice) {
        List<Product> products = productService.searchProducts(name, category, minPrice, maxPrice);
        return ResponseEntity.ok(products);
    }
    
    /**
     * 批量更新产品价格
     * @param request 价格更新请求
     * @return 更新结果
     */
    @PutMapping("/batch/price")
    public ResponseEntity<String> batchUpdatePrice(@RequestBody BatchPriceUpdateRequest request) {
        int count = productService.batchUpdatePrice(request.getProductIds(), request.getAdjustment());
        return ResponseEntity.ok("成功更新" + count + "个产品的价格");
    }
    
    /**
     * 按百分比批量更新产品价格
     * @param request 价格更新请求
     * @return 更新结果
     */
    @PutMapping("/batch/price/percentage")
    public ResponseEntity<String> batchUpdatePriceByPercentage(@RequestBody BatchPriceUpdateRequest request) {
        int count = productService.batchUpdatePriceByPercentage(request.getProductIds(), request.getAdjustment());
        return ResponseEntity.ok("成功更新" + count + "个产品的价格");
    }
    
    /**
     * 批量更新产品库存
     * @param request 库存更新请求
     * @return 更新结果
     */
    @PutMapping("/batch/stock")
    public ResponseEntity<String> batchUpdateStock(@RequestBody BatchStockUpdateRequest request) {
        int count = productService.batchUpdateStock(request.getProductIds(), request.getNewStock());
        return ResponseEntity.ok("成功更新" + count + "个产品的库存");
    }
    
    /**
     * 从Excel导入产品数据
     * @param file Excel文件
     * @return 导入结果
     */
    @PostMapping("/import")
    public ResponseEntity<String> importProductsFromExcel(@RequestParam("file") MultipartFile file) {
        try {
            // 检查文件是否为空
            if (file.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("导入失败: 文件为空");
            }
            
            // 创建临时文件
            java.io.File tempFile = java.io.File.createTempFile("import_", "_" + file.getOriginalFilename());
            file.transferTo(tempFile);
            
            // 导入数据
            ExcelImportService.ImportResult result = excelImportService.importProductsFromExcel(tempFile.getAbsolutePath());
            
            // 删除临时文件
            tempFile.delete();
            
            return ResponseEntity.ok(result.toString());
        } catch (Exception e) {
            e.printStackTrace(); // 打印详细错误信息
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("导入失败: " + e.getMessage());
        }
    }
    
    /**
     * 导出所有产品到Excel
     * @param response HTTP响应
     */
    @GetMapping("/export/all")
    public void exportAllProducts(HttpServletResponse response) {
        try {
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setHeader("Content-Disposition", "attachment; filename=products.xlsx");
            
            String fileName = "excel/export/products_" + System.currentTimeMillis() + ".xlsx";
            boolean success = excelExportService.exportAllProducts(fileName);
            
            if (success) {
                // 读取文件并写入响应
                java.nio.file.Files.copy(
                    java.nio.file.Paths.get(fileName),
                    response.getOutputStream()
                );
                response.getOutputStream().flush();
                
                // 删除临时文件
                java.nio.file.Files.delete(java.nio.file.Paths.get(fileName));
            }
        } catch (Exception e) {
            try {
                response.getWriter().write("导出失败: " + e.getMessage());
                response.getWriter().flush();
            } catch (java.io.IOException ioException) {
                ioException.printStackTrace();
            }
        }
    }
    
    /**
     * 导出筛选后的产品到Excel
     * @param name 产品名称（模糊匹配）
     * @param category 产品分类
     * @param minPrice 最低价格
     * @param maxPrice 最高价格
     * @param response HTTP响应
     */
    @GetMapping("/export/filtered")
    public void exportFilteredProducts(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) Double minPrice,
            @RequestParam(required = false) Double maxPrice,
            HttpServletResponse response) {
        try {
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setHeader("Content-Disposition", "attachment; filename=filtered_products.xlsx");
            
            String fileName = "excel/export/filtered_products_" + System.currentTimeMillis() + ".xlsx";
            boolean success = excelExportService.exportFilteredProducts(fileName, name, category, minPrice, maxPrice);
            
            if (success) {
                // 读取文件并写入响应
                java.nio.file.Files.copy(
                    java.nio.file.Paths.get(fileName),
                    response.getOutputStream()
                );
                response.getOutputStream().flush();
                
                // 删除临时文件
                java.nio.file.Files.delete(java.nio.file.Paths.get(fileName));
            }
        } catch (Exception e) {
            try {
                response.getWriter().write("导出失败: " + e.getMessage());
                response.getWriter().flush();
            } catch (java.io.IOException ioException) {
                ioException.printStackTrace();
            }
        }
    }
    
    /**
     * 价格批量更新请求类
     */
    public static class BatchPriceUpdateRequest {
        private List<String> productIds;
        private double adjustment;
        
        public List<String> getProductIds() {
            return productIds;
        }
        
        public void setProductIds(List<String> productIds) {
            this.productIds = productIds;
        }
        
        public double getAdjustment() {
            return adjustment;
        }
        
        public void setAdjustment(double adjustment) {
            this.adjustment = adjustment;
        }
    }
    
    /**
     * 库存批量更新请求类
     */
    public static class BatchStockUpdateRequest {
        private List<String> productIds;
        private int newStock;
        
        public List<String> getProductIds() {
            return productIds;
        }
        
        public void setProductIds(List<String> productIds) {
            this.productIds = productIds;
        }
        
        public int getNewStock() {
            return newStock;
        }
        
        public void setNewStock(int newStock) {
            this.newStock = newStock;
        }
    }
}