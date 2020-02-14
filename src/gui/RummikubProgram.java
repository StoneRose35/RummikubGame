package gui;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import asp.AspSolver;
import game.Stack;
import game.RummikubFigure;
import game.IRummikubFigureBag;
import game.RummikubPlayer;
import game.RummikubResult;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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

			this.parent.stack.initializeGame();
			this.parent.players.clear();
			Random r = new Random();
			for (int c=0;c<nPlayersChosen;c++)
			{
				RummikubPlayer p = new RummikubPlayer();
				p.setName(this.parent.PLAYER_NAMES[r.nextInt(this.parent.PLAYER_NAMES.length)]);
				for(int cnt=0;cnt<14;cnt++)
				{
					p.getOnShelf().add(this.parent.stack.drawFromStack());
				}
				this.parent.players.add(p);
			}
			this.parent.f.setTableFigures(new ArrayList<IRummikubFigureBag>());
			this.parent.f.setPlayers(this.parent.players);

			this.parent.round_nr=0;
			this.parent.updateGameInfo();
			this.parent.repaint();
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
			RummikubFigure rf = this.parent.stack.drawFromStack();
			if (rf != null)
			{
				this.parent.players.get(this.parent.currentPlayer).getOnShelf().add(rf);
				this.parent.repaint();
			}
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
			if (!this.parent.getCurrentPlayer().getOnShelf().isEmpty())
			{
				RummikubResult res  = this.parent.getCurrentPlayer().solveRound(this.parent.f.getTableFigures());
				if (res.getScoreLaid()==0)
				{
					RummikubFigure rf = this.parent.stack.drawFromStack();
					if (rf != null)
					{
						this.parent.getCurrentPlayer().getOnShelf().add(rf);
					}
					else
					{
						showMessageDialog(this.parent,"No more figures on Stack");
					}
				}
				else
				{
					if (this.parent.getCurrentPlayer().getOnShelf().isEmpty())
					{
						showMessageDialog(this.parent,String.format("Player %s Won!",this.parent.getCurrentPlayer().getName()));
					}
					this.parent.f.setTableFigures(res.getOnTable());
				}
			}

			this.parent.updateGameInfo();
			this.parent.setNextPlayer();
			this.parent.round_nr++;
			this.parent.repaint();
		}
	}
	
}
