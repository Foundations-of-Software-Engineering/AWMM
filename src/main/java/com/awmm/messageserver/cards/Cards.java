package com.awmm.messageserver.cards;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Cards {

//	@Id
//	private int id;
	
//	@GeneratedValue(generator = "uuid")
//	@GenericGenerator(name = "uuid", strategy = "uuid2")
	@Id
	@Column(name = "GAMEID")
	String gameID;
	
	// Suspects
	String plum        ;
	String scarlet     ;
	String mustard     ;
	String peacock     ;
	String green       ;
	String white       ;
	// Weapons
	String rope        ;
	String pipe        ;
	String knife       ;
	String wrench      ;
	String candlestick ;
	String revolver    ;
	// Locations
	String study       ;
	String hall        ;
	String lounge      ;
	String library     ;
	String billiard    ;
	String dining      ;
	String conservatory;
	String ballroom    ;
	String kitchen     ;
	
	public Cards() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Cards(String gameID, String plum, String scarlet, String mustard, String peacock, String green, String white,
			String rope, String pipe, String knife, String wrench, String candlestick, String revolver, String study,
			String hall, String lounge, String library, String billiard, String dining, String conservatory,
			String ballroom, String kitchen) {
		super();
		this.gameID = gameID;
		this.plum = plum;
		this.scarlet = scarlet;
		this.mustard = mustard;
		this.peacock = peacock;
		this.green = green;
		this.white = white;
		this.rope = rope;
		this.pipe = pipe;
		this.knife = knife;
		this.wrench = wrench;
		this.candlestick = candlestick;
		this.revolver = revolver;
		this.study = study;
		this.hall = hall;
		this.lounge = lounge;
		this.library = library;
		this.billiard = billiard;
		this.dining = dining;
		this.conservatory = conservatory;
		this.ballroom = ballroom;
		this.kitchen = kitchen;
	}

	public String getGameID() {
		return gameID;
	}

	public void setGameID(String gameID) {
		this.gameID = gameID;
	}

	public String getPlum() {
		return plum;
	}

	public void setPlum(String plum) {
		this.plum = plum;
	}

	public String getScarlet() {
		return scarlet;
	}

	public void setScarlet(String scarlet) {
		this.scarlet = scarlet;
	}

	public String getMustard() {
		return mustard;
	}

	public void setMustard(String mustard) {
		this.mustard = mustard;
	}

	public String getPeacock() {
		return peacock;
	}

	public void setPeacock(String peacock) {
		this.peacock = peacock;
	}

	public String getGreen() {
		return green;
	}

	public void setGreen(String green) {
		this.green = green;
	}

	public String getWhite() {
		return white;
	}

	public void setWhite(String white) {
		this.white = white;
	}

	public String getRope() {
		return rope;
	}

	@Override
	public String toString() {
		return "Cards [gameID=" + gameID + ", plum=" + plum + ", scarlet=" + scarlet + ", mustard=" + mustard
				+ ", peacock=" + peacock + ", green=" + green + ", white=" + white + ", rope=" + rope + ", pipe=" + pipe
				+ ", knife=" + knife + ", wrench=" + wrench + ", candlestick=" + candlestick + ", revolver=" + revolver
				+ ", study=" + study + ", hall=" + hall + ", lounge=" + lounge + ", library=" + library + ", billiard="
				+ billiard + ", dining=" + dining + ", conservatory=" + conservatory + ", ballroom=" + ballroom
				+ ", kitchen=" + kitchen + "]";
	}

	public void setRope(String rope) {
		this.rope = rope;
	}

	public String getPipe() {
		return pipe;
	}

	public void setPipe(String pipe) {
		this.pipe = pipe;
	}

	public String getKnife() {
		return knife;
	}

	public void setKnife(String knife) {
		this.knife = knife;
	}

	public String getWrench() {
		return wrench;
	}

	public void setWrench(String wrench) {
		this.wrench = wrench;
	}

	public String getCandlestick() {
		return candlestick;
	}

	public void setCandlestick(String candlestick) {
		this.candlestick = candlestick;
	}

	public String getRevolver() {
		return revolver;
	}

	public void setRevolver(String revolver) {
		this.revolver = revolver;
	}

	public String getStudy() {
		return study;
	}

	public void setStudy(String study) {
		this.study = study;
	}

	public String getHall() {
		return hall;
	}

	public void setHall(String hall) {
		this.hall = hall;
	}

	public String getLounge() {
		return lounge;
	}

	public void setLounge(String lounge) {
		this.lounge = lounge;
	}

	public String getLibrary() {
		return library;
	}

	public void setLibrary(String library) {
		this.library = library;
	}

	public String getBilliard() {
		return billiard;
	}

	public void setBilliard(String billiard) {
		this.billiard = billiard;
	}

	public String getDining() {
		return dining;
	}

	public void setDining(String dining) {
		this.dining = dining;
	}

	public String getConservatory() {
		return conservatory;
	}

	public void setConservatory(String conservatory) {
		this.conservatory = conservatory;
	}

	public String getBallroom() {
		return ballroom;
	}

	public void setBallroom(String ballroom) {
		this.ballroom = ballroom;
	}

	public String getKitchen() {
		return kitchen;
	}

	public void setKitchen(String kitchen) {
		this.kitchen = kitchen;
	}
	
}
