import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

class GUIContentPanel extends JPanel{
	private static GUIContentPanel singleton;
	private static final CardLayout LAYOUT = new CardLayout();
	private static final QuestionContentPanel QUESTION_CONTENT_PANEL = new QuestionContentPanel();
	private static final TableContentPanel TABLE_CONTENT_PANEL = new TableContentPanel();
	private static final String QUESTION_CARD = "Question";
	private static final String TABLE_CARD = "Table";
	
	GUIContentPanel(){
		super(LAYOUT);
		add(new JPanel(), "Blank");         //Default card, appears only at beginning when neither question nor table has been selected
		add(QUESTION_CONTENT_PANEL, QUESTION_CARD);
		add(TABLE_CONTENT_PANEL, TABLE_CARD);
		singleton = this;
	}
	
	static void loadContentPanelForQuestion(Question q){
		QUESTION_CONTENT_PANEL.loadContent(q);
		LAYOUT.show(singleton, QUESTION_CARD);
	}
	
	static void loadContentPanelForTable(Table t){
		TABLE_CONTENT_PANEL.loadContent(t);
		LAYOUT.show(singleton, TABLE_CARD);
	}
	
	static class QuestionContentPanel extends JPanel implements TableModelListener{
		private final JTextField labelField;
		private final JTable choicesTable;
		private final String[] titles = new String[]{Column.CODE.title(), Column.LABEL.title(), Column.SKIP_DESTINATION.title()};
		private Question question;
		
		QuestionContentPanel(){
			super(new BorderLayout());
			labelField = new JTextField();
			labelField.setMinimumSize(new Dimension(0, 200));
			
			choicesTable = new JTable();
			choicesTable.setModel(new DefaultTableModel());
			choicesTable.getModel().addTableModelListener(this);
			
			JScrollPane choicesPane = new JScrollPane(choicesTable);
			choicesPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
			
			JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, labelField, choicesPane);
			add(splitPane);
		}
		
		void loadContent(Question q){
			question = q;
			labelField.setText(q.label);
			int len = q.choices.size();
			String[][] choices = new String[len][];
			for(int i = 0; i < len; i++){
				choices[i] = q.choices.get(i);
			}
			DefaultTableModel tm = (DefaultTableModel)choicesTable.getModel();
			tm.getDataVector().removeAllElements();
			tm.setDataVector(choices, titles);
		}
		
		@Override
		//JTable created or data changed
		public void tableChanged(TableModelEvent e){
			int column = e.getColumn();
			int row = e.getFirstRow();
			
			//When a JTable gets created, the changed column is -1 since no change was actually made.
			//This means that we can just exist the method.
			if(column == -1){
				return;
			}
			
			TableModel model = choicesTable.getModel();
			String change = (String)model.getValueAt(row, column);
			
			switch(column){
				case 0:					//Code changed
					Controller.dataQuestionChange(question, Column.CODE, row, change);
					break;
				case 1:					//Label Changed
					Controller.dataQuestionChange(question, Column.LABEL, row, change);
					break;
				case 2:					//Skip Destination changed
					Controller.dataQuestionChange(question, Column.SKIP_DESTINATION, row, change);
			}
			
			System.out.println(column);
			System.out.println(row);
		}
		
		public enum Column{
			CODE("Code"),
			LABEL("Label"),
			SKIP_DESTINATION("Skip to");
			
			private String title;
			
			Column(String s){
				title = s;
			}
			
			public String title(){
				return title;
			}
		}
	}
	
	static class TableContentPanel extends JPanel implements TableModelListener{
		private final JTextField titleField;
		private final JTable rowsAndColumnsTable;
		private final String[] titles = new String[]{Column.LABEL.title(), Column.POSITION.title(), Column.EXTRAS.title()};
		private Table table;
		
		TableContentPanel(){
			super(new BorderLayout());
			titleField = new JTextField();
			titleField.setMinimumSize(new Dimension(0, 200));
			
			rowsAndColumnsTable = new JTable(new String[0][0], titles);
			rowsAndColumnsTable.setModel(new DefaultTableModel());
			rowsAndColumnsTable.getModel().addTableModelListener(this);
			
			JScrollPane rowsPane = new JScrollPane(rowsAndColumnsTable);
			rowsPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
			
			JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, titleField, rowsPane);
			
			add(splitPane);
		}
		
		void loadContent(Table t){
			titleField.setText(t.title);
			DefaultTableModel tm = (DefaultTableModel) rowsAndColumnsTable.getModel();
			tm.getDataVector().removeAllElements();
			tm.setDataVector(t.rows, titles);
			table = t;
		}
		
		@Override
		//Happens whenever the Table data is created or changed
		public void tableChanged(TableModelEvent e){
			int column = e.getColumn();
			int row = e.getFirstRow();
			
			//When a JTable gets created, the changed column is -1 since no change was actually made.
			//This means that we can just exist the method.
			if(column == -1){
				return;
			}
			
			TableModel model = rowsAndColumnsTable.getModel();
			String change = (String)model.getValueAt(row, column);
			
			switch(column){
				case 0:					//Label changed
					Controller.dataTableChange(table, Column.LABEL, row, change);
					break;
				case 1:					//Position Changed
					Controller.dataTableChange(table, Column.POSITION, row, change);
					break;
				case 2:					//Skip Destination changed
					Controller.dataTableChange(table, Column.EXTRAS, row, change);
			}
			
			System.out.println(column);
			System.out.println(row);
		}
		
		public enum Column{
			LABEL("Label"),
			POSITION("Position"),
			EXTRAS("Extras");
			
			private String title;
			
			Column(String s){
				title = s;
			}
			
			public String title(){
				return title;
			}
		}
	}
}
