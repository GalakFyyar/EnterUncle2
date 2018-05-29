import java.util.ArrayList;

class EFileModel{
	static final int START_POS = 248;
	
	private static final ArrayList<Table> tables = new ArrayList<>();
	
	static ArrayList<Table> getTables(){
		return tables;
	}
	
	static Table addTable(int number, String title, String excelWorksheetTitle, String[] labels, int[] positions, String[] codes){
		Table t = new Table(number, title, excelWorksheetTitle, labels, positions, codes);
		tables.add(t);
		return t;
	}
	
	//Also capitalizes the labels
	static void initRowArrays(ArrayList<String[]> choices, int len, int position, String[] labels, int[] positions, String[] codes){
		for(int i = 0; i < len; i++){
			String rawLabel = choices.get(i)[1];
			labels[i] = rawLabel.substring(0, 1).toUpperCase() + rawLabel.substring(1);
			positions[i] = position;
			codes[i] = choices.get(i)[0];
		}
	}
	
	static String getExcelWorksheetTitle(String label, String variable){
		int delimiter = label.indexOf(".");		//find first period
		
		if(delimiter == -1 || delimiter > 9)	//if period not found or too far away then
			delimiter = label.indexOf(" ");		//use first space instead
		
		if(delimiter == -1){					//if space not found either, just use the variable
			return variable;
		}else
			return "Q" + label.substring(0, delimiter).toUpperCase();
	}
	
	static void changeRowLabel(Table t, int row, String newLabel){
		t.changeRowLabel(row, newLabel);
	}
	
	static void changeRowPosition(Table t, int row, String newPosition){
		t.changeRowPosition(row, newPosition);
	}
	
	static void changeRowExtras(Table t, int row, String newExtras){
		t.changeRowExtras(row, newExtras);
	}
	
	//Adds all the special bells and whistles for IVRs
	static void formatAsIVR(){
		Table t = Controller.getTableFromQuestionIdentifier("AGE");
		System.out.println(t.title);
	}
}
