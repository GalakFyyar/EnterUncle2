import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

class Controller{
	private static String errorMessage = "";
	private static final Set<String> rejectableVariables = new HashSet<>(Arrays.asList("TZONE", "LOC", "LDF", "LDE", "AREA", "FSA", "FSA1", "LANG", "IT2", "S1", "S2", "S3", "INT01", "INT02", "INT99", "C3", "INT"));//todo: make extendable
	
	
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
	
	//TODO: more in-house
	//Parse the .ASC file, then the questionnaireModel will populate some more Question fields
	static boolean parseASCFileAndPopulate(String filePath){
		boolean parseSuccess = Parser.parse(filePath);
		boolean finalizeSuccess = QuestionnaireModel.finalizeQuestions();
		return parseSuccess && finalizeSuccess;
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
	
	//TODO: implement method and add comment
	static void selectQuestions(){
		//Set questions = QuestionnaireModel.getQuestions();
	}
}
