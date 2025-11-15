package cn.allbs.excel.test.service;

import cn.allbs.excel.test.entity.*;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.RandomUtil;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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
}
