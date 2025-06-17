package com.baiyi.cratos.ssh.core;

import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

import java.io.IOException;
import java.io.InputStream;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/6/16 11:42
 * &#064;Version 1.0
 */
public class JschRemoteActuator {

    public static void exec() {
        String host = "remote-server.example.com";
        String user = "username";
        String password = "password";
        int port = 22;
        String command = "ls -la; echo 'Hello from remote'";

        try {
            JSch jsch = new JSch();
            Session session = jsch.getSession(user, host, port);
            session.setPassword(password);

            // 跳过主机密钥验证
            java.util.Properties config = new java.util.Properties();
            config.put("StrictHostKeyChecking", "no");
            session.setConfig(config);

            session.connect();
            System.out.println("Connected to " + host);

            // 执行命令
            ChannelExec channel = (ChannelExec) session.openChannel("exec");
            channel.setCommand(command);

            // 获取命令输出
            InputStream in = channel.getInputStream();
            channel.connect();

            // 读取输出
            byte[] tmp = new byte[1024];
            while (true) {
                while (in.available() > 0) {
                    int i = in.read(tmp, 0, 1024);
                    if (i < 0) break;
                    System.out.print(new String(tmp, 0, i));
                }
                if (channel.isClosed()) {
                    if (in.available() > 0) continue;
                    System.out.println("Exit status: " + channel.getExitStatus());
                    break;
                }
                try {
                    Thread.sleep(1000);
                } catch (Exception ee) {
                }
            }

            channel.disconnect();
            session.disconnect();
            System.out.println("Disconnected from " + host);

        } catch (JSchException | IOException e) {
            e.printStackTrace();
        }
    }


}
