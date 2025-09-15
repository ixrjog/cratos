package com.baiyi.cratos.common.table;


import com.baiyi.cratos.common.table.converter.PlainConsoleConverter;
import com.baiyi.cratos.common.table.parser.JsonParser;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.IOException;
import java.util.*;

/**
 * PrettyTable class.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PrettyTable {

    private final List<String> fieldNames = new ArrayList<>();
    private final List<Object[]> rows = new ArrayList<>();
    @Getter
    private boolean comma = false;
    @Getter
    private boolean border = true;
    @Getter
    private boolean color = false;
    @Getter
    private String fontColor = "DEFAULT";
    @Getter
    private String borderColor = "DEFAULT";
    private Parser parser = new JsonParser();
    private Converter converter = new PlainConsoleConverter();

    // Getter methods for accessing private fields
    public List<String> getFieldNames() {
        return new ArrayList<>(fieldNames);
    }

    public List<Object[]> getRows() {
        return new ArrayList<>(rows);
    }

    /**
     * Create PrettyTable with field names
     * @param fieldNames field names array
     * @return PrettyTable instance
     */
    public static PrettyTable fieldNames(final String... fieldNames) {
        if (fieldNames == null || fieldNames.length == 0) {
            throw new IllegalArgumentException("Field names cannot be null or empty");
        }
        PrettyTable pt = new PrettyTable();
        pt.fieldNames.addAll(Arrays.asList(fieldNames));
        return pt;
    }

    /**
     * Create PrettyTable with field names from iterator
     * @param fieldNames field names iterator
     * @return PrettyTable instance
     */
    public static PrettyTable fieldNames(final Iterator<String> fieldNames) {
        if (fieldNames == null) {
            throw new IllegalArgumentException("Field names iterator cannot be null");
        }
        PrettyTable pt = new PrettyTable();
        fieldNames.forEachRemaining(fieldName -> {
            if (fieldName != null) {
                pt.fieldNames.add(fieldName);
            }
        });
        if (pt.fieldNames.isEmpty()) {
            throw new IllegalArgumentException("At least one field name is required");
        }
        return pt;
    }

    /**
     * Add a row to the table
     * @param row row data
     * @return PrettyTable instance
     * @throws IllegalArgumentException if row length doesn't match field count
     */
    public PrettyTable addRow(final Object... row) {
        if (row == null) {
            throw new IllegalArgumentException("Row cannot be null");
        }
        if (!fieldNames.isEmpty() && row.length != fieldNames.size()) {
            throw new IllegalArgumentException(
                String.format("Row length (%d) doesn't match field count (%d)", 
                             row.length, fieldNames.size()));
        }
        this.rows.add(row.clone()); // 防止外部修改
        return this;
    }

    /**
     * Delete a row by index (1-based)
     * @param num row number (1-based)
     * @return PrettyTable instance
     * @throws IndexOutOfBoundsException if index is invalid
     */
    public PrettyTable deleteRow(final int num) {
        if (num < 1 || num > rows.size()) {
            throw new IndexOutOfBoundsException(
                String.format("Row index %d is out of bounds [1, %d]", num, rows.size()));
        }
        this.rows.remove(num - 1);
        return this;
    }

    /**
     * Add a field to the table
     * @param fieldName field name
     * @return PrettyTable instance
     * @throws IllegalArgumentException if fieldName is null or empty
     */
    public PrettyTable addField(final String fieldName) {
        if (fieldName == null || fieldName.trim().isEmpty()) {
            throw new IllegalArgumentException("Field name cannot be null or empty");
        }
        this.fieldNames.add(fieldName);
        return this;
    }

    /**
     * Set comma formatting for numbers
     * @param comma enable comma formatting
     * @return PrettyTable instance
     */
    public PrettyTable comma(final boolean comma) {
        this.comma = comma;
        return this;
    }

    /**
     * Set border display
     * @param border enable border
     * @return PrettyTable instance
     */
    public PrettyTable border(final boolean border) {
        this.border = border;
        return this;
    }

    /**
     * Set color display
     * @param color enable color
     * @return PrettyTable instance
     */
    public PrettyTable color(final boolean color) {
        this.color = color;
        return this;
    }

    /**
     * Set font color
     * @param colorName color name
     * @return PrettyTable instance
     */
    public PrettyTable fontColor(final String colorName) {
        this.fontColor = colorName != null ? colorName : "DEFAULT";
        return this;
    }

    /**
     * Set border color
     * @param colorName color name
     * @return PrettyTable instance
     */
    public PrettyTable borderColor(final String colorName) {
        this.borderColor = colorName != null ? colorName : "DEFAULT";
        return this;
    }

    /**
     * Create PrettyTable with custom parser
     * @param parser custom parser
     * @return PrettyTable instance
     * @throws IllegalArgumentException if parser is null
     */
    public static PrettyTable parser(final Parser parser) {
        if (parser == null) {
            throw new IllegalArgumentException("Parser cannot be null");
        }
        PrettyTable pt = new PrettyTable();
        pt.parser = parser;
        return pt;
    }

    /**
     * Parse text using configured parser
     * @param text text to parse
     * @return PrettyTable instance
     * @throws IOException if parsing fails
     * @throws IllegalArgumentException if text is null
     */
    public PrettyTable fromString(String text) throws IOException {
        if (text == null) {
            throw new IllegalArgumentException("Text cannot be null");
        }
        return parser.parse(text);
    }

    /**
     * Set custom converter
     * @param converter custom converter
     * @return PrettyTable instance
     * @throws IllegalArgumentException if converter is null
     */
    public PrettyTable converter(final Converter converter) {
        if (converter == null) {
            throw new IllegalArgumentException("Converter cannot be null");
        }
        this.converter = converter;
        return this;
    }

    /**
     * Sort table by field name
     * @param fieldName field name to sort by
     * @return PrettyTable instance
     * @throws IllegalArgumentException if field name not found
     */
    public PrettyTable sortTable(final String fieldName) {
        return sortTable(fieldName, false);
    }

    /**
     * Sort table by field name with optional reverse order
     * @param fieldName field name to sort by
     * @param reverse reverse sort order
     * @return PrettyTable instance
     * @throws IllegalArgumentException if field name not found
     */
    public PrettyTable sortTable(final String fieldName, final boolean reverse) {
        if (fieldName == null || fieldName.trim().isEmpty()) {
            throw new IllegalArgumentException("Field name cannot be null or empty");
        }
        
        int idx = fieldNames.indexOf(fieldName);
        if (idx == -1) {
            throw new IllegalArgumentException("Field name '" + fieldName + "' not found");
        }

        rows.sort((o1, o2) -> {
            // 边界检查
            if (o1.length <= idx || o2.length <= idx) {
                return 0; // 如果数据不完整，保持原顺序
            }
            
            Object v1 = o1[idx];
            Object v2 = o2[idx];

            // 处理 null
            if (v1 == null && v2 == null) {
                return 0;
            } else if (v1 == null) {
                return reverse ? 1 : -1;
            } else if (v2 == null) {
                return reverse ? -1 : 1;
            }

            // 优先按数字比较，确保浮点数精度
            Double n1 = tryParseDouble(v1);
            Double n2 = tryParseDouble(v2);

            if (n1 != null && n2 != null) {
                int cmp = Double.compare(n1, n2);
                return reverse ? -cmp : cmp;
            } else {
                // 至少有一个不是数字，按字符串比较
                String s1 = String.valueOf(v1);
                String s2 = String.valueOf(v2);
                int cmp = s1.compareTo(s2);
                return reverse ? -cmp : cmp;
            }
        });

        return this;
    }

    /**
     * Try to parse an object to Double
     *
     * @param obj Object to parse
     * @return Double value if parsing succeeds, null otherwise
     */
    private Double tryParseDouble(Object obj) {
        if (obj instanceof Number) {
            return ((Number) obj).doubleValue();
        } else if (obj instanceof String) {
            String str = ((String) obj).trim();
            
            if (str.isEmpty()) {
                return null;
            }

            // Handle percentage
            boolean isPercentage = str.endsWith("%");
            if (isPercentage) {
                str = str.substring(0, str.length() - 1).trim();
            }

            // Remove thousand separators and ensure decimal point
            str = str.replace(",", "");

            try {
                double value = Double.parseDouble(str);
                return isPercentage ? value / 100.0 : value;
            } catch (NumberFormatException e) {
                return null;
            }
        }
        return null;
    }

    /**
     * Clear all rows but keep field names
     * @return PrettyTable instance
     */
    public PrettyTable clearTable() {
        rows.clear();
        return this;
    }

    /**
     * Clear both rows and field names
     * @return PrettyTable instance
     */
    public PrettyTable deleteTable() {
        rows.clear();
        fieldNames.clear();
        return this;
    }

    /**
     * Get row count
     * @return number of rows
     */
    public int getRowCount() {
        return rows.size();
    }

    /**
     * Get field count
     * @return number of fields
     */
    public int getFieldCount() {
        return fieldNames.size();
    }

    /**
     * Check if table is empty
     * @return true if no rows, false otherwise
     */
    public boolean isEmpty() {
        return rows.isEmpty();
    }

    @Override
    public String toString() {
        if (converter instanceof Bordered) {
            ((Bordered) converter).border(border);
        }
        if (converter instanceof Colored) {
            ((Colored) converter).fontColor(fontColor)
                    .borderColor(borderColor);
        }
        return converter.convert(this);
    }

}