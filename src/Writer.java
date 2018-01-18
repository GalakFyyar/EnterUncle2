import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;

class Writer{
	static void writeFile(File file, ArrayList<Table> tables){
		PrintWriter writer;
		String originalFilePath = file.getParentFile().toString();
		String projectCode = file.getName().replace(".ASC", "");
		try{
			writer = new PrintWriter(originalFilePath + "\\" + projectCode + "_test" + ".e");
		}catch(FileNotFoundException e){
			return;
		}
		
		//Print Questions
		for(Table t: tables){
			writer.println("TABLE " + t.number);
			
			writer.println("T " + t.title);
			writer.println("T &wt" + t.excelWorksheetTitle);
			
			for(String[] r : t.rows){
				writer.println("R " + r[0] + "; " + r[1]);
			}
			
			writer.println();
		}
		writer.println();
		
		writer.close();
	}
}
