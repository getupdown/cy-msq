# cy-msq —— 一个基于pull模型的分布式消息队列

## 1. 消息队列的关注点
### 基本功能
#### 组成部分
消息队列大致分为3个部分:
1. broker 可以理解为所有消息的中转站，这也是整个消息队列最重要的部分
2. consumer 消费者，即消费消息的实例
3. producer 生产者，向broker提交消息，等待消费者去消费。

#### 通信框架
底层使用netty框架来搞定所有网络通信相关的部分

### 实验路线
一步一个脚印，所有功能都从最简单的方式做起。

我准备按照如下的路线进行开发：
- 1 producer - 1 broker - 1 consumer 消息可靠传递, 不考虑一切高级特性（持久化，消费关系维护等等）
- 1 producer - multiBroker - 1 consumer 构建broker的集群，做到消息可靠传递, 考虑broker的容错，高可用，分布式一致性等等
- 1 producer - multiBroker - multiConsumer consumer成为集群，并且加入消费关系维护
- 后面的目标在实验过程中进行探索

### 关于测试
做出来的东西希望是正确的，所以有效的测试很必要。

测试尽量自动化，在云服务器上进行各种测试，例如压测，故障测试。
