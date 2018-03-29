import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

class QuestionnaireModel{
	private static final ArrayList<Question> questions = new ArrayList<>();
	private static final ArrayList<Question> regQuestions = new ArrayList<>();
	private static final ArrayList<Question> demoQuestions = new ArrayList<>();
	private static final Map<String, Question> questionNameMap = new HashMap<>();		//Variable -> Question
	private static String location = "";
	
	public static String getLocation(){
		return location;
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
	
	static ArrayList<Question> getAllQuestions(){
		return questions;
	}
	
	static boolean isEmpty(){
		return questions.isEmpty();
	}
	
	static void addQuestion(String variable, int codeWidth, String label, int quePosition, String shortLabel, String ifDestination, String elseDestination, String skipCondition, ArrayList<String[]> choices){
		Question q = new Question(variable, codeWidth, label, quePosition, shortLabel, ifDestination, elseDestination, skipCondition, choices);
		questions.add(q);
		questionNameMap.put(variable, q);
	}
	//returns true for success, false otherwise
	
	static boolean secondPassParsing(){
		//Find the Intro question and set the location
		for(Question q : questions){
			if(q.variable.equals("INTRO")){
				String shortLabel = q.shortLabel;
				if(!shortLabel.isEmpty())
					location = shortLabel;
				break;
			}
		}
		
		for(Question q : questions){
			//Mark demographic questions
			if(q.variable.charAt(0) == 'D'){
				demoQuestions.add(q);
			}else{
				regQuestions.add(q);
			}
			
			//Find and capitalize first letter of the label
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
			
			//remove / and + and * from the skip destinations
			q.ifDestination = q.ifDestination.replace("/", "").replace("+", "").replace("*", "");
			q.elseDestination = q.elseDestination.replace("/", "").replace("+", "");
			
			Integer ifSkip = setSkipPosition(q.ifDestination, q.quePosition);
			if(ifSkip != null)
				q.ifSkip = ifSkip;
			else{
				Controller.throwErrorMessage("Could not find skip destination :\"" + q.ifDestination + "\" in question " + q.variable);
				System.out.println("null");
				return false;
			}
			
			Integer elseSkip = setSkipPosition(q.elseDestination, q.quePosition);
			if(elseSkip != null)
				q.elseSkip = elseSkip;
			else{
				Controller.throwErrorMessage("Could not find skip destination :\"" + q.elseDestination + "\" in question " + q.variable);
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
	//Skip destination may allready be a number or blank or a name to another variable
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
	
	@SuppressWarnings("unused")
	static void printAll(){
		for(Question q : questions){
			printQuestion(q);
		}
	}
	
	@SuppressWarnings({"unused", "WeakerAccess"})
	static void printQuestion(Question q){
		System.out.println(q.variable);
		if(!q.label.isEmpty())
			System.out.println(q.label);
		else
			System.out.println("EMPTY LABEL");
		System.out.println(q.identifier);
		for(String[] c : q.choices)
			System.out.println(c[0] + "|--|" + c[1]);
		System.out.println();
	}
}
