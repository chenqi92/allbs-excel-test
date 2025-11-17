package cn.allbs.excel.test.entity;

import cn.allbs.excel.annotation.DynamicHeaders;
import cn.allbs.excel.annotation.DynamicHeaderStrategy;
import com.alibaba.excel.annotation.ExcelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * 动态表头示例 DTO
 * 演示 @DynamicHeaders 注解的使用
 *
 * @author ChenQi
 * @since 2025-11-17
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DynamicHeaderDTO {

	@ExcelProperty("产品ID")
	private Long productId;

	@ExcelProperty("产品名称")
	private String productName;

	@ExcelProperty("分类")
	private String category;

	/**
	 * 动态属性（从数据中自动提取表头）
	 * <p>
	 * 不同产品的属性可能不同，表头动态生成
	 * </p>
	 */
	@DynamicHeaders(strategy = DynamicHeaderStrategy.FROM_DATA, headerPrefix = "属性-",
			order = DynamicHeaders.SortOrder.ASC)
	private Map<String, Object> properties;

	/**
	 * 扩展字段（使用预定义表头）
	 */
	@DynamicHeaders(strategy = DynamicHeaderStrategy.FROM_CONFIG, headers = { "备注1", "备注2", "备注3" },
			headerPrefix = "扩展-")
	private Map<String, Object> extFields;

}
