import java.io.File;

public class UncleConvert{
	public static void main(String[] args) {
		System.out.println("START");
		if(args.length > 0 && args[0].equals("-t")){
			test();
		}else{
			launchGUI();
		}
	}

	private static void launchGUI(){
		javax.swing.SwingUtilities.invokeLater(GUI2::new);
	}

	private static void test(){
		String filePath = "G:\\FORUM\\FOMH\\FOMH.ASC";
		//String filePath = "G:\\FORUM\\FOLD\\FOLD - COPY.ASC";
		//String filePath = "E:\\MAN_SON\\Projects\\IdeaProjects\\EnterUncle2\\FOMB.ASC";
		
		File file = new File(filePath);
		
		boolean succ = Controller.parseASCFileAndPopulateQuestionnaireModel(file);
		if(!succ){
			System.out.println("Parse not successful");
			return;
		}
		
		Controller.populateEFile();
		Controller.write(file);
		
		System.out.println("END");
	}
}
