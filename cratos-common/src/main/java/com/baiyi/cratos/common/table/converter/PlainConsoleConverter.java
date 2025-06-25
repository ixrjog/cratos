package com.baiyi.cratos.common.table.converter;

/**
 * Plain console converter for PrettyTable
 */
public class PlainConsoleConverter extends ConsoleConverter {

    private final StringBuilder sb = new StringBuilder();

    @Override
    ConsoleConverter clear() {
        sb.setLength(0); // set length of buffer to 0
        sb.trimToSize(); // trim the underlying buffer
        return this;
    }

    @Override
    ConsoleConverter af(String text) {
        sb.append(text);
        return this;
    }

    @Override
    ConsoleConverter ab(String text) {
        sb.append(text);
        return this; // 修复：返回this而不是null
    }

    @Override
    public String toString() {
        return sb.toString();
    }

}