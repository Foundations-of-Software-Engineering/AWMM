package com.awmm.messageserver.jpa;

import org.springframework.stereotype.Repository;

import com.awmm.messageserver.player.Player;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

@Repository
@Transactional
public class PlayerJpaRepository {

	
	@PersistenceContext
	EntityManager entityManager;
	
	public void insert(Player player) {
		entityManager.merge(player);
	}
	
	public Player findById(long id) {
		return entityManager.find(Player.class, id);
	}
	
	public void deleteById(long id) {
		Player player = entityManager.find(Player.class, id);
		entityManager.remove(player);
	}
	
	
}
