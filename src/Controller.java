import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

import java.io.File;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

class Controller{
	private static final BiMap<Question, Table> questionAndTableBiMap = HashBiMap.create();
	private static final Set<String> unwantedVariables = new HashSet<>(Arrays.asList("TZONE", "LOC", "LDF", "LDE", "AREA", "FSA", "FSA1", "LANG", "IT2", "S1", "S2", "S3", "INT01", "INT02", "INT99", "C3", "INT"));//todo: make extendable
	
	static void throwErrorMessage(String err){
		GUI2.throwErrorMessage(err);
	}
	
	static void clearQuestionnaire(){
		QuestionnaireModel.clearQuestions();
	}
	
	static boolean isQuestionnaireEmpty(){
		return QuestionnaireModel.isEmpty();
	}
	
	//Parse the .ASC file, then the questionnaireModel will populate some more Question fields
	static boolean parseASCFileAndPopulateQuestionnaireModel(File file){
		boolean parseSuccess = Parser.parse(file);
		boolean finalizeSuccess = QuestionnaireModel.secondPassParsing();
		return parseSuccess && finalizeSuccess;
	}
	
	static ArrayList<Question> getAllQuestions() {
		return QuestionnaireModel.getAllQuestions();
	}
	
	static ArrayList<Table> getTables(){
		return EFileModel.getTables();
	}
	
	static void addQuestion(String variable, int codeWidth, String label, int quePosition, String shortLabel, String skipCondition, String ifDestination, String elseDestination, ArrayList<String[]> choices){
		QuestionnaireModel.addQuestion(variable, codeWidth, label, quePosition, shortLabel, skipCondition, ifDestination, elseDestination, choices);
	}
	
	//Initializes EFileModel, questionMap and tableMap
	static void populateEFile(){
		ArrayList<Question> regQuestions = QuestionnaireModel.getRegQuestions();
		ArrayList<Question> demoQuestions = QuestionnaireModel.getDemoQuestions();
		
		Map<String, Integer> positionsOfAllQuestions = calcPositions(regQuestions, demoQuestions);
		
		initializeEFileModel(regQuestions, demoQuestions, positionsOfAllQuestions);
		
		EFileModel.formatAsIVR();
	}
	
	static boolean tableExists(Question q){
		return questionAndTableBiMap.containsKey(q);
	}
	
	static void dataQuestionChange(Question q, GUIContentPanel.QuestionContentPanel.Column column, int choice, String change){
		switch(column){
			case CODE:
				QuestionnaireModel.changeCodeOfQuestion(q, choice, change);
				break;
			case LABEL:
				QuestionnaireModel.changeLabelOfQuestion(q, choice, change);
				break;
			case SKIP_DESTINATION:
				QuestionnaireModel.changeSkipDestinationOfQuestion(q, choice, change);
		}
	}
	
	static void dataTableChange(Table table, GUIContentPanel.TableContentPanel.Column column, int row, String change){
		switch(column){
			case LABEL:
				EFileModel.changeRowLabel(table, row, change);
				break;
			case POSITION:
				EFileModel.changeRowPosition(table, row, change);
				break;
			case EXTRAS:
				EFileModel.changeRowExtras(table, row, change);
		}
	}
	
	static Table getTableFromQuestionIdentifier(String identifier){
		Question q = QuestionnaireModel.getDemoQuestionFromIdentifier(identifier);
		return questionAndTableBiMap.get(q);
	}
	
	static void write(File ascFile){
		Writer.writeFile(ascFile, EFileModel.getTables());
	}
	
	private static Map<String, Integer> calcPositions(ArrayList<Question> regQuestions, ArrayList<Question> demoQuestions){
		Map<String, Integer> positionsOfQuestions = new HashMap<>();
		int pos = EFileModel.START_POS;
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
	
	private static ArrayList<Question> filterOutBadRegQuestions(ArrayList<Question> questions){
		questions.removeIf(q -> q.label.isEmpty());								//filter questions with label
		questions.removeIf(q -> q.choices.isEmpty());							//remove questions with no choices
		questions.removeIf(q -> q.variable.charAt(0) == 'R');					//hopefully only removes recruit questions
		questions.removeIf(q -> unwantedVariables.contains(q.variable));		//remove unwanted variables
		
		return questions;
	}
	
	private static ArrayList<Question> filterOutBadDemoQuestions(ArrayList<Question> questions){
		ArrayList<Question> r;
		
		r = questions.stream()
		.filter(q -> !q.label.isEmpty())										//filter questions with label
		.collect(Collectors.toCollection(ArrayList::new));

		return r;
	}
	
	private static void initializeEFileModel(ArrayList<Question> regQuestions, ArrayList<Question> demoQuestions, Map<String, Integer> positionsOfAllQuestions){
		Predicate<String> containsHearAgain = label -> {
			String l = label.toLowerCase();
			return (l.contains("hear") && l.contains("again")) || (l.contains("repeat") && l.contains("answers"));
		};
		
		int number = 1;
		ArrayList<Question> filteredRegQuestions = filterOutBadRegQuestions(regQuestions);
		for(Question frq : filteredRegQuestions){
			frq.choices.removeIf(choice -> containsHearAgain.test(choice[1]));
			
			int numOfChoices = frq.choices.size();
			String[] labels = new String[numOfChoices];
			int[] positions = new int[numOfChoices];
			String[] codes = new String[numOfChoices];
			int position = positionsOfAllQuestions.get(frq.variable);
			
			EFileModel.initRowArrays(frq.choices, numOfChoices, position, labels, positions, codes);
			String excelWorksheetTitle = EFileModel.getExcelWorksheetTitle(frq.label, frq.variable);
			Table t = EFileModel.addTable(number++, frq.label, excelWorksheetTitle, labels, positions, codes);
			questionAndTableBiMap.put(frq, t);
		}
		
		
		ArrayList<Question> filteredDemoQuestions = filterOutBadDemoQuestions(demoQuestions);
		for(Question fdq : filteredDemoQuestions){
			fdq.choices.removeIf(choice -> containsHearAgain.test(choice[1]));
			
			int numOfChoices = fdq.choices.size();
			String[] labels = new String[numOfChoices];
			int[] positions = new int[numOfChoices];
			String[] codes = new String[numOfChoices];
			int position = positionsOfAllQuestions.get(fdq.variable);
			
			EFileModel.initRowArrays(fdq.choices, numOfChoices, position, labels, positions, codes);
			
			
			Table t = EFileModel.addTable(number++, fdq.label, fdq.variable, labels, positions, codes);
			questionAndTableBiMap.put(fdq, t);
		}
	}
}
