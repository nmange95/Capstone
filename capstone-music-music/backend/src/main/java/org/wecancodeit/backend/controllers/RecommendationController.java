package org.wecancodeit.backend.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.wecancodeit.backend.models.User;
import org.wecancodeit.backend.services.RecommendationService;

import java.util.List;

@RestController
@RequestMapping("/api/recommendations")
public class RecommendationController {

    @Autowired
    private RecommendationService recommendationService;

    @GetMapping("/{username}")
    public ResponseEntity<List<User>> getRecommendations(@PathVariable String username) {
        List<User> recommendations = recommendationService.getRecommendations(username);
        return ResponseEntity.ok(recommendations);
    }
}
