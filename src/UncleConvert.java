public class UncleConvert{
    public static void main(String[] args) {
        System.out.println("Hello World!");
        test();
        launchGUI();
    }
    
    private static void launchGUI(){
		javax.swing.SwingUtilities.invokeLater(() -> {
			new GUI();
		});
    }
    
    private static void test(){
    
    }
}