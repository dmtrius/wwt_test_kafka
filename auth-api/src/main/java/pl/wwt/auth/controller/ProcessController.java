package pl.wwt.auth.controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.wwt.auth.dto.ProcessRequest;
import pl.wwt.auth.dto.ProcessResponse;
import pl.wwt.auth.exception.AuthException;
import pl.wwt.auth.service.ProcessService;

import java.util.Objects;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

@RestController
@RequestMapping("/api/v1")
public class ProcessController {

    private static final String AUTHORIZATION = "Authorization";
    private final ProcessService logService;

    public ProcessController(ProcessService logService) {
        this.logService = logService;
    }

    @PostMapping("/process")
    public ResponseEntity<ProcessResponse> process(
            @RequestHeader(AUTHORIZATION) String auth,
            @RequestBody @Valid ProcessRequest req) {
        if (Objects.isNull(req.text())) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        String result;
        try {
            result = logService.process(auth, req);
        } catch (AuthException _) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        } catch (TimeoutException | InterruptedException | ExecutionException _) {
            return new ResponseEntity<>(HttpStatus.REQUEST_TIMEOUT);
        }
        return ResponseEntity.ok(new ProcessResponse(result));
    }
}
