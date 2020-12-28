package api;

import java.util.ArrayList;
import java.util.Random;
import java.util.stream.Collectors;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import java.util.List;

import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import game.RummikubFigure;
import game.RummikubPlacement;

@CrossOrigin(origins = "http://localhost:4200",allowCredentials = "true")
@RestController
public class RummikubController {
	
	List<RummikubGame> games;
	List<RummikubToken> tokens;
	AspPlayerDispatcher dispatcher;
	
	public RummikubController()
	{
		this.games=new ArrayList<RummikubGame>();
		this.tokens=new ArrayList<RummikubToken>();
		this.dispatcher = new AspPlayerDispatcher();
		this.dispatcher.setGames(this.games);
		this.dispatcher.start();
	}
	
	
	@GetMapping("")
	public String aboutRummikubGame()
	{
		return "This is the Rummikub Game Web API";
	}
	
	@GetMapping("/draw")
	public RummikubFigureApi getFigure(@CookieValue(value = "RKToken", defaultValue = "") String token)
	{
		RummikubToken t = this.tokens.stream().filter(tt -> tt.getToken().equals(token)).findFirst().orElse(null);
		if (t!=null)
		{
			RummikubGame g=t.getGame();
			RummikubPlayer p = t.getPlayer();
			g.increaseRound();
			RummikubFigure f =  g.drawFigure();
			p.getFigures().add(f);
			g.rotatePlayer();
			return RummikubFigureApi.fromRummikubFigure(f);
		}
		return null;
	}
	

	@GetMapping("/newgame")
	public Response generateGame(@RequestParam String name)
	{
		Response r = new Response();
		if (this.games.stream().filter(e -> e.getName().equals(name)).count()==0)
		{
			RummikubGame g = new RummikubGame();
			g.setName(name);
			this.games.add(g);
			r.setMessage("Successfully created game " + name);
		}
		else
		{
			r.setError("Game " + name + " already exists");
		}
		return r;
	}

	@GetMapping("/games")
	public List<RummikubGameApi> getGames()
	{
		return this.games.stream().map(g -> RummikubGameApi.fromRummikubGame(g)).collect(Collectors.toList());
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
				this.tokens.add(t);
				Cookie c = new Cookie("RKToken",t.getToken());
				response.addCookie(c);
				r.setMessage("Sucessfully registered " + name);
				r.setPlayer(new RummikubPlayerApi(t.getPlayer()));
			} catch (RummikubApiException e) {
				r.setError(e.getMessage());
			}
		}
		else
		{
			r.setError("Game " + gameId + " not existent");
		}
		
		return r;
	}
	
	@GetMapping("/registerGame")
	public Response registerGame(@RequestParam String gameId)
	{
		Response r=new Response();
		if (this.getGame(gameId)!=null)
		{
			r.setError("Game " + gameId + " not existent");
		}
		else
		{
			r.setMessage("Successfully joined game " + gameId);
		}
		return r;
	}
	
	@GetMapping("/players")
	public List<RummikubPlayerApi> getPlayers(@CookieValue(value = "RKToken", defaultValue = "") String token)
	{
			return this.tokens.stream()
					.filter(tt -> tt.getToken().equals(token))
					.findFirst()
					.orElse(null).getGame().getPlayers().stream().map(p -> {
				return new RummikubPlayerApi(p);
			}).collect(Collectors.toList());

	}
	
	@GetMapping("/shelfFigures")
	public List<RummikubFigureApi> getShelfFigure(@CookieValue(value = "RKToken", defaultValue = "") String token)
	{
		return this.tokens.stream()
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
		RummikubGame g = 		this.tokens.stream()
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
		RummikubGame currentGame = this.tokens.stream()
				.filter(tt -> tt.getToken().equals(token))
				.findFirst()
				.orElse(null)
				.getGame();
		String playerName = this.tokens.stream()
				.filter(tt -> tt.getToken().equals(token))
				.findFirst()
				.orElse(null).getPlayer().getName();
		gsSubmitted.validate();
		boolean laidDownEnough = true;
		if (currentGame.getRound()==0)
		{
			// check if at least 30 points have been laid down
			laidDownEnough = currentGame.getPlayer(playerName)
				.getFigures() 
				.stream()
				.map(el -> RummikubFigureApi.fromRummikubFigure(el))
				.filter( f -> !gsSubmitted.getShelfFigures().contains(f))
				.mapToInt(f -> { 
					if (f.getInstance()<3)
					{return f.getNumber();}
					return 0;
				}).sum() >= 30;
			
		}
		if (gsSubmitted.isAccepted()==false || laidDownEnough==false)
		{
			gameStateReturned=new GameStateApi();

			gameStateReturned.setTableFigures(currentGame.getTableFigures().stream().map(f -> 
						f.stream().map(el -> RummikubFigureApi.fromRummikubFigure(el)).collect(Collectors.toList())
					).collect(Collectors.toList()));
			gameStateReturned.setShelfFigures(currentGame.getPlayer(playerName).getFigures()
					.stream().map(el -> RummikubFigureApi.fromRummikubFigure(el)).collect(Collectors.toList()));
		}
		else
		{
			gameStateReturned=gsSubmitted;
			currentGame.setTableFigures(gsSubmitted.getTableFiguresStructured());
			currentGame.getPlayer(playerName).setFigures(gsSubmitted.getShelfFigures().stream().map(el -> el.toRummikubFigure(RummikubPlacement.ON_SHELF)).collect(Collectors.toList()));
		}
		currentGame.rotatePlayer();
		return gameStateReturned;
	}
	
	
	
	private RummikubGame getGame(String gameId)
	{
		return this.games.stream().filter(g -> g.getName().equals(gameId)).findFirst().orElse(null);
	}
	
}
