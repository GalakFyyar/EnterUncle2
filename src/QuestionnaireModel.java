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
	
	static void finalizeQuestions(){
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
			
			//Replace tabs and remove square brackets around label
			String label = q.label.replace('\t', ' ');
			label = label.substring(1, label.length() - 1).trim();
			
			//Find and capitalize first letter
			int periodIndex = label.indexOf('.');
			if(periodIndex < 0)
				periodIndex = 0;
			for(int i = periodIndex; i < label.length(); i++){
				if(Character.isLetter(label.charAt(i))){
					q.label = label.substring(0, i) + label.substring(i, i + 1).toUpperCase() + label.substring(i + 1);
					break;
				}
			}
			
			//Format Skips
			//skip destination may be a variable name, if it is, find it, then calculate it's relative position
			//if(ifExists)
			//	q.ifSkip = findQuePosition(skipDestinationIfStr, rq.quePosition, rawQuestions);
			//if(elseExists)
			//	q.elseSkip = findQuePosition(skipDestinationElseStr, rq.quePosition, rawQuestions);
			
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
	}
}
