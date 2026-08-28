package UI;

import java.awt.Color;
import java.awt.Font;

/**
 * UIProperties
 */
public class UIProperties {

	private UIProperties() {
		throw new AssertionError("Utility class");
	}

	static String title = "Courier Management System";
	static int baseLocationX = 0;
	static int baseLocationY = 0;
	static int windowHeight = 800;
	static int windowWidth = 1200;
	static int sectionHeight = 30;
	static int sectionWidth = 50;

	static int elementHeight01 = 150;
	static int elementWidth01 = 300;
	static int elementHeight02 = 30;
	static int elementWidth02 = 70;
	static int elementHeight03 = 20;
	static int elementWidth03 = 60;

	static int elementSpacing01 = 40;
	static int elementSpacing02 = 20;
	static int elementSpacing03 = 10;
	static Color backgroundColor = new Color(79, 70, 229);
	static Color elementColor = new Color(233, 00, 00);
	static Color textColorLight = new Color(45, 00, 00);
	static Color textColorDark = new Color(45, 00, 00);
	
	static Font myFont = new Font("Segoe UI", Font.BOLD, 14);
}
