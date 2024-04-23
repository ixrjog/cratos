package com.baiyi.cratos.shell.pagination;

import com.baiyi.cratos.common.util.StringFormatter;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author baiyi
 * @Date 2024/4/22 下午5:39
 * @Version 1.0
 */
public class TableFooter {

    public static final String ZH_CH = "共 {} 条, 第 {} 页/共 {} 页 < {} 条/页 >";

    public static final String EN_US = "Total {} Items, Page {}/{} pages in total < {}/page >";

    @Builder
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Pagination {

        private long totalNum;

        private int page;

        private int length;

        private String lang;

        public String toStr() {
            String str = "en-us".equals(lang) ? EN_US : ZH_CH;
            int tp = length == -1 ? 0 : (int) (totalNum - 1) / length + 1;
            return StringFormatter.arrayFormat(str, totalNum, page, tp, length);
        }

    }

}
