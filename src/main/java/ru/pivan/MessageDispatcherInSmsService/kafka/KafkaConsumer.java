package ru.pivan.MessageDispatcherInSmsService.kafka;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import ru.pivan.MessageDispatcherInSmsService.service.SmsAeroService;

@Service
public class KafkaConsumer {

    private final SmsAeroService smsAeroService;

    public KafkaConsumer(SmsAeroService smsAeroService) {
        this.smsAeroService = smsAeroService;
    }

    @KafkaListener(topics = "kafka_test")
    public void listener(ConsumerRecord<String, KafkaMessage> record){
        String mes = smsAeroService.notifyUser(record.value().getMessage(), record.value().getPhone_number());
        System.out.println(mes);
    }
}
