package com.kuba.login.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.JsonNode;
import com.kuba.login.models.User;
import com.kuba.login.payload.response.MessageResponse;
import com.kuba.login.repository.UserRepository;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api")
public class UpdateController {

    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder encoder;

    @PutMapping("/updatepassword")
    public ResponseEntity<?> updateUserPassword(@RequestBody JsonNode node) {

        if (node.size() < 1) {
            return ResponseEntity.badRequest().body(new MessageResponse("No Put Body"));
        } else if (!node.has("id")) {
            return ResponseEntity.badRequest().body(new MessageResponse("No User id"));
        } else if (!node.has("password")) {
            return ResponseEntity.badRequest().body(new MessageResponse("No New password"));
        }

        Long id = Long.valueOf(node.get("id").asText());
        String password = node.get("password").toString();

        User user = userRepository.findById(id).get();
        user.setPassword(encoder.encode(password));
        userRepository.save(user);

        return ResponseEntity.ok(new MessageResponse("Updated password successfully"));
    }

    @PutMapping("/updatedetails")
    public ResponseEntity<?> updateUser(@RequestBody JsonNode node) {

        
        if (node.size() < 1) {
            return ResponseEntity.badRequest().body(new MessageResponse("No Put Body"));
        } else if (!node.has("id")) {
            return ResponseEntity.badRequest().body(new MessageResponse("No User id"));
        }

        Long id = Long.valueOf(node.get("id").asText());

        User user = userRepository.findById(id).get();

        if (node.has("username")) {
            user.setUsername(node.get("username").asText());
        }
        if (node.has("email")) {
            user.setEmail(node.get("email").asText());
        }
        if (node.has("name")) {
            user.setName(node.get("name").asText());
        }

        userRepository.save(user);

        return ResponseEntity.ok(new MessageResponse("Updated password successfully"));
    }
}
