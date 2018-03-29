import java.util.*;
import java.util.stream.Collectors;

class EFileModel{
	private static final int START_POS = 248;
	
	private static final ArrayList<Table> tables = new ArrayList<>();
	private static final Set<String> unwantedVariables = new HashSet<>(Arrays.asList("TZONE", "LOC", "LDF", "LDE", "AREA", "FSA", "FSA1", "LANG", "IT2", "S1", "S2", "S3", "INT01", "INT02", "INT99", "C3", "INT"));//todo: make extendable
	
	static ArrayList<Table> getTables(){
		return tables;
	}
	
	static Table addTable(int number, String title, String excelWorksheetTitle, String[] labels, int[] positions, String[] codes){
		Table t = new Table(number, title, excelWorksheetTitle, labels, positions, codes);
		tables.add(t);
		return t;
	}
	
	static Map<String, Integer> calcPositions(ArrayList<Question> regQuestions, ArrayList<Question> demoQuestions){
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
	
	//Also capitalizes the labels
	static void initRowArrays(ArrayList<String[]> choices, int len, int position, String[] labels, int[] positions, String[] codes){
		for(int i = 0; i < len; i++){
			String rawLabel = choices.get(i)[1];
			labels[i] = rawLabel.substring(0, 1).toUpperCase() + rawLabel.substring(1);
			positions[i] = position;
			codes[i] = choices.get(i)[0];
		}
	}
	
	static ArrayList<Question> filterOutBadRegQuestions(ArrayList<Question> questions){
		questions.removeIf(q -> q.label.isEmpty());								//filter questions with label
		questions.removeIf(q -> q.choices.isEmpty());							//remove questions with no choices
		questions.removeIf(q -> q.variable.charAt(0) == 'R');					//hopefully only removes recruit questions
		questions.removeIf(q -> unwantedVariables.contains(q.variable));		//remove unwanted variables
		
//		Function<String, Boolean> testHearAgain = label -> {
//			String l = label.toLowerCase();
//			return (l.contains("hear") && l.contains("again")) || (l.contains("repeat") && l.contains("answers"));
//		};
//		questions.forEach(q -> q.choices.removeIf(choice -> testHearAgain.apply(choice[1])));
		
		return questions;
	}
	
	static ArrayList<Question> filterOutBadDemoQuestions(ArrayList<Question> questions){
		ArrayList<Question> r;
		
		r = questions.stream()
		.filter(q -> !q.label.isEmpty())										//filter questions with label
		.collect(Collectors.toCollection(ArrayList::new));

		return r;
	}
	
	static String getExcelWorksheetTitle(String label, String variable){
		int delimiter = label.indexOf(".");		//find first period
		
		if(delimiter == -1 || delimiter > 9)	//if period not found or too far away then
			delimiter = label.indexOf(" ");		//use first space instead
		
		if(delimiter == -1){					//if space not found either, just use the variable
			return variable;
		}else
			return "Q" + label.substring(0, delimiter).toUpperCase();
	}
}
