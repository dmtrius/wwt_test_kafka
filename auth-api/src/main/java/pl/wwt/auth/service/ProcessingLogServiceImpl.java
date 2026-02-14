package pl.wwt.auth.service;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClient;
import pl.wwt.auth.config.JwtUtil;
import pl.wwt.auth.dto.ProcessRequest;
import pl.wwt.auth.dto.ProcessResponse;
import pl.wwt.auth.entity.ProcessingLog;
import pl.wwt.auth.entity.User;
import pl.wwt.auth.exception.AuthException;
import pl.wwt.auth.exception.BadCommunicationException;
import pl.wwt.auth.repository.ProcessingLogRepository;
import pl.wwt.auth.repository.UserRepository;

import java.util.Objects;

@Transactional
@Service
public class ProcessingLogServiceImpl implements ProcessingLogService {
    @Value("${spring.jwt.internal-token}")
    private String internalToken;
    @Value("${api.data}")
    private String dataApiUrl;

    private static final String X_INTERNAL_TOKEN = "X-Internal-Token";
    public static final String BEARER_PREFIX = "Bearer ";

    private final JwtUtil jwt;
    private final UserRepository userRepo;
    private final ProcessingLogRepository logRepo;


    public ProcessingLogServiceImpl(JwtUtil jwt, UserRepository userRepo, ProcessingLogRepository logRepo) {
        this.jwt = jwt;
        this.userRepo = userRepo;
        this.logRepo = logRepo;
    }

    @Override
    public String process(String auth, ProcessRequest req) {
        String token = auth.replace(BEARER_PREFIX, StringUtils.EMPTY);
        String email = jwt.validate(token);
        User user = userRepo.findByEmail(email)
                .orElseThrow(() -> new AuthException("Invalid credentials"));

        String result = requestData(req.text());

        ProcessingLog log = new ProcessingLog();
        log.setUserId(user.getId());
        log.setInputText(req.text());
        log.setOutputText(result);
        logRepo.save(log);

        return result;
    }

    private String requestData(String req) {
        HttpHeaders headers = new HttpHeaders();
        headers.set(X_INTERNAL_TOKEN, internalToken);
        headers.setContentType(MediaType.APPLICATION_JSON);
        var response = RestClient.builder()
                .requestInitializer(request -> request.getHeaders().addAll(headers))
                .baseUrl(dataApiUrl)
                .build()
                .post().body(req).retrieve().toEntity(ProcessResponse.class);
        if (!response.getStatusCode().is2xxSuccessful()
                || Objects.isNull(response.getBody())
                || Objects.isNull(response.getBody().result())) {
            throw new BadCommunicationException("Error during communication with data-api");
        }
        return response.getBody().result();
    }
}
