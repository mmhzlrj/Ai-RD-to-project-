package com.example.product.service;

import com.example.product.model.ExcelProduct;
import com.example.product.util.ExcelUtil;
import com.example.product.util.ValidationUtil;

import java.io.IOException;
import java.util.List;

/**
 * Excel导入服务类
 * 实现从Excel文件读取产品数据并导入系统的功能
 */
public class ExcelImportService {
    
    private ProductService productService;
    
    public ExcelImportService(ProductService productService) {
        this.productService = productService;
    }
    
    /**
     * 从Excel文件导入产品数据
     * @param filePath Excel文件路径
     * @return 导入结果报告
     */
    public ImportResult importProductsFromExcel(String filePath) {
        ImportResult result = new ImportResult();
        
        try {
            // 从Excel文件读取产品数据
            List<ExcelProduct> excelProducts = ExcelUtil.readProductsFromExcel(filePath);
            result.setTotalRecords(excelProducts.size());
            
            // 逐条处理产品数据
            for (ExcelProduct excelProduct : excelProducts) {
                try {
                    // 数据验证
                    List<String> errors = ValidationUtil.validateProduct(excelProduct);
                    if (!errors.isEmpty()) {
                        result.addFailure(String.format("第%d行数据验证失败: %s", 
                                excelProduct.getRowNumber(), String.join(", ", errors)));
                        continue;
                    }
                    
                    // 导入产品数据
                    boolean success = productService.addProduct(excelProduct);
                    if (success) {
                        result.incrementSuccess();
                    } else {
                        result.addFailure(String.format("第%d行产品导入失败: 产品ID已存在或数据错误", 
                                excelProduct.getRowNumber()));
                    }
                } catch (Exception e) {
                    result.addFailure(String.format("第%d行数据处理异常: %s", 
                            excelProduct.getRowNumber(), e.getMessage()));
                }
            }
        } catch (IOException e) {
            result.setErrorMessage("读取Excel文件失败: " + e.getMessage());
        } catch (Exception e) {
            result.setErrorMessage("导入过程中发生未知错误: " + e.getMessage());
        }
        
        return result;
    }
    
    /**
     * 导入结果类
     */
    public static class ImportResult {
        private int totalRecords = 0;
        private int successCount = 0;
        private StringBuilder failures = new StringBuilder();
        private String errorMessage = null;
        
        public int getTotalRecords() {
            return totalRecords;
        }
        
        public void setTotalRecords(int totalRecords) {
            this.totalRecords = totalRecords;
        }
        
        public int getSuccessCount() {
            return successCount;
        }
        
        public void incrementSuccess() {
            this.successCount++;
        }
        
        public String getFailures() {
            return failures.toString();
        }
        
        public void addFailure(String failure) {
            if (failures.length() > 0) {
                failures.append("\n");
            }
            failures.append(failure);
        }
        
        public String getErrorMessage() {
            return errorMessage;
        }
        
        public void setErrorMessage(String errorMessage) {
            this.errorMessage = errorMessage;
        }
        
        public int getFailureCount() {
            return totalRecords - successCount;
        }
        
        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("导入结果报告:\n");
            sb.append("总记录数: ").append(totalRecords).append("\n");
            sb.append("成功导入: ").append(successCount).append("\n");
            sb.append("导入失败: ").append(getFailureCount()).append("\n");
            
            if (errorMessage != null && !errorMessage.isEmpty()) {
                sb.append("错误信息: ").append(errorMessage).append("\n");
            }
            
            if (failures.length() > 0) {
                sb.append("详细失败信息:\n").append(failures.toString()).append("\n");
            }
            
            return sb.toString();
        }
    }
}