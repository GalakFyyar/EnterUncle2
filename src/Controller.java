import java.util.ArrayList;

class Controller{
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
	
	static boolean parseASCFile(String filePath){
		return Parser.parse(filePath);
	}
	
	static ArrayList<Question> getRegQuestions(){
		return QuestionnaireModel.getRegQuestions();
	}
	
	static ArrayList<Question> getDemoQuestions(){
		return QuestionnaireModel.getDemoQuestions();
	}
	
	static String getParserErrMsg(){
		return Parser.getErrString();
	}
	
	static void addQuestion(String variable, String label, String shortLabel, String skipDestination, String skipCondition, ArrayList<String> choices){
	
	
	}
}
