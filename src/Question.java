import java.util.ArrayList;

class Question{
	public String variable;
	private int codeWidth;
	public String label;
	public String identifier;
	public String position;
	private String shortLabel;
	private String skipCondition;
	private int ifSkip;
	private int elseSkip;
	private ArrayList<String[]> choices = new ArrayList<>();//[0]=code; [1]=label; [2]=skipDestination
	
}
