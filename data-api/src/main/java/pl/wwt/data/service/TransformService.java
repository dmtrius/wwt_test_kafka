package pl.wwt.data.service;

import pl.wwt.data.dto.ProcessRequest;
import pl.wwt.data.dto.ProcessResponse;

public interface TransformService {
    ProcessResponse transform(ProcessRequest body);
}
