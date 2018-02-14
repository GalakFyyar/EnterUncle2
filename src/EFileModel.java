import java.util.*;
import java.util.stream.Collectors;

class EFileModel{
	private static final int START_POS = 248;
	
	private static final ArrayList<Table> tables = new ArrayList<>();
	private static final Set<String> unwantedVariables = new HashSet<>(Arrays.asList("TZONE", "LOC", "LDF", "LDE", "AREA", "FSA", "FSA1", "LANG", "IT2", "S1", "S2", "S3", "INT01", "INT02", "INT99", "C3", "INT"));//todo: make extendable
	
	static ArrayList<Table> getTables(){
		return tables;
	}
	
	static void convertQuestionsToTables(ArrayList<Question> regQuestions, ArrayList<Question> demoQuestions){
		Map<String, Integer> positionsOfAllQuestions = calcPositions(regQuestions, demoQuestions);
		
		int number = 1;
		ArrayList<Question> filteredRegQuestions = filterOutBadRegQuestions(regQuestions);
		for(Question frq : filteredRegQuestions){
			int numOfChoices = frq.choices.size();
			String[] labels = new String[numOfChoices];
			int[] positions = new int[numOfChoices];
			String[] codes = new String[numOfChoices];
			int position = positionsOfAllQuestions.get(frq.variable);
			
			initChoiceArrays(frq.choices, numOfChoices, position, labels, positions, codes);
			
			tables.add(new Table(number++, frq.label, "", labels, positions, codes));
		}
		
		
		ArrayList<Question> filteredDemoQuestions = filterOutBadDemoQuestions(demoQuestions);
		for(Question fdq : filteredDemoQuestions){
			int numOfChoices = fdq.choices.size();
			String[] labels = new String[numOfChoices];
			int[] positions = new int[numOfChoices];
			String[] codes = new String[numOfChoices];
			int position = positionsOfAllQuestions.get(fdq.variable);

			initChoiceArrays(fdq.choices, numOfChoices, position, labels, positions, codes);

			
			tables.add(new Table(number++, fdq.label, null, labels, positions, codes));
		}
	}
	
	private static Map<String, Integer> calcPositions(ArrayList<Question> regQuestions, ArrayList<Question> demoQuestions){
		Map<String, Integer> positionsOfQuestions = new HashMap<>();
		int pos = START_POS;
		for(Question rq : regQuestions){
			positionsOfQuestions.put(rq.variable, pos);
			pos += rq.codeWidth;
		}
		for(Question dq : demoQuestions){
			positionsOfQuestions.put(dq.variable, pos);
			pos += dq.codeWidth;
		}
		return positionsOfQuestions;
	}
	
	private static void initChoiceArrays(ArrayList<String[]> choices, int len, int position, String[] labels, int[] positions, String[] codes){
		for(int i = 0; i < len; i++){
			labels[i] = choices.get(i)[1];
			positions[i] = position;
			codes[i] = choices.get(i)[0];
		}
	}
	
	private static ArrayList<Question> filterOutBadRegQuestions(ArrayList<Question> questions){
		ArrayList<Question> r;
		//Try RemoveIf instead of filter?
		r = questions.stream()
			.filter(q -> !q.label.isEmpty())										//filter questions with label
			.filter(q -> !q.choices.isEmpty())										//remove questions with no choices
			.filter(q -> q.variable.charAt(0) != 'R')								//hopefully only removes recruit questions
			.filter(q -> !unwantedVariables.contains(q.variable))					//remove unwanted variables
			.collect(Collectors.toCollection(ArrayList::new));
		
//		Function<String, Boolean> testHearAgain = label -> {
//			String l = label.toLowerCase();
//			return (l.contains("hear") && l.contains("again")) || (l.contains("repeat") && l.contains("answers"));
//		};
//		questions.forEach(q -> q.choices.removeIf(choice -> testHearAgain.apply(choice[1])));
		
		return r;
	}
	
	private static ArrayList<Question> filterOutBadDemoQuestions(ArrayList<Question> questions){
		ArrayList<Question> r;
		
		r = questions.stream()
		.filter(q -> !q.label.isEmpty())										//filter questions with label
		.collect(Collectors.toCollection(ArrayList::new));

		return r;
	}
}
