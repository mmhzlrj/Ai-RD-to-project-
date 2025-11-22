package com.example.product.util;

import com.example.product.model.Product;

import java.util.List;
import java.util.ArrayList;

/**
 * 数据验证工具类
 */
public class ValidationUtil {
    
    /**
     * 验证产品数据是否有效
     * @param product 产品对象
     * @return 验证错误信息列表，如果没有错误则返回空列表
     */
    public static List<String> validateProduct(Product product) {
        List<String> errors = new ArrayList<>();
        
        if (product == null) {
            errors.add("产品对象不能为空");
            return errors;
        }
        
        // 验证必填字段
        if (product.getName() == null || product.getName().trim().isEmpty()) {
            errors.add("产品名称不能为空");
        }
        
        if (product.getPrice() == null) {
            errors.add("产品价格不能为空");
        } else if (product.getPrice() < 0) {
            errors.add("产品价格不能为负数");
        }
        
        if (product.getStock() == null) {
            errors.add("产品库存不能为空");
        } else if (product.getStock() < 0) {
            errors.add("产品库存不能为负数");
        }
        
        if (product.getCategory() == null || product.getCategory().trim().isEmpty()) {
            errors.add("产品分类不能为空");
        }
        
        return errors;
    }
    
    /**
     * 验证产品ID是否有效
     * @param id 产品ID
     * @return 是否有效
     */
    public static boolean isValidId(String id) {
        return id != null && !id.trim().isEmpty();
    }
}