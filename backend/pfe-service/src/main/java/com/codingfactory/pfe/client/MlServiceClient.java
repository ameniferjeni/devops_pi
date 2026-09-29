package com.codingfactory.pfe.client;

import com.codingfactory.pfe.config.FeignClientConfig;
import com.codingfactory.pfe.dto.MlPredictRequest;
import com.codingfactory.pfe.dto.MlPredictResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(
        name = "ml-service",
        url = "${ml.service.url:http://localhost:8084}",
        configuration = FeignClientConfig.class
)
public interface MlServiceClient {

    @PostMapping("/api/ml/predict")
    MlPredictResponse predict(@RequestBody MlPredictRequest request);
}