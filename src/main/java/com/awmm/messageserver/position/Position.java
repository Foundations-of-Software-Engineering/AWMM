package com.awmm.messageserver.position;

import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;

@Entity
public class Position {
	
	@EmbeddedId
	PositionID positionID;
	@Column(name="grid_row")
	int row;
	@Column(name="grid_col")
	int col;
	
	public Position() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Position(String gameID, int userID, int row, int col) {
		super();
		this.positionID = new PositionID(gameID, userID);
		this.row = row;
		this.col = col;
	}

	public int getRow() {
		return row;
	}

	public void setRow(int row) {
		this.row = row;
	}

	public int getCol() {
		return col;
	}

	public void setCol(int col) {
		this.col = col;
	}

	@Override
	public String toString() {
		return "Position [row=" + row + ", col=" + col + "]";
	}

	@Override
	public int hashCode() {
		return Objects.hash(col, row);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Position other = (Position) obj;
		return col == other.col && row == other.row;
	}
	
}
