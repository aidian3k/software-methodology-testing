package aidian3k.pw.softwaremethodologytesting.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
class HealthCheckController {

    @Value("${project.version}")
    private String projectVersion;

    @GetMapping
    public ResponseEntity<String> handleHealthCheckCall() {
        return new ResponseEntity<>(String.format("Current project version is=[%s]", projectVersion)
                , HttpStatus.OK);
    }
}
