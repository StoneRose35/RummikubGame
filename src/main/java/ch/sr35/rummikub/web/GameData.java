package ch.sr35.rummikub.web;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

@Service
public class GameData {
	
	private List<Game> games;
	private List<Token> tokens;
	
	GameData()
	{
		this.games=new ArrayList<Game>();
		this.tokens=new ArrayList<Token>();
	}
	
	public List<Token> getTokens() {
		return tokens;
	}
	public void setTokens(List<Token> tokens) {
		this.tokens = tokens;
	}
	public List<Game> getGames() {
		return games;
	}
	public void setGames(List<Game> games) {
		this.games = games;
	}

}
