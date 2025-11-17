package cn.allbs.excel.test.entity;

import cn.allbs.excel.annotation.ExcelValidation;
import cn.allbs.excel.annotation.ValidationType;
import com.alibaba.excel.annotation.ExcelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * 数据验证测试实体
 *
 * @author ChenQi
 * @since 2025-11-17
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DataValidationDTO {

	/**
	 * 姓名 - 文本长度验证
	 */
	@ExcelProperty(value = "姓名", index = 0)
	@ExcelValidation(type = ValidationType.TEXT_LENGTH, minLength = 2, maxLength = 10, errorMessage = "姓名长度必须在2-10个字符之间", promptMessage = "请输入2-10个字符的姓名", showPromptBox = true)
	private String name;

	/**
	 * 性别 - 下拉列表验证
	 */
	@ExcelProperty(value = "性别", index = 1)
	@ExcelValidation(type = ValidationType.LIST, options = { "男", "女" }, errorMessage = "性别只能选择：男、女", promptMessage = "请选择性别", showPromptBox = true)
	private String gender;

	/**
	 * 年龄 - 整数范围验证
	 */
	@ExcelProperty(value = "年龄", index = 2)
	@ExcelValidation(type = ValidationType.INTEGER, min = 18, max = 65, errorMessage = "年龄必须在18-65之间", promptMessage = "请输入18-65之间的整数", showPromptBox = true)
	private Integer age;

	/**
	 * 工资 - 小数范围验证
	 */
	@ExcelProperty(value = "工资", index = 3)
	@ExcelValidation(type = ValidationType.DECIMAL, min = 3000.0, max = 50000.0, errorMessage = "工资必须在3000-50000之间", promptMessage = "请输入3000-50000之间的数值", showPromptBox = true)
	private Double salary;

	/**
	 * 入职日期 - 日期验证
	 */
	@ExcelProperty(value = "入职日期", index = 4)
	@ExcelValidation(type = ValidationType.DATE, dateFormat = "yyyy-MM-dd", errorMessage = "请输入有效的日期格式", promptMessage = "请输入日期，格式：yyyy-MM-dd", showPromptBox = true)
	private LocalDate hireDate;

	/**
	 * 部门 - 下拉列表验证
	 */
	@ExcelProperty(value = "部门", index = 5)
	@ExcelValidation(type = ValidationType.LIST, options = { "技术部", "销售部", "人事部", "财务部", "运营部" }, errorMessage = "请从下拉列表中选择部门", promptMessage = "请选择部门", showPromptBox = true)
	private String department;

	/**
	 * 职位 - 下拉列表验证
	 */
	@ExcelProperty(value = "职位", index = 6)
	@ExcelValidation(type = ValidationType.LIST, options = { "初级工程师", "中级工程师", "高级工程师", "技术经理", "技术总监" }, errorMessage = "请从下拉列表中选择职位", promptMessage = "请选择职位", showPromptBox = true)
	private String position;

	/**
	 * 工作年限 - 整数验证（最小值）
	 */
	@ExcelProperty(value = "工作年限", index = 7)
	@ExcelValidation(type = ValidationType.INTEGER, min = 0, max = 50, errorMessage = "工作年限必须大于等于0", promptMessage = "请输入工作年限（年）", showPromptBox = true)
	private Integer workYears;

	/**
	 * 绩效评分 - 小数验证
	 */
	@ExcelProperty(value = "绩效评分", index = 8)
	@ExcelValidation(type = ValidationType.DECIMAL, min = 0.0, max = 10.0, errorMessage = "绩效评分必须在0-10之间", promptMessage = "请输入0-10之间的评分", showPromptBox = true)
	private Double performanceScore;

	/**
	 * 邮箱 - 任意值（仅提示）
	 */
	@ExcelProperty(value = "邮箱", index = 9)
	@ExcelValidation(type = ValidationType.ANY, promptMessage = "请输入有效的邮箱地址，例如：example@company.com", showPromptBox = true, showErrorBox = false)
	private String email;

}
