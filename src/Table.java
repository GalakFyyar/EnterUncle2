import java.util.ArrayList;

class Table{
	int number;
	String title;
	String excelWorksheetTitle;
	boolean hasQualifier;
	String qualifier;
	String base;
	String options;
	String[] rowLabels;
	int[] rowPositions;
	String[] rowCodes;
	String[] columnLabels;
	int[] columnPositions;
	String[] columnCodes;
	String[][] rows;			//[0]=label; [1]=codeAndPosition; [2]=extra
	String[][] columns;			//[0]=label; [1]=codeAndPosition; [2]=extra
	
	Table(int number, String title, String excelWorksheetTitle, String[] rowLabels, int[] rowPositions, String[] rowCodes){
		this(number, title, excelWorksheetTitle, false, null, null, null, rowLabels, rowPositions, rowCodes, null, null, null);
	}
	
	Table(int number, String title, String excelWorksheetTitle, boolean hasQualifier, String qualifier, String base, String options, String[] rowLabels, int[] rowPositions, String[] rowCodes, String[] columnLabels, int[] columnPositions, String[] columnCodes){
		this.number = number;
		this.title = title;
		this.excelWorksheetTitle = excelWorksheetTitle;
		this.hasQualifier = hasQualifier;
		this.qualifier = qualifier;
		this.base = base;
		this.options = options;
		this.rowLabels = rowLabels;
		this.rowPositions = rowPositions;
		this.rowCodes = rowCodes;
		this.columnLabels = columnLabels;
		this.columnPositions = columnPositions;
		this.columnCodes = columnCodes;
		
		if(rowPositions != null)
			rows = formatChoiceArrays(rowLabels, rowPositions, rowCodes);
		if(columnPositions != null)
			columns = formatChoiceArrays(columnLabels, columnPositions, columnCodes);
	}
	
	private static String[][] formatChoiceArrays(String[] labels, int[] positions, String[] codes){
		int length = positions.length;
		String[][] choices = new String[length][];
		for(int i = 0; i < length; i++){
			String[] row = new String[3];
			row[0] = labels[i];
			if(codes[i].length() == 1){
				row[1] = positions[i] + "-" + codes[i];
			}else{
				int codeLength = codes[i].length();
				row[1] = "r(" + positions[i] +  ":" + positions[i]+codeLength + "," + codes[i] + ")";
			}
			
			choices[i] = row;
		}
		return choices;
	}
	
	ArrayList<String> getTitles(){
		ArrayList<String> ret = new ArrayList<>();
		ret.add(title);
		ret.add(excelWorksheetTitle);
		if(hasQualifier)
			ret.add(base);
		return ret;
	}
	
	void changeRowLabel(int row, String newLabel){
		rows[row][0] = newLabel;
	}
	
	void changeRowPosition(int row, String newPosition){
		rows[row][1] = newPosition;
	}
	
	void changeRowExtras(int row, String newExtras){
		rows[row][2] = newExtras;
	}
}
