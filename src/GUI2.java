import javax.swing.*;
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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;

class GUI2 extends JFrame{
	private static final String VERSION = "2.0.1";
	private static final long serialVersionUID = 1L;
	private static final int FRAME_WIDTH = 720;
	private static final int FRAME_HEIGHT = 480;
	private static File ascFile = null;
	
	private static PanelItem selectedItem;
	private static final GUIContentPanel contentPanel = new GUIContentPanel();
	
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
		
		BiFunction<JMenu, String, JMenuItem> createJMenuItem = (menu, name) -> {
			JMenuItem item = new JMenuItem(name);
			menu.add(item);
			return item;
		};
		
		JMenuItem openFileMenuItem   = createJMenuItem.apply(fileMenu, "Open...");
		JMenuItem saveFileMenuItem   = createJMenuItem.apply(fileMenu, "Save");
		JMenuItem exportFileMenuItem = createJMenuItem.apply(fileMenu, "Export");
		fileMenu.addSeparator();
		JMenuItem exitFileMenuItem   = createJMenuItem.apply(fileMenu, "Exit");
		
		ActionListener menuListener = new MenuListener(openFileMenuItem, saveFileMenuItem, exportFileMenuItem, exitFileMenuItem);
		
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
					Controller.setErrorMessage("Drag and Drop Error");
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
		
		
		//TODO -- add ImageIcon icon createImageIcon();
		JPanel questionPanel = new JPanel();					//Goes on the left!
		questionPanel.setLayout(new GridLayout(questions.size(), 1));
		questions.forEach(q -> questionPanel.add(new QuestionPanelItem(q)));
		JScrollPane leftScrollPane = new JScrollPane(questionPanel);
		leftScrollPane.getVerticalScrollBar().setUnitIncrement(16);
		leftScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		
		
		JPanel tablePanel = new JPanel();					//Goes on the right!
		tablePanel.setLayout(new GridLayout(tables.size(), 1));
		tables.forEach(t -> tablePanel.add(new TablePanelItem(t)));
		JScrollPane rightScrollPane = new JScrollPane(tablePanel);
		rightScrollPane.getVerticalScrollBar().setUnitIncrement(16);
		rightScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		
		mainPanel.add(leftScrollPane, BorderLayout.WEST);
		mainPanel.add(rightScrollPane, BorderLayout.EAST);
		mainPanel.add(contentPanel, BorderLayout.CENTER);
		
		mainPanel.revalidate();
		mainPanel.repaint();
	}
	
	static File getASCFile(){
		return ascFile;
	}
	
	
	static void swapSelectedItem(GUI2.PanelItem panelItem){
		if(selectedItem != null)
			selectedItem.unSelect();
		selectedItem = panelItem;
		
	}
	
	private class QuestionPanelItem extends PanelItem{
		private Question question;
		
		QuestionPanelItem(Question q){
			super(q.variable);
			question = q;
		}
		
		@Override
		public void mouseClicked(MouseEvent e){
			super.mouseClicked(e);
			GUIContentPanel.loadContentPanelForQuestion(question);
		}
	}
	
	private class TablePanelItem extends PanelItem{
		private Table table;
		
		TablePanelItem(Table t){
			super("TABLE " + t.number + " - " + (t.title.length() < 20 ? t.title : t.title.substring(0, 20)));
			table = t;
		}
		
		@Override
		public void mouseClicked(MouseEvent e){
			super.mouseClicked(e);
			GUIContentPanel.loadContentPanelForTable(table);
		}
	}
	
	private static class PanelItem extends JPanel implements MouseListener{
		private final Border marginBorder = new EmptyBorder(0, 15, 0, 0);
		private final CompoundBorder raisedButtonBorder;
		private final CompoundBorder loweredButtonBorder;
		
		PanelItem(String item){
			super(new BorderLayout());
			raisedButtonBorder = new CompoundBorder(BorderFactory.createRaisedBevelBorder(), marginBorder);
			loweredButtonBorder = new CompoundBorder(BorderFactory.createLoweredBevelBorder(), marginBorder);
			
			setBorder(raisedButtonBorder);
			setPreferredSize(new Dimension(150, 30));
			setBackground(Color.LIGHT_GRAY);
			add(new JLabel(item));
			
			addMouseListener(this);
		}
		
		@Override
		public void mouseClicked(MouseEvent e){
			swapSelectedItem(this);
			setBorder(loweredButtonBorder);
		}
		
		public void unSelect(){
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
	
	public static class MenuListener implements ActionListener{
		JMenuItem open;
		JMenuItem save;
		JMenuItem export;
		JMenuItem exit;
		
		 MenuListener(JMenuItem open, JMenuItem save, JMenuItem export, JMenuItem exit){
			super();
			
			open.addActionListener(this);
			save.addActionListener(this);
			export.addActionListener(this);
			exit.addActionListener(this);
			
			this.open = open;
			this.save = save;
			this.export = export;
			this.exit = exit;
		}
		
		@Override
		public void actionPerformed(ActionEvent e) {
			if (e.getSource() == open){
				System.out.println("open");
			}else if (e.getSource() == save){
				System.out.println("save");
			}else if (e.getSource() == export){
				System.out.println("export");
			}else if(e.getSource() == exit){
				System.out.println("exit");
				System.exit(0);
			}
		}
	}
}
