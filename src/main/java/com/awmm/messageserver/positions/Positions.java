package com.awmm.messageserver.positions;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Positions {
	
	@Id
	@Column(name = "GAMEID")
	String gameID;

	private int    plumRow;
	private int    plumCol;     
	private int scarletRow;
	private int scarletCol;        
	private int mustardRow;
	private int mustardCol;        
	private int peacockRow;
	private int peacockCol;        
	private int   greenRow;
	private int   greenCol;      
	private int   whiteRow;
	private int   whiteCol;
	
	
	public Positions() {
	}
	
	public Positions(String gameID, int plumRow, int plumCol, int scarletRow, int scarletCol, int mustardRow,
			int mustardCol, int peacockRow, int peacockCol, int greenRow, int greenCol, int whiteRow, int whiteCol) {
		super();
		this.gameID = gameID;
		this.plumRow = plumRow;
		this.plumCol = plumCol;
		this.scarletRow = scarletRow;
		this.scarletCol = scarletCol;
		this.mustardRow = mustardRow;
		this.mustardCol = mustardCol;
		this.peacockRow = peacockRow;
		this.peacockCol = peacockCol;
		this.greenRow = greenRow;
		this.greenCol = greenCol;
		this.whiteRow = whiteRow;
		this.whiteCol = whiteCol;
	}

	public String getGameID() {
		return gameID;
	}
	public void setGameID(String gameID) {
		this.gameID = gameID;
	}
	public int getPlumRow() {
		return plumRow;
	}
	public void setPlumRow(int plumRow) {
		this.plumRow = plumRow;
	}
	public int getScarletRow() {
		return scarletRow;
	}
	public void setScarletRow(int scarletRow) {
		this.scarletRow = scarletRow;
	}
	public int getMustardRow() {
		return mustardRow;
	}
	public void setMustardRow(int mustardRow) {
		this.mustardRow = mustardRow;
	}
	public int getPeacockRow() {
		return peacockRow;
	}
	public void setPeacockRow(int peacockRow) {
		this.peacockRow = peacockRow;
	}
	public int getGreenRow() {
		return greenRow;
	}
	public void setGreenRow(int greenRow) {
		this.greenRow = greenRow;
	}
	public int getWhiteRow() {
		return whiteRow;
	}
	public void setWhiteRow(int whiteRow) {
		this.whiteRow = whiteRow;
	}
	public int getPlumCol() {
		return plumCol;
	}
	public void setPlumCol(int plumCol) {
		this.plumCol = plumCol;
	}
	public int getScarletCol() {
		return scarletCol;
	}
	public void setScarletCol(int scarletCol) {
		this.scarletCol = scarletCol;
	}
	public int getMustardCol() {
		return mustardCol;
	}
	public void setMustardCol(int mustardCol) {
		this.mustardCol = mustardCol;
	}
	public int getPeacockCol() {
		return peacockCol;
	}
	public void setPeacockCol(int peacockCol) {
		this.peacockCol = peacockCol;
	}
	public int getGreenCol() {
		return greenCol;
	}
	public void setGreenCol(int greenCol) {
		this.greenCol = greenCol;
	}
	public int getWhiteCol() {
		return whiteCol;
	}
	public void setWhiteCol(int whiteCol) {
		this.whiteCol = whiteCol;
	}

	@Override
	public String toString() {
		return "Positions [gameID=" + gameID + ", plumRow=" + plumRow + ", scarletRow=" + scarletRow + ", mustardRow="
				+ mustardRow + ", peacockRow=" + peacockRow + ", greenRow=" + greenRow + ", whiteRow=" + whiteRow
				+ ", plumCol=" + plumCol + ", scarletCol=" + scarletCol + ", mustardCol=" + mustardCol + ", peacockCol="
				+ peacockCol + ", greenCol=" + greenCol + ", whiteCol=" + whiteCol + "]";
	}      
	
}
