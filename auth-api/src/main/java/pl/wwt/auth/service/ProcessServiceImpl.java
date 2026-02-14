package pl.wwt.auth.service;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import pl.wwt.auth.config.JwtUtil;
import pl.wwt.auth.dto.ProcessEvent;
import pl.wwt.auth.dto.ProcessRequest;
import pl.wwt.auth.dto.ProcessResultEvent;
import pl.wwt.auth.entity.ProcessingLog;
import pl.wwt.auth.entity.User;
import pl.wwt.auth.exception.AuthException;
import pl.wwt.auth.repository.ProcessingLogRepository;
import pl.wwt.auth.repository.UserRepository;

import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@Service
public class ProcessServiceImpl implements ProcessService {
    @Value("${spring.kafka.topic-requests}")
    private String topic;

    public static final String BEARER_PREFIX = "Bearer ";

    private final KafkaTemplate<String, ProcessEvent> kafka;
    private final JwtUtil jwt;
    private final UserRepository userRepo;
    private final ProcessingLogRepository logRepo;

    private final Map<UUID, CompletableFuture<String>> pending =
            new ConcurrentHashMap<>();

    public ProcessServiceImpl(
            KafkaTemplate<String, ProcessEvent> kafka, JwtUtil jwt, UserRepository userRepo,
            ProcessingLogRepository logRepo) {
        this.kafka = kafka;
        this.jwt = jwt;
        this.userRepo = userRepo;
        this.logRepo = logRepo;
    }

    public String process(String auth, ProcessRequest body) throws ExecutionException, InterruptedException, TimeoutException {
        String token = auth.replace(BEARER_PREFIX, StringUtils.EMPTY);
        String email = jwt.validate(token);
        User user = userRepo.findByEmail(email)
                .orElseThrow(() -> new AuthException("Invalid credentials"));

        UUID requestId = UUID.randomUUID();

        CompletableFuture<String> future = new CompletableFuture<>();
        pending.put(requestId, future);
        kafka.send(topic, new ProcessEvent(requestId, user.getId(), body.text()));

        String result = future.get(5, TimeUnit.SECONDS);

        ProcessingLog log = new ProcessingLog();
        log.setUserId(user.getId());
        log.setInputText(body.text());
        log.setOutputText(result);
        logRepo.save(log);

        return result;
    }

    @KafkaListener(topics = "${spring.kafka.topic-results}")
    public void handleResult(ProcessResultEvent event) {
        CompletableFuture<String> future =
                pending.remove(event.requestId());

        if (!Objects.isNull(future)) {
            future.complete(event.result());
        }
    }
}
