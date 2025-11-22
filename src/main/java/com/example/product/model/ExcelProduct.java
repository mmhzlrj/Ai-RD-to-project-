package com.example.product.model;

/**
 * Excel产品实体类
 * 用于处理从Excel导入的产品数据
 */
public class ExcelProduct extends Product {
    
    // 可以添加Excel特有的字段或处理逻辑
    // 例如行号，用于标识在Excel中的位置，便于错误提示
    
    private int rowNumber; // 行号，方便错误定位
    
    public ExcelProduct() {
        super();
    }
    
    public ExcelProduct(String id, String name, String description, Double price, Integer stock, String category) {
        super(id, name, description, price, stock, category);
    }
    
    public int getRowNumber() {
        return rowNumber;
    }
    
    public void setRowNumber(int rowNumber) {
        this.rowNumber = rowNumber;
    }
    
    @Override
    public String toString() {
        return "ExcelProduct{" +
                "rowNumber=" + rowNumber +
                "} " + super.toString();
    }
}