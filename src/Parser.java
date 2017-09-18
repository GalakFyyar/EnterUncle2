import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;

class Parser{
	private static String errString = "";
	
	static String getErrString(){
		return errString;
	}
	
	static boolean parse(String filePath){
		Scanner sc;
		try{
			sc = new Scanner(new File(filePath), "UTF-8");
		}catch(Exception e){
			errString = "Can't open .ASC file\n" + e.getMessage();
			return false;
		}
		
		String variable = "";
		String label = "";
		String shortLabel = "";
		String skipDestination = "";
		String skipCondition = "";
		ArrayList<String> choices = new ArrayList<>();
		sc.nextLine();
		sc.nextLine();
		sc.nextLine();
		sc.nextLine();
		sc.nextLine();
		String line = sc.nextLine();
		while(true){
			if(line.startsWith("---")){				//Reached end, stop parsing
				break;
			}
			
			//This is the order these commands appear in .ASC files
			if(line.startsWith("*ME")){			//Message to the Interviewer Found"
				sc.nextLine();//do nothing
				line = sc.nextLine();
			}
			if(line.startsWith("*LL")){			//Long Label Found
				variable = line;
				StringBuilder labelBuilder = new StringBuilder(sc.nextLine());
				while(!labelBuilder.toString().endsWith("]")){		//if multiple lines
					line = sc.nextLine();
					
					if(line.length() < 8)
						labelBuilder.append(line);
					else
						labelBuilder.append("\n").append(line);
				}
				label = labelBuilder.toString();
				line = sc.nextLine();
			}
			if(line.startsWith("*SL")){			//Short Label Found
				shortLabel = sc.nextLine();
				line = sc.nextLine();
			}
			if(line.startsWith("*MA")){			//Mask Found
				sc.nextLine();//do nothing
				line = sc.nextLine();
			}
			if(line.startsWith("*SK")){			//Skip Found
				skipDestination = sc.nextLine();
				skipCondition = sc.nextLine();
				
				line = sc.nextLine();
			}
			if(line.startsWith("*CL")){			//Code List Found
				String choice = sc.nextLine();
				do{
					choices.add(choice);
					choice = sc.nextLine();
				}while(!choice.startsWith("---"));
				line = sc.nextLine();
			}
			Controller.addQuestion(variable, label, shortLabel, skipDestination, skipCondition, choices);
		}
		
		sc.close();
		
		
		return true;
	}
}
