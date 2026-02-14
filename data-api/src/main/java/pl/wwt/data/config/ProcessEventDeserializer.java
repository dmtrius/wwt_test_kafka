package pl.wwt.data.config;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.common.errors.SerializationException;
import org.apache.kafka.common.serialization.Deserializer;
import pl.wwt.data.kafka.ProcessEvent;

import java.io.IOException;
import java.util.Objects;

public class ProcessEventDeserializer implements Deserializer<ProcessEvent> {
    private final ObjectMapper objectMapper;

    public ProcessEventDeserializer() {
        this.objectMapper = new ObjectMapper();
        this.objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    @Override
    public ProcessEvent deserialize(String topic, byte[] data) {
        if (Objects.isNull(data)) {
            return null;
        }
        try {
            return objectMapper.readValue(data, ProcessEvent.class);
        } catch (IOException e) {
            throw new SerializationException("Error deserializing JSON to ProcessResultEvent object", e);
        }
    }
}
