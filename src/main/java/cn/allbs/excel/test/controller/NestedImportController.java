package cn.allbs.excel.test.controller;

import cn.allbs.excel.listener.FlattenListReadListener;
import cn.allbs.excel.test.entity.FlattenListOrderDTO;
import cn.allbs.excel.test.entity.NestedPropertyExampleDTO;
import com.alibaba.excel.EasyExcel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 嵌套对象导入功能测试控制器
 * 演示导入增强功能的使用
 */
@Slf4j
@RestController
@RequestMapping("/api/import/nested")
@CrossOrigin(origins = "*")
public class NestedImportController {

	/**
	 * 1. @NestedProperty 导入示例
	 * 演示使用 NestedObjectConverter 进行嵌套对象导入
	 */
	@PostMapping("/nested-property")
	public Map<String, Object> nestedPropertyImport(@RequestParam("file") MultipartFile file) throws IOException {
		List<NestedPropertyExampleDTO> data = EasyExcel.read(file.getInputStream(), NestedPropertyExampleDTO.class,
				null).sheet().doReadSync();

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
	 * 演示将多行数据聚合回包含 List 的对象
	 */
	@PostMapping("/flatten-list-order")
	public Map<String, Object> flattenListOrderImport(@RequestParam("file") MultipartFile file) throws IOException {
		FlattenListReadListener<FlattenListOrderDTO> listener = new FlattenListReadListener<>(
				FlattenListOrderDTO.class);

		EasyExcel.read(file.getInputStream(), listener).sheet().doRead();

		List<FlattenListOrderDTO> result = listener.getResult();

		Map<String, Object> response = new HashMap<>();
		response.put("success", true);
		response.put("count", result.size());
		response.put("data", result);
		response.put("message", "成功导入 " + result.size() + " 个订单");

		log.info("Imported {} orders", result.size());
		return response;
	}

}
