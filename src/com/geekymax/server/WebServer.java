package com.geekymax.server;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @author Max Huang
 */
public class WebServer {

    public void await(){
        ServerSocket serverSocket = null;
        int port = 8080;
        String ip = "127.0.0.1";
        try {
            serverSocket = new ServerSocket(8080, 1, InetAddress.getByName("127.0.0.1"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (serverSocket == null) {
            return;
        }
        while (true) {
            try {
                Socket socket = serverSocket.accept();
                HttpThread httpThread = new HttpThread(socket);
                httpThread.start();
            }catch (Exception e){
                e.printStackTrace();
            }

        }
    }

    public static void main(String[] args) {
        WebServer webServer = new WebServer();
        webServer.await();
    }
}
