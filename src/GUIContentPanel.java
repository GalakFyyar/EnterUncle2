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
		add(questionPanel, QUESTION_CARD);
		add(tablePanel, TABLE_CARD);
		singleton = this;
		System.out.println(getSize());
	}
	
	static void loadContentPanelForQuestion(Question q){
		questionPanel.loadContent(q);
		layout.show(singleton, QUESTION_CARD);
	}
	
	static void loadContentPanelForTable(Table t){
		singleton.removeAll();
		
		JTextField titleField = new JTextField(t.title);
		titleField.setMinimumSize(new Dimension(0, 200));
		
		String[] titles = new String[]{"Label", "Position", "Extras"};
		JTable rowsTable = new JTable(t.rows, titles);
		
		JScrollPane rowsPane = new JScrollPane(rowsTable);
		rowsPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		
		JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, titleField, rowsPane);
		
		singleton.add(splitPane);
		singleton.revalidate();
	}
	
	static class QuestionPanel extends JPanel implements TableModelListener{
		private final JTextField labelField;
		private final JTable choicesTable;
		private final String[] titles = new String[]{"Code", "Label", "Destination"};
		
		QuestionPanel(){
			super();
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
	
	static class TablePanel extends JPanel{
		private final JTextField titleField;
		private final JTable rowsTable;
		private final String[] titles = new String[]{"Label", "Position", "Extras"};
		
		TablePanel(){
			titleField = new JTextField();
			titleField.setMinimumSize(new Dimension(0, 200));
			
			rowsTable = new JTable(new String[0][0], titles);
			
			JScrollPane rowsPane = new JScrollPane(rowsTable);
			rowsPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
			
			JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, titleField, rowsPane);
			
			add(splitPane);
		}
		
		void loadContent(Table q){
//			labelField.setText(q.label);
//			int len = q.choices.size();
//			String[][] choices = new String[len][];
//			for(int i = 0; i < len; i++){
//				choices[i] = q.choices.get(i);
//			}
//			DefaultTableModel tm = (DefaultTableModel) choicesTable.getModel();
//			tm.getDataVector().removeAllElements();
//			tm.setDataVector(choices, titles);
		}
	}
}
