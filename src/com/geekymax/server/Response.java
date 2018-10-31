package com.geekymax.server;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Max Huang
 */
class Response {
    public static final String LOGIN = "3160102267";
    public static final String PASSWORD = "2267";
    private String webRoot = "C:/webroot/";
    private static final int BUFFER_SIZE = 1024;
    private OutputStream outputStream;
    private Request request;
    private String resourcePath;
    private HttpMethod httpMethod;

    Response(Request request, OutputStream outputStream) {
        this.outputStream = outputStream;
        this.request = request;
        this.resourcePath = webRoot + request.getResourceName();
        this.httpMethod = request.getMethod();
    }

    void response() {
        switch (httpMethod) {
            case GET:
                doGet();
                break;
            case POST:
                doPost();
                break;
            default:
                break;
        }
    }

    private void doGet() {
        File file = new File(resourcePath);
        PrintWriter printWriter = new PrintWriter(outputStream);
        if (file.exists()) {
            System.out.println(file.getName() + " start send");
            printWriter.println("HTTP/1.0 200 OK");
            printWriter.println("MIME_version:1.0");
            if (file.getName().endsWith(".jpg")) {
                printWriter.println("Content_Type:image/jpeg");
            } else {
                printWriter.println("Content_Type:text/html");
            }
            int len = (int) file.length();
            printWriter.println("Content_Length:" + len);
            //报文头和信息之间要空一行
            printWriter.println("");
            printWriter.flush();
            try {
                byte[] bytes = new byte[BUFFER_SIZE];
                FileInputStream fileInputStream = new FileInputStream(file);
                int ch = fileInputStream.read(bytes);
                while (ch != -1) {
                    outputStream.write(bytes);
                    ch = fileInputStream.read(bytes);
                }
                outputStream.flush();
                printWriter.close();
                fileInputStream.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {

            printWriter.println("HTTP/1.1 404 File Not Found");
            printWriter.println("Content-Type:text/html");
            printWriter.println("Content-Length:23");
            printWriter.println("");
            printWriter.println("<h1>File Not Found</h1>");
            try {
                printWriter.flush();
                outputStream.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void doPost() {
        PrintWriter printWriter = new PrintWriter(outputStream);
        if ("dopost".equals(request.getResourceName())) {
            String content = request.getContent();
            Map<String, String> map = parseContentToMap(content);
            String result;
            if (LOGIN.equals(map.get("login")) && PASSWORD.equals(map.get("pass"))) {
                result = "<html><body>login successfully!</body></html>";

            } else {
                result = "<html><body>login fail!</body></html>";
            }
            printWriter.println("HTTP/1.1 200 OK");
            printWriter.println("Content-Type:text/html");
            printWriter.println("Content-Length:" + result.length());
            printWriter.println("");
            printWriter.println(result);
        } else {
            printWriter.println("HTTP/1.1 404 File Not Found");
            printWriter.println("Content-Type:text/html");
            printWriter.println("Content-Length:0");
            printWriter.println("");
        }
        try {
            printWriter.flush();
            printWriter.close();
            outputStream.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private Map<String, String> parseContentToMap(String content) {
        Map<String, String> map = new HashMap<>();
        String[] items = content.split("&");

        for (String item : items) {
            String[] splitItem = item.split("=");
            if (splitItem.length == 2) {
                map.put(splitItem[0], splitItem[1]);
            }
        }
        return map;
    }
}
