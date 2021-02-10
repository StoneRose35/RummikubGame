package ch.sr35.rummikub.web;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

@Service
public class RummikubGameData {
	
	private List<RummikubGame> games;
	private List<RummikubToken> tokens;
	
	RummikubGameData()
	{
		this.games=new ArrayList<RummikubGame>();
		this.tokens=new ArrayList<RummikubToken>();
	}
	
	public List<RummikubToken> getTokens() {
		return tokens;
	}
	public void setTokens(List<RummikubToken> tokens) {
		this.tokens = tokens;
	}
	public List<RummikubGame> getGames() {
		return games;
	}
	public void setGames(List<RummikubGame> games) {
		this.games = games;
	}

}
