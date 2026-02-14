package pl.wwt.data.kafka;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class ProcessConsumer {
    @Value("${spring.kafka.topic-results}")
    private String topicResults;

    private final KafkaTemplate<String, ProcessResultEvent> kafka;

    public ProcessConsumer(KafkaTemplate<String, ProcessResultEvent> kafka) {
        this.kafka = kafka;
    }

    @KafkaListener(topics = "${spring.kafka.topic-requests}")
    public void consume(ProcessEvent event) {
        String text = event.text();
        if (Objects.isNull(text)) {
            return;
        }
        String result = new StringBuilder(text)
                .reverse()
                .toString()
                .toUpperCase();
        kafka.send(topicResults,
                new ProcessResultEvent(
                        event.requestId(),
                        result));
    }
}
