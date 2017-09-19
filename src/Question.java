import java.util.ArrayList;

class Question{
	String variable;
	int codeWidth;
	String label;
	String identifier;
	String position;
	int quePosition;
	String shortLabel;
	String skipCondition;
	String skipDestination;
	int ifSkip;
	int elseSkip;
	private ArrayList<String[]> choices = new ArrayList<>();//[0]=code; [1]=label; [2]=skipDestination
	
	public Question(){
		variable = "";
		codeWidth = -1;
		label = "";
		identifier = "";
		position = "";
		quePosition = -1;
		shortLabel = "";
		skipCondition = "";
		skipDestination = "";
		ifSkip = 0;
		elseSkip = 0;
	}
	
	public void addChoice(String code, String label){
		choices.add(new String[] {code, label});
	}
	
	public ArrayList<String[]> getChoices(){
		return choices;
	}
}
