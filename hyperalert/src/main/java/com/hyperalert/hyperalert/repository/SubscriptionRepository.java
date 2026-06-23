package com.hyperalert.hyperalert.repository;

import com.hyperalert.hyperalert.entity.Subscription;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {
    List<Subscription> findByPincode(String pincode);
    List<Subscription> findByUsername(String username);
    boolean existsByUsernameAndPincode(String username, String pincode);
}
