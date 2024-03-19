package ru.pivan.MessageDispatcherInSmsService.kafka;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.common.errors.SerializationException;
import org.apache.kafka.common.serialization.Deserializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class KafkaMessageDeserializer implements Deserializer<KafkaMessage> {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {}

    @Override
    public KafkaMessage deserialize(String topic, byte[] data) {
        try {
            if (data == null) {
                return null;
            } else {
                return objectMapper.readValue(data, KafkaMessage.class);
            }
        } catch (Exception e) {
            throw new SerializationException("Error when deserializing byte[] in KafkaMessage", e);
        }
    }
}
