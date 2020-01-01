package niochatroom;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.Scanner;

/**
 * Nio客户端
 *
 * @author 58212
 * @date 2019-11-15 23:05
 */
public class NioClient {
    /**
     * 启动客户端
     */
    public void start(String nickName) throws IOException {
        /**
         * 连接服务器端
         */
        SocketChannel socketChannel = SocketChannel.open(new InetSocketAddress("127.0.0.1", 8000));
        /**
         * 接收服务器端响应
         */
        //新开线程，专门负责来接收服务器端的响应数据
        //selector，socketChannel, 注册
        Selector selector = Selector.open();
        socketChannel.configureBlocking(false);
        socketChannel.register(selector, SelectionKey.OP_READ);
        new Thread(new NioClientHandler(selector)).start();

        /**
         * 向服务器端发送数据
         */
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()) {
            String request = scanner.nextLine();
            if (request != null && request.length() > 0) {
                socketChannel.write(Charset.forName("UTF-8").encode(nickName + " : " + request));
            }
        }

        /**
         * 接收服务器端响应
         */

    }

    public static void main(String[] args) throws IOException {
        new NioClient().start("clientA");
    }

}
