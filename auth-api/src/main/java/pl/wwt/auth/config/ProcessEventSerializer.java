package pl.wwt.auth.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.common.errors.SerializationException;
import org.apache.kafka.common.serialization.Serializer;
import pl.wwt.auth.dto.ProcessEvent;

import java.util.Objects;

public class ProcessEventSerializer implements Serializer<ProcessEvent> {
    private final ObjectMapper objectMapper;

    public ProcessEventSerializer() {
        this.objectMapper = new ObjectMapper();
        this.objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    @Override
    public byte[] serialize(String topic, ProcessEvent data) {
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
