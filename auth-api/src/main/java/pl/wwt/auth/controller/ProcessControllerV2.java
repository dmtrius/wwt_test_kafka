package pl.wwt.auth.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClientException;
import pl.wwt.auth.dto.ProcessRequest;
import pl.wwt.auth.dto.ProcessResponse;
import pl.wwt.auth.exception.AuthException;
import pl.wwt.auth.exception.BadCommunicationException;
import pl.wwt.auth.service.ProcessingLogService;

import java.util.Objects;

@RestController
@RequestMapping("/api/v2")
public class ProcessControllerV2 {

    private static final String AUTHORIZATION = "Authorization";

    private final ProcessingLogService logService;

    public ProcessControllerV2(ProcessingLogService logService) {
        this.logService = logService;
    }

    @PostMapping("/process")
    public ResponseEntity<ProcessResponse> process(
            @RequestHeader(AUTHORIZATION) String auth,
            @RequestBody ProcessRequest req) {
        if (Objects.isNull(req.text())) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        String result;
        try {
            result = logService.process(auth, req);
        } catch(AuthException _) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        } catch (RestClientException | BadCommunicationException _) {
            return new ResponseEntity<>(HttpStatus.SERVICE_UNAVAILABLE);
        }
        return ResponseEntity.ok(new ProcessResponse(result));
    }
}
