package ch.sr35.rummikub.web;

import java.util.ArrayList;
import java.util.stream.Collectors;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import ch.sr35.rummikub.common.Figure;
import ch.sr35.rummikub.common.Placement;
import ch.sr35.rummikub.common.exceptions.ApiException;
import ch.sr35.rummikub.common.utils.HexStringHelper;
import ch.sr35.rummikub.web.dao.GameStateApi;
import ch.sr35.rummikub.web.dao.FigureApi;
import ch.sr35.rummikub.web.dao.GameApi;
import ch.sr35.rummikub.web.dao.PlayerApi;
import ch.sr35.rummikub.web.responses.NewGameResponse;
import ch.sr35.rummikub.web.responses.PlayerResponse;
import ch.sr35.rummikub.web.responses.Response;
import ch.sr35.rummikub.web.ui.AiPlayerNameGenerator;
import ch.sr35.rummikub.web.ui.AvatarImageGenerator;

@CrossOrigin(origins = {"http://localhost:4200"},allowCredentials = "true")
@org.springframework.web.bind.annotation.RestController
public class RestController {
	
	@Autowired
	WebsocketController wsController;
	
	@Autowired
	GameData data;
	
	@Autowired
	AvatarImageGenerator avatarIG;
	
	static Logger logger = LoggerFactory.getLogger(RestController.class);
	
	public RestController()
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
		Token t = this.data.getTokens().stream().filter(tt -> tt.getToken().equals(token)).findFirst().orElse(null);
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
		Token t = this.data.getTokens().stream().filter(tt -> tt.getToken().equals(token)).findFirst().orElse(null);
		if (t!=null)
		{
			PlayerResponse r=new PlayerResponse();
			r.setMessage("reconnected player " + t.getPlayer().getName() + " to game " + t.getGame().getName());
			r.setPlayer((PlayerApi)t.getPlayer());
			r.setToken(t.getToken());
			r.setGameName(t.getGame().getName());
			r.setGameId(t.getGame().getGameId());
			return r;
		}
		return null;
	}
	
	@GetMapping("/draw")
	public FigureApi getFigure(@CookieValue(value = "RKToken", defaultValue = "") String token)
	{

		Token t = this.data.getTokens().stream().filter(tt -> tt.getToken().equals(token)).findFirst().orElse(null);
		if (t!=null)
		{
			Game g=t.getGame();
			Player p = t.getPlayer();
			
			Stopwatch sw = g.getStopwatch();
			boolean doDraw = false;
			if ((sw != null && sw.isAlive()))
			{
				sw.Abort();
				doDraw=true;
			}
			else if (sw==null)
			{
				doDraw=true;
			}
			
			if(doDraw)
			{
				Figure f =  g.drawFigure();
				try {
					p.addFigure(f);
				}
				catch (ApiException e) {
					return null;
				}
				g.rotatePlayer();
				wsController.updatePlayers(g);
				return FigureApi.fromRummikubFigure(f);
			}
			
		}
		return null;
	}
	

	@GetMapping("/newgame")
	public NewGameResponse generateGame(@RequestParam String name,@RequestParam int nrAiPlayers,@RequestParam int maxDuration,
			@CookieValue(value = "RKToken", defaultValue = "") String token)
	{
		NewGameResponse r = new NewGameResponse();
		if (nrAiPlayers < 0 || nrAiPlayers > 3)
		{
			r.setError("Number of Ai Players must be in the range from 0 to 3");
			return r;
		}
		if (this.data.getGames().stream().filter(e -> e.getName().equals(name)).count()==0 && this.data.getTokens().stream().anyMatch(e -> e.getToken()==token))
		{
			Game g = new Game(this.wsController);
			g.setGameId(HexStringHelper.getHexString((short) 12));
			r.setGameId(g.getGameId());
			g.setName(name);
			this.data.getTokens().stream().filter(e -> e.getToken()==token).findFirst().ifPresent(tk -> g.setOwner(tk.getPlayer()));
			if (maxDuration > 0)
			{
				g.initStopwatch(maxDuration);
			}
			this.data.getGames().add(g);
			
			for(int c=0;c<nrAiPlayers;c++)
			{
				PlayerAsp aiPlayer = new PlayerAsp();
				aiPlayer.setName(AiPlayerNameGenerator.generateName());
				aiPlayer.setAvatar(avatarIG.generateAvatar());
				try {
					g.addPlayer(aiPlayer);
				} catch (ApiException e1) {
					r.setError(e1.getMessage());
					return r;
				}
			}
			
			
			r.setMessage("Successfully created game " + name);
			r.setGame(GameApi.fromRummikubGame(g));
			wsController.updateGames();
			return r;
		}
		else if (this.data.getGames().stream().filter(e -> e.getName().equals(name)).count() > 0)
		{
			r.setError("Game " + name + " already exists");
			return r;
		}
		else if (!this.data.getTokens().stream().anyMatch(e -> e.getToken()==token))
		{
			r.setError("You must register as a player first");
			return r;
		}
		return r;
	}

	@GetMapping("/games")
	public List<GameApi> getGames(@CookieValue(value = "RKToken", defaultValue = "") String token)
	{
		if (token.length() > 0)
		{
			Token t = this.data.getTokens().stream().filter(tt -> tt.getToken().equals(token)).findFirst().orElse(null);
			if (t!=null)
			{
				List<GameApi> apiGames = (List<GameApi>)this.data.getGames().stream().map(g -> GameApi.fromRummikubGame(g)).collect(Collectors.toList());
				apiGames.stream().filter(g -> g.getName()==t.getGame().getName()).forEach(el -> el.declareAsJoined());
				return apiGames;
			}
		}
		return this.data.getGames().stream().map(g -> GameApi.fromRummikubGame(g)).collect(Collectors.toList());
	}

	@GetMapping("/addPlayer")
	public PlayerResponse addPlayer(@RequestParam String name,HttpServletResponse response)
	{
		PlayerResponse r=new PlayerResponse();
		if (this.data.getTokens().stream().anyMatch(el -> el.getPlayer().getName()==name))
		{
			r.setError("Player Name " + name + " already taken, please choose another one");
			return r;
		}
		Token t = new Token();
		Player p = new Player();
		p.setName(name);
		p.setAvatar(avatarIG.generateAvatar());
		t.setPlayer(p);
		t.setToken(HexStringHelper.getHexString((short) 10));
		this.data.getTokens().add(t);
		Cookie c = new Cookie("RKToken",t.getToken());
		response.addCookie(c);
		r.setMessage("Sucessfully added " + name);
		r.setPlayer((PlayerApi)t.getPlayer());
		r.setToken(t.getToken());
		return r;
	}
	
	@GetMapping("/registerPlayer")
	public PlayerResponse registerPlayer(@RequestParam String name,@RequestParam String gameId,HttpServletResponse response)
	{
		PlayerResponse r=new PlayerResponse();
		
		Game game = this.getGame(gameId);
		if (game!=null)
		{
			try {
				Token t = new Token();
				t.setGame(game);
				Player p = game.addPlayer(name);
				p.setAvatar(avatarIG.generateAvatar());
				t.setPlayer(p);
				t.setToken(HexStringHelper.getHexString((short) 10));
				this.data.getTokens().add(t);
				Cookie c = new Cookie("RKToken",t.getToken());
				response.addCookie(c);
				r.setMessage("Sucessfully registered " + name + " with game " + game.getName());
				r.setPlayer((PlayerApi)t.getPlayer());
				r.setToken(t.getToken());
				r.setGameName(game.getName());
				r.setGameId(gameId);
				wsController.updatePlayers(game);
			} catch (ApiException e) {
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
	public List<PlayerApi> getPlayers(@CookieValue(value = "RKToken", defaultValue = "") String token)
	{
			return this.data.getTokens().stream()
					.filter(tt -> tt.getToken().equals(token))
					.findFirst()
					.orElse(null).getGame().getPlayers().stream().map(p -> {
				return new PlayerApi(p);
			}).collect(Collectors.toList());

	}
	
	@GetMapping("/shelfFigures")
	public List<FigureApi> getShelfFigure(@CookieValue(value = "RKToken", defaultValue = "") String token)
	{
		return this.data.getTokens().stream()
				.filter(tt -> tt.getToken().equals(token))
				.findFirst()
				.orElse(null)
				.getPlayer()
				.getFigures()
				.stream()
				.map(f -> FigureApi.fromRummikubFigure(f))
				.collect(Collectors.toList());
	}
	
	
	@GetMapping("/tableFigures")
	public List<List<FigureApi>> getTableFigures(@CookieValue(value = "RKToken", defaultValue = "") String token)
	{
		List<List<FigureApi>> res=new ArrayList<List<FigureApi>>();
		Game g = 		this.data.getTokens().stream()
				.filter(tt -> tt.getToken().equals(token))
				.findFirst()
				.orElse(null).getGame();
		if (g!=null)
		{
			g.getTableFigures().stream().forEach((f) -> {
				try
				{
					res.add(f.stream().map(el -> FigureApi.fromRummikubFigure(el)).collect(Collectors.toList()));
				}
				catch (Exception e)
				{
					logger.error(e.toString());
				}
			} );
			
		}
		return res;
	}
	
	
	@PostMapping("/submitMove")
	public GameStateApi submitMove(@RequestBody GameStateApi gsSubmitted,@CookieValue(value = "RKToken", defaultValue = "") String token)
	{
		GameStateApi gameStateReturned;
		Game currentGame = this.data.getTokens().stream()
				.filter(tt -> tt.getToken().equals(token))
				.findFirst()
				.orElse(null)
				.getGame();
		Player player = this.data.getTokens().stream()
				.filter(tt -> tt.getToken().equals(token))
				.findFirst()
				.orElse(null).getPlayer();
		String playerName = player.getName();
		gsSubmitted.validate();
		boolean laidDownEnough = true;
		int sumLaidDown = currentGame.getPlayer(playerName)
				.getFigures() 
				.stream()
				.map(el -> FigureApi.fromRummikubFigure(el))
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

		Stopwatch sw = currentGame.getStopwatch();
		boolean doRotate=false;
		if (sw != null && sw.isAlive())
		{
			sw.Abort();
			doRotate=true;
		}
		else if (sw == null)
		{
			doRotate=true;
		}
		
		if (doRotate)
		{
			if (gsSubmitted.isAccepted()==false || laidDownEnough==false)
			{
				gameStateReturned=new GameStateApi();
	
				gameStateReturned.setTableFigures(currentGame.getTableFigures().stream().map(f -> 
							f.stream().map(el -> FigureApi.fromRummikubFigure(el)).collect(Collectors.toList())
						).collect(Collectors.toList()));
				Figure f =  currentGame.drawFigure();
				try {
					currentGame.getPlayer(playerName).addFigure(f);
				}
				catch (ApiException e)
				{
					return null;
				}
				gameStateReturned.setShelfFigures(currentGame.getPlayer(playerName).getFigures()
						.stream().map(el -> FigureApi.fromRummikubFigure(el)).collect(Collectors.toList()));
			}
			else
			{
				player.setRoundNr(player.getRoundNr()+1);
				gameStateReturned=gsSubmitted;
				currentGame.setTableFigures(gsSubmitted.getTableFiguresStructured());
				currentGame.getPlayer(playerName).setFigures(gsSubmitted.getShelfFigures().stream().map(el -> el.toRummikubFigure(Placement.ON_SHELF)).collect(Collectors.toList()));
				
				if (gsSubmitted.getShelfFigures().size()==0)
				{
					// player has legibly placed all the figures on the Table, s*he wins!
					// set final scores for all the players
					currentGame.getPlayers().forEach(p -> {
						p.setFinalScore(p.getFigures().stream().mapToInt(f -> f.getScore()).sum());
					});
					
				}
				
			}
			if (gsSubmitted.getShelfFigures().size()!=0)
			{
				currentGame.rotatePlayer();
			}
			wsController.updatePlayers(currentGame);
			return gameStateReturned;
		}
		return null;
	}
	
	@GetMapping("/ready")
	public Response setReady(@CookieValue(value = "RKToken", defaultValue = "") String token)
	{
		Response r = new Response();
		Game currentGame = this.data.getTokens().stream()
				.filter(tt -> tt.getToken().equals(token))
				.findFirst()
				.orElse(null)
				.getGame();
		Player player = this.data.getTokens().stream()
				.filter(tt -> tt.getToken().equals(token))
				.findFirst()
				.orElse(null).getPlayer();
		if (currentGame.getStarted())
		{
			r.setError("Game " + currentGame.getName() + " has already started");
		}
		else {
			player.setReady(true);
			r.setMessage("Player " + player.getName() + " is ready");
		}
		currentGame.getWsController().updatePlayers(currentGame);
		if(currentGame.getStarted())
		{
			if(currentGame.getStopwatch()!=null)
			{
				currentGame.getStopwatch().start();
			}
			else
			{
				currentGame.getActivePlayer().setTimeRemaining(100.0);
			}
		}
		return r;
	}
	
	@PostMapping("/updateShelf")
	public Response updateShelfFigures(@RequestBody List<FigureApi> figures,@CookieValue(value = "RKToken", defaultValue = "") String token)
	{
		Player player = this.data.getTokens().stream()
				.filter(tt -> tt.getToken().equals(token))
				.findFirst()
				.orElse(null).getPlayer();
		
		List<Figure> replacedFigures = figures.stream().map(f ->f.toRummikubFigure(Placement.ON_SHELF)).collect(Collectors.toList());
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
	

	
	private Game getGame(String gameId)
	{
		return this.data.getGames().stream().filter(g -> g.getGameId().equals(gameId)).findFirst().orElse(null);
	}
	
}
