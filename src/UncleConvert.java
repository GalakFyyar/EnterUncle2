public class UncleConvert{
    public static void main(String[] args) {
        System.out.println("START");
        test();
        //launchGUI();
    }
    
    private static void launchGUI(){
		javax.swing.SwingUtilities.invokeLater(GUI::new);
    }
    
    private static void test(){
		String fileName = "G:\\FORUM\\FOLD\\FOLD.ASC";
		Controller.parseASCFileAndPopulate(fileName);
		System.out.println("END");
	}
}
