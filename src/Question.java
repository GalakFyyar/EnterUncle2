import java.util.ArrayList;

class Question{
	String variable;
	int codeWidth;
	String label;
	String identifier;
	int quePosition;	//Position in ASC file, maybe unnecessary, if no questions are removed, then this value will be the same as this object's index in the question list
	String shortLabel;
	String skipCondition;
	String ifDestination;
	String elseDestination;
	int ifSkip;
	int elseSkip;
	ArrayList<String[]> choices;//[0]=code; [1]=label; [2]=skipDestination
	
	Question(String variable, int codeWidth, String label, String identifier, int quePosition, String shortLabel, String skipCondition, String ifDestination, String elseDestination, int ifSkip, int elseSkip, ArrayList<String[]> choices){
		this.variable = variable;
		this.codeWidth = codeWidth;
		this.label = label;
		this.identifier = identifier;
		this.quePosition = quePosition;
		this.shortLabel = shortLabel;
		this.skipCondition = skipCondition;
		this.ifDestination = ifDestination;
		this.elseDestination = elseDestination;
		this.ifSkip = ifSkip;
		this.elseSkip = elseSkip;
		this.choices = choices;
	}
	
	Question(String variable, int codeWidth, String label, int quePosition, String shortLabel, String skipCondition, String ifDestination, String elseDestination, ArrayList<String[]> choices){
		this(variable, codeWidth, label, "", quePosition, shortLabel, skipCondition, ifDestination, elseDestination, 0, 0, choices);
	}
	
	//These three changeChoice methods maybe able to be reduced to one.
	//If one takes the columnNumber int and just propagate it all the way to here, instead of having three different paths to take.
	void changeChoiceCode(int choice, String newCode){
		String[] oldChoice = choices.get(choice);
		String[] newChoice = {newCode, oldChoice[1], oldChoice[2]};
		choices.set(choice, newChoice);
	}
	
	void changeChoiceLabel(int choice, String newLabel){
		String[] oldChoice = choices.get(choice);
		String[] newChoice = {oldChoice[0], newLabel, oldChoice[2]};
		choices.set(choice, newChoice);
	}
	
	void changeChoiceSkipDestination(int choice, String newSkip){
		String[] oldChoice = choices.get(choice);
		String[] newChoice = {oldChoice[0], oldChoice[1], newSkip};
		choices.set(choice, newChoice);
	}
}
