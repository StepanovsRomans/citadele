package lv.citadele.paymentformcore.controllers;

import lv.citadele.paymentformcore.entities.PaymentEntity;
import lv.citadele.paymentformcore.services.EmailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
public class PaymentController {

    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private EmailService emailService;

    @CrossOrigin(origins = "http://localhost:8080", maxAge = 3600)
    @PostMapping(value = "/payment")
    public ResponseEntity<String> sendEmail(@Valid @RequestBody PaymentEntity requestUserDetails) {
        LOGGER.info("Received POST request on /payment with payload: " + requestUserDetails);

        try {
            emailService.sendEmail(requestUserDetails);
        } catch (RuntimeException e) {
            LOGGER.error(e.getMessage());
            return ResponseEntity.status(500).body("Something went wrong... " + e.getLocalizedMessage());
        }
        return ResponseEntity.ok("Email sent");
    }
}
