package cn.allbs.excel.test.entity;

import cn.allbs.excel.annotation.CellStyleDef;
import cn.allbs.excel.annotation.Condition;
import cn.allbs.excel.annotation.ConditionalStyle;
import com.alibaba.excel.annotation.ExcelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * 条件样式示例 DTO
 * 演示 @ConditionalStyle 注解的使用
 *
 * @author ChenQi
 * @since 2025-11-17
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ConditionalStyleDTO {

	@ExcelProperty("学生姓名")
	private String studentName;

	@ExcelProperty("考试分数")
	@ConditionalStyle(conditions = { @Condition(value = ">=90", style = @CellStyleDef(backgroundColor = "#00FF00", // 绿色
			bold = true)), @Condition(value = ">=60", style = @CellStyleDef(backgroundColor = "#FFFF00" // 黄色
	)), @Condition(value = "<60", style = @CellStyleDef(backgroundColor = "#FF0000", // 红色
			fontColor = "#FFFFFF")) })
	private Integer score;

	@ExcelProperty("任务状态")
	@ConditionalStyle(conditions = {
			@Condition(value = "已完成", style = @CellStyleDef(backgroundColor = "#00FF00", fontColor = "#FFFFFF")),
			@Condition(value = "进行中", style = @CellStyleDef(backgroundColor = "#FFFF00")),
			@Condition(value = "已取消", style = @CellStyleDef(backgroundColor = "#808080", fontColor = "#FFFFFF")) })
	private String status;

	@ExcelProperty("销售额")
	@ConditionalStyle(conditions = {
			@Condition(value = ">=10000", style = @CellStyleDef(backgroundColor = "#FFD700", // 金色
					bold = true)),
			@Condition(value = ">=5000", style = @CellStyleDef(backgroundColor = "#87CEEB" // 天蓝色
			)) })
	private BigDecimal salesAmount;

	@ExcelProperty("等级")
	@ConditionalStyle(conditions = {
			@Condition(value = "regex:^A.*", style = @CellStyleDef(backgroundColor = "#00FF00", bold = true)),
			@Condition(value = "regex:^B.*", style = @CellStyleDef(backgroundColor = "#FFFF00")),
			@Condition(value = "regex:^C.*", style = @CellStyleDef(backgroundColor = "#FFA500")) // 橙色
	})
	private String grade;

}
