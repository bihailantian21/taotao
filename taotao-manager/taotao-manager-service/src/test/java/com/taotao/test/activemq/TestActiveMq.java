package com.taotao.test.activemq;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.junit.Test;

import javax.jms.*;

public class TestActiveMq {

    @Test
    public void testQueueProducer() throws JMSException{
        /**
         * 生产者：生产消息，发送端。
         * 第一步：创建ConnectionFactory对象，需要指定服务端ip及端口号。
         * 第二步：使用ConnectionFactory对象创建一个Connection对象。
         * 第三步：开启连接，调用Connection对象的start方法。
         * 第四步：使用Connection对象创建一个Session对象。
         * 第五步：使用Session对象创建一个Destination对象（topic、queue），此处创建一个Queue对象。
         * 第六步：使用Session对象创建一个Producer对象。
         * 第七步：创建一个Message对象，创建一个TextMessage对象。
         * 第八步：使用Producer对象发送消息。
         * 第九步：关闭资源。
         */
        //1.创建一个连接工厂对象ConnectionFactory对象。需要指定mq服务的ip及端口号。注意参数brokerURL的开头是
        //tcp://而不是我们通常的http://，端口是61616而不是我们访问activemq后台管理页面所使用的8161
        ConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://192.168.173.148:61616");
        //2.使用ConnectionFactory创建一个连接Connection对象
        Connection connection = connectionFactory.createConnection();
        //3.开启连接。调用Connection对象的start方法
        connection.start();
        //4.使用Connection对象创建一个Session对象
        //第一个参数是是否开启事务，一般不使用分布式事务，因为它特别消耗性能，而且顾客体验特别差，现在互联网的
        //做法是保证数据的最终一致（也就是允许暂时数据不一致），比如顾客下单购买东西，一旦订单生成完就立刻响应给用户
        //下单成功。至于下单后一系列的操作，比如通知会计记账、通知物流发货、商品数量同步等等都先不用管，只需要
        //发送一条消息到消息队列，消息队列来告知各模块进行相应的操作，一次告知不行就两次，直到完成所有相关操作为止，这
        //也就做到了数据的最终一致性。如果第一个参数为true，那么第二个参数将会被忽略掉。如果第一个参数为false，那么
        //第二个参数为消息的应答模式，常见的有手动和自动两种模式，我们一般使用自动模式。
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        //5.使用Session对象创建一个Destination对象，两种形式queue、topic。现在我们使用queue
        //参数就是消息队列的名称
        Queue queue = session.createQueue("test-queue");
        //6.使用Session对象创建一个Producer对象
        MessageProducer producer = session.createProducer(queue);
        //7.创建一个TextMessage对象
        //有两种方式，第一种方式：
//		TextMessage textMessage = new ActiveMQTextMessage();
//		textMessage.setText("hello,activemq!!!");
        //第二种方式：
        TextMessage textMessage = session.createTextMessage("hello,activemq!!1111");
        //8.发送消息
        producer.send(textMessage);
        //9.关闭资源
        producer.close();
        session.close();
        connection.close();
    }



    @Test
    public void testQueueConsumer() throws Exception{
        /**
         * 消费者：接收消息。
         * 第一步：创建一个ConnectionFactory对象。
         * 第二步：从ConnectionFactory对象中获得一个Connection对象。
         * 第三步：开启连接。调用Connection对象的start方法。
         * 第四步：使用Connection对象创建一个Session对象。
         * 第五步：使用Session对象创建一个Destination对象。和发送端保持一致queue，并且队列的名称一致。
         * 第六步：使用Session对象创建一个Consumer对象。
         * 第七步：接收消息。
         * 第八步：打印消息。
         * 第九步：关闭资源
         */
        //1.创建一个连接工厂对象ConnectionFactory对象。需要指定mq服务的ip及端口号。注意参数brokerURL的开头是
        //tcp://而不是我们通常的http://，端口是61616而不是我们访问activemq后台管理页面所使用的8161
        ConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://192.168.173.148:61616");
        //2.使用ConnectionFactory创建一个连接Connection对象
        Connection connection = connectionFactory.createConnection();
        //3.开启连接。调用Connection对象的start方法
        connection.start();
        //4.使用Connection对象创建一个Session对象
        //第一个参数是是否开启事务，一般不使用分布式事务，因为它特别消耗性能，而且顾客体验特别差，现在互联网的
        //做法是保证数据的最终一致（也就是允许暂时数据不一致），比如顾客下单购买东西，一旦订单生成完就立刻响应给用户
        //下单成功。至于下单后一系列的操作，比如通知会计记账、通知物流发货、商品数量同步等等都先不用管，只需要
        //发送一条消息到消息队列，消息队列来告知各模块进行相应的操作，一次告知不行就两次，直到完成所有相关操作为止，这
        //也就做到了数据的最终一致性。如果第一个参数为true，那么第二个参数将会被忽略掉。如果第一个参数为false，那么
        //第二个参数为消息的应答模式，常见的有手动和自动两种模式，我们一般使用自动模式。
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        //5.使用Session对象创建一个Destination对象，两种形式queue、topic。现在我们使用queue
        //参数就是消息队列的名称
        Queue queue = session.createQueue("test-queue");
        //6.使用Session对象创建一个Consumer对象
        MessageConsumer consumer = session.createConsumer(queue);
        //7.向Consumer对象中设置一个MessageListener对象，用来接收消息
        consumer.setMessageListener(new MessageListener() {

            @Override
            public void onMessage(Message message) {
                if(message instanceof TextMessage) {
                    TextMessage textMessage = (TextMessage)message;
                    try {
                        String text = textMessage.getText();
                        System.out.println(text);
                    } catch (JMSException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        //8.程序等待接收用户结束操作
        //程序自己并不知道什么时候有消息，也不知道什么时候不再发送消息了，这就需要手动干预，
        //当我们想停止接收消息时，可以在控制台输入任意键，然后回车即可结束接收操作（也可以直接按回车）。
        System.in.read();
        //9.关闭资源
        consumer.close();
        session.close();
        connection.close();
    }



    @Test
    public void testTopicProducer() throws JMSException{
        /**
         * 使用步骤：
         * 第一步：创建ConnectionFactory对象，需要指定服务端ip及端口号。
         * 第二步：使用ConnectionFactory对象创建一个Connection对象。
         * 第三步：开启连接，调用Connection对象的start方法。
         * 第四步：使用Connection对象创建一个Session对象。
         * 第五步：使用Session对象创建一个Destination对象（topic、queue），此处创建一个Topic对象。
         * 第六步：使用Session对象创建一个Producer对象。
         * 第七步：创建一个Message对象，创建一个TextMessage对象。
         * 第八步：使用Producer对象发送消息。
         * 第九步：关闭资源。
         */
        //1.创建一个连接工厂对象ConnectionFactory对象。需要指定mq服务的ip及端口号。注意参数brokerURL的开头是
        //tcp://而不是我们通常的http://，端口是61616而不是我们访问activemq后台管理页面所使用的8161
        ConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://192.168.173.148:61616");
        //2.使用ConnectionFactory创建一个连接Connection对象
        Connection connection = connectionFactory.createConnection();
        //3.开启连接。调用Connection对象的start方法
        connection.start();
        //4.使用Connection对象创建一个Session对象
        //第一个参数是是否开启事务，一般不使用分布式事务，因为它特别消耗性能，而且顾客体验特别差，现在互联网的
        //做法是保证数据的最终一致（也就是允许暂时数据不一致），比如顾客下单购买东西，一旦订单生成完就立刻响应给用户
        //下单成功。至于下单后一系列的操作，比如通知会计记账、通知物流发货、商品数量同步等等都先不用管，只需要
        //发送一条消息到消息队列，消息队列来告知各模块进行相应的操作，一次告知不行就两次，直到完成所有相关操作为止，这
        //也就做到了数据的最终一致性。如果第一个参数为true，那么第二个参数将会被忽略掉。如果第一个参数为false，那么
        //第二个参数为消息的应答模式，常见的有手动和自动两种模式，我们一般使用自动模式。
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        //5.使用Session对象创建一个Destination对象，两种形式queue、topic。现在我们使用topic
        //参数就是消息队列的名称
        Topic topic = session.createTopic("test-topic");//与发送队列消息唯一不同的地方
        //6.使用Session对象创建一个Producer对象
        MessageProducer producer = session.createProducer(topic);
        //7.创建一个TextMessage对象
        //有两种方式，第一种方式：
//		TextMessage textMessage = new ActiveMQTextMessage();
//		textMessage.setText("hello,activemq!!!");
        //第二种方式：
        TextMessage textMessage = session.createTextMessage("hello,activemq topic");
        //8.发送消息
        producer.send(textMessage);
        //9.关闭资源
        producer.close();
        session.close();
        connection.close();
    }




    @Test
    public void testTopicConsumer() throws Exception{
        /**
         * 消费者：接收消息。
         * 第一步：创建一个ConnectionFactory对象。
         * 第二步：从ConnectionFactory对象中获得一个Connection对象。
         * 第三步：开启连接。调用Connection对象的start方法。
         * 第四步：使用Connection对象创建一个Session对象。
         * 第五步：使用Session对象创建一个Destination对象。和发送端保持一致topic，并且话题的名称一致。
         * 第六步：使用Session对象创建一个Consumer对象。
         * 第七步：接收消息。
         * 第八步：打印消息。
         * 第九步：关闭资源
         */
        //1.创建一个连接工厂对象ConnectionFactory对象。需要指定mq服务的ip及端口号。注意参数brokerURL的开头是
        //tcp://而不是我们通常的http://，端口是61616而不是我们访问activemq后台管理页面所使用的8161
        ConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://192.168.173.148:61616");
        //2.使用ConnectionFactory创建一个连接Connection对象
        Connection connection = connectionFactory.createConnection();
        //3.开启连接。调用Connection对象的start方法
        connection.start();
        //4.使用Connection对象创建一个Session对象
        //第一个参数是是否开启事务，一般不使用分布式事务，因为它特别消耗性能，而且顾客体验特别差，现在互联网的
        //做法是保证数据的最终一致（也就是允许暂时数据不一致），比如顾客下单购买东西，一旦订单生成完就立刻响应给用户
        //下单成功。至于下单后一系列的操作，比如通知会计记账、通知物流发货、商品数量同步等等都先不用管，只需要
        //发送一条消息到消息队列，消息队列来告知各模块进行相应的操作，一次告知不行就两次，直到完成所有相关操作为止，这
        //也就做到了数据的最终一致性。如果第一个参数为true，那么第二个参数将会被忽略掉。如果第一个参数为false，那么
        //第二个参数为消息的应答模式，常见的有手动和自动两种模式，我们一般使用自动模式。
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        //5.使用Session对象创建一个Destination对象，两种形式queue、topic。现在我们使用queue
        //参数就是消息队列的名称
        Topic topic = session.createTopic("test-topic");//与消费队列消息的方法这一行代码不一样
        //6.使用Session对象创建一个Consumer对象
        MessageConsumer consumer = session.createConsumer(topic);
        //7.向Consumer对象中设置一个MessageListener对象，用来接收消息
        consumer.setMessageListener(new MessageListener() {

            @Override
            public void onMessage(Message message) {
                if(message instanceof TextMessage) {
                    TextMessage textMessage = (TextMessage)message;
                    try {
                        String text = textMessage.getText();
                        System.out.println(text);
                    } catch (JMSException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        //8.程序等待接收用户结束操作
        //程序自己并不知道什么时候有消息，也不知道什么时候不再发送消息了，这就需要手动干预，
        //当我们想停止接收消息时，可以在控制台输入任意键，然后回车即可结束接收操作（也可以直接按回车）。
        System.out.println("topic消费者2222。。。。。");//为了模拟多个消费者，在这里镖师是哪个消费者
        System.in.read();
        //9.关闭资源
        consumer.close();
        session.close();
        connection.close();
    }


/**
 * 小结
 *
 * queue 是点对点模式，只能是一个生产者产生一个消息，被一个消费者消费。
 * topic 是发布订阅模式，一个生产者可以一个消息，可以被多个消费者消费。
 *
 * queue 默认是存在于MQ的服务器中的，发送消息之后，消费者随时取。但是一定是一个消费者取，消费完消息也就没有了。
 * topic 默认是不存在于MQ服务器中的，一旦发送之后，如果没有订阅，消息则丢失。
 */

    /**
     * 持久化topic消息，生产者
     * @throws JMSException
     */
    @Test
    public void testTopicPersistenceProducer() throws JMSException{
        //1.创建一个连接工厂对象ConnectionFactory对象。需要指定mq服务的ip及端口号。注意参数brokerURL的开头是
        //tcp://而不是我们通常的http://，端口是61616而不是我们访问activemq后台管理页面所使用的8161
        ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://192.168.173.148:61616");
        //2.设置使用异步发送消息，这样可以显著提高发送性能
        connectionFactory.setUseAsyncSend(true);
        //3.使用ConnectionFactory创建一个连接Connection对象
        Connection connection = connectionFactory.createConnection();
        //4.对于每一个生产者来说，其clientID的值必须唯一
        connection.setClientID("producer2");
        //5.开启连接。调用Connection对象的start方法
        connection.start();
        //6.使用Connection对象创建一个Session对象
        //第一个参数是是否开启事务，一般不使用分布式事务，因为它特别消耗性能，而且顾客体验特别差，现在互联网的
        //做法是保证数据的最终一致（也就是允许暂时数据不一致），比如顾客下单购买东西，一旦订单生成完就立刻响应给用户
        //下单成功。至于下单后一系列的操作，比如通知会计记账、通知物流发货、商品数量同步等等都先不用管，只需要
        //发送一条消息到消息队列，消息队列来告知各模块进行相应的操作，一次告知不行就两次，直到完成所有相关操作为止，这
        //也就做到了数据的最终一致性。如果第一个参数为true，那么第二个参数将会被忽略掉。如果第一个参数为false，那么
        //第二个参数为消息的应答模式，常见的有手动和自动两种模式，我们一般使用自动模式。
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        //7.使用Session对象创建一个Destination对象，两种形式queue、topic。现在我们使用topic
        //参数就是消息队列的名称
        Topic topic = session.createTopic("test-topic");
        //8.使用Session对象创建一个Producer对象
        MessageProducer producer = session.createProducer(topic);
        //9.DeliveryMode设置为PERSISTENT（持久化）
        producer.setDeliveryMode(DeliveryMode.PERSISTENT);
        //10.创建一个TextMessage对象
        //有两种方式，第一种方式：
//		TextMessage textMessage = new ActiveMQTextMessage();
//		textMessage.setText("hello,activemq!!!");
        //第二种方式：
        TextMessage textMessage = session.createTextMessage("hello,activemq topic444");
        //11.发送消息
        producer.send(textMessage);
        //12.关闭资源
        producer.close();
        session.close();
        connection.close();
    }



    /**
     * 持久化topic消息，消费者
     * @throws Exception
     */
    @Test
    public void testTopicPersistenceConsumer() throws Exception{
        //1.创建一个连接工厂对象ConnectionFactory对象。需要指定mq服务的ip及端口号。注意参数brokerURL的开头是
        //tcp://而不是我们通常的http://，端口是61616而不是我们访问activemq后台管理页面所使用的8161
        ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://192.168.173.148:61616");
        //2.设置使用异步接收消息，这样可以显著提高接收性能
        connectionFactory.setUseAsyncSend(true);
        //3.使用ConnectionFactory创建一个连接Connection对象
        Connection connection = connectionFactory.createConnection();
        //4.设置消费者ID，每个消费者的clientID都不能相同！
        connection.setClientID("consumer1");
        //5.开启连接。调用Connection对象的start方法
        connection.start();
        //6.使用Connection对象创建一个Session对象
        //第一个参数是是否开启事务，一般不使用分布式事务，因为它特别消耗性能，而且顾客体验特别差，现在互联网的
        //做法是保证数据的最终一致（也就是允许暂时数据不一致），比如顾客下单购买东西，一旦订单生成完就立刻响应给用户
        //下单成功。至于下单后一系列的操作，比如通知会计记账、通知物流发货、商品数量同步等等都先不用管，只需要
        //发送一条消息到消息队列，消息队列来告知各模块进行相应的操作，一次告知不行就两次，直到完成所有相关操作为止，这
        //也就做到了数据的最终一致性。如果第一个参数为true，那么第二个参数将会被忽略掉。如果第一个参数为false，那么
        //第二个参数为消息的应答模式，常见的有手动和自动两种模式，我们一般使用自动模式。
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        //7.使用Session对象创建一个Destination对象，两种形式queue、topic。现在我们使用queue
        //参数就是消息队列的名称
        Topic topic = session.createTopic("test-topic");
        //8.使用Session对象创建一个Consumer对象
        MessageConsumer consumer = session.createDurableSubscriber(topic, "consumer1");
        //9.向Consumer对象中设置一个MessageListener对象，用来接收消息
        consumer.setMessageListener(new MessageListener() {

            @Override
            public void onMessage(Message message) {
                if(message instanceof TextMessage) {
                    TextMessage textMessage = (TextMessage)message;
                    try {
                        String text = textMessage.getText();
                        System.out.println(text);
                    } catch (JMSException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        //10.程序等待接收用户结束操作
        //程序自己并不知道什么时候有消息，也不知道什么时候不再发送消息了，这就需要手动干预，
        //当我们想停止接收消息时，可以在控制台输入任意键，然后回车即可结束接收操作（也可以直接按回车）。
        System.out.println("topic消费者1111。。。。。");
        System.in.read();
        //11.关闭资源
        consumer.close();
        session.close();
        connection.close();
    }













}

