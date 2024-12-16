package com.baiyi.cratos.common.util.beetl;

import com.baiyi.cratos.common.exception.BeetlTemplateException;
import org.beetl.core.ConsoleErrorHandler;
import org.beetl.core.GroupTemplate;
import org.beetl.core.exception.BeetlException;
import org.beetl.core.exception.ErrorInfo;

import java.io.Writer;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/12/16 17:22
 * &#064;Version 1.0
 */
public class BeetlErrorHandler extends ConsoleErrorHandler {

    public BeetlErrorHandler() {
    }

    public void processException(BeetlException ex, GroupTemplate groupTemplate, Writer writer) {
        ErrorInfo error = new ErrorInfo(ex);
        if (error.errorCode.equals("CLIENT_IO_ERROR_ERROR")) {
            BeetlTemplateException.runtime("Beetl client io error.");
        } else {
            int line = error.errorTokenLine;
            String sb = "Template " + error.type + ":" + error.errorTokenText + " at" + (line != 0 ? line + " line" : "");
            BeetlTemplateException.runtime(sb);
        }
    }

}