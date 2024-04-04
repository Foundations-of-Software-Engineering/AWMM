package com.awmm.messageserver.cards;

import org.springframework.stereotype.Repository;

import com.awmm.messageserver.cards.Cards;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

@Repository
@Transactional
public class CardsRepository {

	@PersistenceContext
	EntityManager entityManager;
	
	public void insert(Cards cards) {
		entityManager.merge(cards);
	}
	
	public Cards findById(String id) {
		return entityManager.find(Cards.class, id);
	}
	
	public void deleteById(String id) {
		Cards cards = entityManager.find(Cards.class, id);
		entityManager.remove(cards);
	}
	
}
