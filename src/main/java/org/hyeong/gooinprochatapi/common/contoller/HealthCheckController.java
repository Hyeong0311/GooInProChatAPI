package org.hyeong.gooinprochatapi.common.contoller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/chat/api/v1/")
@Log4j2
@RequiredArgsConstructor
public class HealthCheckController {

    @GetMapping("")
    public String healthCheckController() {

        return "Health Check Complete";
    }
}
