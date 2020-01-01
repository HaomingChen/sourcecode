package niochatroom;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.Set;

/**
 * NIO客户端 -> NIO编程模型的Java实现 -> 文件夹的读写, 读写UDP, TCP消息
 *
 * @author 58212
 * @date 2019-11-14 22:30
 */
public class NioServer {

    /**
     * 启动
     */
    public void start() throws IOException {
        /**
         * 1. 创建Selector
         * 在NIO网络模型中,Selector是客户端与Acceptor建立连接事件(from client), 和Acceptor注册
         * 连接事件(from Acceptor Handler)的中介
         * 循环检测注册事件就绪情况
         */
        Selector selector = Selector.open();
        /**
         * 2. 通过ServerSocketChannel创建channel通道
         */
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        /**
         * 3. 为channel通道绑定监听接口(client -> socket -> channel -> selector)
         */
        serverSocketChannel.bind(new InetSocketAddress(8000));
        /**
         * 4. 设置channel为非阻塞模式
         */
        serverSocketChannel.configureBlocking(false);
        /**
         * 5. 将channel注册到selector上,监听连接事件
         * 将channel注册进selector反而调用channel的register应该是观察者模式的思想。被观察者selector不需要关心channel的具体实现。
         * 而channel可以在合适的时间(例如初始化完成后?)利用传入的selector实例进行注册。
         * 且channel可以保证自身注册时初始化等操作已完成,规避了线程安全的问题。
         */
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        /**
         * 6. 循环等待新接入的连接
         */
        System.out.println("服务器端启动成功");
        for (; ; ) {
            /**
             * TODO 获取可用的channel数量
             */
            int readyChannels = selector.select();
            /**
             * TODO 为什么要这样
             */
            if (readyChannels == 0) continue;
            /**
             * 获取可用channel的集合
             */
            Set<SelectionKey> selectionKeys = selector.selectedKeys();
            Iterator iterator = selectionKeys.iterator();
            while (iterator.hasNext()) {
                /**
                 * selectionKey实例
                 */
                SelectionKey selectionKey = (SelectionKey) iterator.next();
                /**
                 * 移除Set中的当前selectionKey
                 */
                iterator.remove();
                /**
                 * 7. 根据就绪状态，调用对应方法处理业务逻辑
                 */
                /**
                 * 如果是接入事件
                 */
                if (selectionKey.isAcceptable()) {
                    acceptHandler(serverSocketChannel, selector);
                }
                /**
                 * 如果是可读事件
                 */
                if (selectionKey.isReadable()) {
                    readHandler(selectionKey, selector);
                }
            }
        }
    }

    /**
     * 接入事件处理器
     */
    private void acceptHandler(ServerSocketChannel serverSocketChannel, Selector selector)
            throws IOException {
        /**
         * 如果是要接入事件，创建socketChannel
         */
        SocketChannel socketChannel = serverSocketChannel.accept();
        /**
         * 将socketChannel设置为非阻塞工作模式
         */
        socketChannel.configureBlocking(false);
        /**
         * 将channel注册到selector上，监听可读事件
         */
        socketChannel.register(selector, SelectionKey.OP_READ);
        /**
         * 回复客户端提示信息
         */
        socketChannel.write(Charset.forName("UTF-8").encode("你与聊天室其他人都是不是朋友关系, 请注意隐私安全"));
    }

    /**
     * 可读事件处理器
     */
    private void readHandler(SelectionKey selectionKey, Selector selector) throws IOException {
        /**
         * 要从selectionKey中获取到已经就绪的channel
         */
        SocketChannel socketChannel = (SocketChannel) selectionKey.channel();
        /**
         * 创建buffer
         */
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
        /**
         * 循环读取客户端请求信息
         */
        String request = "";
        while (socketChannel.read(byteBuffer) > 0) {
            /**
             * 切换buffer为读模式
             */
            byteBuffer.flip();
            /**
             * 读取buffer中内容
             */
            request += Charset.forName("UTF-8").decode(byteBuffer);
        }
        /**
         * 将channel再次注册到selector上，监听他的可读事件
         */
        socketChannel.register(selector, SelectionKey.OP_READ);
        /**
         * 将客户端发送的请求信息 广播给其他客户端
         */
        if (request.length() > 0) {
            //广播给其他客户端
            System.out.println("::" + request);
            broadCast(selector, socketChannel, request);
        }
    }

    /**
     * 广播给其他客户端
     */
    private void broadCast(Selector selector, SocketChannel sourceChannel, String request) {
        /**
         * 获取到所有已接入的客户端channel
         */
        Set<SelectionKey> selectionKeySet = selector.keys();
        /**
         * 循环向所有channel广播信息
         */
        selectionKeySet.forEach(selectionKey -> {
            Channel targetChannel = selectionKey.channel();
            //剔除发消息的客户端
            if (targetChannel instanceof SocketChannel && targetChannel != sourceChannel) {
                try {
                    //将信息发送到targetChannel客户端
                    ((SocketChannel) targetChannel).write(Charset.forName("UTF-8").encode(request));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public static void main(String[] args) throws IOException {
        NioServer nioServer = new NioServer();
        nioServer.start();
    }
}