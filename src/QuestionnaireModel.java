import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

class QuestionnaireModel{
	private static final int START_POS = 248;
	private static final ArrayList<Question> questions = new ArrayList<>();
	private static final ArrayList<Question> regQuestions = new ArrayList<>();
	private static final ArrayList<Question> demoQuestions = new ArrayList<>();
	private static final Set<String> rejectableVariables = new HashSet<>(Arrays.asList(new String[]{"TZONE", "LOC", "LDF", "LDE", "AREA", "FSA", "FSA1", "LANG", "IT2", "S1", "S2", "S3", "INT01", "INT02", "INT99", "C3", "INT"}));
	private static String location = "";
	
	static int getStartingPosition(){
		return START_POS;
	}
	
	static void clearQuestions(){
	
	}
	
	static ArrayList<Question> getRegQuestions(){
		return regQuestions;
	}
	
	static ArrayList<Question> getDemoQuestions(){
		return demoQuestions;
	}
	
	static boolean isEmpty(){
		return questions.isEmpty();
	}
}
