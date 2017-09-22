import java.util.ArrayList;

class Question{
	String variable;
	int codeWidth;
	String label;
	String identifier;
	String position;	//Position in TXT file
	int quePosition;	//Position in ASC file, maybe unnecessary, if no questions are removed, then this value will be the same as this object's index in the question list
	String shortLabel;
	String skipCondition;
	String ifDestination = "";
	String elseDestination = "";
	int ifSkip;
	int elseSkip;
	ArrayList<String[]> choices = new ArrayList<>();//[0]=code; [1]=label; [2]=skipDestination
	
	Question(String variable, int codeWidth, String label, int quePosition, String shortLabel, String skipCondition, String ifDestination, String elseDestination, ArrayList<String[]> choices){
		this.variable = variable;
		this.codeWidth = codeWidth;
		this.label = label;
		this.quePosition = quePosition;
		this.shortLabel = shortLabel;
		this.skipCondition = skipCondition;
		this.ifDestination = ifDestination;
		this.elseDestination = elseDestination;
		this.choices = choices;
	}
}
