package com.identity_service.configuration;

import java.time.LocalDateTime;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.identity_service.repository.InvalidatedTokenRepository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TokenCleanupScheduler {
    InvalidatedTokenRepository tokenRepository;

    @Scheduled(cron = "0 0 0 * * ?")
    public void scheduleCollectInvalidatedToken() {
        log.info("Running token cleanup at {}", LocalDateTime.now());
        int deletedCount = tokenRepository.findAll().size();
        tokenRepository.deleteAll();
        log.info("Deleted " + deletedCount + " tokens");
    }
}
