package api;

import java.util.ArrayList;
import java.util.Random;
import java.util.stream.Collectors;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import java.util.List;

import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import game.RummikubFigure;
import game.RummikubPlacement;

@RestController
public class RummikubController {
	
	List<RummikubGame> games;
	List<RummikubToken> tokens;
	
	public RummikubController()
	{
		this.games=new ArrayList<RummikubGame>();
		this.tokens=new ArrayList<RummikubToken>();
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
	public Response registerPlayer(@RequestParam String name,@RequestParam String gameId,HttpServletResponse response)
	{
		Response r=new Response();
		
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
	public List<RummikubPlayerApi> getPlayers(@RequestParam String gameId)
	{
		RummikubGame g = this.getGame(gameId);
		if (g!=null)
		{
			return g.getPlayers().stream().map(p -> {
				return new RummikubPlayerApi(p);
			}).collect(Collectors.toList());
		}
		return null;
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
	public List<List<RummikubFigureApi>> getTableFigures(@RequestParam String gameId)
	{
		List<List<RummikubFigureApi>> res=new ArrayList<List<RummikubFigureApi>>();
		RummikubGame g = this.getGame(gameId);
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
	public GameStateApi submitMove(GameStateApi gsSubmitted,@CookieValue(value = "RKToken", defaultValue = "") String token)
	{
		GameStateApi gameStateReturned;
		RummikubGame currentGame = this.tokens.stream()
				.filter(tt -> tt.getToken().equals(token))
				.findFirst()
				.orElse(null)
				.getGame();
		gsSubmitted.validate();
		if (gsSubmitted.isAccepted()==false)
		{
			gameStateReturned=new GameStateApi();

			gameStateReturned.setTableFigures(currentGame.getTableFigures().stream().map(f -> 
						f.stream().map(el -> RummikubFigureApi.fromRummikubFigure(el)).collect(Collectors.toList())
					).collect(Collectors.toList()));
			gameStateReturned.setShelfFigures(currentGame.getPlayer(gsSubmitted.getPlayer().getName()).getFigures()
					.stream().map(el -> RummikubFigureApi.fromRummikubFigure(el)).collect(Collectors.toList()));
		}
		else
		{
			gameStateReturned=gsSubmitted;
			currentGame.setTableFigures(gsSubmitted.getTableFiguresStructured());
			currentGame.getPlayer(gsSubmitted.getPlayer().getName()).setFigures(gsSubmitted.getShelfFigures().stream().map(el -> el.toRummikubFigure(RummikubPlacement.ON_SHELF)).collect(Collectors.toList()));
		}
		currentGame.rotatePlayer();
		return gameStateReturned;
	}
	
	
	
	private RummikubGame getGame(String gameId)
	{
		return this.games.stream().filter(g -> g.getName().equals(gameId)).findFirst().orElse(null);
	}
	
}
