import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.*;
import java.util.List;

class GUI2 extends JFrame{
	private static final String VERSION = "2.0";
	private static final long serialVersionUID = 1L;
	private static final int FRAME_WIDTH = 720;
	private static final int FRAME_HEIGHT = 480;
	
	private JButton writeOutBTN;
	private JDesktopPane fileArea;
	private JTextField fileToConvPathTF;
	private JCheckBox startPosCB;
	private JTextField startPosTF;
	private JTextField statusTF;
	private JPanel bannerWrap;
	
	private ButtonGroup governmentLevel = new ButtonGroup();
	private Map<JCheckBox, Question> bannerRegQuestions = new LinkedHashMap<>();
	private Map<JCheckBox, Question> bannerDemoQuestions = new LinkedHashMap<>();
	private JRadioButton[] radioButtons = {new JRadioButton("Municipal", true), new JRadioButton("Provincial"), new JRadioButton("Federal")};
	
	GUI2(){
		try{
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		}catch(Exception ignored){}
		
		setTitle("Enter Uncle v" + VERSION);
		setSize(FRAME_WIDTH, FRAME_HEIGHT);
		setLocationRelativeTo(null);		//Center
		//setResizable(false);
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		
		governmentLevel.add(radioButtons[0]);
		governmentLevel.add(radioButtons[1]);
		governmentLevel.add(radioButtons[2]);
		
		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new GridLayout(0, 2, 15, 0));
		loadComponents(mainPanel);
		
		ButtonListener convertListener = new ButtonListener();
		writeOutBTN.addActionListener(convertListener);
		
		DropTarget qaxDropTarget = new DropTarget(){
			public synchronized void drop(DropTargetDropEvent evt){
				File ascFile = null;
				try{
					evt.acceptDrop(DnDConstants.ACTION_COPY);
					//noinspection unchecked
					List<File> droppedFiles = (List<File>)evt.getTransferable().getTransferData(DataFlavor.javaFileListFlavor);
					ascFile = droppedFiles.get(0);
					
					Controller.clearQuestionnaire();
					bannerDemoQuestions.clear();
					bannerRegQuestions.clear();
					bannerWrap.removeAll();
					getRootPane().revalidate();
				}catch(Exception ex){
					statusTF.setText("Drag and Drop Error");
				}
				
				if(ascFile != null)
					readFile(ascFile);
			}
		};
		fileArea.setDropTarget(qaxDropTarget);
		
		getContentPane().add(mainPanel);
		//setExtendedState(JFrame.MAXIMIZED_BOTH);
		//setUndecorated(true);
		setVisible(true);
		
		System.out.println("LOADED GUI");
	}
	
	private void loadComponents(JPanel mainPanel){
		mainPanel.setBorder(new EmptyBorder(15, 15, 15, 15));
		
		JPanel leftPanel = new JPanel();
		leftPanel.setLayout(new BorderLayout(0, 15));
		
		bannerWrap = new JPanel();
		bannerWrap.setLayout(new GridLayout(0, 2));
		JScrollPane scrollPane = new JScrollPane(bannerWrap);
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		leftPanel.add(scrollPane, BorderLayout.CENTER);
		
		JPanel rightPanel = new JPanel();
		rightPanel.setLayout(new BorderLayout(0, 0));
		
		fileToConvPathTF = new JTextField();
		fileToConvPathTF.setForeground(Color.GRAY);
		fileToConvPathTF.setText("ASC input file");
		rightPanel.add(fileToConvPathTF, BorderLayout.NORTH);
		
		fileArea = new JDesktopPane();
		fileArea.setToolTipText("Drag files onto here");
		fileArea.setBackground(Color.LIGHT_GRAY);
		rightPanel.add(fileArea, BorderLayout.CENTER);
		
		JPanel buttonWrap = new JPanel();
		buttonWrap.setLayout(new GridLayout(4, 1, 0, 10));
		
		JPanel governmentLevelWrap = new JPanel();
		governmentLevelWrap.add(radioButtons[0]);
		governmentLevelWrap.add(radioButtons[1]);
		governmentLevelWrap.add(radioButtons[2]);
		governmentLevelWrap.setPreferredSize(new Dimension(0, 24));
		buttonWrap.add(governmentLevelWrap);
		
		
		JPanel startPosWrap = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		startPosCB = new JCheckBox("Start Position Override:");
		startPosCB.addActionListener(new CheckListener());
		startPosTF = new JTextField(String.valueOf(Controller.GetStartingPosition()), 4);
		startPosTF.setHorizontalAlignment(SwingConstants.RIGHT);
		startPosWrap.add(startPosCB);
		startPosWrap.add(startPosTF);
		startPosWrap.setPreferredSize(new Dimension(0, 26));
		buttonWrap.add(startPosWrap);
		
		
		statusTF = new JTextField();
		statusTF.setForeground(Color.BLACK);
		statusTF.setText("Status");
		buttonWrap.add(statusTF);
		
		writeOutBTN = new JButton("Write!");
		buttonWrap.add(writeOutBTN);
		
		rightPanel.add(buttonWrap, BorderLayout.SOUTH);
		
		mainPanel.add(leftPanel);
		mainPanel.add(rightPanel);
	}
	
	private void readFile(File ascFile){
		fileToConvPathTF.setText(ascFile.getAbsolutePath());
		
		boolean success = Controller.parseASCFileAndPopulate(fileToConvPathTF.getText());
		if(!success){
			String errorMessage = Controller.getErrorMessage();
			if(errorMessage.isEmpty())
				errorMessage = "Bad file, no questions loaded. Try converting to UTF-8?";
			statusTF.setText(errorMessage);
		}else
			statusTF.setText("Read Successfully");
		
		QuestionnaireModel.printAll();
		Controller.filterOutBadQuestions();
		System.out.println("==================\n==================\n==================");
		QuestionnaireModel.printAll();
		
		
		//populate banner pane
		for(Question dq : Controller.getDemoQuestions()){
			JCheckBox jcb = new JCheckBox(dq.variable);
			jcb.setSelected(true);
			bannerWrap.add(jcb);
			bannerDemoQuestions.put(jcb, dq);
		}
		for(Question rq : Controller.getRegQuestions()){
			JCheckBox jcb = new JCheckBox(rq.variable);
			bannerWrap.add(jcb);
			bannerRegQuestions.put(jcb, rq);
		}
		getRootPane().revalidate();
	}
	
	private class ButtonListener implements ActionListener{
		public void actionPerformed(ActionEvent e){
			if(e.getSource() == writeOutBTN){
				GovernmentLevel govLvl = GovernmentLevel.MUNICIPAL;
				if(governmentLevel.getSelection() == radioButtons[0].getModel())
					govLvl = GovernmentLevel.MUNICIPAL;
				else if(governmentLevel.getSelection() == radioButtons[1].getModel())
					govLvl = GovernmentLevel.PROVINCIAL;
				else if(governmentLevel.getSelection() == radioButtons[2].getModel())
					govLvl = GovernmentLevel.FEDERAL;
				
				
				if(Controller.isQuestionnaireEmpty()){
					statusTF.setText("Can't Write - Empty Qnair");
					return;
				}
				ArrayList<Question> checked = new ArrayList<>();
				for(Map.Entry entry : bannerDemoQuestions.entrySet()){
					if(((JCheckBox) entry.getKey()).isSelected())
						checked.add((Question)entry.getValue());
				}
				for(Map.Entry entry : bannerRegQuestions.entrySet()){
					if(((JCheckBox) entry.getKey()).isSelected())
						checked.add((Question)entry.getValue());
				}
				Controller.write();
				
				statusTF.setText("Conversion Complete");
				System.out.println("DONE");
			}
		}
	}
	
	private class CheckListener implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e){
			System.out.println("hi");
		}
	}
}
