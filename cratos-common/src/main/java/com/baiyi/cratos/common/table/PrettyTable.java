package com.baiyi.cratos.common.table;


import com.baiyi.cratos.common.table.converter.PlainConsoleConverter;
import com.baiyi.cratos.common.table.parser.JsonParser;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.io.IOException;
import java.util.*;

/**
 * PrettyTable class.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PrettyTable {

    public List<String> fieldNames = new ArrayList<>();
    public List<Object[]> rows = new ArrayList<>();
    public boolean comma = false;
    public boolean border = true;
    public boolean color = false;
    public String fontColor = "DEFAULT";
    public String borderColor = "DEFAULT";
    private Parser parser = new JsonParser();
    private Converter converter = new PlainConsoleConverter();

    /**
     * @param fieldNames
     * @return
     */
    public static PrettyTable fieldNames(final String... fieldNames) {
        PrettyTable pt = new PrettyTable();
        pt.fieldNames.addAll(Arrays.asList(fieldNames));
        return pt;
    }

    public static PrettyTable fieldNames(final Iterator<String> fieldNames) {
        PrettyTable pt = new PrettyTable();
        fieldNames.forEachRemaining(pt.fieldNames::add);
        return pt;
    }

    public PrettyTable addRow(final Object... row) {
        this.rows.add(row);
        return this;
    }

    public PrettyTable deleteRow(final int num) {
        this.rows.remove(num - 1);
        return this;
    }

    public PrettyTable addField(final String fieldName) {
        this.fieldNames.add(fieldName);
        return this;
    }

    public PrettyTable comma(final boolean comma) {
        this.comma = comma;
        return this;
    }

    public PrettyTable border(final boolean border) {
        this.border = border;
        return this;
    }

    public PrettyTable color(final boolean color) {
        this.color = color;
        return this;
    }

    public PrettyTable fontColor(final String colorName) {
        this.fontColor = colorName;
        return this;
    }

    public PrettyTable borderColor(final String colorName) {
        this.borderColor = colorName;
        return this;
    }

    public static PrettyTable parser(final Parser parser) {
        PrettyTable pt = new PrettyTable();
        pt.parser = parser;
        return pt;
    }

    public PrettyTable fromString(String text) throws IOException {
        return parser.parse(text);
    }

    public PrettyTable converter(final Converter converter) {
        this.converter = converter;
        return this;
    }

    public PrettyTable sortTable(final String fieldName) {
        return sortTable(fieldName, false);
    }

    public PrettyTable sortTable(final String fieldName, final boolean reverse) {
        int idx = fieldNames.indexOf(fieldName);

        if (idx == -1) {
            throw new IllegalArgumentException("Field name '" + fieldName + "' not found");
        }

        rows.sort((o1, o2) -> {
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

            // Handle percentage
            boolean isPercentage = str.endsWith("%");
            if (isPercentage) {
                str = str.substring(0, str.length() - 1);
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

    public PrettyTable clearTable() {
        rows.clear();
        return this;
    }

    public PrettyTable deleteTable() {
        rows.clear();
        fieldNames.clear();
        return this;
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