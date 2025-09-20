package br.com.amorimluiz.consumer_rabbit.rabbitmq.consumers;

import constants.RabbitMQConstants;
import dtos.ProductDTO;
import lombok.extern.log4j.Log4j2;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Log4j2
@Component
public class ProductConsumer {

    @RabbitListener(queues = {RabbitMQConstants.QUEUE_PRODUCT_LOG})
    public void consumer(ProductDTO product) {
        log.info("Consumer received the message: {}", product.toString());
    }
}
