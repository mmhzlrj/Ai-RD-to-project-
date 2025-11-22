package com.example.product.service;

import com.example.product.model.Product;
import com.example.product.util.ValidationUtil;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.DeserializationFeature;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * 产品管理服务类
 * 实现产品的增删改查功能
 */
public class ProductService {
    
    // 使用内存存储模拟数据库
    private Map<String, Product> productDatabase = new ConcurrentHashMap<>();
    
    // 数据文件路径
    private static final String DATA_FILE = "products.json";
    
    // Jackson ObjectMapper 用于JSON序列化/反序列化
    private ObjectMapper objectMapper = new ObjectMapper();
    
    /**
     * 构造函数，初始化时加载数据
     */
    public ProductService() {
        // 配置ObjectMapper忽略未知属性
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        loadData();
    }
    
    /**
     * 从文件加载数据
     */
    private void loadData() {
        try {
            File file = new File(DATA_FILE);
            if (file.exists()) {
                List<Product> products = objectMapper.readValue(file, new TypeReference<List<Product>>() {});
                for (Product product : products) {
                    productDatabase.put(product.getId(), product);
                }
                System.out.println("成功加载 " + products.size() + " 个产品数据");
            } else {
                System.out.println("数据文件不存在，将创建新文件");
            }
        } catch (IOException e) {
            System.err.println("加载数据时出错: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * 保存数据到文件
     */
    private void saveData() {
        try {
            List<Product> products = new ArrayList<>(productDatabase.values());
            objectMapper.writeValue(new File(DATA_FILE), products);
        } catch (IOException e) {
            System.err.println("保存数据时出错: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * 添加产品
     * @param product 产品对象
     * @return 添加成功返回true，否则返回false
     */
    public boolean addProduct(Product product) {
        // 验证产品数据
        List<String> errors = ValidationUtil.validateProduct(product);
        if (!errors.isEmpty()) {
            System.err.println("产品数据验证失败: " + errors);
            return false;
        }
        
        // 检查ID是否已存在
        if (productDatabase.containsKey(product.getId())) {
            System.err.println("产品ID已存在: " + product.getId());
            return false;
        }
        
        productDatabase.put(product.getId(), product);
        saveData(); // 保存数据到文件
        return true;
    }
    
    /**
     * 根据ID获取产品
     * @param id 产品ID
     * @return 产品对象，如果不存在返回null
     */
    public Product getProductById(String id) {
        return productDatabase.get(id);
    }
    
    /**
     * 更新产品
     * @param product 产品对象
     * @return 更新成功返回true，否则返回false
     */
    public boolean updateProduct(Product product) {
        // 验证产品数据
        List<String> errors = ValidationUtil.validateProduct(product);
        if (!errors.isEmpty()) {
            System.err.println("产品数据验证失败: " + errors);
            return false;
        }
        
        // 检查产品是否存在
        if (!productDatabase.containsKey(product.getId())) {
            System.err.println("产品不存在: " + product.getId());
            return false;
        }
        
        productDatabase.put(product.getId(), product);
        saveData(); // 保存数据到文件
        return true;
    }
    
    /**
     * 删除产品
     * @param id 产品ID
     * @return 删除成功返回true，否则返回false
     */
    public boolean deleteProduct(String id) {
        if (!productDatabase.containsKey(id)) {
            System.err.println("产品不存在: " + id);
            return false;
        }
        
        productDatabase.remove(id);
        saveData(); // 保存数据到文件
        return true;
    }
    
    /**
     * 获取所有产品
     * @return 产品列表
     */
    public List<Product> getAllProducts() {
        return new ArrayList<>(productDatabase.values());
    }
    
    /**
     * 根据条件查询产品
     * @param name 产品名称（模糊匹配）
     * @param category 产品分类
     * @param minPrice 最低价格
     * @param maxPrice 最高价格
     * @return 符合条件的产品列表
     */
    public List<Product> searchProducts(String name, String category, Double minPrice, Double maxPrice) {
        return productDatabase.values().stream()
                .filter(product -> name == null || product.getName().toLowerCase().contains(name.toLowerCase()))
                .filter(product -> category == null || product.getCategory().equals(category))
                .filter(product -> minPrice == null || product.getPrice() >= minPrice)
                .filter(product -> maxPrice == null || product.getPrice() <= maxPrice)
                .collect(Collectors.toList());
    }
    
    /**
     * 批量更新产品价格
     * @param productIds 产品ID列表
     * @param adjustment 调整值（正数表示涨价，负数表示降价）
     * @return 成功更新的产品数量
     */
    public int batchUpdatePrice(List<String> productIds, double adjustment) {
        int count = 0;
        for (String id : productIds) {
            Product product = productDatabase.get(id);
            if (product != null && product.getPrice() != null) {
                product.setPrice(product.getPrice() + adjustment);
                count++;
            }
        }
        if (count > 0) {
            saveData(); // 保存数据到文件
        }
        return count;
    }
    
    /**
     * 按百分比批量更新产品价格
     * @param productIds 产品ID列表
     * @param percentage 调整百分比（正数表示涨价，负数表示降价）
     * @return 成功更新的产品数量
     */
    public int batchUpdatePriceByPercentage(List<String> productIds, double percentage) {
        int count = 0;
        for (String id : productIds) {
            Product product = productDatabase.get(id);
            if (product != null && product.getPrice() != null) {
                product.setPrice(product.getPrice() * (1 + percentage / 100));
                count++;
            }
        }
        if (count > 0) {
            saveData(); // 保存数据到文件
        }
        return count;
    }
    
    /**
     * 批量更新产品库存
     * @param productIds 产品ID列表
     * @param newStock 新库存数量
     * @return 成功更新的产品数量
     */
    public int batchUpdateStock(List<String> productIds, int newStock) {
        int count = 0;
        for (String id : productIds) {
            Product product = productDatabase.get(id);
            if (product != null) {
                product.setStock(newStock);
                count++;
            }
        }
        if (count > 0) {
            saveData(); // 保存数据到文件
        }
        return count;
    }
}