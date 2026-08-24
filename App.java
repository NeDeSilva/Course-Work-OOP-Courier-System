import java.awt.*;
import javax.swing.*;

class App{
	public static void main(String[] args){
		System.out.println("hello");
			JFrame frame = new JFrame("CMS");
			frame.setSize(CoreUI.windowWidth, CoreUI.windowHeight);
			frame.setLocation(CoreUI.baseLocationX, CoreUI.baseLocationY);
			frame.setLayout(null);
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			frame.setVisible(true);
		}
}