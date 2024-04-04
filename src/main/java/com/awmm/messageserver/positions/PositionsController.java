package com.awmm.messageserver.positions;

import java.util.HashMap;
import java.util.Map;

import com.awmm.messageserver.board.Board;

public class PositionsController {
	public Map<String, Integer[]> getPositionsMap(Positions positions) {
		HashMap<String, Integer[]> map = new HashMap<>();
		
		map.put(Board.ProfessorPlumName, new Integer[]{   positions.getPlumRow(),    positions.getPlumCol()});
		map.put(Board.MissScarletName  , new Integer[]{positions.getScarletRow(), positions.getScarletCol()});
		map.put(Board.ColMustardName   , new Integer[]{positions.getMustardRow(), positions.getMustardCol()});
		map.put(Board.MrsPeacockName   , new Integer[]{positions.getPeacockRow(), positions.getPeacockCol()});
		map.put(Board.MrGreenName      , new Integer[]{  positions.getGreenRow(),   positions.getGreenCol()});
		map.put(Board.MrsWhiteName     , new Integer[]{  positions.getWhiteRow(),   positions.getWhiteCol()});
		
		return map;
	}
}
