import java.util.ArrayList;

class Controller{
	private static String errorMessage = "";
	
	static void setErrorMessage(String err){
		errorMessage = err;
	}
	
	static int getStartingPosition(){
		return QuestionnaireModel.getStartingPosition();
	}
	
	static void clearQuestionnaire(){
		QuestionnaireModel.clearQuestions();
	}
	
	static boolean isQuestionnaireEmpty(){
		return QuestionnaireModel.isEmpty();
	}
	
	//Parse the .ASC file, then the questionnaireModel will populate some more Question fields
	static boolean parseASCFileAndPopulateQuestionnaireModel(String filePath){
		boolean parseSuccess = Parser.parse(filePath);
		boolean finalizeSuccess = QuestionnaireModel.secondPassParsing();
		return parseSuccess && finalizeSuccess;
	}
	
	static ArrayList<Question> getRegQuestions(){
		return QuestionnaireModel.getRegQuestions();
	}
	
	static ArrayList<Question> getDemoQuestions(){
		return QuestionnaireModel.getDemoQuestions();
	}
	
	static ArrayList<Question> getAllQuestions() {
		return QuestionnaireModel.getAllQuestions();
	}
	
	static ArrayList<Table> getTables(){
		return EFileModel.getTables();
	}
	
	static String getErrorMessage(){
		return errorMessage;
	}
	
	static void addQuestion(String variable, int codeWidth, String label, int quePosition, String shortLabel, String ifDestination, String elseDestination, String skipCondition, ArrayList<String[]> choices){
		QuestionnaireModel.addQuestion(variable, codeWidth, label, quePosition, shortLabel, ifDestination, elseDestination, skipCondition, choices);
	}
	
	static void populateEFileModel(){
		ArrayList<Question> regQuestions = QuestionnaireModel.getRegQuestions();
		ArrayList<Question> demoQuestions = QuestionnaireModel.getDemoQuestions();
		EFileModel.convertQuestionsToTables(regQuestions, demoQuestions);
	}
	
	static void write(){
		Writer.writeFile(GUI2.getASCFile(), EFileModel.getTables());
	}
}
