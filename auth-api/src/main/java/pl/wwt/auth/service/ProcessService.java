package pl.wwt.auth.service;

import pl.wwt.auth.dto.ProcessRequest;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

public interface ProcessService {
    String process(String auth, ProcessRequest body) throws ExecutionException, InterruptedException, TimeoutException;
}
