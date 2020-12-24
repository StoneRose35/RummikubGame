package api;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import game.GameState;
import game.IRummikubFigureBag;
import game.RummikubFigure;
import game.Stack;

@RestController
public class RummikubController {
	
	List<RummikubGame> games;
	
	public RummikubController()
	{
		this.games=new ArrayList<RummikubGame>();
	}
	
	
	@GetMapping("")
	public String aboutRummikubGame()
	{
		return "This is the Rummikub Game Web API";
	}
	
	@GetMapping("/draw")
	public RummikubFigure getFigure(@RequestParam String name,@RequestParam String gameId)
	{
		RummikubGame g=this.getGame(gameId);
		if (g!=null)
		{
			g.increaseRound();
			return g.drawFigure();
		}
		return null;
	}
	
	@GetMapping("/newgame")
	public String generateGame()
	{
		boolean nameUnique = false;
		String fname="";
		Random r = new Random();
		byte[] b=new byte[3];
		
		while (nameUnique==false)
		{
			r.nextBytes(b);
			char[] hexDigits = new char[6];
			for (int c=0;c<3;c++)
			{
				
		    	hexDigits[2*c] = Character.forDigit((b[c] >> 4) & 0xF, 16);
		    	hexDigits[2*c+1] = Character.forDigit((b[c] & 0xF), 16);
			}
			final String name=new String(hexDigits);
			nameUnique = this.games.stream().filter(e -> e.getName().equals(name)).count()==0;
			fname=name;
		}
		RummikubGame g = new RummikubGame();
		g.setName(fname);
		this.games.add(g);
		return fname;
	}
	
	@GetMapping("/registerPlayer")
	public Response registerPlayer(@RequestParam String name,@RequestParam String gameId)
	{
		Response r=new Response();
		
		RummikubGame game = this.getGame(gameId);
		if (game!=null)
		{
			try {
				game.addPlayer(name);
				r.setMessage("Sucessfully registered " + name);
				// TODO generate a token and return it as a cookie
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
	public List<RummikubFigure> getShelfFigure(@RequestParam String gameId,@RequestParam String playerName)
	{
		return this.games.stream()
			.filter(g -> g.getName().equals(gameId))
			.findFirst()
			.orElse(null)
			.getPlayer(playerName).getFigures();
	}
	
	@GetMapping("/tableFigures")
	public List<List<RummikubFigure>> getTableFigures(@RequestParam String gameId)
	{
		List<List<RummikubFigure>> res=new ArrayList<List<RummikubFigure>>();
		RummikubGame g = this.getGame(gameId);
		if (g!=null)
		{
			g.getTableFigures().stream().forEach((f) -> {
				List<RummikubFigure> l=new ArrayList<RummikubFigure>();
				f.iterator().forEachRemaining(el -> l.add(el));
				res.add(l);
			} );
			
		}
		return res;
	}
	
	@PostMapping("/submitMove")
	public GameStateApi submitMove(GameStateApi gsSubmitted)
	{
		GameStateApi gameStateReturned;
		RummikubGame currentGame = this.games.stream()
				.filter(g -> g.getName().equals(gsSubmitted.getGameId()))
				.findFirst()
				.orElse(null);
		gsSubmitted.validate();
		if (gsSubmitted.isAccepted()==false)
		{
			gameStateReturned=new GameStateApi();

			gameStateReturned.setTableFigures(currentGame.getTableFigures().stream().map(f -> 
						f.stream().collect(Collectors.toList())
					).collect(Collectors.toList()));
			gameStateReturned.setShelfFigures(currentGame.getPlayer(gsSubmitted.getPlayer().getName()).getFigures());
		}
		else
		{
			gameStateReturned=gsSubmitted;
			currentGame.setTableFigures(gsSubmitted.getTableFiguresStructured());
			currentGame.getPlayer(gsSubmitted.getPlayer().getName()).setFigures(gsSubmitted.getShelfFigures());
		}
		currentGame.rotatePlayer();
		return gameStateReturned;
	}
	
	
	
	private RummikubGame getGame(String gameId)
	{
		return this.games.stream().filter(g -> g.getName().equals(gameId)).findFirst().orElse(null);
	}
	
}
