import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

class QuestionnaireModel{
	private static final int START_POS = 248;
	private static final ArrayList<Question> questions = new ArrayList<>();
	private static final ArrayList<Question> regQuestions = new ArrayList<>();
	private static final ArrayList<Question> demoQuestions = new ArrayList<>();
	private static final Set<String> rejectableVariables = new HashSet<>(Arrays.asList("TZONE", "LOC", "LDF", "LDE", "AREA", "FSA", "FSA1", "LANG", "IT2", "S1", "S2", "S3", "INT01", "INT02", "INT99", "C3", "INT"));//todo: make extendable
	private static String location = "";
	
	static int getStartingPosition(){
		return START_POS;
	}
	
	static void clearQuestions(){
		questions.clear();
		regQuestions.clear();
		demoQuestions.clear();
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
	
	static void addQuestion(String variable, int codeWidth, String label, int quePosition, String shortLabel, String ifDestination, String elseDestination, String skipCondition, ArrayList<String[]> choices){
		questions.add(new Question(variable, codeWidth, label, quePosition, shortLabel, ifDestination, elseDestination, skipCondition, choices));
	}
	
	//returns true for success, false otherwise
	static boolean finalizeQuestions(){
		//Find the Intro question and set the location
		for(Question q : questions){
			if(q.variable.equals("INTRO")){
				String shortLabel = q.shortLabel;
				if(!shortLabel.isEmpty())
					location = shortLabel.substring(1, shortLabel.length() - 1);
				break;
			}
		}
		
		int pos = START_POS;
		for(Question q : questions){
			//Calculate position
			q.position = pos + "-";
			pos += q.codeWidth;
			
			//Mark demographic questions
			if(q.variable.charAt(0) == 'D'){
				demoQuestions.add(q);
			}else{
				regQuestions.add(q);
			}
			
			//Find and capitalize first letter
			String label = q.label;
			int periodIndex = label.indexOf('.');
			if(periodIndex < 0)
				periodIndex = 0;
			for(int i = periodIndex; i < label.length(); i++){
				if(Character.isLetter(label.charAt(i))){
					q.label = label.substring(0, i) + label.substring(i, i + 1).toUpperCase() + label.substring(i + 1);
					break;
				}
			}
			
			//remove / and + and *
			q.ifDestination = q.ifDestination.replace("/", "").replace("+", "").replace("*", "");
			q.elseDestination = q.elseDestination.replace("/", "").replace("+", "");
			
			Integer ifSkip = setSkipPosition(q.ifDestination, q.quePosition);
			if(ifSkip != null)
				q.ifSkip = ifSkip;
			else{
				Controller.setErrorMessage("Could not find skip destination :\"" + q.ifDestination + "\" in question " + q.variable);
				System.out.println("null");
				return false;
			}
			
			Integer elseSkip = setSkipPosition(q.elseDestination, q.quePosition);
			if(elseSkip != null)
				q.elseSkip = elseSkip;
			else{
				Controller.setErrorMessage("Could not find skip destination :\"" + q.elseDestination + "\" in question " + q.variable);
				System.out.println("null");
				return false;
			}
		}
		
		//Determine the identifier of regular questions
		for(Question q : regQuestions){
			String label = q.label;
			if(!label.isEmpty()){
				int delimiter = label.indexOf(".");		//find first period
				
				if(delimiter == -1 || delimiter > 9)	//period not found or too far away
					delimiter = label.indexOf(" ");		//use first space instead
				
				if(delimiter == -1){					//space not found either
					q.identifier = q.variable;
				}else
					q.identifier = label.substring(0, delimiter).toUpperCase();
			}
		}
		
		return true;
	}
	
	//This method converts the skip destination to an integer.
	//However, skip destination may allready be a number or blank or a name to another variable
	//Skip destination may not contain slashes '/' pluses '+' or stars '*'
	private static Integer setSkipPosition(String skipDestination, int quePosition){
		if(skipDestination.isEmpty())											//Is Blank
			return quePosition;
		
		boolean isNumber = true;
		for(int i = 0; i < skipDestination.length(); i++){
			if(!Character.isDigit(skipDestination.charAt(i)))
				isNumber = false;
		}
		
		Integer position = null;
		if(isNumber){															//Is Number
			position = quePosition + Integer.parseInt(skipDestination);
		}else{																	//Is Name
			//Find the variable that the skip points to
			for(Question q : questions){
				if(q.variable.equals(skipDestination)){
					position = q.quePosition;
					break;
				}
			}
		}
		return position;
	}
}
