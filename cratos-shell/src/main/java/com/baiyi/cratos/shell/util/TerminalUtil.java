package com.baiyi.cratos.shell.util;

import com.baiyi.cratos.ssh.core.handler.RemoteInvokeHandler;
import com.baiyi.cratos.ssh.core.model.JSchSession;
import com.baiyi.cratos.ssh.core.model.JSchSessionHolder;
import com.jcraft.jsch.ChannelShell;
import org.jline.terminal.Attributes;
import org.jline.terminal.Size;
import org.jline.terminal.Terminal;

import java.util.EnumSet;

/**
 * @Author baiyi
 * @Date 2021/6/7 9:55 上午
 * @Version 1.0
 */
public final class TerminalUtil {

    private static final int OFF = 0;

    private static final int ON = 1;


    private TerminalUtil() {
    }

    /**
     * 行模式支持 Ctrl+C
     * https://ftp.gnu.org/old-gnu/Manuals/glibc-2.2.3/html_node/libc_355.html
     *
     * @param terminal
     */
    public static void enterRawMode(Terminal terminal) {
        Attributes prvAttr = terminal.getAttributes();
        Attributes newAttr = new Attributes(prvAttr);
        // adopted from org.jline.builtins.Nano
        // see also https://en.wikibooks.org/wiki/Serial_Programming/termios#Basic_Configuration_of_a_Serial_Interface
        // no line processing
        // canonical mode off, echo off, echo newline off, extended input processing off / 规范模式关闭，回显关闭，回显换行关闭，扩展输入处理关闭
        newAttr.setLocalFlags(EnumSet.of(
                // 行模式，屏蔽则变成自定义模式
                Attributes.LocalFlag.ICANON,
                // 回显，如果屏蔽则不显示输入的字符，像输入密码一样
                Attributes.LocalFlag.ECHO, Attributes.LocalFlag.IEXTEN), false);
        // turn off input processing / 关闭输入处理
        newAttr.setInputFlags(EnumSet.of(Attributes.InputFlag.IXON,
                // 按下回车换行，屏蔽则不换行打印一个^M
                Attributes.InputFlag.ICRNL, Attributes.InputFlag.INLCR), false);

        // 使终端产生的信号(Ctrl+c/Ctrl+z等)起作用，屏蔽则忽略信号
        newAttr.setLocalFlags(EnumSet.of(Attributes.LocalFlag.ISIG), true);

        // one input byte is enough to return from read, inter-character timer off
        // VMIN 1 / VTIME 0 / VINTR 0
        // http://unixwiz.net/techtips/termios-vmin-vtime.html
        newAttr.setControlChar(Attributes.ControlChar.VMIN, ON);
        newAttr.setControlChar(Attributes.ControlChar.VTIME, OFF);
        newAttr.setControlChar(Attributes.ControlChar.VINTR, OFF);
        terminal.setAttributes(newAttr);
    }

    public static void resize(String sessionId, String instanceId, Size size) {
        JSchSession jSchSession = JSchSessionHolder.getSession(sessionId, instanceId);
        assert jSchSession != null;
        RemoteInvokeHandler.setChannelPtySize((ChannelShell) jSchSession.getChannel(), size);
    }

}