package api;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.ArrayList;
import java.util.Arrays;

public class AiPlayerNameGenerator {
    private static List<String> PLAYER_NAMES = new ArrayList<String>(Arrays.asList( "Anna","Anton","Antonia","Arthur","August","Augusta","Benno","Bruno","Charlotte","Clemens","Dorothea","Edda","Elisa","Elisabeth","Elsa","Emil","Emma","Eugen","Franka","Franz","Franziska","Frederick","Frieda","Friederike","Friedrich","Gabriel","Georg","Greta","Gustav","Hagen","Hedda","Helene","Henri","Henriette","Hugo","Ida","Johann","Johanna","Johannes","Josephine","Julius","Justus","Karl","Karla","Karolina","Kaspar","Katharina","K","Konrad","Konstantin","Korbinian","Leonhard","Leopold","Lorenz","Ludwig","Luise","Margarete","Maria","Martha","Margarete","Mathilda","Maximilian","Oskar","Otto","Paul","Paula","Richard","Ruth","Thea","Theodor","Theresa","Viktoria","Wilhelmine"));

    
    public static String generateName()
    {
    	Random r = new Random();
    	return r.ints(1,0,AiPlayerNameGenerator.PLAYER_NAMES.size())
    						.mapToObj(idx -> AiPlayerNameGenerator.PLAYER_NAMES.get(idx))
    						.collect(Collectors.toList()).get(0) + (r.nextInt(89)+10);
    }

}
