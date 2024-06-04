package com.example.foodx_be.service;

import com.example.foodx_be.repository.InvalidatedTokenRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Date;

@Service
@Transactional
public class TokensPurgeTask {
    @Autowired
    private InvalidatedTokenRepository invalidatedTokenRepository;

    @Scheduled(cron = "${purge.cron.expression}")
    public void purgeExpired() {
        Date now = Date.from(Instant.now());
        invalidatedTokenRepository.deleteByExpiryTimeLessThan(now);
    }
}
