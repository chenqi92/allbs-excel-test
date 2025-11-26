package cn.allbs.excel.test.controller;

import cn.allbs.excel.annotation.ImportExcel;
import cn.allbs.excel.test.entity.FlattenListOrderDTO;
import cn.allbs.excel.test.entity.NestedPropertyExampleDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 嵌套对象导入功能测试控制器
 * 演示导入增强功能的使用 - 基于 @ImportExcel 注解
 */
@Slf4j
@RestController
@RequestMapping("/api/import/nested")
@CrossOrigin(origins = "*")
public class NestedImportController {

	/**
	 * 1. @NestedProperty 导入示例
	 * 演示使用 @ImportExcel 注解自动处理嵌套对象导入
	 * 框架会自动检测 @NestedProperty 注解并注册 NestedObjectConverter
	 */
	@PostMapping("/nested-property")
	public Map<String, Object> nestedPropertyImport(@ImportExcel List<NestedPropertyExampleDTO> data) {
		Map<String, Object> result = new HashMap<>();
		result.put("success", true);
		result.put("count", data.size());
		result.put("data", data);
		result.put("message", "成功导入 " + data.size() + " 条数据");

		log.info("Imported {} records", data.size());
		return result;
	}

	/**
	 * 2. @FlattenList 导入示例 - 订单明细聚合
	 * 演示使用 @ImportExcel 注解自动处理多行数据聚合
	 * 框架会自动检测 @FlattenList 注解并使用 FlattenListReadListener
	 */
	@PostMapping("/flatten-list-order")
	public Map<String, Object> flattenListOrderImport(@ImportExcel List<FlattenListOrderDTO> data) {
		Map<String, Object> response = new HashMap<>();
		response.put("success", true);
		response.put("count", data.size());
		response.put("data", data);
		response.put("message", "成功导入 " + data.size() + " 个订单");

		log.info("Imported {} orders", data.size());
		return response;
	}

}
