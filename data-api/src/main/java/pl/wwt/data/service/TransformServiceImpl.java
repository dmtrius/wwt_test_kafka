package pl.wwt.data.service;

import org.springframework.stereotype.Service;
import pl.wwt.data.dto.ProcessRequest;
import pl.wwt.data.dto.ProcessResponse;

import java.util.Objects;

@Service
public class TransformServiceImpl implements TransformService {
    @Override
    public ProcessResponse transform(ProcessRequest body) {
        String text = body.text();
        if (Objects.isNull(text)) {
            throw new RuntimeException("no data");
        }
        String result = new StringBuilder(text).reverse().toString().toUpperCase();
        return new ProcessResponse(result);
    }
}
