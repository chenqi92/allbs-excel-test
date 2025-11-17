package cn.allbs.excel.test.service;

import cn.allbs.excel.test.entity.*;
import cn.allbs.excel.test.entity.flatten.*;
import cn.allbs.excel.test.entity.nested.Department;
import cn.allbs.excel.test.entity.nested.Leader;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.RandomUtil;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

/**
 * 测试数据生成服务
 */
@Service
public class TestDataService {

    private static final String[] NAMES = {"张三", "李四", "王五", "赵六", "钱七", "孙八", "周九", "吴十",
                                           "郑十一", "王十二", "冯十三", "陈十四", "褚十五", "卫十六", "蒋十七", "沈十八"};
    private static final String[] DEPARTMENTS = {"技术部", "市场部", "人力资源部", "财务部"};
    private static final String[] POSITIONS_TECH = {"Java工程师", "前端工程师", "Python工程师", "测试工程师"};
    private static final String[] POSITIONS_MARKET = {"市场专员", "销售经理", "市场总监"};
    private static final String[] POSITIONS_HR = {"招聘专员", "薪酬专员", "人力资源经理"};
    private static final String[] POSITIONS_FINANCE = {"会计", "出纳", "财务经理"};
    private static final String[] STATUSES = {"正常", "禁用", "锁定"};
    private static final String[] ORDER_STATUSES = {"待付款", "已付款", "已发货", "已完成", "已取消"};
    private static final String[] CATEGORIES = {"电子产品", "家居用品", "服装鞋帽", "食品饮料", "图书音像"};

    /**
     * 生成用户测试数据
     */
    public List<UserDTO> generateUsers(int count) {
        List<UserDTO> users = new ArrayList<>();
        for (int i = 1; i <= count; i++) {
            UserDTO user = new UserDTO();
            user.setId((long) i);
            user.setUsername(NAMES[i % NAMES.length] + i);
            user.setEmail("user" + i + "@example.com");
            user.setCreateTime(LocalDateTime.now().minusDays(RandomUtil.randomInt(1, 365)));
            user.setAge(RandomUtil.randomInt(18, 60));
            user.setStatus(STATUSES[RandomUtil.randomInt(0, STATUSES.length)]);
            users.add(user);
        }
        return users;
    }

    /**
     * 生成员工测试数据（用于测试合并单元格）
     */
    public List<EmployeeDTO> generateEmployees(int count) {
        List<EmployeeDTO> employees = new ArrayList<>();
        int index = 0;

        for (String dept : DEPARTMENTS) {
            int empCount = count / DEPARTMENTS.length;
            for (int i = 0; i < empCount && index < count; i++) {
                EmployeeDTO emp = new EmployeeDTO();
                emp.setDepartment(dept);
                emp.setName(NAMES[index % NAMES.length]);

                // 根据部门设置职位
                String position = getPositionByDepartment(dept, i);
                emp.setPosition(position);
                emp.setSalary(new BigDecimal(RandomUtil.randomInt(8000, 30000)));
                emp.setJoinDate(DateUtil.format(
                    DateUtil.offsetMonth(DateUtil.date(), -RandomUtil.randomInt(1, 60)),
                    "yyyy-MM-dd"
                ));

                employees.add(emp);
                index++;
            }
        }

        return employees;
    }

    private String getPositionByDepartment(String dept, int index) {
        return switch (dept) {
            case "技术部" -> POSITIONS_TECH[index % POSITIONS_TECH.length];
            case "市场部" -> POSITIONS_MARKET[index % POSITIONS_MARKET.length];
            case "人力资源部" -> POSITIONS_HR[index % POSITIONS_HR.length];
            case "财务部" -> POSITIONS_FINANCE[index % POSITIONS_FINANCE.length];
            default -> "员工";
        };
    }

    /**
     * 生成订单测试数据
     */
    public List<OrderDTO> generateOrders(int count) {
        List<OrderDTO> orders = new ArrayList<>();
        for (int i = 1; i <= count; i++) {
            OrderDTO order = new OrderDTO();
            order.setOrderNo("ORD" + System.currentTimeMillis() + i);
            order.setCustomerName(NAMES[RandomUtil.randomInt(0, NAMES.length)]);
            order.setAmount(new BigDecimal(RandomUtil.randomDouble(100, 10000)));
            order.setStatus(ORDER_STATUSES[RandomUtil.randomInt(0, ORDER_STATUSES.length)]);
            order.setCreateTime(LocalDateTime.now().minusDays(RandomUtil.randomInt(1, 90)));
            order.setRemark("订单备注" + i);
            orders.add(order);
        }
        return orders;
    }

    /**
     * 生成敏感信息用户数据（用于测试脱敏）
     */
    public List<SensitiveUserDTO> generateSensitiveUsers(int count) {
        List<SensitiveUserDTO> users = new ArrayList<>();
        for (int i = 1; i <= count; i++) {
            SensitiveUserDTO user = new SensitiveUserDTO();
            user.setId((long) i);
            user.setName(NAMES[i % NAMES.length]);
            user.setPhone("138" + String.format("%08d", RandomUtil.randomInt(10000000, 99999999)));
            user.setIdCard("11010119" + (1990 + i % 20) + String.format("%08d", i));
            user.setEmail("user" + i + "@example.com");
            user.setBankCard("6222021234567890" + String.format("%04d", i));
            user.setAddress("北京市海淀区中关村软件园" + i + "号楼");
            user.setSex(i % 2 == 0 ? "1" : "0");  // 字典值：0-女，1-男
            user.setStatus(String.valueOf(i % 3));  // 字典值：0-正常，1-禁用，2-锁定
            users.add(user);
        }
        return users;
    }

    /**
     * 生成产品测试数据
     */
    public List<ProductDTO> generateProducts(int count) {
        List<ProductDTO> products = new ArrayList<>();
        for (int i = 1; i <= count; i++) {
            ProductDTO product = new ProductDTO();
            product.setId((long) i);
            product.setName("产品" + i);
            product.setCategory(CATEGORIES[RandomUtil.randomInt(0, CATEGORIES.length)]);
            product.setPrice(new BigDecimal(RandomUtil.randomDouble(10, 9999)));
            product.setStock(RandomUtil.randomInt(0, 1000));
            product.setDescription("这是产品" + i + "的详细描述信息，包含产品的各种特性和优势。");
            products.add(product);
        }
        return products;
    }

    // ==================== 新功能测试数据生成 ====================

    private static final String[] SKILLS = {"Java", "Python", "Go", "JavaScript", "TypeScript", "React", "Vue", "Spring Boot"};
    private static final String[] CITIES = {"北京", "上海", "广州", "深圳", "杭州", "成都", "武汉", "西安"};
    private static final String[] DEPT_CODES = {"TECH", "FIN", "HR", "MARKET", "ADMIN"};
    private static final String[] DEPT_TYPES = {"研发", "行政", "业务", "支持"};
    private static final String[] PRODUCTS = {"iPhone15", "MacBook Pro", "AirPods Pro", "iPad Air", "Apple Watch", "小米手机", "华为手机", "联想笔记本"};
    private static final String[] COURSE_NAMES = {"数学", "英语", "物理", "化学", "历史", "地理"};
    private static final String[] AWARD_NAMES = {"一等奖学金", "二等奖学金", "三等奖学金", "优秀学生", "优秀干部", "学习标兵"};

    /**
     * 生成 @NestedProperty 示例数据
     */
    public List<NestedPropertyExampleDTO> generateNestedPropertyExamples(int count) {
        List<NestedPropertyExampleDTO> list = new ArrayList<>();
        for (int i = 1; i <= count; i++) {
            NestedPropertyExampleDTO dto = new NestedPropertyExampleDTO();
            dto.setId((long) i);
            dto.setName(NAMES[i % NAMES.length] + i);

            // 创建部门对象
            Department dept = new Department();
            dept.setCode(DEPT_CODES[i % DEPT_CODES.length]);
            dept.setName(DEPARTMENTS[i % DEPARTMENTS.length]);
            dept.setType(DEPT_TYPES[i % DEPT_TYPES.length]);
            dept.setInternalId("INTERNAL-" + i);

            // 创建领导对象
            Leader leader = new Leader();
            leader.setName(NAMES[(i + 5) % NAMES.length] + "经理");
            leader.setPosition("部门经理");
            leader.setPhone("138" + String.format("%08d", RandomUtil.randomInt(10000000, 99999999)));
            dept.setLeader(leader);

            dto.setDepartment(dept);
            dto.setDepartment2(dept);
            dto.setDepartment3(dept);
            dto.setDepartment4(dept);

            // 技能列表
            List<String> skills = new ArrayList<>();
            int skillCount = RandomUtil.randomInt(1, 5);
            for (int j = 0; j < skillCount; j++) {
                skills.add(SKILLS[(i + j) % SKILLS.length]);
            }
            dto.setSkills(skills);
            dto.setMainSkill(skills);
            dto.setAllSkills(skills);

            // Map 属性
            Map<String, Object> properties = new HashMap<>();
            properties.put("city", CITIES[i % CITIES.length]);
            properties.put("joinYear", 2020 + (i % 5));
            dto.setProperties(properties);
            dto.setCity(properties);
            dto.setJoinYear(properties);

            list.add(dto);
        }
        return list;
    }

    /**
     * 生成 @FlattenProperty 示例数据
     */
    public List<FlattenPropertyExampleDTO> generateFlattenPropertyExamples(int count) {
        List<FlattenPropertyExampleDTO> list = new ArrayList<>();
        for (int i = 1; i <= count; i++) {
            FlattenPropertyExampleDTO dto = new FlattenPropertyExampleDTO();
            dto.setId((long) i);
            dto.setName(NAMES[i % NAMES.length] + i);
            dto.setAge(25 + (i % 20));

            // 部门
            Department dept = new Department();
            dept.setCode(DEPT_CODES[i % DEPT_CODES.length]);
            dept.setName(DEPARTMENTS[i % DEPARTMENTS.length]);
            dept.setType(DEPT_TYPES[i % DEPT_TYPES.length]);
            dto.setDepartment(dept);

            // 上级部门
            Department parentDept = new Department();
            parentDept.setCode(DEPT_CODES[(i + 1) % DEPT_CODES.length]);
            parentDept.setName(DEPARTMENTS[(i + 1) % DEPARTMENTS.length] + "-总部");
            parentDept.setType(DEPT_TYPES[(i + 1) % DEPT_TYPES.length]);
            dto.setParentDept(parentDept);

            // 主管部门（带领导，用于测试递归）
            Department managerDept = new Department();
            managerDept.setCode(DEPT_CODES[(i + 2) % DEPT_CODES.length]);
            managerDept.setName(DEPARTMENTS[(i + 2) % DEPARTMENTS.length]);
            managerDept.setType(DEPT_TYPES[(i + 2) % DEPT_TYPES.length]);

            Leader leader = new Leader();
            leader.setName(NAMES[(i + 3) % NAMES.length] + "总监");
            leader.setPosition("总监");
            leader.setPhone("139" + String.format("%08d", RandomUtil.randomInt(10000000, 99999999)));
            managerDept.setLeader(leader);
            dto.setManagerDept(managerDept);

            list.add(dto);
        }
        return list;
    }

    /**
     * 生成 @FlattenList 订单示例数据
     */
    public List<FlattenListOrderDTO> generateFlattenListOrders(int count) {
        List<FlattenListOrderDTO> list = new ArrayList<>();
        for (int i = 1; i <= count; i++) {
            FlattenListOrderDTO dto = new FlattenListOrderDTO();
            dto.setOrderNo("ORDER" + String.format("%06d", i));
            dto.setOrderTime(LocalDateTime.now().minusDays(RandomUtil.randomInt(1, 30)));
            dto.setStatus(ORDER_STATUSES[i % ORDER_STATUSES.length]);

            // 客户信息
            Customer customer = new Customer();
            customer.setName(NAMES[i % NAMES.length]);
            customer.setPhone("138" + String.format("%08d", RandomUtil.randomInt(10000000, 99999999)));
            customer.setCity(CITIES[i % CITIES.length]);
            dto.setCustomer(customer);

            // 订单明细
            List<OrderItem> items = new ArrayList<>();
            int itemCount = RandomUtil.randomInt(1, 5);
            for (int j = 0; j < itemCount; j++) {
                OrderItem item = new OrderItem();
                item.setProductName(PRODUCTS[(i + j) % PRODUCTS.length]);
                item.setSku("SKU-" + (i * 100 + j));
                item.setQuantity(RandomUtil.randomInt(1, 10));
                BigDecimal price = new BigDecimal(RandomUtil.randomDouble(100, 5000)).setScale(2, BigDecimal.ROUND_HALF_UP);
                item.setPrice(price);
                item.setSubtotal(price.multiply(new BigDecimal(item.getQuantity())));
                items.add(item);
            }
            dto.setItems(items);

            list.add(dto);
        }
        return list;
    }

    /**
     * 生成 @FlattenList 学生示例数据（多 List）
     */
    public List<FlattenListStudentDTO> generateFlattenListStudents(int count) {
        List<FlattenListStudentDTO> list = new ArrayList<>();
        for (int i = 1; i <= count; i++) {
            FlattenListStudentDTO dto = new FlattenListStudentDTO();
            dto.setName(NAMES[i % NAMES.length]);
            dto.setStudentNo("STU" + String.format("%06d", i));
            dto.setClassName("2024级" + (i % 5 + 1) + "班");

            // 课程列表
            List<Course> courses = new ArrayList<>();
            int courseCount = RandomUtil.randomInt(3, 6);
            for (int j = 0; j < courseCount; j++) {
                Course course = new Course();
                course.setCourseName(COURSE_NAMES[j % COURSE_NAMES.length]);
                course.setScore(RandomUtil.randomInt(60, 100));
                courses.add(course);
            }
            dto.setCourses(courses);

            // 奖项列表
            List<Award> awards = new ArrayList<>();
            int awardCount = RandomUtil.randomInt(1, 4);
            for (int j = 0; j < awardCount; j++) {
                Award award = new Award();
                award.setAwardName(AWARD_NAMES[j % AWARD_NAMES.length]);
                award.setAwardDate(DateUtil.format(
                    DateUtil.offsetMonth(DateUtil.date(), -RandomUtil.randomInt(1, 24)),
                    "yyyy-MM-dd"
                ));
                awards.add(award);
            }
            dto.setAwards(awards);

            list.add(dto);
        }
        return list;
    }

    /**
     * 生成条件样式示例数据
     */
    public List<ConditionalStyleDTO> generateConditionalStyleData(int count) {
        List<ConditionalStyleDTO> list = new ArrayList<>();
        String[] statuses = {"已完成", "进行中", "已取消"};
        String[] grades = {"A+", "A", "B+", "B", "C+", "C"};

        for (int i = 1; i <= count; i++) {
            ConditionalStyleDTO dto = new ConditionalStyleDTO();
            dto.setStudentName(NAMES[i % NAMES.length] + i);
            dto.setScore(RandomUtil.randomInt(30, 100));
            dto.setStatus(statuses[i % statuses.length]);
            dto.setSalesAmount(new BigDecimal(RandomUtil.randomInt(1000, 20000)));
            dto.setGrade(grades[i % grades.length]);
            list.add(dto);
        }
        return list;
    }

    /**
     * 生成动态表头示例数据
     */
    public List<DynamicHeaderDTO> generateDynamicHeaderData(int count) {
        List<DynamicHeaderDTO> list = new ArrayList<>();
        String[] categories = {"电子产品", "家居用品", "服装鞋帽"};

        for (int i = 1; i <= count; i++) {
            DynamicHeaderDTO dto = new DynamicHeaderDTO();
            dto.setProductId((long) i);
            dto.setProductName("产品" + i);
            dto.setCategory(categories[i % categories.length]);

            // 动态属性（不同产品的属性不同）
            Map<String, Object> properties = new LinkedHashMap<>();
            if (categories[i % categories.length].equals("电子产品")) {
                properties.put("品牌", "品牌" + (i % 5 + 1));
                properties.put("型号", "Model-" + i);
                properties.put("颜色", i % 2 == 0 ? "黑色" : "白色");
                properties.put("内存", (i % 4 + 2) * 4 + "GB");
                properties.put("屏幕尺寸", (i % 3 + 5) + "英寸");
            } else if (categories[i % categories.length].equals("家居用品")) {
                properties.put("材质", i % 2 == 0 ? "木质" : "金属");
                properties.put("尺寸", (i % 3 + 1) + "米");
                properties.put("颜色", i % 2 == 0 ? "原木色" : "白色");
                properties.put("产地", "中国");
            } else {
                properties.put("品牌", "品牌" + (i % 3 + 1));
                properties.put("尺码", (i % 5 + 36) + "码");
                properties.put("颜色", i % 2 == 0 ? "黑色" : "蓝色");
                properties.put("材质", "棉质");
                properties.put("季节", i % 2 == 0 ? "春秋" : "夏季");
            }
            dto.setProperties(properties);

            // 扩展字段（使用预定义表头）
            Map<String, Object> extFields = new LinkedHashMap<>();
            extFields.put("备注1", "备注信息" + i);
            extFields.put("备注2", RandomUtil.randomInt(1, 100));
            if (i % 3 == 0) {
                extFields.put("备注3", "特殊标记");
            }
            dto.setExtFields(extFields);

            list.add(dto);
        }
        return list;
    }
}
