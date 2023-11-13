package aidian3k.pw.softwaremethodologytesting.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
class HealthCheckController {
    @GetMapping
    public ResponseEntity<String> handleHealthCheckCall() {
        return new ResponseEntity<>("Health-check", HttpStatus.OK);
    }
}
