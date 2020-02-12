package gui;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import asp.AspSolver;
import game.Game;
import game.GameState;
import game.RummikubFigure;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.border.BevelBorder;
import java.awt.BorderLayout;
import javax.swing.JButton;
import javax.swing.SwingConstants;
import java.awt.Font;
import static javax.swing.JOptionPane.showMessageDialog;

public class RummikubProgram extends JFrame{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1418187771696355888L;
	private JPanel contentPane;
	private Game stack;
	private AspSolver solver;
    private FigureRepresentation f;
	
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
		this.stack = new Game();
		this.solver = new AspSolver();
		
		setTitle("Rummikub Program");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));
		
		this.f = new FigureRepresentation();
		this.f.setState(new GameState());
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
		

		btnInitGame.addActionListener(new BtnInitGameHandler(this));
		
		btnDrawFigure.addActionListener(new BtnDrawFigureHandler(this));
		
		btnPlayRound.addActionListener(new BtnPlayRoundHandler(this));

	}
	
	
	public Game getGame() {
		return stack;
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
			this.parent.stack.initializeGame();
			this.parent.f.getState().initialize();
			for(int cnt=0;cnt<14;cnt++)
			{
				this.parent.f.getState().addFigure(this.parent.stack.drawFromStack());
			}
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
				this.parent.f.getState().addFigure(rf);
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
			GameState s_new = this.parent.solver.solve_round(this.parent.f.getState());
			if (s_new.getSumLaid()==0)
			{
				RummikubFigure rf = this.parent.stack.drawFromStack();
				if (rf != null)
				{
					s_new.addFigure(rf);
					this.parent.repaint();
				}
				else
				{
					showMessageDialog(this.parent,"No more figures on Stack");
				}
			}
			else
			{
				if (s_new.getShelfFigures().isEmpty())
				{
					showMessageDialog(this.parent,"Game Won!");
				}
				else
				{
					this.parent.f.setTableFigures(this.parent.solver.getTableDescription());
				}
			}
			this.parent.f.setState(s_new);
			this.parent.repaint();
		}
	}
	
}
