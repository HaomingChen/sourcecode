1. ctrl + f5利用在http request head中加入Cache-Control: no-cache与
Pargma: no-cache避免请求到前端缓存如Varnish代理缓存服务器。且ctrl + f5会
避免请求浏览器自身缓存直接向目标url发起请求

2. DNS域名解析过程
a) 浏览器缓存 b) 操作系统文件 c) local DNS server(LDNS from ISP) 查看缓存(有 -> 返回)
d) Root Server e) gTLD 主域名服务器 f) gTLD主域名服务器返回此域名对应的Name Server
域名服务器 g) Name Server 返回IP和TTL返回DNS Server域名服务器 h)解析结果
返回用户

浏览器缓存 -> 系统缓存 -> 路由器缓存 -> IPS服务器缓存 -> 根域名服务器缓存 -> 顶级域名服务器缓存

3.1 redis基本数据类型
String: 最基本的数据类型, 二进制安全 
命令: set, get, incr
Hash:String元素组成的字典, 适合用于存储对象
命令：hmset, hget
List:列表,按照String元素插入顺序排序(栈结构) —> 应用: 最新消息排行
命令：lpush, lrange
Set: String元素组成的无序集合，通过哈希表实现, 不允许重复 --> 应用： 关注,粉丝求交集: 对不同的人做共同关注操作
命令: sadd, smembers
Sorted Set: 通过分数来为集合中的成员进行从小到大的排列
命令: zadd, zrangebyscore
HyperLogLog, Geo

3.11 
分布式锁
命令: setnx, expire

异步队列
命令: rpush, lpop, blpop(等待一段时间)
subscrible, publish 实现生产消费者模式

3.12
redis String的底层数据结构: sds
len free buf[] -> 扩容策略: 当字符串长度小于 1M 时，扩容都是加倍现有的空间，如果超过 1M，扩容时一次只会多扩 1M 的空间。(字符串最大长度为 512M)

3.2 redis持久化原理:

save: 主线程进行创建redis文件备份
bgsave: redis fork一个子线程进行备份

COPY ON WRITE(linux) 若多个调用者需求相同的资源， 他们会获取指针指向
相同的资源, 并不复制专门的副本给调用者, 仅当调用者试图对资源进行修改时，
系统才会复制专门的副本给调用者。 

redis bgsave持久化过程:
父进程fork出子进程 -> 子进程将当前记录写入临时文件 -> 父进程的写操作在
os为父进程操作的副本中进行 -> 子进程可以同时进行临时文件的记录 -> 子进程
将临时文件存入快照RDB文件。

RDB: 二进制字节文件

AOF: redis指令

aof持久化: 将指令实时写入到aof文件中

aof rewrite
父进程fork出子进程 -> 子进程将当前记录重新生成aof指令写入临时文件 -> 
父进程将变动写入副本及原来的aof文件中 -> 主进程获得子进程完成写入AOF信号
-> 主进程向新AOF临时文件中同步变动。 -> 将临时文件替代旧的快照AOF文件。

redis 4.0 -> RDB - AOF 混合持久化
RDB全量数据 + AOF增量持久化

4. Redis同步

slave发送think命令至主
写master 读slave
全量同步 bgsave + aof增量同步(主进程将增量aof写入slave)

4.1. Redis Pipeline

将命令为单个指令变为pipeline执行: pipeline不仅减少了RTT(指令发送 -> 处理 -> 返回结果), 
同时也减少了IO调用次数（IO调用涉及到用户态到内核态之间的切换）

5. class文件 -> Class Loader(依据特定格式, 加载class文件到内存) -> 
java内存结构 -> execution engine(对命令进行解析相当于实际机器上的cpu) -> 
Native Interface(融合不同开发语言的原生库为Java所用) -> Native Libraries

6. 反射: 在Java运行状态中, 对于任意一个类, 都能够知道这个类的所有属性和方法; 
对于任意一个对象, 都能够调用它的任意方法和属性; 这种动态获取信息以及动态调用对
象方法的功能称为java语言的反射机制反射的基础 -> 任意一个类都被ClassLoader加载入JVM虚拟机内存中

7. Class的GetMethod(仅能获取public方法但是可以获得父类方法), 
GetDeclaredMethod(可以获取所有可见性的方法,但是只能获得当前类的方法)

8. Class对象都是单例的

9. 什么是内核态
现代计算机系统中，所有的硬件资源都是由操作系统负责管理的，硬件资源包括 CPU 的使用时间、物理内存的使用、硬盘的读写等等。
操作系统内核当然是工作在内核态下，普通的用户进程工作在用户态下。
处于内核态的操作系统内核对于硬件有不受限制的使用权限，并且可以执行任何 CPU 指令以及访问任意的内存地址。
而用户态进程没有能力直接操作硬件，也没有能力访问任意的内存地址空间。用户态进程只能通过操作系统内核提供的系统调用受限地
使用硬件资源。并且用户态进程不能执行一些 CPU 特权指令。
系统调用属于内核空间代码，而用户态进程运行在用户态，所以执行系统调用时，程序的执行流程要从用户态切换到内核态。
早期 Linux 通过中断的方式实现系统调用，中断号为 0x80。从用户态切换到内核态的代价是，首先要进行现场保护，
即保存当前用户态的状态，以便执行完系统调用后恢复；其次是要从用户堆栈切换到内核堆栈；接着执行 0x80 号中断对应的中断
处理程序，再通过中断处理程序找到相应的系统调用代码。系统调用执行完毕后又要切换堆栈、恢复现场。新的 CPU 提供了专门用于
系统调用的 sysenter 和 sysexit 指令，使得系统调用的执行速度更快更高效。

每个运行中的Java程序都是一个JVM实例
10. JVM内存结构
线程私有:
1) 程序计数器(Program Counter Register)
(字节码指令no OOM)
当前线程所执行的字节码行号指示器(逻辑)
改变计数器的值来选取下一条需要执行的字节码指令(分支， 跳转， 循环，异常处理等)
若为native方法则计数器值为undefined
2) 虚拟机栈(Stack) NOTE: 在递归方法调用时, 外层递归方法将被压入虚拟机栈中
Java方法执行的内存模型
栈帧: 
局部变量表(Local Variable Table) 
操作栈(Operand Stack): 操作数栈可理解为java虚拟机栈中的一个用于计算的临时数据存储区。
动态连接(Dynamic Linking)
返回地址（Return Address)...
(java方法SOF&OOM)
3) 本地方法栈
(native方法SOF&OOM)

非线程私有:
1) MetaSpace(元空间) -> -> 本地内存 -> 实现方法区(Method Area): JVM规范
作用: 存储class的method, field
Before jdk8: PermGen(永久代) -> jvm内存 -> 包含字符串常量池(after 1.8: 移至堆中)
类的方法和信息的大小难以确定, 给永久代的大小指定带来困难
为GC带来不必要的复杂性
方便HotSpot与其他JVM如Jrockit的的集成(其它JVM不存在永久代)
类的元数据与类加载器的生命周期一致
(类加载信息OOM)
2) GC Heap堆(数组和类对象OOM)
对象实例的分配区域
a) 常量池(字面量和符号引用量)

判断是否为垃圾:
1)引用计数算法:
优点: 可以在运行时计算, 对系统的吞吐量影响较小
缺点: 无法回收循环引用对象
2)可达性分析算法:
虚拟机栈中的引用对象(栈帧中的本地变量表): 例
public void get(){
    Object s = new Object(); s处于虚拟机栈中s的引用对象
}
方法区中的常量引用的对象 final: 例
Class hello{
    public final Object = new Object();
}
方法区中的类静态属性引用的对象: 例
Class hello{
    public static Object = new Object();
}
本地方法栈中JNI(Native)的引用对象
活跃线程的引用对象

垃圾回收算法: 
 a) 标记清除算法 -> 
 b) 复制算法 -> 对象存货率低比较好 -> 复制的对象比较少 一般年轻代的垃圾收集器使用该算法
 c) 压缩算法 -> 对象存活率高比较好 -> 移动的对象比较少 一般老年代的垃圾收集器使用该算法 

3) happens-before原则
1. 如果一个操作happens-before(之前发生)另一个操作, 那么第一个操作的执行结果将对第二个操作可见
且第一个操作的执行顺序排在第二个操作之前。
2. 两个操作之间存在happens-before关系, 并不意味着一定要按照happens-before原则制定的顺序来执行
。 如果重排序之后的执行结果与happens-before关系来执行的结果一致，那么这种重排序并不非法。

volatile变量为何立即可见
当写一个volatile变量时,JMM会把该线程对应的工作内存中的共享变量值
刷新到主内存中。
当读取一个volatile变量时，JMM会把该线程对应的工作内存置为无效

内存屏障: memory barrier -> cpu指令

9. TCP三次握手: 
客户端发送SYN = 1 seq = n由closed进入syn sent状态
服务端接收到消息返回 seq = y ACK = 1 SYN = 1 ack = n + 1 由listen进入syn rcvd状态
-> SYN Flood防护措施 SYN队列满后, 通过tcp_syncookies参数回发SYN Cookie, 若为正常连接CLinet会回发SYN Cookie直接建立连接
客户端返回ACK = 1 seq = n + 1 ack = y + 1 客户端和服务器均进入established状态
问: 为什么需要三次挥手, 两次可不可以 -> 如果有未传送成功的tcp包 tcp的重传机制会重新发送该tcp包 若建立连接之后该tcp包又送达
就会再建立一次连接, 造成资源浪费

TCP的四次挥手:
第一次挥手: 客户端发送一个FIN seq = 1用来关闭Client到Server的数据传送, Client进入FIN_WAIT_1状态
第二次挥手: Server收到FIN后, 发送一个ACK给client ack = seq + 1(与SYN相同, 一个FIN占用一个序号), Server进入CLOSE_WAIT状态
第三次挥手: Server发送一个FIN,用来关闭Server到Client的数据传送, Server进入LAST_ACK状态
第四次挥手: Client收到FIN后, Client进入TIME_WAIT状态(等待2MSL), 接着发送一个ACK给Server,确认序号为收到序号+ 1
Server进入CLOSED状态, 完成四次挥手。

问: 为什么TCP握手是三次而挥手是四次 -> 在TCP连接时的三次握手, 服务端发送TCP的报文段同时初始化了
自身的seq number但是在TCP连接断开的握手时TCP服务端需要额外发送剩余的数据包, 所以将对客户端的应答
以及自身的SYN分开。

RTT: 发送一个数据包到收到对应的ACK, 所花费的时间
RTO: 重传时间间隔

TCP: 的滑动窗口 -> 做流量控制与乱序重排
发送端 : LastByteAcked(接收端接收且响应) ---
--- LastByteSent(已发送但未被接收端响应) ---
--- LastByteWritten(应用程序已准备的数据段)

接收端: LastByteRead(已接收且响应的数据) ---
--- NextByteExpected(已接受但未发回ACK的数据) ---
---LastByteRcved(接收到seq最大的数据)

滑动窗口大小(告知发送端还可以接收的数据) 
接收端:
AdvertisedWindow = MaxRcvBuffer - (LastByteRcvd - LastByteRead)
注: LastByteRcvd - LastByteRead是已接收的数据缓存
发送端(还能发送多少):           最后发送未被响应的数据  被响应的数据
EffectiveWindow = AdvertisedWindow - (LastByteSent - LastByteAcked)

滑动窗口原理: 基于ACK

TCP的11种状态:
CLOSED ：初始状态，表示TCP连接是“关闭着的”或“未打开的”。
LISTEN ：表示服务器端的某个SOCKET处于监听状态，可以接受客户端的连接。
SYN_RCVD ：表示服务器接收到了来自客户端请求连接的SYN报文。在正常情况下，这个状态是服务器端的SOCKET在建立TCP连接时的三次握手会话过程中的一个中间状态，很短暂，基本上用netstat很难看到这种状态，除非故意写一个监测程序，将三次TCP握手过程中最后一个ACK报文不予发送。当TCP连接处于此状态时，再收到客户端的ACK报文，它就会进入到ESTABLISHED 状态。
SYN_SENT ：这个状态与SYN_RCVD 状态相呼应，当客户端SOCKET执行connect()进行连接时，它首先发送SYN报文，然后随即进入到SYN_SENT 状态，并等待服务端的发送三次握手中的第2个报文。SYN_SENT 状态表示客户端已发送SYN报文。
ESTABLISHED ：表示TCP连接已经成功建立。
FIN_WAIT_1 ：这个状态得好好解释一下，其实FIN_WAIT_1 和FIN_WAIT_2 两种状态的真正含义都是表示等待对方的FIN报文。而这两种状态的区别是：FIN_WAIT_1状态实际上是当SOCKET在ESTABLISHED状态时，它想主动关闭连接，向对方发送了FIN报文，此时该SOCKET进入到FIN_WAIT_1 状态。而当对方回应ACK报文后，则进入到FIN_WAIT_2 状态。当然在实际的正常情况下，无论对方处于任何种情况下，都应该马上回应ACK报文，所以FIN_WAIT_1 状态一般是比较难见到的，而FIN_WAIT_2 状态有时仍可以用netstat看到。
FIN_WAIT_2 ：上面已经解释了这种状态的由来，实际上FIN_WAIT_2状态下的SOCKET表示半连接，即有一方调用close()主动要求关闭连接。注意：FIN_WAIT_2 是没有超时的（不像TIME_WAIT 状态），这种状态下如果对方不关闭（不配合完成4次挥手过程），那这个 FIN_WAIT_2 状态将一直保持到系统重启，越来越多的FIN_WAIT_2 状态会导致内核crash。
TIME_WAIT ：表示收到了对方的FIN报文，并发送出了ACK报文。 TIME_WAIT状态下的TCP连接会等待2*MSL（Max Segment Lifetime，最大分段生存期，指一个TCP报文在Internet上的最长生存时间。每个具体的TCP协议实现都必须选择一个确定的MSL值，RFC 1122建议是2分钟，但BSD传统实现采用了30秒，Linux可以cat /proc/sys/net/ipv4/tcp_fin_timeout看到本机的这个值），然后即可回到CLOSED 可用状态了。如果FIN_WAIT_1状态下，收到了对方同时带FIN标志和ACK标志的报文时，可以直接进入到TIME_WAIT状态，而无须经过FIN_WAIT_2状态。（这种情况应该就是四次挥手变成三次挥手的那种情况）
CLOSING ：这种状态在实际情况中应该很少见，属于一种比较罕见的例外状态。正常情况下，当一方发送FIN报文后，按理来说是应该先收到（或同时收到）对方的ACK报文，再收到对方的FIN报文。但是CLOSING 状态表示一方发送FIN报文后，并没有收到对方的ACK报文，反而却也收到了对方的FIN报文。什么情况下会出现此种情况呢？那就是当双方几乎在同时close()一个SOCKET的话，就出现了双方同时发送FIN报文的情况，这是就会出现CLOSING 状态，表示双方都正在关闭SOCKET连接。
CLOSE_WAIT ：表示正在等待关闭。怎么理解呢？当对方close()一个SOCKET后发送FIN报文给自己，你的系统毫无疑问地将会回应一个ACK报文给对方，此时TCP连接则进入到CLOSE_WAIT状态。接下来呢，你需要检查自己是否还有数据要发送给对方，如果没有的话，那你也就可以close()这个SOCKET并发送FIN报文给对方，即关闭自己到对方这个方向的连接。有数据的话则看程序的策略，继续发送或丢弃。简单地说，当你处于CLOSE_WAIT 状态下，需要完成的事情是等待你去关闭连接。
LAST_ACK ：当被动关闭的一方在发送FIN报文后，等待对方的ACK报文的时候，就处于LAST_ACK 状态。当收到对方的ACK报文后，也就可以进入到CLOSED 可用状态了。

10: 常用虚拟机指令: 
-Xmx: 设置最大Java堆大小
-Xms: 设置初始Java堆大小
java -XX:+PrintFlagsFinal -version | findstr /i "HeapSize PermSize ThreadStackSize": 查看默认堆, 永久代，栈大小

11. HashMap.put()方法逻辑 -> 主要逻辑在put调用的putVal中
    1.若HashMap未被初始化，则进行初始化操作
    2.对Key求Hash值，依据Hash值计算下标
    3.若未发生碰撞，则直接放入桶中
    4.若发生碰撞，则以链表的方式链接到后面
    5.若链表长度超过阈值(TREEIFY_THRESHOLD)且HashMap元素超过最低树化容量，则将链表转成红黑树
    6.若节点已经存在，则用新值替换旧值
    7.若桶满了(默认容量16 * 扩容因子0.75), 就需要resize(扩容两倍后重排)
    
    HashMap的哈希计算方法:
    key.hashCode() >>> 16 将32位hashcode从高位移至低位
    将移位后的结果与原hashCode进行异或运算(混合原始哈希的高位和低位)
    
    (n - 1) & hash 与操作替换取模提高效率 -> 计算数组下标
    因hashmap的数组大小总为2的n次方
    
 12. ACID，指数据库事务正确执行的四个基本要素的缩写。包含：原子性（Atomicity）、一致性（Consistency）、
 隔离性（Isolation）、持久性（Durability）。一个支持事务（Transaction）的数据库，
 必须要具有这四种特性，否则在事务过程（Transaction processing）当中无法保证数据的正确性,
 交易过程极可能达不到交易方的要求。
 
 13. HTTP 1.1 相较于 1.0 -> 引入Keep-alive长链接技术
 
 Http请求报文的结构       Http响应报文的结构
 请求头                   状态行
 空行                     响应头
 请求行                   响应正文
 请求体

HTTP和HTTPS的区别

HTTP: HTTP TCP IP
HTTPS: HTTP SSL or TLS TCP IP

SSL(Security Sockets Layer, 安全套接层)

HTTPS需要到CA申请证书, HTTP不需要
HTTPS密文传输, HTTP明文传输
连接方式不同，HTTPS默认使用443端口, HTTP使用80端口
HTTPS = HTTP+加密+认证+完整性保护，较HTTP安全

HTTPS为HTTP跳转而来, 有被劫持的风险, 利用HSTS(HTTP Strict Transport Security)优化

GET 和 POST的区别
1. GET将请求信息放在URL, POST放在报文体中
2. 数据库层面: GET符合幂等性和安全性, POST不符合
3. GET可以被缓存、被存储、而POST不行 利于CDN缓存GET

14. Socket简介
Socket是对TCP/IP协议的抽象，是操作系统对外开放的接口

Socket的Java实现 DatagramSocket: 发送与接受UDP报文
Socket: 客户端TCP报文 ServerSocket: 服务端TCP报文
扩展: 因Http为TCP的应用层协议, 所以Socket也可发送HTTP请求

15. 数据库索引数据结构为树时: 每增加一层深度就会增加一次将根的数据读入到内存中的IO操作
B+树是B树的变体, 其定义基本与B树相同, 除了:
非叶子节点的子树指针与关键字个数相同
非叶子节点的子树指针P[i], 指向关键字值[K[i], K[i+1]]的子树
非叶子节点仅用来索引, 数据都保存在叶子节点中(所以可以读入所有索引至内存进行查询, 因索引所占空间较小)
所有的叶子节点均有一个链指针指向下一个叶子节点(便于统计)

利用哈希作索引的区别: 无法使用范围查询, 无法被用来避免数据的排序操作(Hash无序),不能利用部分索引键查询
不能避免表扫描, 遇到大量Hash值相等的情况后性能并不一定就会比B-Tree高
 
Oracle: 位图索引

16.0 零拷贝: 不需要CPU进行用户态与内核态的切换即可进行的拷贝, 省去了状态切换的开销
应用: FileChannels: transferTo,transferFrom
16. IO多路复用的操作系统函数: 
O(n)
select: 单个进程所能打开的最大连接数由FD_SETSIZE宏定义, 其大小是32个整数的大小(在32位的机器上, 大小是32*32， 
64位机器上FD_SETSIZE为32*64), 我们可以对其进行修改然后重新编译内核。底层实现为数组。
O(n)
poll: 没有最大连接数的限制、基于链表
O(1)
epoll: 连接数很大 1G内存10万左右, 活跃的socket调用callback实现
应用: Redis, Java的NIO

17. 分布式系统中Session的同步
方法: 将请求转发至固定节点来同步Session
场景: 需要充分利用一台高性能服务器(例: 8核16G内存)的资源时, 可以在这台物理机器上建立逻辑集群, 保证单台JVM做full gc时造成
过高的停顿时间, 可以使用无session复制的亲合式集群-因并发请求并不是性能的瓶颈, 利用负载均衡器将一个固定的用户请求分配到
固定的集群节点处理即可。