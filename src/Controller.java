import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

class Controller{
	private static final Map<Question, Table> questionMap = new HashMap<>();			//Question -> Table
	private static final Map<Table, Question> tableMap = new HashMap<>();				//Table -> Question
	
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
		
		Map<String, Integer> positionsOfAllQuestions = EFileModel.calcPositions(regQuestions, demoQuestions);
		
		int number = 1;
		ArrayList<Question> filteredRegQuestions = EFileModel.filterOutBadRegQuestions(regQuestions);
		for(Question frq : filteredRegQuestions){
			int numOfChoices = frq.choices.size();
			String[] labels = new String[numOfChoices];
			int[] positions = new int[numOfChoices];
			String[] codes = new String[numOfChoices];
			int position = positionsOfAllQuestions.get(frq.variable);
			
			EFileModel.initRowArrays(frq.choices, numOfChoices, position, labels, positions, codes);
			String excelWorksheetTitle = EFileModel.getExcelWorksheetTitle(frq.label, frq.variable);
			Table t = EFileModel.addTable(number++, frq.label, excelWorksheetTitle, labels, positions, codes);
			tableMap.put(t, frq);
		}
		
		
		ArrayList<Question> filteredDemoQuestions = EFileModel.filterOutBadDemoQuestions(demoQuestions);
		for(Question fdq : filteredDemoQuestions){
			int numOfChoices = fdq.choices.size();
			String[] labels = new String[numOfChoices];
			int[] positions = new int[numOfChoices];
			String[] codes = new String[numOfChoices];
			int position = positionsOfAllQuestions.get(fdq.variable);
			
			EFileModel.initRowArrays(fdq.choices, numOfChoices, position, labels, positions, codes);
			
			
			Table t = EFileModel.addTable(number++, fdq.label, fdq.variable, labels, positions, codes);
			tableMap.put(t, fdq);
		}
		
		//adds all entries in tableMap to questionMap, effectively making a BiMap
		for(Map.Entry<Table, Question> e : tableMap.entrySet()){
			questionMap.put(e.getValue(), e.getKey());
		}
		
		EFileModel.formatAsIVR();
	}
	
	static boolean tableExists(Question q){
		return questionMap.containsKey(q);
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
		return questionMap.get(q);
	}
	
	static void write(File ascFile){
		Writer.writeFile(ascFile, EFileModel.getTables());
	}
}
