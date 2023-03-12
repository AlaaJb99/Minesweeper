package mines;

import javafx.scene.control.Button;
//class to define a button plus the location of it in the grid according to the game
public class Button1 extends Button {
	protected int x;
	protected int y;
	
	public Button1(int x, int y) {
		super();
		this.x=x;
		this.y=y;
	}
	
	public Button1 getSource() {
		return this;
	}
	
}
