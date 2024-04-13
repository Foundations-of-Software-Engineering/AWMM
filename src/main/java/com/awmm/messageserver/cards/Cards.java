package com.awmm.messageserver.cards;

import com.awmm.messageserver.board.Board;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Cards {

	@Id
	@Column(name = "GAMEID")
	String gameID;
	
	private String plum        ;
	private String scarlet     ;
	private String mustard     ;
	private String peacock     ;
	private String green       ;
	private String white       ;
	// Weapons
	private String rope        ;
	private String pipe        ;
	private String knife       ;
	private String wrench      ;
	private String candlestick ;
	private String revolver    ;
	// Locations
	private String study       ;
	private String hall        ;
	private String lounge      ;
	private String library     ;
	private String billiard    ;
	private String dining      ;
	private String conservatory;
	private String ballroom    ;
	private String kitchen     ;
	// Suggestions
	@Column(name = "SUGGESTED_SUSPECT")
	private String suggestedSuspect;
	@Column(name = "SUGGESTED_WEAPON")
	private String suggestedWeapon ;
	@Column(name = "SUGGESTED_ROOM")
	private String suggestedRoom   ;
	
	public Cards() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Cards(String gameID, String plum, String scarlet, String mustard, String peacock, String green, String white,
			String rope, String pipe, String knife, String wrench, String candlestick, String revolver, String study,
			String hall, String lounge, String library, String billiard, String dining, String conservatory,
			String ballroom, String kitchen, String suggestedSuspect, String suggestedWeapon, String suggestedRoom) {
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
		this.suggestedSuspect = suggestedSuspect;
		this.suggestedWeapon = suggestedWeapon;
		this.suggestedRoom = suggestedRoom;
	}

	public String getSuggestedSuspect() {
		return suggestedSuspect;
	}

	public void setSuggestedSuspect(String suggestedSuspect) {
		this.suggestedSuspect = suggestedSuspect;
	}

	public String getSuggestedWeapon() {
		return suggestedWeapon;
	}

	public void setSuggestedWeapon(String suggestedWeapon) {
		this.suggestedWeapon = suggestedWeapon;
	}

	public String getSuggestedRoom() {
		return suggestedRoom;
	}

	public void setSuggestedRoom(String suggestedRoom) {
		this.suggestedRoom = suggestedRoom;
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
	
	@Override
	public String toString() {
		return "Cards [gameID=" + gameID + ", plum=" + plum + ", scarlet=" + scarlet + ", mustard=" + mustard
				+ ", peacock=" + peacock + ", green=" + green + ", white=" + white + ", rope=" + rope + ", pipe=" + pipe
				+ ", knife=" + knife + ", wrench=" + wrench + ", candlestick=" + candlestick + ", revolver=" + revolver
				+ ", study=" + study + ", hall=" + hall + ", lounge=" + lounge + ", library=" + library + ", billiard="
				+ billiard + ", dining=" + dining + ", conservatory=" + conservatory + ", ballroom=" + ballroom
				+ ", kitchen=" + kitchen + ", suggestedSuspect=" + suggestedSuspect + ", suggestedWeapon="
				+ suggestedWeapon + ", suggestedRoom=" + suggestedRoom + "]";
	}
	
	public void set(String player, String card) {
		switch (card) {
		case Board.ProfessorPlumName: { setPlum(player); return;}
		case Board.  MissScarletName: { setScarlet(player); return; }
		case Board.   ColMustardName: { setMustard(player); return; }
		case Board.   MrsPeacockName: { setPeacock(player); return; }
		case Board.      MrGreenName: { setGreen(player); return; }
		case Board.     MrsWhiteName: { setWhite(player); return; }
		case Board.         RopeName: { setRope(player); return; }
		case Board.     LeadPipeName: { setPipe(player); return; }
		case Board.        KnifeName: { setKnife(player); return; }
		case Board.       WrenchName: { setWrench(player); return; }
		case Board.  CandlestickName: { setCandlestick(player); return; }
		case Board.     RevolverName: { setRevolver(player); return; }
		case Board.        StudyName: { setStudy(player); return; }
		case Board.         HallName: { setHall(player); return; }
		case Board.       LoungeName: { setLounge(player); return; }
		case Board.      LibraryName: { setLibrary(player); return; }
		case Board. BilliardRoomName: { setBilliard(player); return; }
		case Board.   DiningRoomName: { setDining(player); return; }
		case Board. ConservatoryName: { setConservatory(player); return; }
		case Board.     BallroomName: { setBallroom(player); return; }
		case Board.      KitchenName: { setKitchen(player); return; }
		default: return;
		}
	}
	
	public String get(String card) {
		switch(card) {
		case Board.ProfessorPlumName: { return getPlum();        }
		case Board.  MissScarletName: { return getScarlet();     }
		case Board.   ColMustardName: { return getMustard();     }
		case Board.   MrsPeacockName: { return getPeacock();     }
		case Board.      MrGreenName: { return getGreen();       }
		case Board.     MrsWhiteName: { return getWhite();       }
	    case Board.         RopeName: { return getRope();        }
	    case Board.     LeadPipeName: { return getPipe();        }
	    case Board.        KnifeName: { return getKnife();       }
	    case Board.       WrenchName: { return getWrench();      }
	    case Board.  CandlestickName: { return getCandlestick(); }
	    case Board.     RevolverName: { return getRevolver();    }
	    case Board.	       StudyName: { return getStudy();       } 
	    case Board.	        HallName: { return getHall();        } 
	    case Board.	      LoungeName: { return getLounge();      } 
	    case Board.	     LibraryName: { return getLibrary();     } 
	    case Board.	BilliardRoomName: { return getBilliard();    } 
	    case Board.	  DiningRoomName: { return getDining();      } 
	    case Board.	ConservatoryName: { return getConservatory();} 
	    case Board.	    BallroomName: { return getBallroom();    } 
	    case Board.	     KitchenName: { return getKitchen();     } 
		default: return null;
		}
	}
	
//	public void getSolutions() {
	
}
