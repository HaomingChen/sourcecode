package io;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class BIOPlainEchoServer {
    public void serve(int port) throws IOException {
        //将ServerSocket绑定到指定的端口里
        final ServerSocket socket = new ServerSocket(port);
        while (true) {
            //阻塞直到收到新的客户端连接
            final Socket clientSocket = socket.accept();
            System.out.println("Accepted connection from " + clientSocket);
            //创建一个子线程去处理客户端的请求
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try (BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()))) {
                        PrintWriter writer = new PrintWriter(clientSocket.getOutputStream(), true);
                        //从客户端读取数据并原封不动回写回去
                        while (true) {
                            writer.println(reader.readLine());
                            writer.flush();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
    }

    public void improvedServe(int port) throws IOException {
        //将ServerSocket绑定到指定的端口里
        final ServerSocket socket = new ServerSocket(port);
        //创建一个线程池
        ExecutorService executorService = Executors.newFixedThreadPool(6);
        while (true) {
            //阻塞直到收到新的客户端连接
            final Socket clientSocket = socket.accept();
            System.out.println("Accepted connection from " + clientSocket);
            //将请求提交给线程池去执行
            executorService.execute(() -> {
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()))) {
                    PrintWriter writer = new PrintWriter(clientSocket.getOutputStream(), true);
                    //从客户端读取数据并原封不动回写回去
                    while (true) {
                        writer.println(reader.readLine());
                        writer.flush();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        }
    }
}
