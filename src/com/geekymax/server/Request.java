package com.geekymax.server;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * @author Max Huang
 */
class Request {
    private String webRoot = "C:/webroot/";
    private InputStream inputStream;
    private HttpMethod method;
    private String resourceName;
    private String content;
    private BufferedReader bufferedReader;

    Request(InputStream inputStream) {
        this.inputStream = inputStream;
        parse();
    }

    private void parse() {
        bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        try {
            String s = bufferedReader.readLine();
            resourceName = parseResourceName(s);
            method = parseMethod(s);
            content = parseContent(bufferedReader);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private String parseResourceName(String s) {
        String resourceName = s.substring(s.indexOf(' ') + 1);
        resourceName = resourceName.substring(0, resourceName.indexOf(' '));
        if ("".equals(resourceName)) {
            resourceName = "index.html";
        }
        if (resourceName.startsWith("/")) {
            resourceName = resourceName.substring(1);
        }
        return resourceName;
    }

    private HttpMethod parseMethod(String s) {
        String method = s.split(" ")[0].trim().toLowerCase();
        if ("get".equals(method)) {
            return HttpMethod.GET;
        } else if ("post".equals(method)) {
            return HttpMethod.POST;
        } else {
            return null;
        }
    }

    private String parseContent(BufferedReader bufferedReader) {
        int contentLength = 0;
        try {
//            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String line = bufferedReader.readLine();
            StringBuffer stringBuffer = new StringBuffer();
            boolean inContent = false;
            while (line != null) {
                line = line.trim();
                System.out.println(line);
                if ("".equals(line)) {
                    System.out.println("start content:-----");
                    break;
                } else if (line.startsWith("Content-Length")) {
                    String[] strings = line.split(":");
                    if (strings.length == 2) {
                        contentLength = Integer.parseInt(strings[1].trim());
                    }
                }
                line = bufferedReader.readLine();
            }
            char[] buffer = new char[contentLength];
            bufferedReader.read(buffer, 0, contentLength);
            return new String(buffer);
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    void close() {
        try {
            inputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    InputStream getInputStream() {
        return inputStream;
    }

    HttpMethod getMethod() {
        return method;
    }

    String getResourceName() {
        return resourceName;
    }

    String getContent() {
        return content;
    }

    public void setWebRoot(String webRoot) {
        this.webRoot = webRoot;
    }

}
