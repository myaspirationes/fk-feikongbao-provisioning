package com.yodoo.feikongbao.provisioning.util;

import com.jcraft.jsch.*;
import org.apache.commons.io.IOUtils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Vector;

/**
 * @Author houzhen
 * @Date 10:18 2019/6/12
 **/
public class SshUtils {

    private static final String ENCODING = "UTF-8";

    private static final int PORT = 22;

    private static final int TIMEOUT = 60 * 60 * 1000;

    public static Session getJschSession(DestHost destHost) throws JSchException {
        JSch jsch = new JSch();
        Session session = jsch.getSession(destHost.getUsername(), destHost.getHost(), destHost.getPort());
        session.setPassword(destHost.getPassword());
        // 第一次访问服务器时不用输入yes
        session.setConfig("StrictHostKeyChecking", "no");
        session.setTimeout(destHost.getTimeout());
        session.connect();
        return session;
    }

    public static String execCommandByJsch(DestHost destHost, String command) throws IOException, JSchException {
        return execCommandByJsch(destHost, command, ENCODING);
    }

    public static String execCommandByJsch(DestHost destHost, String command, String resultEncoding) throws IOException, JSchException {
        Session session = getJschSession(destHost);
        String result = execCommandByJsch(session, command, resultEncoding);
        session.disconnect();

        return result;
    }

    public static String execCommandByJsch(Session session, String command) throws IOException, JSchException {
        return execCommandByJsch(session, command, ENCODING);
    }

    public static String execCommandByJsch(Session session, String command, String resultEncoding) throws IOException, JSchException {
        ChannelExec channelExec = (ChannelExec) session.openChannel("exec");
        InputStream in = channelExec.getInputStream();
        channelExec.setCommand(command);
        channelExec.setErrStream(System.err);
        channelExec.connect();

        String result = IOUtils.toString(in, resultEncoding);

        channelExec.disconnect();

        return result;
    }

    public static void closeJSChSession(Session session) {
        if (session != null && session.isConnected()) {
            session.disconnect();
        }
    }

    public static void makeFileByJSCh(Session session, String filePath, String fileContent) throws JSchException, SftpException {
        ChannelSftp channelSftp = (ChannelSftp) session.openChannel("sftp");
        channelSftp.connect();
        // 文件输入流
        InputStream in = null;
        try {
            // 判断文件是否存在
            SftpATTRS stat = channelSftp.stat(filePath);
        } catch (SftpException e) {
            // 截取目录 如：/xxx/xxx/
            String dir = filePath.substring(0, filePath.lastIndexOf("/") + 1);
            // 创建文件夹
            SshUtils.makeDirsByJSCh(channelSftp, dir);
            // 文件内容输入
            in = new ByteArrayInputStream(fileContent.getBytes());
            // 上传文件
            channelSftp.put(in, filePath);
        } finally {
            if (channelSftp != null && channelSftp.isConnected()) {
                channelSftp.disconnect();
            }
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static boolean makeDirsByJSCh(ChannelSftp channelSftp, String dir) throws SftpException {
        String dirs = dir.substring(1, dir.length() - 1);
        String[] dirArr = dirs.split("/");
        String base = "";
        for (String d : dirArr) {
            base += "/" + d;
            if (dirExist(base + "/", channelSftp)) {
                continue;
            } else {
                channelSftp.mkdir(base + "/");
            }
        }
        return true;
    }

    private static boolean dirExist(final String dir, final ChannelSftp sftp) {
        try {
            Vector<?> vector = sftp.ls(dir);
            return null != vector;
        } catch (SftpException e) {
            return false;
        }
    }

    /**
     * 目标登录主机信息
     */
    public static class DestHost {
        private String host = "";
        private String username = "";
        private String password = "";
        private int port = PORT;
        private int timeout = TIMEOUT;

        public DestHost(String host, String username, String password) {
            this(host, username, password, PORT, TIMEOUT);
        }

        public DestHost(String host, int port, String username, String password) {
            this.host = host;
            this.username = username;
            this.password = password;
            this.port = port;
        }

        public DestHost(String host, String username, String password, int timeout) {
            this(host, username, password, PORT, timeout);
        }

        public DestHost(String host, String username, String password, int port, int timeout) {
            this.host = host;
            this.username = username;
            this.password = password;
            this.port = port;
            this.timeout = timeout;
        }

        public String getHost() {
            return host;
        }

        public void setHost(String host) {
            this.host = host;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public int getPort() {
            return port;
        }

        public void setPort(int port) {
            this.port = port;
        }

        public int getTimeout() {
            return timeout;
        }

        public void setTimeout(int timeout) {
            this.timeout = timeout;
        }

    }

}
