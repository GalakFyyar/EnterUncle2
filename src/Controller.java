import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Function;

class Controller{
	private static String errorMessage = "";
	private static final Set<String> unwantedVariables = new HashSet<>(Arrays.asList("TZONE", "LOC", "LDF", "LDE", "AREA", "FSA", "FSA1", "LANG", "IT2", "S1", "S2", "S3", "INT01", "INT02", "INT99", "C3", "INT"));//todo: make extendable
	
	
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
	
	//Parse the .ASC file, then the questionnaireModel will populate some more Question fields
	static boolean parseASCFileAndPopulate(String filePath){
		boolean parseSuccess = Parser.parse(filePath);
		boolean finalizeSuccess = QuestionnaireModel.finalizeQuestions();// TODO have this in Controller?
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
	
	static String getErrorMessage(){
		return errorMessage;
	}
	
	static void addQuestion(String variable, int codeWidth, String label, int quePosition, String shortLabel, String ifDestination, String elseDestination, String skipCondition, ArrayList<String[]> choices){
		QuestionnaireModel.addQuestion(variable, codeWidth, label, quePosition, shortLabel, ifDestination, elseDestination, skipCondition, choices);
	}
	
	static void filterOutBadQuestions(){
		ArrayList<Question> questions = QuestionnaireModel.getAllQuestions();
		questions.removeIf(q -> q.label.isEmpty());									//remove questions with no label
		questions.removeIf(q -> q.choices.isEmpty());								//remove questions with no choices
		questions.removeIf(q -> q.variable.charAt(0) == 'R');						//hopefully only removes recruit questions
		questions.removeIf(q -> unwantedVariables.contains(q.variable));			//remove unwanted variables
		
		Function<String, Boolean> testHearAgain = label -> {
			String l = label.toLowerCase();
			return (l.contains("hear") && l.contains("again")) || (l.contains("repeat") && l.contains("answers"));
		};
		questions.forEach(q -> q.choices.removeIf(choice -> testHearAgain.apply(choice[1])));
	}
	
	static void write(){
	
	}
}
