package com.baiyi.cratos.common.table;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

/**
 * PrettyTable 测试类
 */
public class PrettyTableTest {

    private PrettyTable table;

    @BeforeEach
    void setUp() {
        table = PrettyTable.fieldNames("Name", "Age", "Score");
    }

    @Test
    void testFieldNamesValidation() {
        // 测试空字段名
        assertThrows(IllegalArgumentException.class, () -> {
            PrettyTable.fieldNames();
        });

        // 测试null字段名
        assertThrows(IllegalArgumentException.class, () -> {
            PrettyTable.fieldNames((String[]) null);
        });
    }

    @Test
    void testAddRowValidation() {
        // 测试正常添加行
        assertDoesNotThrow(() -> {
            table.addRow("Alice", 25, 95.5);
        });

        // 测试行长度不匹配
        assertThrows(IllegalArgumentException.class, () -> {
            table.addRow("Bob", 30); // 缺少一列
        });

        // 测试null行
        assertThrows(IllegalArgumentException.class, () -> {
            table.addRow((Object[]) null);
        });
    }

    @Test
    void testDeleteRowValidation() {
        table.addRow("Alice", 25, 95.5);
        table.addRow("Bob", 30, 88.0);

        // 测试正常删除
        assertDoesNotThrow(() -> {
            table.deleteRow(1);
        });

        // 测试无效索引
        assertThrows(IndexOutOfBoundsException.class, () -> {
            table.deleteRow(5); // 超出范围
        });

        assertThrows(IndexOutOfBoundsException.class, () -> {
            table.deleteRow(0); // 小于1
        });
    }

    @Test
    void testSortTableValidation() {
        table.addRow("Alice", 25, 95.5);
        table.addRow("Bob", 30, 88.0);

        // 测试正常排序
        assertDoesNotThrow(() -> {
            table.sortTable("Age");
        });

        // 测试不存在的字段
        assertThrows(IllegalArgumentException.class, () -> {
            table.sortTable("NonExistentField");
        });

        // 测试null字段名
        assertThrows(IllegalArgumentException.class, () -> {
            table.sortTable(null);
        });

        // 测试空字段名
        assertThrows(IllegalArgumentException.class, () -> {
            table.sortTable("");
        });
    }

    @Test
    void testAddFieldValidation() {
        // 测试正常添加字段
        assertDoesNotThrow(() -> {
            table.addField("Grade");
        });

        // 测试null字段名
        assertThrows(IllegalArgumentException.class, () -> {
            table.addField(null);
        });

        // 测试空字段名
        assertThrows(IllegalArgumentException.class, () -> {
            table.addField("");
        });
    }

    @Test
    void testTableOperations() {
        table.addRow("Alice", 25, 95.5);
        table.addRow("Bob", 30, 88.0);

        // 测试行数
        assertEquals(2, table.getRowCount());

        // 测试字段数
        assertEquals(3, table.getFieldCount());

        // 测试非空
        assertFalse(table.isEmpty());

        // 测试清空行
        table.clearTable();
        assertEquals(0, table.getRowCount());
        assertTrue(table.isEmpty());

        // 测试删除表
        table.deleteTable();
        assertEquals(0, table.getFieldCount());
    }

    @Test
    void testChineseCharacterHandling() {
        PrettyTable chineseTable = PrettyTable.fieldNames("姓名", "年龄", "分数");
        chineseTable.addRow("张三", 25, 95.5);
        chineseTable.addRow("李四", 30, 88.0);

        // 测试中文表格渲染
        String result = chineseTable.toString();
        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertTrue(result.contains("张三"));
        assertTrue(result.contains("李四"));
    }

    @Test
    void testNumberFormatting() {
        table.addRow("Alice", 25000, 95.567);
        table.addRow("Bob", 30000, 88.123);

        // 测试千分位格式
        table.comma(true);
        String result = table.toString();
        assertNotNull(result);
        assertFalse(result.isEmpty());
    }

    @Test
    void testConverterValidation() {
        // 测试null转换器
        assertThrows(IllegalArgumentException.class, () -> {
            table.converter(null);
        });
    }

    @Test
    void testParserValidation() {
        // 测试null解析器
        assertThrows(IllegalArgumentException.class, () -> {
            PrettyTable.parser(null);
        });
    }

    @Test
    void testFromStringValidation() {
        // 测试null文本
        assertThrows(IllegalArgumentException.class, () -> {
            table.fromString(null);
        });
    }

    @Test
    void testColorAndBorderSettings() {
        // 测试颜色和边框设置
        assertDoesNotThrow(() -> {
            table.color(true)
                 .border(false)
                 .fontColor("RED")
                 .borderColor("BLUE");
        });

        // 测试null颜色设置
        assertDoesNotThrow(() -> {
            table.fontColor(null)
                 .borderColor(null);
        });
    }
}
