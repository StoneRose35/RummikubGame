package gui;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import game.Stack;
import game.RummikubFigure;
import game.IRummikubFigureBag;
import game.RummikubPlayer;
import game.RummikubResult;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

import javax.swing.border.BevelBorder;
import java.awt.BorderLayout;
import javax.swing.JButton;
import java.awt.Font;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static javax.swing.JOptionPane.showMessageDialog;

public class RummikubProgram extends JFrame{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1418187771696355888L;
	private JPanel contentPane;
	private Stack stack;
    private FigureRepresentation f;
    private int round_nr;
    private List<RummikubPlayer> players;
    private int currentPlayer;
    private final String[] PLAYER_NAMES = {"Anna","Anton","Antonia","Arthur","August","Augusta","Benno","Bruno","Charlotte","Clemens","Dorothea","Edda","Elisa","Elisabeth","Elsa","Emil","Emma","Eugen","Franka","Franz","Franziska","Frederick","Frieda","Friederike","Friedrich","Gabriel","Georg","Greta","Gustav","Hagen","Hedda","Helene","Henri","Henriette","Hugo","Ida","Johann","Johanna","Johannes","Josephine","Julius","Justus","Karl","Karla","Karolina","Kaspar","Katharina","K","Konrad","Konstantin","Korbinian","Leonhard","Leopold","Lorenz","Ludwig","Luise","Margarete","Maria","Martha","Margarete","Mathilda","Maximilian","Oskar","Otto","Paul","Paula","Richard","Ruth","Thea","Theodor","Theresa","Viktoria","Wilhelmine"};
	private final int GAME_ABORTED = 0;
	private final int GAME_ONGOING = 1;
    
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					RummikubProgram frame = new RummikubProgram();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public RummikubProgram() {
		this.stack = new Stack();
		
		setTitle("Rummikub Program");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));
		
		this.f = new FigureRepresentation();
		contentPane.add(this.f, BorderLayout.CENTER);
		
		JPanel buttonsPanel = new JPanel();
		contentPane.add(buttonsPanel, BorderLayout.PAGE_END);
		
		JButton btnInitGame = new JButton("Init Game");
		btnInitGame.setFont(new Font("Dialog", Font.BOLD, 12));
		buttonsPanel.add(btnInitGame);
		
		JButton btnPlayRound = new JButton("Play round");
		buttonsPanel.add(btnPlayRound);
		
		JButton btnDrawFigure = new JButton("draw Figure");
		buttonsPanel.add(btnDrawFigure);
		
		JTextArea gameInfoText = new JTextArea();
		gameInfoText.setRows(10);
		gameInfoText.setEditable(false);
		gameInfoText.setColumns(10);
		contentPane.add(gameInfoText, BorderLayout.EAST);
		

		btnInitGame.addActionListener(new BtnInitGameHandler(this));
		
		btnDrawFigure.addActionListener(new BtnDrawFigureHandler(this));
		
		btnPlayRound.addActionListener(new BtnPlayRoundHandler(this));
		
		this.round_nr=0;
		this.players = new ArrayList<RummikubPlayer>();
		

	}
	
	private void testingScript()
	{
		File logfile=new File("log.txt");
		FileWriter fw;
		Rounder rounder;

		try {
			fw = new FileWriter(logfile);
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write("'Player number won','round nr','stack size at end of game'\n");
			int cnt = 0;
			rounder = new Rounder(this);
			while(cnt < 50)
			{
				boolean gameEnds = false;
				this.initNewGame(2);
				while(!gameEnds)
				{
					int res = this.playRound();
					this.repaint();

					if (res > 9)
					{
						bw.write(String.format("%d,%d,%d\n",res-10,this.round_nr,this.stack.getSize()));
						bw.flush();
						gameEnds = true;
					}
					else if (res == this.GAME_ABORTED)
					{
						bw.write(String.format("%d,%d,%d\n",0,this.round_nr,this.stack.getSize()));
						bw.flush();
						gameEnds=true;
					}
					System.out.println(String.format("played round %d of game %d",this.round_nr,cnt));
				}
				cnt++;
			}
			bw.close();
		} catch (Exception e)
		{
		
		} 
	}
	
	public Stack getStack() {
		return stack;
	}
	
	private void updateGameInfo()
	{
		String fmt = "Round Nr: %d\nStack Size: %d\nLast Action: %s\n";
		JTextArea ta =(JTextArea)this.contentPane.getComponent(2);
		fmt = String.format(fmt, this.round_nr, this.stack.getSize(), "");
		for (RummikubPlayer p : this.players)
		{
			fmt += String.format("Player #%s\nScore %d\n\n",p.getName(),p.getTotalScore());
		}
		ta.setText(fmt);
	}


	public RummikubPlayer getCurrentPlayer() {
		return this.players.get(this.currentPlayer);
	}

	public void setCurrentPlayer(int currentPlayer) {
		this.currentPlayer = currentPlayer;
	}
	
	public void setNextPlayer()
	{
		this.currentPlayer++;
		if(this.currentPlayer == this.players.size())
		{
			this.currentPlayer = 0;
		}
	}
	
	private void initNewGame(int nPlayers)
	{
		this.stack.initializeGame();
		this.players.clear();
		Random r = new Random();
		for (int c=0;c<nPlayers;c++)
		{
			RummikubPlayer p = new RummikubPlayer();
			p.setName(this.PLAYER_NAMES[r.nextInt(this.PLAYER_NAMES.length)]);
			for(int cnt=0;cnt<14;cnt++)
			{
				p.getOnShelf().add(this.stack.drawFromStack());
			}
			this.players.add(p);
		}
		this.f.setTableFigures(new ArrayList<IRummikubFigureBag>());
		this.f.setPlayers(this.players);

		this.round_nr=0;
		this.updateGameInfo();
		this.repaint();
	}
	
	private int playRound()
	{
		int result=this.GAME_ONGOING;
		if (!this.getCurrentPlayer().getOnShelf().isEmpty())
		{
			RummikubResult res  = this.getCurrentPlayer().solveRound(this.f.getTableFigures());
			if (res.getScoreLaid()==0)
			{
				RummikubFigure rf = this.stack.drawFromStack();
				if (rf != null)
				{
					this.getCurrentPlayer().getOnShelf().add(rf);
				}
				else
				{
					result = this.GAME_ABORTED;
				}
			}
			else
			{
				if (this.getCurrentPlayer().getOnShelf().isEmpty())
				{
					result = 10 + this.currentPlayer;  
				}
				this.f.setTableFigures(res.getOnTable());
			}
		}

		this.updateGameInfo();
		this.setNextPlayer();
		this.round_nr++;
		return result;
	}


	class BtnInitGameHandler implements ActionListener
	{

		private RummikubProgram parent;
		public BtnInitGameHandler(RummikubProgram parent)
		{
			this.parent=parent;
		}
		@Override
		public void actionPerformed(ActionEvent arg0) {
			Object[] playerSelection = {1,2,3,4};
			int nPlayersChosen = (int)JOptionPane.showInputDialog(this.parent, 
					"Please select number of Players (1-4)",
					"Number of Players",
					JOptionPane.PLAIN_MESSAGE,
					null,
					playerSelection,
					2);

			this.parent.initNewGame(nPlayersChosen);
		}
		
	}
	
	class BtnDrawFigureHandler implements ActionListener
	{

		private RummikubProgram parent;
		public BtnDrawFigureHandler(RummikubProgram parent)
		{
			this.parent=parent;
		}
		
		@Override
		public void actionPerformed(ActionEvent arg0) {
			Rounder r = new Rounder(this.parent);
			r.start();
		}
	}
	
	class BtnPlayRoundHandler implements ActionListener
	{

		private RummikubProgram parent;
		public BtnPlayRoundHandler(RummikubProgram parent)
		{
			this.parent=parent;
		}
		
		@Override
		public void actionPerformed(ActionEvent arg0) {
			int res = this.parent.playRound();
			if (res == this.parent.GAME_ABORTED)
			{
				showMessageDialog(this.parent,"Non more figures on Stack");
			}
			if (res >= 10)
			{
				showMessageDialog(this.parent,String.format("Player %s Won!",this.parent.players.get(res-10).getName()));
			}
		}
	}
	
	class Rounder extends Thread
	{
		private int result = 0;
		private RummikubProgram parent;
		
		public Rounder(RummikubProgram parent)
		{
			this.parent = parent;
		}
		@Override
		public void run()
		{
			this.parent.testingScript();
		}
		public int getResult() {
			return result;
		}

	}
	
}
