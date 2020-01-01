
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

5. 
