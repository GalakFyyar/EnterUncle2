import java.io.File;

public class UncleConvert{
	public static void main(String[] args) {
		System.out.println("START");
		//test();
		launchGUI();
	}

	private static void launchGUI(){
		javax.swing.SwingUtilities.invokeLater(GUI2::new);
	}

	private static void test(){
		//String filePath = "G:\\FORUM\\FOLD\\FOLD.ASC";
		String filePath = "E:\\MAN_SON\\Projects\\IdeaProjects\\EnterUncle2\\FOMB.ASC";
		
		File file = new File(filePath);
		
		boolean succ = Controller.parseASCFileAndPopulateQuestionnaireModel(file);
		if(!succ){
			System.out.println("Parse not successful");
			return;
		}
		
		Controller.populateEFileModel();
		Controller.write(file);
		
		System.out.println("END");
	}
}
