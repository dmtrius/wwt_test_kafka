package pl.wwt.auth.service;

import pl.wwt.auth.dto.ProcessRequest;

public interface ProcessingLogService {
    String process(String auth, ProcessRequest req);
}
