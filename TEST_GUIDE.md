# allbs-excel 测试指南

## 项目简介

这是一个完整的 allbs-excel 功能测试项目，包含了所有功能点的测试接口和前端测试页面。

## 项目结构

```
allbs-excel-test/
├── src/main/java/cn/allbs/excel/test/
│   ├── ExcelTestApplication.java          # 启动类
│   ├── entity/                             # 实体类
│   │   ├── UserDTO.java                    # 用户实体（基本功能测试）
│   │   ├── EmployeeDTO.java                # 员工实体（合并单元格测试）
│   │   ├── OrderDTO.java                   # 订单实体（多Sheet测试）
│   │   ├── SensitiveUserDTO.java           # 敏感信息用户（脱敏+字典测试）
│   │   └── ProductDTO.java                 # 产品实体（样式测试）
│   ├── controller/                         # 控制器
│   │   ├── BasicExportController.java      # 基本导出功能
│   │   ├── AdvancedExportController.java   # 高级导出功能
│   │   └── ImportController.java           # 导入功能
│   ├── service/                            # 服务层
│   │   ├── TestDataService.java            # 测试数据生成服务
│   │   └── DictServiceImpl.java            # 字典服务实现
│   └── listener/                           # 监听器
│       └── ConsoleProgressListener.java    # 进度监听器
└── src/main/resources/
    ├── application.yml                     # 配置文件
    └── static/
        └── index.html                      # 测试页面

```

## 快速开始

### 1. 启动项目

```bash
# 使用 Maven
mvn spring-boot:run

# 或者使用 IDE 运行 ExcelTestApplication
```

### 2. 访问测试页面

浏览器访问: http://localhost:8080

## 功能测试清单

### 📤 基本导出功能

#### 1. 基本导出
- **接口**: `GET /api/export/basic/simple?count=10`
- **功能**: 最简单的导出方式，返回 List 即可
- **测试要点**: 验证基本导出功能是否正常

#### 2. 空数据导出（带表头）⭐ NEW
- **接口**: `GET /api/export/basic/empty`
- **功能**: 导出只有表头的空 Excel
- **测试要点**: 验证空数据时是否能正确生成表头

#### 3. 只导出有注解的字段 ⭐ NEW
- **接口**: `GET /api/export/basic/only-annotated?count=10`
- **功能**: 只导出标注了 @ExcelProperty 的字段
- **测试要点**: 验证 onlyExcelProperty 配置是否生效

#### 4. 多 Sheet 导出
- **接口**: `GET /api/export/basic/multi-sheet?userCount=10&orderCount=20`
- **功能**: 在一个 Excel 文件中导出多个 Sheet
- **测试要点**: 验证多 Sheet 导出功能

#### 5. 多 Sheet 空数据导出 ⭐ NEW
- **接口**: `GET /api/export/basic/multi-sheet-empty`
- **功能**: 导出多个 Sheet，每个都只有表头
- **测试要点**: 验证多 Sheet 空数据导出

#### 6. 列宽和行高设置
- **接口**: `GET /api/export/basic/styled?count=15`
- **功能**: 自定义列宽和行高
- **测试要点**: 验证样式设置是否生效

#### 7. 动态文件名
- **接口**: `GET /api/export/basic/dynamic-name?date=2025-11-15&count=10`
- **功能**: 使用 SpEL 表达式动态生成文件名
- **测试要点**: 验证文件名是否包含日期参数

### 🔥 高级导出功能

#### 1. 合并单元格 ⭐ NEW
- **接口**: `GET /api/export/advanced/merge?count=20`
- **功能**: 自动合并相同值的单元格，支持依赖关系
- **测试要点**:
  - 验证相同部门的单元格是否合并
  - 验证依赖关系合并是否正确

#### 2. 数据脱敏 + 字典转换 ⭐ NEW
- **接口**: `GET /api/export/advanced/desensitize?count=10`
- **功能**: 敏感数据脱敏，字典值自动转换
- **测试要点**:
  - 手机号: 138****1234
  - 身份证: 110101********1234
  - 邮箱: a***@example.com
  - 银行卡: 622202******1234
  - 性别: 0 → 女, 1 → 男
  - 状态: 0 → 正常, 1 → 禁用

#### 3. 导出进度回调 ⭐ NEW
- **接口**: `GET /api/export/advanced/with-progress?count=100`
- **功能**: 实时监听导出进度
- **测试要点**: 查看后端控制台日志，验证进度回调

#### 4. 大数据量导出
- **接口**: `GET /api/export/advanced/large-data?count=1000`
- **功能**: 测试大数据量导出性能
- **测试要点**: 验证性能和进度回调

#### 5. 合并 + 进度
- **接口**: `GET /api/export/advanced/merge-with-progress?count=50`
- **功能**: 合并单元格结合进度回调
- **测试要点**: 验证两个功能同时使用是否正常

### 📥 导入功能

#### 1. 基本导入
- **接口**: `POST /api/import/simple`
- **功能**: 上传 Excel 文件，自动解析为 Java 对象
- **测试步骤**:
  1. 先导出一个用户列表
  2. 上传刚导出的文件
  3. 查看返回的解析结果

#### 2. 带验证的导入
- **接口**: `POST /api/import/validate`
- **功能**: 导入时自动进行数据校验
- **测试步骤**:
  1. 导出一个用户列表
  2. 修改文件中的数据（如：将邮箱改为无效格式）
  3. 上传文件，查看验证错误信息

#### 3. 跳过空行导入
- **接口**: `POST /api/import/skip-empty`
- **功能**: 导入时自动跳过空行
- **测试步骤**:
  1. 导出一个用户列表
  2. 在文件中插入几行空行
  3. 上传文件，验证空行被跳过

## 测试数据说明

所有测试数据都由 `TestDataService` 自动生成，包括：

- **用户数据**: 随机生成姓名、邮箱、年龄等
- **员工数据**: 按部门分组，用于测试合并功能
- **订单数据**: 随机生成订单信息
- **敏感信息数据**: 包含手机号、身份证等敏感信息
- **产品数据**: 包含产品描述等长文本

## 注意事项

1. **进度回调功能**: 需要查看后端控制台日志才能看到进度信息
2. **数据脱敏**: 只在导出时生效，导入时不进行脱敏处理
3. **字典转换**: 已内置性别和状态两个字典，可根据需要扩展
4. **大数据量测试**: 建议从小数据量开始测试，逐步增加

## 常见问题

### Q1: 导入功能报错 400
**A**: 检查文件是否正确上传，确保前端使用了正确的 FormData

### Q2: 导出的 Excel 打不开
**A**: 检查后端日志是否有异常，确保数据生成正常

### Q3: 合并单元格不生效
**A**: 确保数据已按照合并字段排序，且配置了 `autoMerge = true`

### Q4: 字典转换不生效
**A**: 检查是否配置了 `DictConverter` 和 `@ExcelDict` 注解

## API 接口列表

### 基本导出
| 接口 | 方法 | 说明 |
|------|------|------|
| `/api/export/basic/simple` | GET | 基本导出 |
| `/api/export/basic/empty` | GET | 空数据导出 |
| `/api/export/basic/only-annotated` | GET | 只导出注解字段 |
| `/api/export/basic/multi-sheet` | GET | 多Sheet导出 |
| `/api/export/basic/multi-sheet-empty` | GET | 多Sheet空数据 |
| `/api/export/basic/styled` | GET | 样式设置 |
| `/api/export/basic/dynamic-name` | GET | 动态文件名 |

### 高级导出
| 接口 | 方法 | 说明 |
|------|------|------|
| `/api/export/advanced/merge` | GET | 合并单元格 |
| `/api/export/advanced/desensitize` | GET | 数据脱敏+字典 |
| `/api/export/advanced/with-progress` | GET | 进度回调 |
| `/api/export/advanced/large-data` | GET | 大数据导出 |
| `/api/export/advanced/merge-with-progress` | GET | 合并+进度 |

### 导入
| 接口 | 方法 | 说明 |
|------|------|------|
| `/api/import/simple` | POST | 基本导入 |
| `/api/import/validate` | POST | 带验证导入 |
| `/api/import/skip-empty` | POST | 跳过空行导入 |

## 技术栈

- Spring Boot 3.2.0
- allbs-excel 3.0.0
- EasyExcel 4.0.3
- Hutool 5.8.25
- Lombok 1.18.36

## 版本信息

- 项目版本: 1.0.0
- Java版本: 17
- allbs-excel版本: 3.0.0
