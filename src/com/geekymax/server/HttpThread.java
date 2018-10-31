package com.geekymax.server;

import java.io.InputStream;
import java.net.Socket;

/**
 * @author Max Huang
 */
public class HttpThread extends Thread {
    private Socket socket;

    HttpThread(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            Request request = new Request(socket.getInputStream());
            Response response = new Response(request, socket.getOutputStream());
            response.response();
            request.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
