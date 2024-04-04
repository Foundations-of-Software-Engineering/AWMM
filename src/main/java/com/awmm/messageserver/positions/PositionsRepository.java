package com.awmm.messageserver.positions;

import org.springframework.stereotype.Repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

@Repository
@Transactional
public class PositionsRepository {
	
	@PersistenceContext
	EntityManager entityManager;
	
	public void insert(Positions positions) {
		entityManager.merge(positions);
	}
	
	public Positions findById(String id) {
		return entityManager.find(Positions.class, id);
	}
	
	public void deleteById(String id) {
		Positions positions = entityManager.find(Positions.class, id);
		entityManager.remove(positions);
	}
}
