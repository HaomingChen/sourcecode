1. ctrl + f5利用在http request head中加入Cache-Control: no-cache与
Pargma: no-cache避免请求到前端缓存如Varnish代理缓存服务器。且ctrl + f5会
避免请求浏览器自身缓存直接向目标url发起请求
2. DNS域名解析过程
a) 浏览器缓存 b) 操作系统文件 c) local DNS server(LDNS from ISP) 查看缓存(有 -> 返回)
d) Root Server e) gTLD 主域名服务器 f) gTLD主域名服务器返回此域名对应的Name Server
域名服务器 g) Name Server 返回IP和TTL返回DNS Server域名服务器 h)解析结果
返回用户

3. redis持久化原理:

RDB:

二进制字节文件

save: 主线程进行创建redis文件备份
bgsave: redis fork一个子线程进行备份

COPY ON WRITE(linux) 若多个调用者需求相同的资源， 他们会获取指针指向
相同的资源, 并不复制专门的副本给调用者, 仅当调用者试图对资源进行修改时，
系统才会复制专门的副本给调用者。 

redis bgsave持久化过程:
父进程fork出子进程 -> 子进程将当前记录写入临时文件 -> 父进程的写操作在
os为父进程操作的副本中进行 -> 子进程可以同时进行临时文件的记录 -> 子进程
将临时文件存入快照RDB文件。

AOF: 

redis指令文件

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
标记清除算法: 

