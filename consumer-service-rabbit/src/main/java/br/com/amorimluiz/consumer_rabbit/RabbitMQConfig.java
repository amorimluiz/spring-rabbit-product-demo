package br.com.amorimluiz.consumer_rabbit;

import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.amqp.support.converter.SimpleMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class RabbitMQConfig {

    @Bean
    public MessageConverter messageConverter() {
        SimpleMessageConverter conv = new SimpleMessageConverter();
        conv.setAllowedListPatterns(List.of("dtos.*"));
        return conv;
    }

}
