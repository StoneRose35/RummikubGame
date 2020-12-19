package api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import game.Stack;

@RestController
public class RummikubController {
	
	
	@GetMapping("")
	public String aboutRummikubGame()
	{
		return "This is the Rummikub Game Web API";
	}
	
	@GetMapping("/stack")
	public Stack getStack()
	{
		return new Stack();
	}
	

}
