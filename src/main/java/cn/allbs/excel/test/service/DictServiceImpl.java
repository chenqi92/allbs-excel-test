package cn.allbs.excel.test.service;

import cn.allbs.excel.service.DictService;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * 字典服务实现 - 模拟字典查询
 */
@Service
public class DictServiceImpl implements DictService {

    // 模拟字典数据
    private static final Map<String, Map<String, String>> DICT_DATA = new HashMap<>();

    static {
        // 性别字典
        Map<String, String> sexDict = new HashMap<>();
        sexDict.put("0", "女");
        sexDict.put("1", "男");
        sexDict.put("2", "未知");
        DICT_DATA.put("sys_user_sex", sexDict);

        // 状态字典
        Map<String, String> statusDict = new HashMap<>();
        statusDict.put("0", "正常");
        statusDict.put("1", "禁用");
        statusDict.put("2", "锁定");
        DICT_DATA.put("sys_user_status", statusDict);
    }

    @Override
    public String getLabel(String dictType, String dictValue) {
        Map<String, String> dict = DICT_DATA.get(dictType);
        if (dict != null) {
            return dict.get(dictValue);
        }
        return dictValue;
    }

    @Override
    public String getValue(String dictType, String dictLabel) {
        Map<String, String> dict = DICT_DATA.get(dictType);
        if (dict != null) {
            for (Map.Entry<String, String> entry : dict.entrySet()) {
                if (entry.getValue().equals(dictLabel)) {
                    return entry.getKey();
                }
            }
        }
        return dictLabel;
    }
}
