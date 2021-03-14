package ch.sr35.rummikub.web;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import ch.sr35.rummikub.asp.AspSolver;

@SpringBootApplication
public class WebRunner {
	
	
	static Logger logger = LoggerFactory.getLogger(WebRunner.class);

	
	public static void main(String[] args) {
		logger.info("Starting Rummikub Web Application");
		logger.info("check if clingo is available");
		AspSolver as = new AspSolver();
		String version = as.getVersion();
		if (version != null)
		{
			logger.info("found Clingo Solver, Version:");
			logger.info(version);
			logger.info("Installing ASP encoding Files");
			AspSolver aspSolver = new AspSolver();
			int res = aspSolver.deploy();
			if (res==0)
			{
				SpringApplication.run(WebRunner.class, args);
			}
			else
			{
				logger.error("Installation of ASP Folder failed. Make sure " + System.getProperty("user.dir") + " is writeable");
			}
		}
		else
		{
			logger.error("no clingo found, go to https://github.com/potassco/clingo/releases/ to get the current version");
		}
		
	}
	

}
