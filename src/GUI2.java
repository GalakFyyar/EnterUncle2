import javax.swing.BorderFactory;
import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.UIManager;
import javax.swing.WindowConstants;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.datatransfer.DataFlavor;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

class GUI2 extends JFrame{
	private static final String VERSION = "2.0";
	private static final long serialVersionUID = 1L;
	private static final int FRAME_WIDTH = 720;
	private static final int FRAME_HEIGHT = 480;
	private static File ascFile = null;
	
	private static PanelItem selectedItem;
	
	GUI2(){
		try{
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		}catch(Exception ignored){}
		
		//Menu Bar
		loadMenuBar(this);
		
		JPanel mainPanel = new JPanel(new BorderLayout());
		
		//Load Components on mainPanel
		loadFileArea(mainPanel);
		
		getContentPane().add(mainPanel);
		setTitle("Enter Uncle v" + VERSION);
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		setMinimumSize(new Dimension(FRAME_WIDTH, FRAME_HEIGHT));
		setExtendedState(JFrame.MAXIMIZED_BOTH);
		setVisible(true);
		
		System.out.println("LOADED GUI");
	}
	
	private void loadMenuBar(JFrame context){
		JMenuBar menuBar = new JMenuBar();
		
		JMenu fileMenu = new JMenu("File");
		JMenuItem openFileItem = new JMenuItem("Open...");
		fileMenu.add(openFileItem);
		JMenuItem saveFileItem = new JMenuItem("Save");
		fileMenu.add(saveFileItem);
		menuBar.add(fileMenu);
		
		JMenu optionsMenu = new JMenu("Options");
		JMenuItem killItem = new JMenuItem("Kill All Humans!");
		optionsMenu.add(killItem);
		menuBar.add(optionsMenu);
		
		context.setJMenuBar(menuBar);
	}
	
	private void loadFileArea(JPanel mainPanel){
		JDesktopPane fileArea = new JDesktopPane();
		fileArea.setToolTipText("Drag files onto here");
		fileArea.setBackground(Color.DARK_GRAY);
		
		DropTarget qaxDropTarget = new DropTarget(){
			public synchronized void drop(DropTargetDropEvent evt){
				try{
					evt.acceptDrop(DnDConstants.ACTION_COPY);
					//noinspection unchecked
					List<File> droppedFiles = (List<File>)evt.getTransferable().getTransferData(DataFlavor.javaFileListFlavor);
					ascFile = droppedFiles.get(0);
				}catch(Exception ex){
					//statusTF.setText("Drag and Drop Error");
					ex.printStackTrace();
				}
				
				if(ascFile != null){
					Controller.parseASCFileAndPopulateQuestionnaireModel(ascFile.getAbsolutePath());
					Controller.populateEFileModel();
					loadMainPanel(mainPanel);
				}
			}
		};
		fileArea.setDropTarget(qaxDropTarget);
		
		mainPanel.add(fileArea);
	}
	
	private void loadMainPanel(JPanel mainPanel){
		mainPanel.removeAll();
		ArrayList<Question> questions = Controller.getAllQuestions();
		ArrayList<Table> tables = Controller.getTables();
		mainPanel.setLayout(new BorderLayout());
		
		JPanel contentPanel = new JPanel(new BorderLayout());
		
		//TODO -- add ImageIcon icon createImageIcon();
		JPanel questionPanel = new JPanel();					//Goes on the left!
		questionPanel.setLayout(new GridLayout(questions.size(), 1));
		questions.forEach(q -> questionPanel.add(new QuestionPanelItem(contentPanel, q)));
		JScrollPane leftScrollPane = new JScrollPane(questionPanel);
		leftScrollPane.getVerticalScrollBar().setUnitIncrement(16);
		leftScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		
		
		JPanel tablePanel = new JPanel();					//Goes on the right
		tablePanel.setLayout(new GridLayout(questions.size(), 1));
		tables.forEach(t -> tablePanel.add(new TablePanelItem(contentPanel, t)));
		JScrollPane rightScrollPane = new JScrollPane(tablePanel);
		rightScrollPane.getVerticalScrollBar().setUnitIncrement(16);
		rightScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		
		mainPanel.add(leftScrollPane, BorderLayout.WEST);
		mainPanel.add(rightScrollPane, BorderLayout.EAST);
		mainPanel.add(contentPanel, BorderLayout.CENTER);
		
		mainPanel.revalidate();
		mainPanel.repaint();
	}
	
	private void loadContentPanelForQuestion(JPanel contentPanel, Question q){
		contentPanel.removeAll();
		contentPanel.add(new JLabel("Q: " + q.label), BorderLayout.CENTER);
		contentPanel.revalidate();
	}
	
	private void loadContentPanelForTable(JPanel contentPanel, Table t){
		contentPanel.removeAll();
		contentPanel.add(new JLabel(t.title), BorderLayout.CENTER);
		contentPanel.revalidate();
	}
	
	static File getASCFile(){
		return ascFile;
	}
	
	private class QuestionPanelItem extends PanelItem{
		private Question question;
		
		QuestionPanelItem(JPanel aContentPanel, Question q){
			super(aContentPanel, q.variable);
			question = q;
		}
		
		@Override
		public void mouseClicked(MouseEvent e){
			super.mouseClicked(e);
			loadContentPanelForQuestion(super.contentPanel, question);
		}
	}
	
	private class TablePanelItem extends PanelItem{
		private Table table;
		
		TablePanelItem(JPanel aContentPanel, Table t){
			super(aContentPanel, "TABLE " + t.number);
			table = t;
		}
		
		@Override
		public void mouseClicked(MouseEvent e){
			super.mouseClicked(e);
			loadContentPanelForTable(super.contentPanel, table);
		}
	}
	
	private class PanelItem extends JPanel implements MouseListener{
		private final Border marginBorder = new EmptyBorder(0, 15, 0, 0);
		private final CompoundBorder raisedButtonBorder;
		private final CompoundBorder loweredButtonBorder;
		private JPanel contentPanel;
		
		PanelItem(JPanel aContentPanel, String item){
			super(new BorderLayout());
			raisedButtonBorder = new CompoundBorder(BorderFactory.createRaisedBevelBorder(), marginBorder);
			loweredButtonBorder = new CompoundBorder(BorderFactory.createLoweredBevelBorder(), marginBorder);
			
			setBorder(raisedButtonBorder);
			setPreferredSize(new Dimension(150, 30));
			setBackground(Color.LIGHT_GRAY);
			add(new JLabel(item));
			
			addMouseListener(this);
			contentPanel = aContentPanel;
		}
		
		@Override
		public void mouseClicked(MouseEvent e){
			if(selectedItem != null)
				selectedItem.unSelect();
			selectedItem = this;
			setBorder(loweredButtonBorder);
		}
		
		private void unSelect(){
			setBorder(raisedButtonBorder);
		}
		
		@Override
		public void mousePressed(MouseEvent e){
		
		}
		
		@Override
		public void mouseReleased(MouseEvent e){
		
		}
		
		@Override
		public void mouseEntered(MouseEvent e){
		
		}
		
		@Override
		public void mouseExited(MouseEvent e){
		
		}
	}
}
