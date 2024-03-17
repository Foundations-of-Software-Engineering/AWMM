create table player 
(
	id bigint not null, 
	game_id bigint not null,
	name varchar(255) not null,
	location varchar(255) not null,
	primary key (id)
);

--create table card
--( 
--	name varchar(255) not null,
--	owner varchar(255) not null,
--	gameId bigint not null,
--	primary key (gameId, name)
--);