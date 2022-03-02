## 基本概念



## 快速开始

以下演示均为单机版，如果想玩集群可自行搜索相关配置，一般与单机版配置差距不大，通常需要填写集群中其他机器的IP。

### 操作系统

CentOS7

### JDK

JDK8

### ZK、Kafka版本

- Zookeeper 3.6.0
- Kafka_2.11-2.4.1

Kafka版本说明：zk的版本号很清楚，跟绝大部分软件一样。而Kafka的版本号是两个数字拼起来的，存在一定歧义，比如2.11-2.4.1。其中2.11代表Scala的版本，2.4.1才是Kafka的版本。Kafka正是由Scala编写的，其也是JVM系的一种编程语言。

### 下载、解压

```shell
# 下载zookeeper安装包
wget https://archive.apache.org/dist/zookeeper/zookeeper-3.6.0/apache-zookeeper-3.6.0-bin.tar.gz
tar -zxvf apache-zookeeper-3.6.0-bin.tar.gz
# 移至自己的工作目录
mv apache-zookeeper-3.6.0-bin /usr/zookeeper/

# 下载kafka
wget https://archive.apache.org/dist/kafka/2.4.1/kafka_2.11-2.4.1.tgz
tar -zxvf kafka_2.11-2.4.1.tgz
mv kafka_2.11-2.4.1 /usr/kafka
```

### 配置、启动

**首先配置、启动zookeeper**

```shell
#进入zookpeer的conf目录
cp zoo_sample.cfg zoo.cfg
vim zoo.cfg
```

zk的默认配置下，就可以启动了。但是其默认配置中数据和日志是同一目录，即dataDir，我们需要手动指定一下日志目录，即dataLogDir：

```shell
# zk服务器心跳间隔，单位ms
tickTime=2000
# 新leader初始化时间
initLimit=10
# leader与follower心跳检测最大容忍时间：tickTime*syncLimit
syncLimit=5
# 数据目录
dataDir=/tmp/zookeeper/data
# 日志目录
dataLogDir=/tmp/zookeeper/log
# 对外服务端口
clientPort=2181
```

如果你的系统中没有上述配置中的两个目录，记得手动创建一下：

```shell
mkdir -p /tmp/zookeeper/data
mkdir -p /tmp/zookeeper/log
# 启动zookeeper
/usr/zookeeper/bin/zkServer.sh start
# 查看zookeeper状态
/usr/zookeeper/bin/zkServer.sh status
```

**接着配置、启动Kafka**

```shell
# 修改配置文件
vim /usr/kafka/config/server.properties

# 需填写如下配置：
# 服务器编号，如果是集群模式，需要保证改编号唯一
broker.id=0
# kafka监听的ip和端口
listeners=PLAINTEXT://localhost:9092
# 如果是云服务部署的，需要外网访问，还需要如下配置，上面的localhost改成内网IP
advertised.listeners=PLAINTEXT://公网IP:9092
# 日志目录
log.dirs=/tmp/kafka-logs
# zookeeper的ip和端口
zookeeper.connect=localhost:2181/kafka
```

保存退出后，启动Kafka：

```shell
/usr/kafka/bin/kafka-server-start.sh /usr/kafka/config/server.properties
```

## 快速实战
