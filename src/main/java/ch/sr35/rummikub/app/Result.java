package ch.sr35.rummikub.app;

import java.util.List;

import ch.sr35.rummikub.common.IFigureBag;

public class Result {
	private List<IFigureBag> onTable;
	private int scoreLaid;

	public List<IFigureBag> getOnTable() {
		return onTable;
	}

	public void setOnTable(List<IFigureBag> onTable) {
		this.onTable = onTable;
	}

	public int getScoreLaid() {
		return scoreLaid;
	}

	public void setScoreLaid(int scoreLaid) {
		this.scoreLaid = scoreLaid;
	}
	

}
