package pl.wwt.auth.config;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.common.errors.SerializationException;
import org.apache.kafka.common.serialization.Deserializer;
import pl.wwt.auth.dto.ProcessResultEvent;

import java.io.IOException;
import java.util.Objects;

public class ProcessResultEventDeserializer implements Deserializer<ProcessResultEvent> {
    private final ObjectMapper objectMapper;

    public ProcessResultEventDeserializer() {
        this.objectMapper = new ObjectMapper();
        this.objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    @Override
    public ProcessResultEvent deserialize(String topic, byte[] data) {
        if (Objects.isNull(data)) {
            return null;
        }
        try {
            return objectMapper.readValue(data, ProcessResultEvent.class);
        } catch (IOException e) {
            throw new SerializationException("Error deserializing JSON to ProcessResultEvent object", e);
        }
    }
}
