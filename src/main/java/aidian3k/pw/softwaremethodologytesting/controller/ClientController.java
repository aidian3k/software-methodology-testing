package aidian3k.pw.softwaremethodologytesting.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@Validated
class ClientController {
    @GetMapping("/health-check")
    public ResponseEntity<String> handleHealthCheckCall() {
        return new ResponseEntity<>("Health-Check", HttpStatus.OK);
    }
}
