package br.com.felipe.producer_rabbit.configs;

import constants.RabbitMQConstants;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class RabbitMQConfig {
    @Bean
    public Queue queue() {
        return new Queue(RabbitMQConstants.QUEUE_PRODUCT_LOG, false, false, false);
    }

    @Bean
    public DirectExchange exchange() {
        return new DirectExchange(RabbitMQConstants.EXG_NAME_MARKETPLACE, false, false);
    }

    @Bean
    public Binding binding() {
        return BindingBuilder
                .bind(queue())
                .to(exchange())
                .with(RabbitMQConstants.RK_PRODUCT_LOG);
    }

}
