import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

class EFileModel{
	private static final ArrayList<Table> tables = new ArrayList<>();
	private static final Set<String> unwantedVariables = new HashSet<>(Arrays.asList("TZONE", "LOC", "LDF", "LDE", "AREA", "FSA", "FSA1", "LANG", "IT2", "S1", "S2", "S3", "INT01", "INT02", "INT99", "C3", "INT"));//todo: make extendable
	
	static ArrayList<Table> getTables(){
		return tables;
	}
	
	static void convertQuestionsToTables(ArrayList<Question> regQuestions, ArrayList<Question> demoQuestions){
		int number = 1;
		ArrayList<Question> filteredRegQuestions = filterOutBadRegQuestions(regQuestions);
		for(Question frq : filteredRegQuestions){
			ArrayList<String[]> choiceRows = new ArrayList<>();
			for(String[] choice : frq.choices){
				String[] row = new String[2];
				row[0] = choice[1];
				row[1] = choice[0];
				choiceRows.add(row);
			}
			
			tables.add(new Table(number++, null, null, frq.label, null, choiceRows));
		}
		
		
		ArrayList<Question> filteredDemoQuestions = filterOutBadDemoQuestions(regQuestions);
		for(Question fdq : filteredDemoQuestions){
			tables.add(new Table(number++, null, null, fdq.label, null,fdq.choices));
		}
	}
	
	private static ArrayList<Question> filterOutBadRegQuestions(ArrayList<Question> questions){
		ArrayList<Question> r;
		
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
