package pacman;
import javax.swing.JFrame;

public class Pacman extends JFrame{

	public Pacman() {
		Model m = new Model();
		add(m);
	}
	
	
	public static void main(String[] args) {
		Pacman pac = new Pacman();
		pac.setVisible(true);
		pac.setTitle("Pacman");
		pac.setSize(400,500);
		pac.setDefaultCloseOperation(EXIT_ON_CLOSE);
		pac.setLocationRelativeTo(null);
		
	}

}