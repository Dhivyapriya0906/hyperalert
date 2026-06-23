package com.hyperalert.hyperalert.service;

import com.hyperalert.hyperalert.entity.Subscription;
import com.hyperalert.hyperalert.repository.SubscriptionRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class SubscriptionService {

    private final SubscriptionRepository subscriptionRepository;

    public SubscriptionService(SubscriptionRepository subscriptionRepository) {
        this.subscriptionRepository = subscriptionRepository;
    }



    public List<Subscription> getSubscriptionsByPincode(String pincode) {
        return subscriptionRepository.findByPincode(pincode);
    }

    public List<Subscription> getMySubscriptions(String username) {
        return subscriptionRepository.findByUsername(username);
    }

    public void unsubscribe(Long id) {
        subscriptionRepository.deleteById(id);
    }
    public Subscription subscribe(String username, String email, String pincode) {
        // Check for duplicate subscription
        if (subscriptionRepository.existsByUsernameAndPincode(username, pincode)) {
            throw new RuntimeException("You are already subscribed to pincode: " + pincode);
        }
        Subscription sub = new Subscription();
        sub.setUsername(username);
        sub.setEmail(email);
        sub.setPincode(pincode);
        sub.setActive(true);
        return subscriptionRepository.save(sub);
    }
}