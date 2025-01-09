package com.baiyi.cratos.domain.param;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/9/25 10:24
 * &#064;Version 1.0
 */
public interface HasTerminalSize {

    double W = 7.0;
    double H = 14.4166;

    Integer getWidth();
    Integer getHeight();
    Integer getCols();
    Integer getRows();

    default int getTerminalCols() {
        if (getCols() != null) {
            return getCols();
        }
        return (int) Math.floor(getWidth() / W);
    }

    default int getTerminalRows() {
        if (getRows() != null) {
            return getRows();
        }
        return (int) Math.floor(getHeight() / H);
    }

}
