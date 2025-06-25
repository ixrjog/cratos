package com.baiyi.cratos.common.table;

/**
 * PrettyTable 使用示例
 */
public class PrettyTableExample {

    public static void main(String[] args) {
        // 示例1：基本使用
        System.out.println("=== 基本使用示例 ===");
        PrettyTable basicTable = PrettyTable.fieldNames("Name", "Age", "Score")
                .addRow("Alice", 25, 95.5)
                .addRow("Bob", 30, 88.0)
                .addRow("Charlie", 22, 92.3);
        
        System.out.println(basicTable);

        // 示例2：中文支持
        System.out.println("\n=== 中文支持示例 ===");
        PrettyTable chineseTable = PrettyTable.fieldNames("姓名", "年龄", "分数")
                .addRow("张三", 25, 95.5)
                .addRow("李四", 30, 88.0)
                .addRow("王五", 22, 92.3);
        
        System.out.println(chineseTable);

        // 示例3：数字格式化
        System.out.println("\n=== 数字格式化示例 ===");
        PrettyTable numberTable = PrettyTable.fieldNames("Product", "Price", "Sales")
                .addRow("iPhone", 12999, 150000)
                .addRow("MacBook", 25999, 80000)
                .addRow("iPad", 8999, 200000)
                .comma(true); // 启用千分位分隔符
        
        System.out.println(numberTable);

        // 示例4：排序功能
        System.out.println("\n=== 排序功能示例 ===");
        PrettyTable sortTable = PrettyTable.fieldNames("Name", "Age", "Score")
                .addRow("Alice", 25, 95.5)
                .addRow("Bob", 30, 88.0)
                .addRow("Charlie", 22, 92.3)
                .sortTable("Age"); // 按年龄排序
        
        System.out.println("按年龄排序：");
        System.out.println(sortTable);

        // 按分数倒序排序
        sortTable.sortTable("Score", true);
        System.out.println("按分数倒序排序：");
        System.out.println(sortTable);

        // 示例5：无边框表格
        System.out.println("\n=== 无边框表格示例 ===");
        PrettyTable noBorderTable = PrettyTable.fieldNames("Name", "Status", "Progress")
                .addRow("Task 1", "Complete", "100%")
                .addRow("Task 2", "In Progress", "75%")
                .addRow("Task 3", "Pending", "0%")
                .border(false);
        
        System.out.println(noBorderTable);

        // 示例6：表格操作
        System.out.println("\n=== 表格操作示例 ===");
        PrettyTable operationTable = PrettyTable.fieldNames("ID", "Name", "Value")
                .addRow(1, "Item A", 100)
                .addRow(2, "Item B", 200)
                .addRow(3, "Item C", 300);
        
        System.out.println("原始表格：");
        System.out.println(operationTable);
        
        // 删除第二行
        operationTable.deleteRow(2);
        System.out.println("删除第二行后：");
        System.out.println(operationTable);
        
        // 添加新行
        operationTable.addRow(4, "Item D", 400);
        System.out.println("添加新行后：");
        System.out.println(operationTable);

        // 示例7：错误处理演示
        System.out.println("\n=== 错误处理演示 ===");
        try {
            PrettyTable errorTable = PrettyTable.fieldNames("A", "B", "C");
            errorTable.addRow("1", "2"); // 这会抛出异常，因为列数不匹配
        } catch (IllegalArgumentException e) {
            System.out.println("捕获到预期的异常: " + e.getMessage());
        }

        try {
            PrettyTable errorTable = PrettyTable.fieldNames("A", "B", "C");
            errorTable.addRow("1", "2", "3");
            errorTable.deleteRow(5); // 这会抛出异常，因为索引超出范围
        } catch (IndexOutOfBoundsException e) {
            System.out.println("捕获到预期的异常: " + e.getMessage());
        }

        // 示例8：百分比和特殊数字处理
        System.out.println("\n=== 百分比和特殊数字处理示例 ===");
        PrettyTable percentTable = PrettyTable.fieldNames("Metric", "Value", "Percentage")
                .addRow("CPU Usage", "2.5GB", "75%")
                .addRow("Memory", "8.2GB", "82%")
                .addRow("Disk", "120GB", "45%")
                .sortTable("Percentage"); // 按百分比排序
        
        System.out.println(percentTable);
    }
}
