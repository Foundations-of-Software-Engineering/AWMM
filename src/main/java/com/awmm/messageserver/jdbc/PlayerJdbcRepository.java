//package com.awmm.messageserver.jdbc;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.jdbc.core.BeanPropertyRowMapper;
//import org.springframework.jdbc.core.JdbcTemplate;
//import org.springframework.stereotype.Repository;
//
//import com.awmm.messageserver.player.Player;
//
//@Repository
//public class PlayerJdbcRepository {
//
//	@Autowired
//	private JdbcTemplate springJdbcTemplate;
//	
//	private static String INSERT_QUERY = 
//			"""
//				insert into player (id, game_id, name, location)
//				values(?, ?, ?, ?);
//			""";
//			
//	private static String DELETE_QUERY = 
//			"""
//				delete from player where id=?
//			""";
//	
//	private static String SELECT_QUERY = 
//		"""
//			select * from player
//			where id = ?
//		""";
//
//	public void insert(Player player) {
//		springJdbcTemplate.update(INSERT_QUERY, 
//				player.getId(), player.getGameId(), player.getName(), player.getLocation());
//	}
//	
//	public void deleteById(long id)
//	{
//		springJdbcTemplate.update(DELETE_QUERY, id);
//	
//	}
//	
//	public Player findById(long id) {
//		return springJdbcTemplate.queryForObject(SELECT_QUERY, 
//				new BeanPropertyRowMapper<>(Player.class), id);
//	}
//}
