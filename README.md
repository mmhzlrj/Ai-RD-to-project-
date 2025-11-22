# 跨境电商产品管理系统

## 项目简介

这是一个用于跨境电商的产品管理系统，支持产品信息的增删改查、Excel批量导入导出以及批量数据处理功能。这是项目的第一个稳定版本（v1.0.0），包含了所有核心功能。

## 功能特性

### 产品管理
- 添加产品
- 编辑产品
- 删除产品
- 查询产品

### Excel导入
- 从Excel读取产品数据
- 批量导入到系统
- 数据验证

### Excel导出
- 导出所有产品
- 导出筛选后的产品
- 导出跨境电商标题

### 数据处理
- 价格批量调整
- 库存批量更新
- 标题批量生成

## 项目结构

```
product-data-manager/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/example/product/
│   │   │       ├── model/
│   │   │       │   ├── Product.java
│   │   │       │   └── ExcelProduct.java
│   │   │       ├── service/
│   │   │       │   ├── ProductService.java
│   │   │       │   ├── ExcelImportService.java
│   │   │       │   └── ExcelExportService.java
│   │   │       ├── util/
│   │   │       │   ├── ExcelUtil.java
│   │   │       │   └── ValidationUtil.java
│   │   │       ├── controller/
│   │   │       │   └── ProductController.java
│   │   │       ├── config/
│   │   │       │   └── WebConfig.java
│   │   │       └── Application.java
│   │   └── resources/
│   │       ├── data/
│   │       │   └── products.json
│   │       └── templates/
│   │           └── product_template.xlsx
├── excel/
│   ├── import/     # 待导入的Excel文件
│   └── export/     # 导出的Excel文件
├── pom.xml
└── README.md
```

## 开发环境配置

### 热加载配置

本项目集成了Spring Boot DevTools以支持热加载功能，提高开发效率。

#### 配置步骤：

1. Maven依赖已在pom.xml中配置：
   ```xml
   <dependency>
       <groupId>org.springframework.boot</groupId>
       <artifactId>spring-boot-devtools</artifactId>
       <scope>runtime</scope>
       <optional>true</optional>
   </dependency>
   ```

2. IntelliJ IDEA配置：
   - 打开设置(Settings) -> Build, Execution, Deployment -> Compiler
   - 启用"Build project automatically"
   - 使用快捷键Ctrl+Shift+A打开搜索框，搜索"Registry"
   - 找到并启用"compiler.automake.allow.when.app.running"选项

3. 使用方法：
   - 启动Spring Boot应用
   - 修改代码后，使用Ctrl+F9快捷键触发重新编译
   - 应用会自动重新加载变更内容

## 版本发布

### v1.0.0 (2025-11-25)
- 实现基础产品管理功能（增删改查）
- 实现Excel导入导出功能
- 实现批量数据处理功能
- 创建现代化Vue.js前端界面
- 完成所有单元测试和集成测试
- 修复所有已知问题，发布第一个稳定版本

## 部署到 GitHub

详细的部署指南请查看 [GITHUB_DEPLOYMENT_GUIDE.md](GITHUB_DEPLOYMENT_GUIDE.md) 文件。

## 使用说明

1. 克隆仓库：
   ```
   git clone <repository-url>
   ```

2. 进入项目目录：
   ```
   cd Day5_Ai_Requirement_document_to_project
   ```

3. 启动应用程序：
   ```
   mvn spring-boot:run
   ```

4. 访问应用程序：
   打开浏览器访问 http://localhost:8080

5. 使用功能：
   - 产品管理：添加、编辑、删除和查询产品
   - 产品搜索：根据名称、分类和价格范围搜索产品
   - 批量操作：批量调整产品价格和库存
   - Excel导入：上传Excel文件批量导入产品数据
   - Excel导出：将产品数据导出为Excel文件

6. 注意事项：
   - 确保端口8080未被其他应用程序占用
   - Excel文件格式需符合模板要求
   - 建议使用现代浏览器以获得最佳体验


## 贡献指南

欢迎对本项目做出贡献！请遵循以下步骤：

1. Fork本仓库
2. 创建功能分支 (`git checkout -b feature/AmazingFeature`)
3. 提交更改 (`git commit -m 'Add some amazing feature'`)
4. 推送到分支 (`git push origin feature/AmazingFeature`)
5. 开启一个Pull Request

### 开发环境设置

1. 确保已安装Java 8或更高版本
2. 确保已安装Maven 3.6或更高版本
3. 确保已安装Git
4. 克隆项目到本地
5. 导入到您喜欢的IDE中
6. 运行单元测试确保环境配置正确

### 代码规范

- 遵循Java编码规范
- 添加适当的单元测试
- 保持代码清晰和文档完整
