import java.awt.Color;
import java.awt.Container;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class GridDisplayer extends JFrame implements MouseListener {

	private static final long serialVersionUID = 1L;
	private GridRunner runner;
	private int cellSideLength;
	private long simSpeed = 1000;
	private boolean isStaggered = true;
	private final int DISTANCE_FROM_TOP_MARGIN = 150;
	private final int DISTANCE_FROM_LEFT_MARGIN = 150;
	private final int GRID_LENGTH = 700;
	private int gridTime = 0;

	private Container contents;
	private JLabel deathCountDisplay = new JLabel("");
	private JLabel organismsCountDisplay = new JLabel("");
	private JLabel turnCount = new JLabel("");

	private JButton btnPausePlay = new JButton("Play");
	private JButton btnStaggerMode = new JButton("Unstagger");
	private JButton btnSpeedUp = new JButton("Speed Up");
	private JButton btnSlowDown = new JButton("Slow Down");
	private JButton btnEvolutionMode = new JButton("Turn on Evolution");
	private JButton btnRoundWorldMode = new JButton("Turn on Round World");
	private JButton btnClearGrid = new JButton("Clear Grid");
	private JButton btnRestartGrid = new JButton("Restart Grid");
	private JButton btnQuit = new JButton("Quit");

	private GridButton[][] buttonGrid;

	private boolean isClicked = false;

	//double buffering variables
	private int bufferWidth;
	private int bufferHeight;
	private Image bufferImage;
	private Graphics bufferGraphics;

	public GridRunner getRunner() { return runner; }
	public void setSimSpeed(long simSpeedIn) { simSpeed = simSpeedIn; }

	public int getGridTime() { return gridTime; }
	public void setGridTime(int gridTimeIn) { gridTime = gridTimeIn; }

	public void setIsClicked(boolean isClickedIn) { isClicked = isClickedIn; }
	public boolean getIsClicked() { return isClicked; }

	public GridDisplayer(GridRunner runnerIn, int width, int height){
		super("Simulation 1");

		//instantiate private variables
		runner = runnerIn;
		cellSideLength = (int)(GRID_LENGTH/runner.getGrid().getHeight());

		//set up layout with FlowLayout object
		contents = getContentPane();
		contents.setLayout(null);

		//add labels
		organismsCountDisplay.setText("Organisms: " + runner.getGrid().getNumOrganisms());
		organismsCountDisplay.setBounds(50, 65, 100, 50);
		contents.add(organismsCountDisplay);
		deathCountDisplay.setText("Deaths: " + runner.getNumDead());
		deathCountDisplay.setBounds(50, 50, 200, 50);
		contents.add(deathCountDisplay);
		turnCount.setText("Turn: " + runner.getGridTime());
		turnCount.setBounds(450, 65, 100, 50);
		contents.add(turnCount);

		//add buttons
		btnPausePlay.setBounds(50, 10, 100, 50);
		contents.add(btnPausePlay);
		btnStaggerMode.setBounds(150, 10, 100, 50);
		contents.add(btnStaggerMode);
		btnSpeedUp.setBounds(250, 10, 100, 50);
		contents.add(btnSpeedUp);
		btnSlowDown.setBounds(350, 10, 100, 50);
		contents.add(btnSlowDown);
		btnEvolutionMode.setBounds(450, 10, 150, 50);
		contents.add(btnEvolutionMode);
		btnRoundWorldMode.setBounds(600, 60, 160, 50);
		contents.add(btnRoundWorldMode);
		btnClearGrid.setBounds(600, 10, 100, 50);
		contents.add(btnClearGrid);
		btnRestartGrid.setBounds(700, 10, 110, 50);
		contents.add(btnRestartGrid);
		btnQuit.setBounds(810, 10, 100, 50);
		contents.add(btnQuit);

		//instantiate button handlers
		GridButtonHandler gbh = new GridButtonHandler();
		MyMouseListener mml = new MyMouseListener();
		ButtonHandler bh = new ButtonHandler();
		//bh.setGridDisplayer(this);

		//add button handler to buttons
		btnPausePlay.addActionListener(bh);
		btnStaggerMode.addActionListener(bh);
		btnSpeedUp.addActionListener(bh);
		btnSlowDown.addActionListener(bh);
		btnEvolutionMode.addActionListener(bh);
		btnRoundWorldMode.addActionListener(bh);
		btnClearGrid.addActionListener(bh);
		btnRestartGrid.addActionListener(bh);
		btnQuit.addActionListener(bh);

		buttonGrid = new GridButton[runner.getGrid().getHeight()][runner.getGrid().getWidth()];

		for(int y = 0; y < runner.getGrid().getHeight(); y++) {
			for(int x = 0; x < runner.getGrid().getWidth(); x++) {
				GridButton btn = new GridButton();
				btn.setVisible(true);
				contents.add(btn);
				buttonGrid[y][x] =  btn;

				int xCord = cellSideLength * x + DISTANCE_FROM_LEFT_MARGIN;
				int yCord = cellSideLength * y + DISTANCE_FROM_TOP_MARGIN;

				btn.addActionListener(gbh);
				btn.addMouseListener(mml);
				//if (isStaggered) {
				//	btn.setBounds(xCord - 7, yCord - 30, cellSideLength - 1, cellSideLength - 1);
				//}
				//else {
				btn.setBounds(xCord - 7, yCord - 30, cellSideLength, cellSideLength);
				//}
				btn.setOpaque(true);
				btn.setDoubleBuffered(true);
				btn.setContentAreaFilled(false);
				btn.setBorderPainted(false);
				contents.add(btn);
			}
		}
		setSize(width, height);
		setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	private class ButtonHandler implements ActionListener {
		public void actionPerformed(ActionEvent ae) {
			if ( ae.getSource() == btnSpeedUp){
				speedUp();
			}
			else if ( ae.getSource() ==  btnSlowDown) {
				slowDown();
			}
			else if ( ae.getSource() == btnPausePlay) {
				if (runner.getIsPaused()) {
					runner.setIsPaused(false);
					btnPausePlay.setText("Pause");
				}
				else {
					runner.setIsPaused(true);
					btnPausePlay.setText("Play");
				}
			}
			else if ( ae.getSource() == btnStaggerMode) {
				if (isStaggered) {
					isStaggered = false;
					btnStaggerMode.setText("Stagger");
				}
				else {
					isStaggered = true;
					btnStaggerMode.setText("Unstagger");
				}
			}
			else if ( ae.getSource() == btnEvolutionMode) {
				if (runner.getEvolutionOn()) {
					runner.setEvolutionOn(false);
					btnEvolutionMode.setText("Turn on Evolution");
				}
				else {
					runner.setEvolutionOn(true);
					btnEvolutionMode.setText("Turn off Evolution");
				}
			}
			else if (ae.getSource() == btnRoundWorldMode) {
				if (runner.getGrid().getRoundWorldOn()) {
					runner.getGrid().setRoundWorldOn(false);
					btnRoundWorldMode.setText("Turn on Round World");
				}
				else {
					runner.getGrid().setRoundWorldOn(true);
					btnRoundWorldMode.setText("Turn off Round World");
				}
			}
			else if ( ae.getSource() == btnClearGrid) {
				runner.getGrid().resetGrid();
			}
			else if ( ae.getSource() == btnRestartGrid) {
				runner.restartGrid();
				runner.setIsPaused(true);
				btnPausePlay.setText("Play");
				updateStats();
			}
			else if ( ae.getSource() == btnQuit ) {
				runner.setIsDone(true);
			}
		}
	}

	private class GridButtonHandler implements ActionListener {
		public void actionPerformed(ActionEvent ae) {
			for(int y = 0; y < runner.getGrid().getHeight(); y++) {
				for(int x = 0; x < runner.getGrid().getWidth(); x++) {
					if(buttonGrid[y][x] == ae.getSource()){
						runner.getGrid().createOrganism(5, x, y);
						organismsCountDisplay.setText("Organisms: " + runner.getGrid().getNumOrganisms());
						repaint();
						return;
					}
				}
			}
		}
	}

	private class MyMouseListener implements MouseListener {
		@Override
		public void mouseClicked(MouseEvent evt) {
			// TODO Auto-generated method stub

		}
		@Override
		public void mouseEntered(MouseEvent evt) {
			if (getIsClicked()) {
				for(int y = 0; y < runner.getGrid().getHeight(); y++) {
					for(int x = 0; x < runner.getGrid().getWidth(); x++) {
						if(buttonGrid[y][x] == evt.getSource()){
							runner.getGrid().createOrganism(5, x, y);
							organismsCountDisplay.setText("Organisms: " + runner.getGrid().getNumOrganisms());
							repaint();
							return;
						}
					}
				}
			}
		}
		@Override
		public void mouseExited(MouseEvent evt) {
			// TODO Auto-generated method stub

		}
		@Override
		public void mousePressed(MouseEvent evt) {
			setIsClicked(true);
		}
		@Override
		public void mouseReleased(MouseEvent evt) {
			setIsClicked(false);
		}
	}
	public long getSimSpeed() {
		return simSpeed;
	}

	public void setTimeToSleep(long simSpeedIn) {
		simSpeed = simSpeedIn;
	}

	public void speedUp() {
		simSpeed -= 200;
		if (simSpeed < 50) {
			simSpeed = 50;
		}
	}

	public void slowDown() {
		simSpeed += 200;
	}
	private void resetBuffer(){
		// always keep track of the image size
		bufferWidth=getSize().width;
		bufferHeight=getSize().height;

		//    clean up the previous image
		if(bufferGraphics!=null){
			bufferGraphics.dispose();
			bufferGraphics=null;
		}
		if(bufferImage!=null){
			bufferImage.flush();
			bufferImage=null;
		}
		System.gc();

		//    create the new image with the size of the panel
		bufferImage=createImage(bufferWidth,bufferHeight);
		bufferGraphics=bufferImage.getGraphics();
	}
	public void paint(Graphics g){
		resetBuffer();

		if(bufferGraphics!=null){
			//this clears the offscreen image, not the onscreen one
			bufferGraphics.clearRect(0,0,bufferWidth,bufferHeight);

			//calls the paintbuffer method with
			//the offscreen graphics as a param
			paintBuffer(bufferGraphics);

			//we finaly paint the offscreen image onto the onscreen image
			g.drawImage(bufferImage,0,0,this);
		}
	}
	public void paintBuffer(Graphics g) {
		super.paint(g);

		if(bufferWidth!=getSize().width || bufferHeight!=getSize().height ||
				bufferImage==null || bufferGraphics==null) {
			resetBuffer();
		}

		g.drawRect(DISTANCE_FROM_LEFT_MARGIN, DISTANCE_FROM_TOP_MARGIN, GRID_LENGTH + 1, GRID_LENGTH + 1);

		for (Organism temp : runner.getGrid().getOrganisms()) {
			Point p = runner.getGrid().getCoordinates(temp.getID());
			int y = (int) p.getY();
			int x = (int) p.getX();
			int xCord = cellSideLength * x + DISTANCE_FROM_LEFT_MARGIN;
			int yCord = cellSideLength * y + DISTANCE_FROM_TOP_MARGIN;
			if (temp instanceof MovingOrganism) {

				g.setColor(Color.GRAY);
				if (isStaggered) {
					g.fillRect(xCord + 1, yCord + 1, cellSideLength - 1, cellSideLength - 1);
				}
				else {
					g.fillRect(xCord + 1, yCord + 1, cellSideLength, cellSideLength);
				}
			}
			else {

				g.setColor(Color.BLACK);
				if (isStaggered) {
					g.fillRect(xCord + 1, yCord + 1, cellSideLength - 1, cellSideLength - 1);
				}
				else {
					g.fillRect(xCord + 1, yCord + 1, cellSideLength, cellSideLength);
				}
			}
		}
	}
	public void updateStats() {
		deathCountDisplay.setText("Deaths: " + runner.getNumDead());
		organismsCountDisplay.setText("Organisms: " + runner.getGrid().getNumOrganisms());
		turnCount.setText("Turn: " + runner.getGridTime());
	}
	/**
	 * executes one turn of the simulation
	 */
	public void takeTurn() {
		runner.takeTurn();
		updateStats();
		try {
			Thread.sleep(getSimSpeed());
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	@Override
	public void mouseClicked(MouseEvent evt) {
		// TODO Auto-generated method stub

	}
	@Override
	public void mouseEntered(MouseEvent evt) {
		JButton btn = (JButton) evt.getComponent();
		if (isClicked) {

		}
	}
	@Override
	public void mouseExited(MouseEvent evt) {
		// TODO Auto-generated method stub

	}
	@Override
	public void mousePressed(MouseEvent evt) {
		isClicked = true;

	}
	@Override
	public void mouseReleased(MouseEvent evt) {
		isClicked = false;		
	}
}
