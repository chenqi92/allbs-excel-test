package cn.allbs.excel.test.entity;

import cn.allbs.excel.annotation.ConditionalFormat;
import com.alibaba.excel.annotation.ExcelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * Performance Data DTO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PerformanceDataDTO {

    @ExcelProperty(value = "Employee", index = 0)
    private String employeeName;

    @ExcelProperty(value = "Department", index = 1)
    private String department;

    @ExcelProperty(value = "Score", index = 2)
    @ConditionalFormat(
        type = ConditionalFormat.FormatType.ICON_SET,
        iconSet = ConditionalFormat.IconSetType.THREE_TRAFFIC_LIGHTS_1
    )
    private Integer score;

    @ExcelProperty(value = "Sales Amount", index = 3)
    @ConditionalFormat(
        type = ConditionalFormat.FormatType.ICON_SET,
        iconSet = ConditionalFormat.IconSetType.THREE_ARROWS
    )
    private BigDecimal sales;

    @ExcelProperty(value = "Completion Rate", index = 4)
    @ConditionalFormat(
        type = ConditionalFormat.FormatType.ICON_SET,
        iconSet = ConditionalFormat.IconSetType.THREE_FLAGS
    )
    private Double completionRate;
}
