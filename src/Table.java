import java.util.ArrayList;

class Table{
	int number;
	String title;
	String excelWorksheetTitle;
	boolean hasQualifier;
	String qualifier;
	String base;
	String options;
	ArrayList<String[]> columns = new ArrayList<>();		//[0]=label; [1]=codeAndPosition; [2]=tag
	ArrayList<String[]> rows = new ArrayList<>();			//[0]=label; [1]=codeAndPosition; [2]=tag
	
	Table(int number, String qualifier, String options, String title, ArrayList<String[]> columns, ArrayList<String[]> rows){
		this.number = number;
		this.qualifier = qualifier;
		this.options = options;
		this.title = title;
		this.columns = columns;
		this.rows = rows;
	}
	
	ArrayList<String> getTitles(){
		ArrayList<String> ret = new ArrayList<>();
		ret.add(title);
		ret.add(excelWorksheetTitle);
		if(hasQualifier)
			ret.add(base);
		return ret;
	}
}
