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

@CrossOrigin(origins = {"http://localhost:4200","http://192.168.0.207:4200"},allowCredentials = "true")
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
			RummikubFigure f =  g.drawFigure();
			p.getFigures().add(f);
			g.rotatePlayer();
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
		if (this.games.stream().filter(e -> e.getName().equals(name)).count()==0)
		{
			RummikubGame g = new RummikubGame();
			g.setName(name);
			this.games.add(g);
			
			for(int c=0;c<nrAiPlayers;c++)
			{
				RummikubPlayerAsp aiPlayer = new RummikubPlayerAsp();
				aiPlayer.setActive(false);
				aiPlayer.setName(AiPlayerNameGenerator.generateName());
				for (int cc=0;cc<14;cc++)
				{
					aiPlayer.getFigures().add(g.drawFigure());
				}
				g.getPlayers().add(aiPlayer);
			}
			
			r.setMessage("Successfully created game " + name);
			return r;
		}
		else
		{
			r.setError("Game " + name + " already exists");
			return r;
		}
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
				r.setPlayer((RummikubPlayerApi)t.getPlayer());
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
		RummikubPlayer player = this.tokens.stream()
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
			currentGame.getPlayer(playerName).getFigures().add(f);
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
		return gameStateReturned;
	}
	
	
	
	private RummikubGame getGame(String gameId)
	{
		return this.games.stream().filter(g -> g.getName().equals(gameId)).findFirst().orElse(null);
	}
	
}
