package com.example.product.util;

import com.example.product.model.ExcelProduct;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Excel处理工具类
 */
public class ExcelUtil {
    
    /**
     * 从Excel文件中读取产品数据
     * @param filePath Excel文件路径
     * @return 产品列表
     * @throws IOException IO异常
     */
    public static List<ExcelProduct> readProductsFromExcel(String filePath) throws IOException {
        List<ExcelProduct> products = new ArrayList<>();
        
        FileInputStream fis = new FileInputStream(filePath);
        Workbook workbook = null;
        
        try {
            // 根据文件扩展名选择适当的Workbook实现
            if (filePath.endsWith(".xlsx")) {
                workbook = new XSSFWorkbook(fis);
            } else if (filePath.endsWith(".xls")) {
                workbook = new HSSFWorkbook(fis);
            } else {
                throw new IllegalArgumentException("不支持的文件格式: " + filePath);
            }
            
            Sheet sheet = workbook.getSheetAt(0); // 读取第一个工作表
            
            // 读取表头
            Row headerRow = sheet.getRow(0);
            if (headerRow == null) {
                throw new IllegalArgumentException("Excel文件格式错误：缺少表头");
            }
            
            // 从第二行开始读取数据（第一行为表头）
            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row != null) {
                    ExcelProduct product = readProductFromRow(row, i + 1);
                    if (product != null) {
                        products.add(product);
                    }
                }
            }
        } finally {
            if (workbook != null) {
                workbook.close();
            }
            fis.close();
        }
        
        return products;
    }
    
    /**
     * 从Excel行中读取产品数据
     * @param row Excel行
     * @param rowNumber 行号（用于错误提示）
     * @return 产品对象
     */
    private static ExcelProduct readProductFromRow(Row row, int rowNumber) {
        ExcelProduct product = new ExcelProduct();
        product.setRowNumber(rowNumber);
        
        try {
            // 读取各列数据 (假设列顺序为: ID, 名称, 描述, 价格, 库存, 分类)
            Cell idCell = row.getCell(0);
            Cell nameCell = row.getCell(1);
            Cell brandCell = row.getCell(2); // 品牌列
            Cell categoryCell = row.getCell(3); // 分类列
            Cell priceCell = row.getCell(4); // 价格列
            Cell stockCell = row.getCell(5); // 库存列
            Cell descriptionCell = row.getCell(6); // 描述列
            
            if (idCell != null) {
                product.setId(getCellValueAsString(idCell));
            }
            
            if (nameCell != null) {
                product.setName(getCellValueAsString(nameCell));
            }
            
            // 如果品牌存在，则将其添加到描述中
            if (brandCell != null) {
                String brand = getCellValueAsString(brandCell);
                if (brand != null && !brand.isEmpty()) {
                    product.setDescription((product.getDescription() != null ? product.getDescription() + " " : "") + "品牌: " + brand);
                }
            }
            
            if (categoryCell != null) {
                product.setCategory(getCellValueAsString(categoryCell));
            }
            
            if (priceCell != null) {
                Double price = getCellValueAsDouble(priceCell);
                product.setPrice(price);
            }
            
            if (stockCell != null) {
                Integer stock = getCellValueAsInteger(stockCell);
                product.setStock(stock);
            }
            
            if (descriptionCell != null) {
                String description = getCellValueAsString(descriptionCell);
                if (description != null && !description.isEmpty()) {
                    product.setDescription((product.getDescription() != null ? product.getDescription() + " " : "") + description);
                }
            }
            
            return product;
        } catch (Exception e) {
            // 发生异常时返回null，表示该行数据无效
            System.err.println("读取第" + rowNumber + "行数据时发生错误: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
    
    /**
     * 获取单元格的字符串值
     * @param cell 单元格
     * @return 字符串值
     */
    private static String getCellValueAsString(Cell cell) {
        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue();
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    return cell.getDateCellValue().toString();
                } else {
                    // 数字转字符串，去掉小数点后的.0
                    double numericValue = cell.getNumericCellValue();
                    if (numericValue == Math.floor(numericValue)) {
                        return String.valueOf((long) numericValue);
                    } else {
                        return String.valueOf(numericValue);
                    }
                }
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            case FORMULA:
                return cell.getCellFormula();
            default:
                return "";
        }
    }
    
    /**
     * 获取单元格的double值
     * @param cell 单元格
     * @return double值
     */
    private static Double getCellValueAsDouble(Cell cell) {
        switch (cell.getCellType()) {
            case NUMERIC:
                return cell.getNumericCellValue();
            case STRING:
                try {
                    return Double.parseDouble(cell.getStringCellValue());
                } catch (NumberFormatException e) {
                    return null;
                }
            default:
                return null;
        }
    }
    
    /**
     * 获取单元格的integer值
     * @param cell 单元格
     * @return integer值
     */
    private static Integer getCellValueAsInteger(Cell cell) {
        switch (cell.getCellType()) {
            case NUMERIC:
                return (int) cell.getNumericCellValue();
            case STRING:
                try {
                    return Integer.parseInt(cell.getStringCellValue());
                } catch (NumberFormatException e) {
                    return null;
                }
            default:
                return null;
        }
    }
    
    /**
     * 将产品数据写入Excel文件
     * @param products 产品列表
     * @param filePath 输出文件路径
     * @throws IOException IO异常
     */
    public static void writeProductsToExcel(List<ExcelProduct> products, String filePath) throws IOException {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("产品数据");
        
        // 创建表头
        Row headerRow = sheet.createRow(0);
        String[] headers = {"ID", "名称", "描述", "价格", "库存", "分类"};
        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
        }
        
        // 写入数据
        for (int i = 0; i < products.size(); i++) {
            ExcelProduct product = products.get(i);
            Row row = sheet.createRow(i + 1);
            
            row.createCell(0).setCellValue(product.getId());
            row.createCell(1).setCellValue(product.getName());
            row.createCell(2).setCellValue(product.getDescription());
            row.createCell(3).setCellValue(product.getPrice());
            row.createCell(4).setCellValue(product.getStock());
            row.createCell(5).setCellValue(product.getCategory());
        }
        
        // 自动调整列宽
        for (int i = 0; i < headers.length; i++) {
            sheet.autoSizeColumn(i);
        }
        
        // 写入文件
        FileOutputStream fos = new FileOutputStream(filePath);
        try {
            workbook.write(fos);
        } finally {
            fos.close();
            workbook.close();
        }
    }
}