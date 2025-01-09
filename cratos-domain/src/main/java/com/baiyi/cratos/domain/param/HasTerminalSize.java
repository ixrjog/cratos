package com.baiyi.cratos.domain.param;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/9/25 10:24
 * &#064;Version 1.0
 */
public interface HasTerminalSize {

    double W = 7.0;
    double H = 14.4166;

    int getWidth();

    int getHeight();

    default int getCols() {
        return (int) Math.floor(getWidth() / W);
    }

    default int getRows() {
        return (int) Math.floor(getHeight() / H);
    }

}
