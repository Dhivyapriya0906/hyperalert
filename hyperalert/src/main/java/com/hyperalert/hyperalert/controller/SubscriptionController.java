package com.hyperalert.hyperalert.controller;

import com.hyperalert.hyperalert.entity.Subscription;
import com.hyperalert.hyperalert.service.SubscriptionService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/subscriptions")
public class SubscriptionController {

    private final SubscriptionService subscriptionService;

    public SubscriptionController(SubscriptionService subscriptionService) {
        this.subscriptionService = subscriptionService;
    }

    @PostMapping
    public Subscription subscribe(@RequestBody Map<String, String> request, Authentication authentication) {
        String username = authentication.getName();
        return subscriptionService.subscribe(request.get("pincode"), request.get("email"), username);
    }

    @GetMapping("/my")
    public List<Subscription> getMySubscriptions(Authentication authentication) {
        String username = authentication.getName();
        return subscriptionService.getMySubscriptions(username);
    }

    @DeleteMapping("/{id}")
    public void unsubscribe(@PathVariable Long id) {
        subscriptionService.unsubscribe(id);
    }
}