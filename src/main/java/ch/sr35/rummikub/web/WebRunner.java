package ch.sr35.rummikub.web;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import ch.sr35.rummikub.asp.AspSolver;

@SpringBootApplication
public class WebRunner {
	

	
	public static void main(String[] args) {
		System.out.println("Starting Rummikub Web Application");
		System.out.println("check if clingo is available");
		AspSolver as = new AspSolver();
		String version = as.getVersion();
		if (version != null)
		{
			System.out.println("found Clingo Solver, Version:");
			System.out.print(version);
			System.out.println("Installing ASP encoding Files");
			AspSolver aspSolver = new AspSolver();
			int res = aspSolver.deploy();
			if (res==0)
			{
				SpringApplication.run(WebRunner.class, args);
			}
			else
			{
				System.out.println("Installation of ASP Folder failed. Make sure " + System.getProperty("user.dir") + " is writeable");
			}
		}
		else
		{
			System.out.println("no clingo found, go to https://github.com/potassco/clingo/releases/ to get the current version");
		}
		
	}
	

}
