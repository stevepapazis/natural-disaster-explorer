package app.gui;

import javax.swing.JFrame;

public class GuiStarter {

	public static void createGui() {
		//DesktopFrame rootFrame = new DesktopFrame();  
		JFrameLevel00RootFrame rootFrame = new JFrameLevel00RootFrame();
		rootFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		rootFrame.setSize(1200, 800); 
		rootFrame.setVisible(true);     
	}
}
