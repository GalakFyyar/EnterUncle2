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
	
	//Parse the .ASC file. The questionnaireModel will populate it's own variables afterwards
	static boolean parseASCFile(String filePath){
		return Parser.parse(filePath);
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
	
	static void addQuestion(String variable, int codeWidth, String label, String shortLabel, String skipDestination, String skipCondition, ArrayList<String[]> choices){
		//capitalization
		
	}
}
