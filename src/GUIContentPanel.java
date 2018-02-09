import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

public class GUIContentPanel extends JPanel{
	private static GUI2.PanelItem selectedItem;
	
	static void loadContentPanelForQuestion(JPanel contentPanel, Question q){
		contentPanel.removeAll();
		
		JTextField labelContentField = new JTextField();
		labelContentField.setMinimumSize(new Dimension(0, 200));
		labelContentField.setText(q.label);
		
		JPanel choicesContentPanel = new JPanel(new GridLayout(q.choices.size(), 1));
		q.choices.forEach(c -> choicesContentPanel.add(new JLabel(c[1])));
		
		JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, labelContentField, choicesContentPanel);
		
		contentPanel.add(splitPane);
		contentPanel.revalidate();
	}
	
	static void loadContentPanelForTable(JPanel contentPanel, Table t){
		contentPanel.removeAll();
		contentPanel.add(new JLabel(t.title), BorderLayout.CENTER);
		contentPanel.revalidate();
	}
	
	public static void swapSelectedItem(GUI2.PanelItem panelItem){
		if(selectedItem != null)
			selectedItem.unSelect();
		selectedItem = panelItem;
		
	}
}
