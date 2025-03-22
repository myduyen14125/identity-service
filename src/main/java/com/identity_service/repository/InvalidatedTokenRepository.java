package com.identity_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.identity_service.entity.InvalidatedToken;

public interface InvalidatedTokenRepository extends JpaRepository<InvalidatedToken, String> {}
