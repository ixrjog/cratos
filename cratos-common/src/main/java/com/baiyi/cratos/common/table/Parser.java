package com.baiyi.cratos.common.table;

import java.io.IOException;

/**
 * Interface for Parser.
 */
public interface Parser {

    PrettyTable parse(final String text) throws IOException;

}