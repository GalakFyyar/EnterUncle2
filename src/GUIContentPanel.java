import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

class GUIContentPanel extends JPanel{
	private static GUIContentPanel singleton;
	private static final CardLayout layout = new CardLayout();
	private static final QuestionPanel questionPanel = new QuestionPanel();
	private static final TablePanel tablePanel = new TablePanel();
	private static final String QUESTION_CARD = "Question";
	private static final String TABLE_CARD = "Table";
	
	GUIContentPanel(){
		super(layout);
		add(new JPanel(), "Blank");         //Default card, appears only at beginning when neither question nor table has been selected
		add(questionPanel, QUESTION_CARD);
		add(tablePanel, TABLE_CARD);
		singleton = this;
	}
	
	static void loadContentPanelForQuestion(Question q){
		questionPanel.loadContent(q);
		layout.show(singleton, QUESTION_CARD);
	}
	
	static void loadContentPanelForTable(Table t){
		tablePanel.loadContent(t);
		layout.show(singleton, TABLE_CARD);
	}
	
	static class QuestionPanel extends JPanel implements TableModelListener{
		private final JTextField labelField;
		private final JTable choicesTable;
		private final String[] titles = new String[]{"Code", "Label", "Destination"};
		
		QuestionPanel(){
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
		public void tableChanged(TableModelEvent e){
			//Data changed
			//System.out.println(e.toString());
		}
	}
	
	static class TablePanel extends JPanel implements TableModelListener{
		private final JTextField titleField;
		private final JTable rowsAndColumnsTable;
		private final String[] titles = new String[]{"Label", "Position", "Extras"};
		
		TablePanel(){
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
		}
		
		@Override
		public void tableChanged(TableModelEvent e){
			//Data changed
			//System.out.println(e.toString());
		}
	}
}
