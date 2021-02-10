package ch.sr35.rummikub.web;

import java.util.ArrayList;
import java.util.Random;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ch.sr35.rummikub.common.RummikubFigure;
import ch.sr35.rummikub.common.RummikubPlacement;
import ch.sr35.rummikub.web.dao.GameStateApi;
import ch.sr35.rummikub.web.dao.RummikubFigureApi;
import ch.sr35.rummikub.web.dao.RummikubGameApi;
import ch.sr35.rummikub.web.dao.RummikubPlayerApi;

@CrossOrigin(origins = {"http://localhost:4200"},allowCredentials = "true")
@RestController
public class RummikubController {
	
	@Autowired
	WebsocketController wsController;
	
	@Autowired
	RummikubGameData data;
	
	public RummikubController()
	{
	}
	
	
	@GetMapping("/ping")
	public String aboutRummikubGame()
	{
		return "This is the Rummikub Game Web API";
	}
	
	@GetMapping("/dispose")
	public Response disposeGame(@CookieValue(value = "RKToken", defaultValue = "") String token)
	{
		Response r = new Response();
		RummikubToken t = this.data.getTokens().stream().filter(tt -> tt.getToken().equals(token)).findFirst().orElse(null);
		if (t!=null)
		{
			this.data.getGames().remove(t.getGame());
			this.data.getTokens().remove(t);
		}
		wsController.updateGames();
		return r;
	}
	
	@GetMapping("/reconnect")
	public PlayerResponse handleReconnect(@CookieValue(value = "RKToken", defaultValue = "") String token)
	{
		RummikubToken t = this.data.getTokens().stream().filter(tt -> tt.getToken().equals(token)).findFirst().orElse(null);
		if (t!=null)
		{
			PlayerResponse r=new PlayerResponse();
			r.setMessage("reconnected player " + t.getPlayer().getName() + " to game " + t.getGame().getName());
			r.setPlayer((RummikubPlayerApi)t.getPlayer());
			r.setToken(t.getToken());
			r.setGameName(t.getGame().getName());
			return r;
		}
		return null;
	}
	
	@GetMapping("/draw")
	public RummikubFigureApi getFigure(@CookieValue(value = "RKToken", defaultValue = "") String token)
	{
		RummikubToken t = this.data.getTokens().stream().filter(tt -> tt.getToken().equals(token)).findFirst().orElse(null);
		if (t!=null)
		{
			RummikubGame g=t.getGame();
			RummikubPlayer p = t.getPlayer();
			RummikubFigure f =  g.drawFigure();
			try {
				p.addFigure(f);
			}
			catch (RummikubApiException e) {
				return null;
			}
			g.rotatePlayer();
			wsController.updatePlayers(g);
			return RummikubFigureApi.fromRummikubFigure(f);
		}
		return null;
	}
	

	@GetMapping("/newgame")
	public Response generateGame(@RequestParam String name,@RequestParam int nrAiPlayers)
	{
		Response r = new Response();
		if (nrAiPlayers < 0 || nrAiPlayers > 3)
		{
			r.setError("Number of Ai Players must be in the range from 0 to 3");
			return r;
		}
		if (this.data.getGames().stream().filter(e -> e.getName().equals(name)).count()==0)
		{
			RummikubGame g = new RummikubGame(this.wsController);
			g.setName(name);
			this.data.getGames().add(g);
			
			for(int c=0;c<nrAiPlayers;c++)
			{
				RummikubPlayerAsp aiPlayer = new RummikubPlayerAsp();
				aiPlayer.setName(AiPlayerNameGenerator.generateName());
				try {
					g.addPlayer(aiPlayer);
				} catch (RummikubApiException e1) {
					r.setError(e1.getMessage());
					return r;
				}
			}
			
			r.setMessage("Successfully created game " + name);
			wsController.updateGames();
			return r;
		}
		else
		{
			r.setError("Game " + name + " already exists");
			return r;
		}
	}

	@GetMapping("/games")
	public List<RummikubGameApi> getGames(@CookieValue(value = "RKToken", defaultValue = "") String token)
	{
		if (token.length() > 0)
		{
			RummikubToken t = this.data.getTokens().stream().filter(tt -> tt.getToken().equals(token)).findFirst().orElse(null);
			if (t!=null)
			{
				List<RummikubGameApi> apiGames = (List<RummikubGameApi>)this.data.getGames().stream().map(g -> RummikubGameApi.fromRummikubGame(g)).collect(Collectors.toList());
				apiGames.stream().filter(g -> g.getName()==t.getGame().getName()).forEach(el -> el.declareAsJoined());
				return apiGames;
			}
		}
		return this.data.getGames().stream().map(g -> RummikubGameApi.fromRummikubGame(g)).collect(Collectors.toList());
	}

	
	@GetMapping("/registerPlayer")
	public PlayerResponse registerPlayer(@RequestParam String name,@RequestParam String gameId,HttpServletResponse response)
	{
		PlayerResponse r=new PlayerResponse();
		
		RummikubGame game = this.getGame(gameId);
		if (game!=null)
		{
			try {
				RummikubToken t = new RummikubToken();
				t.setGame(game);
				t.setPlayer(game.addPlayer(name));
				t.setToken(this.getHexString((short) 10));
				this.data.getTokens().add(t);
				Cookie c = new Cookie("RKToken",t.getToken());
				response.addCookie(c);
				r.setMessage("Sucessfully registered " + name);
				r.setPlayer((RummikubPlayerApi)t.getPlayer());
				r.setToken(t.getToken());
				r.setGameName(gameId);
				wsController.updatePlayers(game);
			} catch (RummikubApiException e) {
				r.setError(e.getMessage());
			}
		}
		else
		{
			r.setError("Game " + gameId + " not existent");
		}
		wsController.updateGames();

		return r;
	}
	
	@GetMapping("/players")
	public List<RummikubPlayerApi> getPlayers(@CookieValue(value = "RKToken", defaultValue = "") String token)
	{
			return this.data.getTokens().stream()
					.filter(tt -> tt.getToken().equals(token))
					.findFirst()
					.orElse(null).getGame().getPlayers().stream().map(p -> {
				return new RummikubPlayerApi(p);
			}).collect(Collectors.toList());

	}
	
	@GetMapping("/shelfFigures")
	public List<RummikubFigureApi> getShelfFigure(@CookieValue(value = "RKToken", defaultValue = "") String token)
	{
		return this.data.getTokens().stream()
				.filter(tt -> tt.getToken().equals(token))
				.findFirst()
				.orElse(null)
				.getPlayer()
				.getFigures()
				.stream()
				.map(f -> RummikubFigureApi.fromRummikubFigure(f))
				.collect(Collectors.toList());
	}
	
	@GetMapping("/tableFigures")
	public List<List<RummikubFigureApi>> getTableFigures(@CookieValue(value = "RKToken", defaultValue = "") String token)
	{
		List<List<RummikubFigureApi>> res=new ArrayList<List<RummikubFigureApi>>();
		RummikubGame g = 		this.data.getTokens().stream()
				.filter(tt -> tt.getToken().equals(token))
				.findFirst()
				.orElse(null).getGame();
		if (g!=null)
		{
			g.getTableFigures().stream().forEach((f) -> {
				List<RummikubFigureApi> l=new ArrayList<RummikubFigureApi>();
				f.iterator().forEachRemaining(el -> l.add(RummikubFigureApi.fromRummikubFigure(el)));
				res.add(l);
			} );
			
		}
		return res;
	}
	
	
	@PostMapping("/submitMove")
	public GameStateApi submitMove(@RequestBody GameStateApi gsSubmitted,@CookieValue(value = "RKToken", defaultValue = "") String token)
	{
		GameStateApi gameStateReturned;
		RummikubGame currentGame = this.data.getTokens().stream()
				.filter(tt -> tt.getToken().equals(token))
				.findFirst()
				.orElse(null)
				.getGame();
		RummikubPlayer player = this.data.getTokens().stream()
				.filter(tt -> tt.getToken().equals(token))
				.findFirst()
				.orElse(null).getPlayer();
		String playerName = player.getName();
		gsSubmitted.validate();
		boolean laidDownEnough = true;
		int sumLaidDown = currentGame.getPlayer(playerName)
				.getFigures() 
				.stream()
				.map(el -> RummikubFigureApi.fromRummikubFigure(el))
				.filter( f -> !gsSubmitted.getShelfFigures().contains(f))
				.mapToInt(f -> { 
					if (f.getInstance()<3)
					{return f.getNumber();}
					return 0;
				}).sum();
		if (player.getRoundNr()==0)
		{
			// check if at least 30 points have been laid down
			laidDownEnough = sumLaidDown >= 30;
			
		} else
		{
			// the player must lay down at least one point
			laidDownEnough = sumLaidDown > 0;
		}
		
		if (gsSubmitted.isAccepted()==false || laidDownEnough==false)
		{
			gameStateReturned=new GameStateApi();

			gameStateReturned.setTableFigures(currentGame.getTableFigures().stream().map(f -> 
						f.stream().map(el -> RummikubFigureApi.fromRummikubFigure(el)).collect(Collectors.toList())
					).collect(Collectors.toList()));
			RummikubFigure f =  currentGame.drawFigure();
			try {
				currentGame.getPlayer(playerName).addFigure(f);
			}
			catch (RummikubApiException e)
			{
				return null;
			}
			gameStateReturned.setShelfFigures(currentGame.getPlayer(playerName).getFigures()
					.stream().map(el -> RummikubFigureApi.fromRummikubFigure(el)).collect(Collectors.toList()));
		}
		else
		{
			player.setRoundNr(player.getRoundNr()+1);
			gameStateReturned=gsSubmitted;
			currentGame.setTableFigures(gsSubmitted.getTableFiguresStructured());
			currentGame.getPlayer(playerName).setFigures(gsSubmitted.getShelfFigures().stream().map(el -> el.toRummikubFigure(RummikubPlacement.ON_SHELF)).collect(Collectors.toList()));
			
			if (gsSubmitted.getShelfFigures().size()==0)
			{
				// player has legibly placed all the figures on the Table, s*he wins!
				// set final scores for all the players
				currentGame.getPlayers().forEach(p -> {
					p.setFinalScore(p.getFigures().stream().mapToInt(f -> f.getScore()).sum());
				});
				
			}
			
		}
		currentGame.rotatePlayer();
		wsController.updatePlayers(currentGame);
		return gameStateReturned;
	}
	
	@PostMapping("/updateShelf")
	public Response updateShelfFigures(@RequestBody List<RummikubFigureApi> figures,@CookieValue(value = "RKToken", defaultValue = "") String token)
	{
		RummikubPlayer player = this.data.getTokens().stream()
				.filter(tt -> tt.getToken().equals(token))
				.findFirst()
				.orElse(null).getPlayer();
		
		List<RummikubFigure> replacedFigures = figures.stream().map(f ->f.toRummikubFigure(RummikubPlacement.ON_SHELF)).collect(Collectors.toList());
		Response r = new Response();
		if (replacedFigures.containsAll(player.getFigures()))
		{
			player.setFigures(replacedFigures);
			r.setMessage("updated Shelf");
		}
		else
		{
			r.setError("Got erroneous Figures from player");
		}
		return r;
	}
	
	
	private String getHexString(short stringSize) {
		Random r = new Random();
		byte[] b=new byte[stringSize];
		r.nextBytes(b);
		char[] hexDigits = new char[stringSize*2];
		for (int c=0;c<stringSize;c++)
		{
			
			hexDigits[2*c] = Character.forDigit((b[c] >> 4) & 0xF, 16);
			hexDigits[2*c+1] = Character.forDigit((b[c] & 0xF), 16);
		}
		final String name=new String(hexDigits);
		return name;
	}
	
	private RummikubGame getGame(String gameId)
	{
		return this.data.getGames().stream().filter(g -> g.getName().equals(gameId)).findFirst().orElse(null);
	}
	
}
