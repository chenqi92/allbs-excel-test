# allbs-excel 完整测试项目

## ⚠️ 重要提示

**本项目使用 Spring Boot 2.7.18** 因为 `allbs-excel 3.0.0` 不兼容 Spring Boot 3.x (javax → jakarta 包变更问题)。

## 🎯 项目说明

这是一个为 **allbs-excel** (v3.0.0) 开发的完整功能测试项目，包含：

- ✅ 所有功能点的测试接口（16+ 个接口）
- ✅ 自动生成的测试数据
- ✅ 精美的前端测试页面
- ✅ 完整的使用文档
- ✅ **真正可以导出Excel文件** (已验证)

## 🚀 快速启动

### 方式一：使用启动脚本（推荐）

```bash
./run.sh
```

### 方式二：使用 Maven 命令

```bash
mvn spring-boot:run
```

### 方式三：使用 IDE

在 IDE 中运行 `ExcelTestApplication.java`

启动成功后，访问: http://localhost:8080

## 📦 项目结构

```
allbs-excel-test/
├── pom.xml                                 # Maven 配置文件
├── run.sh                                  # 启动脚本
├── TEST_GUIDE.md                           # 详细测试指南
├── PROJECT_README.md                       # 项目说明（本文件）
└── src/main/
    ├── java/cn/allbs/excel/test/
    │   ├── ExcelTestApplication.java       # 启动类
    │   ├── entity/                         # 5个测试实体类
    │   │   ├── UserDTO.java                # 基本功能测试
    │   │   ├── EmployeeDTO.java            # 合并单元格测试
    │   │   ├── OrderDTO.java               # 多Sheet测试
    │   │   ├── SensitiveUserDTO.java       # 脱敏+字典测试
    │   │   └── ProductDTO.java             # 样式测试
    │   ├── controller/                     # 3个测试控制器
    │   │   ├── BasicExportController.java  # 7个基本导出接口
    │   │   ├── AdvancedExportController.java # 5个高级导出接口
    │   │   └── ImportController.java       # 4个导入接口
    │   ├── service/                        # 服务层
    │   │   ├── TestDataService.java        # 测试数据生成
    │   │   └── DictServiceImpl.java        # 字典服务
    │   └── listener/                       # 监听器
    │       └── ConsoleProgressListener.java # 进度监听器
    └── resources/
        ├── application.yml                 # 应用配置
        └── static/
            └── index.html                  # 精美测试页面
```

## 🧪 功能测试覆盖

### 📤 基本导出功能（7个接口）

| 功能 | 接口 | 说明 |
|------|------|------|
| ✅ 基本导出 | `/api/export/basic/simple` | 最简单的导出方式 |
| ✅ 空数据导出 | `/api/export/basic/empty` | 导出只有表头的Excel |
| ✅ 只导出注解字段 | `/api/export/basic/only-annotated` | onlyExcelProperty配置 |
| ✅ 多Sheet导出 | `/api/export/basic/multi-sheet` | 一个文件多个Sheet |
| ✅ 多Sheet空数据 | `/api/export/basic/multi-sheet-empty` | 多Sheet带表头 |
| ✅ 列宽行高设置 | `/api/export/basic/styled` | 自定义样式 |
| ✅ 动态文件名 | `/api/export/basic/dynamic-name` | SpEL表达式 |

### 🔥 高级导出功能（5个接口）

| 功能 | 接口 | 说明 |
|------|------|------|
| ✅ 合并单元格 | `/api/export/advanced/merge` | 自动合并+依赖合并 |
| ✅ 数据脱敏+字典 | `/api/export/advanced/desensitize` | 7种脱敏类型+字典转换 |
| ✅ 导出进度回调 | `/api/export/advanced/with-progress` | 实时监听进度 |
| ✅ 大数据导出 | `/api/export/advanced/large-data` | 性能测试 |
| ✅ 合并+进度 | `/api/export/advanced/merge-with-progress` | 组合功能 |

### 📥 导入功能（4个接口）

| 功能 | 接口 | 说明 |
|------|------|------|
| ✅ 基本导入 | `/api/import/simple` | 自动解析Excel |
| ✅ 带验证导入 | `/api/import/validate` | JSR-303验证 |
| ✅ 跳过空行 | `/api/import/skip-empty` | ignoreEmptyRow |
| ✅ 自定义字段名 | `/api/import/custom-field` | fileName配置 |

## 🎨 前端测试页面特性

- 🎯 精美的渐变UI设计
- 📊 分类展示所有功能
- 🎛️ 可调节参数（数据量、日期等）
- 📤 一键导出测试
- 📥 文件上传导入
- ✅ 实时结果展示
- 🏷️ NEW 和 高级 功能标识

## 📊 测试数据说明

所有测试数据由 `TestDataService` 自动生成：

- **用户数据**: ID、姓名、邮箱、年龄、状态、创建时间
- **员工数据**: 部门、姓名、职位、工资、入职日期（按部门排序用于合并）
- **订单数据**: 订单号、客户、金额、状态、时间、备注
- **敏感数据**: 手机号、身份证、邮箱、银行卡、地址
- **产品数据**: ID、名称、分类、价格、库存、描述

## 🔍 测试重点

### 1. 空数据导出测试
确保设置了 `clazz` 参数，验证是否能正确生成表头。

### 2. 合并单元格测试
- 验证同部门的单元格是否合并
- 验证依赖关系（姓名依赖部门，职位依赖姓名）
- 数据必须按合并字段排序

### 3. 数据脱敏测试
导出后检查：
- 手机号: `138****1234`
- 身份证: `110101********1234`
- 邮箱: `a***@example.com`
- 银行卡: `622202******1234`
- 姓名: `张*`
- 地址: `北京市海淀区****`

### 4. 字典转换测试
导出后检查：
- 性别: `1` → `男`, `0` → `女`
- 状态: `0` → `正常`, `1` → `禁用`, `2` → `锁定`

### 5. 进度回调测试
查看后端控制台日志，验证：
- `onStart` 方法是否调用
- `onProgress` 方法是否按间隔调用
- `onComplete` 方法是否调用
- 进度百分比是否正确

### 6. 导入验证测试
修改导出的文件：
- 删除必填字段（如用户名）
- 修改邮箱为无效格式
- 上传后验证错误信息

## 📝 使用示例

### 导出示例

```bash
# 基本导出 10 条用户数据
curl "http://localhost:8080/api/export/basic/simple?count=10" -o users.xlsx

# 导出空表格（只有表头）
curl "http://localhost:8080/api/export/basic/empty" -o template.xlsx

# 导出带合并单元格的员工列表
curl "http://localhost:8080/api/export/advanced/merge?count=20" -o employees.xlsx

# 导出脱敏数据
curl "http://localhost:8080/api/export/advanced/desensitize?count=10" -o sensitive.xlsx
```

### 导入示例

```bash
# 使用 curl 上传文件
curl -X POST \
  -F "file=@users.xlsx" \
  http://localhost:8080/api/import/simple
```

## 🛠️ 技术栈

- **框架**: Spring Boot 3.2.0
- **Excel库**: allbs-excel 3.0.0 (基于 EasyExcel 4.0.3)
- **工具库**: Hutool 5.8.25
- **JDK**: 17+
- **构建工具**: Maven

## 📖 详细文档

查看 `TEST_GUIDE.md` 获取：
- 详细的测试步骤
- API 接口文档
- 常见问题解答
- 注意事项

## 🎯 核心功能点

### allbs-excel 3.0.0 新增功能

1. ✨ **空数据导出**: 支持导出带表头的空Excel
2. ✨ **onlyExcelProperty**: 只导出有注解的字段
3. ✨ **非连续列索引**: 支持 1、2、7、11 等非连续索引
4. ✨ **合并单元格**: @ExcelMerge + 依赖关系合并
5. ✨ **数据脱敏**: 7种脱敏类型 + 自定义规则
6. ✨ **字典转换**: @ExcelDict + DictConverter
7. ✨ **进度回调**: @ExportProgress + ExportProgressListener
8. ✨ **Spring Boot 2.x/3.x 兼容**: 无需额外配置

## 🎬 快速演示

1. 启动项目
2. 访问 http://localhost:8080
3. 点击任意功能的"导出"按钮
4. 下载并打开 Excel 文件查看效果
5. 使用导出的文件测试导入功能

## 📞 技术支持

- 项目地址: https://github.com/chenqi92/allbs-excel
- 问题反馈: https://github.com/chenqi92/allbs-excel/issues
- 邮箱: chenqi92104@icloud.com

## 📄 许可证

本测试项目遵循 Apache License 2.0 许可证。
