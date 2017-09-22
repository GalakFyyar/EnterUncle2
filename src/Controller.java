import java.util.ArrayList;

class Controller{
	private static String errorMessage = "";
	
	static void setErrorMessage(String err){
		errorMessage = err;
	}
	
	static int GetStartingPosition(){
		return QuestionnaireModel.getStartingPosition();
	}
	
	static void clearQuestionnaire(){
		QuestionnaireModel.clearQuestions();
	}
	
	static boolean isQuestionnaireEmpty(){
		return QuestionnaireModel.isEmpty();
	}
	
	static void write(){
	
	}
	
	//Parse the .ASC file, then the questionnaireModel will populate some more Question fields
	static boolean parseASCFileAndPopulate(String filePath){
		boolean success = Parser.parse(filePath);
		QuestionnaireModel.finalizeQuestions();
		return success;
	}
	
	static ArrayList<Question> getRegQuestions(){
		return QuestionnaireModel.getRegQuestions();
	}
	
	static ArrayList<Question> getDemoQuestions(){
		return QuestionnaireModel.getDemoQuestions();
	}
	
	static String getErrorMessage(){
		return errorMessage;
	}
	
	static void addQuestion(String variable, int codeWidth, String label, int quePosition, String shortLabel, String ifDestination, String elseDestination, String skipCondition, ArrayList<String[]> choices){
		QuestionnaireModel.addQuestion(variable, codeWidth, label, quePosition, shortLabel, ifDestination, elseDestination, skipCondition, choices);
	}
	
	//static void addQuestion(String variable, int codeWidth, String label, int quePosition, String shortLabel, String skipDestination, String skipCondition, ArrayList<String[]> choices){
	//	QuestionnaireModel.addQuestion(variable, codeWidth, label, quePosition, shortLabel, skipDestination, skipCondition, choices);
	//}
}
