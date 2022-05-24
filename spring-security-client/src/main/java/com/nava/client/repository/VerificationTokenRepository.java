package com.nava.client.repository;

import com.nava.client.entity.VerificationToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VerificationTokenRepository extends JpaRepository<VerificationToken,Long> {

    VerificationToken findByToken(String token);
}
