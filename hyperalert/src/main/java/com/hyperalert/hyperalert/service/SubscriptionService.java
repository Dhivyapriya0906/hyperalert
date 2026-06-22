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

    public Subscription subscribe(String pincode, String email, String username) {
        Subscription sub = new Subscription();
        sub.setPincode(pincode);
        sub.setEmail(email);
        sub.setUsername(username);
        return subscriptionRepository.save(sub);
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
}