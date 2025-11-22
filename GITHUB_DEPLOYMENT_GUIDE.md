# GitHub 部署指南

## 前置条件

1. 确保已安装 Git
2. 确保已注册 GitHub 账户
3. 确保项目已通过所有测试

## 安装 Git（如果尚未安装）

### Windows 系统：

1. 访问 https://git-scm.com/downloads
2. 下载 Windows 版本的 Git
3. 运行安装程序，使用默认设置即可

### 验证安装：

打开命令提示符或 PowerShell，输入以下命令：

```bash
git --version
```

如果显示版本号，则说明安装成功。

## 配置 Git

在推送代码之前，需要配置 Git 用户信息：

```bash
git config --global user.name "Your Name"
git config --global user.email "your.email@example.com"
```

## 初始化本地 Git 仓库

在项目根目录下执行以下命令：

```bash
cd C:\Users\Jet\Desktop\JetBrainProjet\Day5_Ai_Requirement_document_to_project
git init
git add .
git commit -m "Initial commit: First stable version v1.0.0"
```

## 在 GitHub 上创建远程仓库

1. 登录 GitHub 账户
2. 点击右上角的 "+" 号，选择 "New repository"
3. 仓库名称建议使用：`cross-border-ecommerce-product-manager`
4. 描述可以填写：`跨境电商产品管理系统 - 第一个稳定版本`
5. 选择 Public（公开）或 Private（私有）
6. 不要初始化 README、.gitignore 或许可证
7. 点击 "Create repository"

## 推送代码到 GitHub

创建仓库后，GitHub 会显示如何推送现有仓库的说明。按照以下步骤操作：

1. 在项目根目录下添加远程仓库地址（将 `username` 替换为您的 GitHub 用户名）：

```bash
git remote add origin https://github.com/username/cross-border-ecommerce-product-manager.git
```

2. 推送代码到 GitHub：

```bash
git push -u origin main
```

如果遇到 "main" 分支不存在的问题，可以使用以下命令：

```bash
git push -u origin master
```

## 创建第一个稳定版本（v1.0.0）

推送代码后，可以在 GitHub 上创建版本标签：

1. 在 GitHub 仓库页面，点击 "Releases" 标签
2. 点击 "Create a new release"
3. 在 "Tag version" 字段中输入 `v1.0.0`
4. 在 "Release title" 字段中输入 `v1.0.0 - First Stable Release`
5. 在描述中可以写入：
   ```
   # 第一个稳定版本
   
   ## 功能特性
   - 产品管理（增删改查）
   - Excel导入导出
   - 批量数据处理
   - 现代化Vue.js前端界面
   
   ## 技术栈
   - Spring Boot
   - Vue.js
   - Element UI
   - Apache POI
   - Jackson
   ```
6. 点击 "Publish release"

## 后续维护

发布第一个稳定版本后，如果需要继续开发新功能：

1. 创建新分支进行开发：
   ```bash
   git checkout -b feature/new-feature
   ```

2. 开发完成后合并到主分支：
   ```bash
   git checkout main
   git merge feature/new-feature
   git push origin main
   ```

3. 发布新版本时创建新的标签：
   ```bash
   git tag v1.1.0
   git push origin v1.1.0
   ```

## 常见问题解决

### 1. 推送时遇到权限问题

可以使用 GitHub 的 Personal Access Token：

1. 在 GitHub 上生成 Personal Access Token
2. 使用以下命令推送：
   ```bash
   git remote set-url origin https://<token>@github.com/username/repository.git
   ```

### 2. 推送时遇到冲突

先拉取远程代码再推送：
```bash
git pull origin main
git push origin main
```

### 3. 忘记添加某些文件

添加遗漏的文件并提交：
```bash
git add .
git commit -m "Add missing files"
git push origin main
```