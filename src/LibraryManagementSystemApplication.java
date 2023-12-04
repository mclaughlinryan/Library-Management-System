import javax.swing.JFrame;

public class LibraryManagementSystemApplication {
    public static void main (String[] args) {
        LibraryManagementSystem application = new LibraryManagementSystem();
        application.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        application.setSize(1000, 700);
        application.setVisible(true);
    }
}
