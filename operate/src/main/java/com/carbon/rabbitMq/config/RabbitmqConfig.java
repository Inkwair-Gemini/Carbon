package com.carbon.rabbitMq.config;

import jakarta.annotation.Resource;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitmqConfig {
    @Resource
    private ConnectionFactory connectionFactory;

    //rabbit动态操作接口
    @Bean
    public RabbitAdmin rabbitAdmin() {
        RabbitAdmin rabbitAdmin = new RabbitAdmin(connectionFactory);
        rabbitAdmin.setAutoStartup(true);
        return rabbitAdmin;
    }

    /**
     * 设置回调
     */
    @Bean
    public RabbitTemplate createRabbitTemplate(ConnectionFactory connectionFactory){
        RabbitTemplate rabbitTemplate = new RabbitTemplate();
        rabbitTemplate.setConnectionFactory(connectionFactory);
        //setMessageConverter 方法使用jackson2去实现序列化(消息传输格式为json)
        rabbitTemplate.setMessageConverter(new Jackson2JsonMessageConverter());

        //设置开启Mandatory,才能触发回调函数,无论消息推送结果怎么样都强制调用回调函数
        rabbitTemplate.setMandatory(true);

        rabbitTemplate.setConfirmCallback((correlationData, ack, cause) -> {
            System.out.println("ConfirmCallback:     "+"相关数据："+correlationData);
            System.out.println("ConfirmCallback:     "+"确认情况："+ack);
            System.out.println("ConfirmCallback:     "+"原因："+cause);
        });

        rabbitTemplate.setReturnsCallback(returnedMessage -> {
            System.out.println("ReturnCallback:     "+"消息：" + returnedMessage.getMessage());
            System.out.println("ReturnCallback:     "+"回应码："+ returnedMessage.getReplyCode());
            System.out.println("ReturnCallback:     "+"回应信息："+returnedMessage.getReplyText());
            System.out.println("ReturnCallback:     "+"交换机："+returnedMessage.getExchange());
            System.out.println("ReturnCallback:     "+"路由键："+returnedMessage.getRoutingKey());
        });

        return rabbitTemplate;
    }

    /********************************直接交换机*************************************/

    /**
     * 队列名称：directQueue
     */
//    @Bean
//    public Queue directQueue() {
//        // durable: 是否持久化，默认是false。持久化队列：会被存储在磁盘上，当消息代理重启时仍然存在
//        // exclusive: 默认也是false，只能被当前创建的连接使用，而且当连接关闭后队列即被删除。此参考优先级高于durable
//        // autoDelete: 是否自动删除，当没有生产者或者消费者使用此队列，该队列会自动删除。
//        // 一般设置一下队列的持久化就好，其余两个就是默认false
//        return new Queue("directQueue", true);
//    }

    /**
     * Direct交换机名称：directExchange
     *
     * @return
     */
//    @Bean
//    public DirectExchange directExchange() {
//        return new DirectExchange("directExchange", true, false);
//    }

    /**
     * 将队列和交换机绑定, 并设置匹配路由键：directRouting
     */
//    @Bean
//    public Binding bindingDirectExchange() {
//        return BindingBuilder.bind(directQueue()).to(directExchange()).with("directRouting");
//    }

    /********************************主题订阅*************************************/
//    @Bean
//    public Queue topicFirstQueue() {
//        return new Queue("topicFirstQueue", true);
//    }
//
//    @Bean
//    public Queue topicSecondQueue() {
//        return new Queue("topicSecondQueue", true);
//    }

//    @Bean
//    public TopicExchange topicExchange() {
//        return new TopicExchange("topicExchange", true, false);
//    }

//    @Bean
//    public Binding bindingTopicFirstExchange() {
//        return BindingBuilder.bind(topicFirstQueue()).to(topicExchange()).with("topic.first");
//    }
//
//    @Bean
//    public Binding bindingTopicSecondExchange() {
//        return BindingBuilder.bind(topicSecondQueue()).to(topicExchange()).with("topic.#");
//    }

    /********************************消息广播*************************************/
//    @Bean
//    public Queue fanoutFirstQueue() {
//        return new Queue("fanoutFirstQueue", true);
//    }
//
//    @Bean
//    public Queue fanoutSecondQueue() {
//        return new Queue("fanoutSecondQueue", true);
//    }

    @Bean
    public FanoutExchange createAnnouncementFanoutExchange() {
        FanoutExchange announcementFanoutExchange = new FanoutExchange("AnnouncementFanoutExchange", true, false);
        rabbitAdmin().declareExchange(announcementFanoutExchange);
        return announcementFanoutExchange;
    }
    @Bean
    public FanoutExchange createNewsFanoutExchange() {
        FanoutExchange newsFanoutExchange = new FanoutExchange("NewsFanoutExchange", true, false);
        rabbitAdmin().declareExchange(newsFanoutExchange);
        return newsFanoutExchange;
    }

//    @Bean
//    public Binding bindingFanoutFirstExchange() {
//        return BindingBuilder.bind(fanoutFirstQueue()).to(fanoutExchange());
//    }
//
//    @Bean
//    public Binding bindingFanoutSecondExchange() {
//        return BindingBuilder.bind(fanoutSecondQueue()).to(fanoutExchange());
//    }
}
