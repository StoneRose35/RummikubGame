package game;

import java.util.List;

public class RummikubResult {
	private List<IRummikubFigureBag> onTable;
	private int scoreLaid;

	public List<IRummikubFigureBag> getOnTable() {
		return onTable;
	}

	public void setOnTable(List<IRummikubFigureBag> onTable) {
		this.onTable = onTable;
	}

	public int getScoreLaid() {
		return scoreLaid;
	}

	public void setScoreLaid(int scoreLaid) {
		this.scoreLaid = scoreLaid;
	}
	

}
