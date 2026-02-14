package pl.wwt.data.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.common.errors.SerializationException;
import org.apache.kafka.common.serialization.Serializer;
import pl.wwt.data.kafka.ProcessResultEvent;

import java.util.Objects;

public class ProcessResultEventSerializer implements Serializer<ProcessResultEvent> {
    private final ObjectMapper objectMapper;

    public ProcessResultEventSerializer() {
        this.objectMapper = new ObjectMapper();
        this.objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    @Override
    public byte[] serialize(String topic, ProcessResultEvent data) {
        if (Objects.isNull(data)) {
            return null;
        }
        try {
            return objectMapper.writeValueAsBytes(data);
        } catch (JsonProcessingException e) {
            throw new SerializationException("Error serializing ProcessEvent object to JSON", e);
        }
    }
}
