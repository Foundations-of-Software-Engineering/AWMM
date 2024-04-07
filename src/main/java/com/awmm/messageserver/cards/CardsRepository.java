package com.awmm.messageserver.cards;

import org.springframework.data.jpa.repository.JpaRepository;

import jakarta.transaction.Transactional;

@Transactional
public interface CardsRepository extends JpaRepository<Cards, String>{
}
