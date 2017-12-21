import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

class GUI2 extends JFrame{
	private static final String VERSION = "2.0";
	private static final long serialVersionUID = 1L;
	
	GUI2(){
		try{
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		}catch(Exception ignored){}
		
		//Menu Bar
		loadMenuBar(this);
		
		JPanel mainPanel = new JPanel(new BorderLayout());
		//mainPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		
		//Load Components on mainPanel
		loadFileArea(mainPanel);
		
		getContentPane().add(mainPanel);
		setTitle("Enter Uncle v" + VERSION);
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
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
				File ascFile = null;
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
					Controller.parseASCFileAndPopulate(ascFile.getAbsolutePath());
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
		mainPanel.setLayout(new BorderLayout());
		
		
		//TODO -- add ImageIcon icon createImageIcon();
		JPanel leftPane = new JPanel();
		leftPane.setLayout(new GridLayout(questions.size(), 1));
		questions.forEach(q -> leftPane.add(new JLabel(q.variable)));
		JScrollPane leftScrollPane = new JScrollPane(leftPane);
		leftScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		
		
		JPanel rightPane = new JPanel();
		rightPane.setLayout(new GridLayout(questions.size(), 1));
		questions.forEach(q -> rightPane.add(new JTextField(q.variable, 15)));
		JScrollPane rightScrollPane = new JScrollPane(rightPane);
		rightScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		
		mainPanel.add(leftScrollPane, BorderLayout.WEST);
		mainPanel.add(rightScrollPane, BorderLayout.EAST);
		
		mainPanel.addMouseListener(new MouseAdapter(){
			@Override
			public void mouseClicked(MouseEvent e){
				System.out.println("Hey");
			}
		});
		
		mainPanel.revalidate();
		mainPanel.repaint();
	}
	
	private void loadContentPanelForQuestion(JPanel contentPanel){
	
	}
	
	private void loadContentPanelForTable(JPanel contentPanel){
	
	}
}
