package com.group4.paperAnalyst.service;

import com.jcraft.jsch.*;
import org.springframework.stereotype.Service;

import java.io.*;

@Service
public class StreamService {

    private static final String USER_NAME = "hadoop";
    private static final String PASSWORD = "123456";
    private static final String HOST = "172.19.241.172";

    public String startPaperCountStream() {
        String command = "nohup ~/scripts/start-stream-paper-count.sh &";
        try {
            this.executeCommand(command);
            return "success";
        } catch (Exception e) {
            e.printStackTrace();
            return "failed";
        }
    }

    private void executeCommand(String command) throws Exception {
        Session session = null;
        Channel channel = null;
        try {
            session = new JSch().getSession(USER_NAME, HOST);
            session.setPassword(PASSWORD);
            session.setConfig("StrictHostKeyChecking", "no");
            session.connect();

            channel = session.openChannel("shell");
            channel.connect();
            InputStream inputStream = channel.getInputStream();//从远程端到达的所有数据都能从这个流中读取到
            OutputStream outputStream = channel.getOutputStream();
            PrintWriter printWriter = new PrintWriter(outputStream);

            printWriter.println(command);
            printWriter.println("exit");
            printWriter.flush();

            BufferedReader in = new BufferedReader(new InputStreamReader(inputStream));
            String msg = null;
            while ((msg = in.readLine()) != null) {
                System.out.println(msg);
            }
            in.close();
        } finally {
            if (session != null) {
                session.disconnect();
            }
            if (channel != null) {
                channel.disconnect();
            }
        }

    }
}
