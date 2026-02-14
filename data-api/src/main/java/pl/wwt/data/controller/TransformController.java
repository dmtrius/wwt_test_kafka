package pl.wwt.data.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.wwt.data.dto.ProcessRequest;
import pl.wwt.data.dto.ProcessResponse;
import pl.wwt.data.service.TransformService;

@RestController
@RequestMapping("/api/v1")
public class TransformController {

    @Value("${spring.jwt.internal-token}")
    private String internalToken;
    private static final String X_INTERNAL_TOKEN = "X-Internal-Token";

    private final TransformService transformService;

    public TransformController(TransformService transformService) {
        this.transformService = transformService;
    }

    @PostMapping("/transform")
    public ResponseEntity<ProcessResponse> transform(
            @RequestHeader(value = X_INTERNAL_TOKEN, required = false) String token,
            @RequestBody @Valid ProcessRequest body) {

        if (!internalToken.equals(token)) {
            return ResponseEntity.status(403).build();
        }

        return ResponseEntity.ok(transformService.transform(body));
    }
}
