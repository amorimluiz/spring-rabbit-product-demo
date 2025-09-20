package br.com.felipe.producer_rabbit.services;

import constants.RabbitMQConstants;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Log4j2
@Service
@RequiredArgsConstructor
public class StringService {

    private final RabbitTemplate rabbitTemplate;

    public void produce(String message) {
        log.info("Received message: {}", message);
        rabbitTemplate.convertAndSend(RabbitMQConstants.EXG_NAME_MARKETPLACE, RabbitMQConstants.RK_PRODUCT_LOG, message);
    }
}
