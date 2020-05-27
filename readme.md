SpringBoot集成redisson分布式锁

根据打印结果可以明显看到，未加锁的count--后值是乱序的，而加锁后的结果和我们预期的一样

加锁：
虽然redis是单线程(但请求并不是一定按先后顺序处理的，多个请求会被redis交叉着执行,redis需要锁也不是专门为了分布式锁，多个请求的异步交叉处理才是根本原因，一定程度上可以理解为出现了对共享资源的"并发"访问，所以要锁)，
可以同时有多个客户端访问，每个客户端会有一个线程。客户端访问之间存在竞争。
因为存在多客户端并发，所以必须保证操作的原子性。比如银行卡扣款问题，获取余额，判断，扣款，写回就必须构成事务，否则就可能出错

在传统单体应用单机部署的情况下，可以使用Java并发相关的锁，如ReentrantLcok或synchronized进行互斥控制。
但是，随着业务发展的需要，原单体单机部署的系统，渐渐的被部署在多机器多JVM上同时提供服务，
这使得原单机部署情况下的并发控制锁策略失效了，为了解决这个问题就需要一种跨JVM的互斥机制来控制共享资源的访问，
这就是分布式锁要解决的问题

分布式锁的实现条件

1、互斥性，和单体应用一样，要保证任意时刻，只能有一个客户端持有锁

2、可靠性，要保证系统的稳定性，不能产生死锁

3、一致性，要保证锁只能由加锁人解锁，不能产生A的加锁被B用户解锁的情况

Redis实现分布式锁不同的人可能有不同的实现逻辑。

分布式环境下，数据一致性问题一直是一个比较重要的话题，而又不同于单进程的情况。
分布式与单机情况下最大的不同在于其不是多线程而是多进程。
多线程由于可以共享堆内存，因此可以简单的采取内存作为标记存储位置。
而进程之间甚至可能都不在同一台物理机上，因此需要将标记存储在一个所有进程都能看到的地方


常见的是秒杀场景，订单服务部署了多个实例。
如秒杀商品有4个，第一个用户购买3个，第二个用户购买2个，理想状态下第一个用户能购买成功，第二个用户提示购买失败，反之亦可。
而实际可能出现的情况是，两个用户都得到库存为4，第一个用户买到了3个，更新库存之前，第二个用户下了2个商品的订单，更新库存为2，导致出错



分布式锁实际是要让多机远程访问一个加锁的共享资源，redis可以做分布式锁是因为其可以对同一个key的访问加锁，
这跟单不单线程没有关系。memcached可以 ，mysql可以，zookeeper也可以