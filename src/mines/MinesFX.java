package mines;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

import javafx.scene.paint.Color;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class MinesFX extends Application {
	public Mines mines;

	@Override
	public void start(Stage stage) {
		try {
			HBox root = new HBox();
			Image im;
			// get the path of the image i put it in the file of the exersize
			FileInputStream inputstream1 = new FileInputStream(
					"C:\\Users\\Alaa Jbareen\\eclipse-workspace\\ex5\\src\\mines\\code.PNG");
			im = new Image(inputstream1);// get the image
			BackgroundImage bgim = new BackgroundImage(im, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT,
					BackgroundPosition.CENTER, BackgroundSize.DEFAULT);
			Background bg = new Background(bgim);
			stage.setTitle("The Amazing Mines Sweeper");
			// add the game
			root.getChildren().add(createGame());
			// set the maximum size
			root.setMinSize(700, 600);
			root.setMaxSize(700, 600);
			// put the image to be background
			root.setBackground(bg);
			Scene scene = new Scene(root);
			stage.setScene(scene);
			stage.sizeToScene();
			stage.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public HBox createGame() throws FileNotFoundException {
		HBox root = new HBox();// this box will have the vbox and the grid
		int[] j = new int[3];
		VBox vBox = new VBox();// vbox have the reset and the textFields
		Label[] label = new Label[3];
		TextField[] textField = new TextField[3];
		HBox[] hBox = new HBox[3];
		Button b = new Button();
		GridPane grid = new GridPane();// grid will have the buttons of the game
		Label win = new Label();
		grid.setHgap(3);
		grid.setVgap(3);
		Image flag, mine1, mine2;
		// get the images
		FileInputStream is1 = new FileInputStream(
				"C:\\Users\\Alaa Jbareen\\eclipse-workspace\\ex5\\src\\mines\\mine.PNG");
		FileInputStream is2 = new FileInputStream(
				"C:\\Users\\Alaa Jbareen\\eclipse-workspace\\ex5\\src\\mines\\flag.PNG");
		FileInputStream is3 = new FileInputStream(
				"C:\\Users\\Alaa Jbareen\\eclipse-workspace\\ex5\\src\\mines\\mine2.PNG");
		// initialize of images
		flag = new Image(is2);
		mine1 = new Image(is1);
		mine2 = new Image(is3);
		b.setText("Reset");// write text on the button
		b.setStyle("-fx-base: lightpink");// color the background
		b.setPadding(new Insets(5, 35, 5, 35));// make position for the button in the vbox
		b.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				for (int i = 0; i < 3; i++)
					textField[i].clear();// reset the textField
				win.setText("");// delete the label win
				mines = new Mines(0, 0, 0);
				grid.getChildren().removeAll(grid.getChildren());// remove all the button s (children) of grid
			}
		});
		for (int i = 0; i < 3; i++) {
			label[i] = new Label();
			label[i].setTextFill(Color.PINK);// put color
			textField[i] = new TextField();
			hBox[i] = new HBox();
			textField[i].setMaxSize(60, 20);// define the size of the text bar
			hBox[i].setAlignment(Pos.CENTER_RIGHT);// put the items in the box in the center
		}

		// class to define a button plus the location of it in the grid according to the
		// game
		class makeButton implements EventHandler<ActionEvent> {
			Button1[][] sIcone;// matrix of buttons

			@Override
			public void handle(ActionEvent event) {
				j[2] = Integer.valueOf(textField[2].getText());// get the value in integer
				mines = new Mines(j[1], j[0], j[2]);// initialize the game
				sIcone = new Button1[j[1]][j[0]];// initialize buttons matrix with the height and the width of
				for (int i = 0; i < mines.getHeight(); i++)
					for (int j = 0; j < mines.getWidth(); j++) {
						Button1 b1 = new Button1(i, j);
						sIcone[i][j] = b1;// make object for every button
						b1.setText("?");// set start text on the button
						b1.setFont(new Font("Combaria", 25));
						b1.setTextFill(Color.STEELBLUE);// put color on the button
						b1.setStyle("-fx-base: lightblue");
						grid.add(b1, i, j);// add the button to the grid
						b1.setOnMousePressed(new EventHandler<MouseEvent>() {// when the button clicked by the mouse
							@Override
							public void handle(MouseEvent event) {
								MouseButton button = event.getButton();
								if (((MouseEvent) event).getButton().equals(MouseButton.SECONDARY)) {// check if it's a
																										// right click
									mines.toggleFlag(b1.x, b1.y);// add or remove the flag
									if (mines.get(b1.x, b1.y) == "F") {// if put flag show flag image
										b1.setGraphic(new ImageView(flag));
										b1.setText("");
									} else if (mines.get(b1.x, b1.y) == ".") {// if it was flag remove it
										b1.setGraphic(new ImageView());
										b1.setText("?");
									}
								} else if (button == MouseButton.PRIMARY) {// if it's a left click
									if (mines.getIsMine(b1.x, b1.y) == true && (mines.get(b1.x, b1.y) == ".")) {// if
																												// it's
																												// mine
																												// and
																												// it's
																												// closed
										b1.setGraphic(new ImageView(mine2));// view red mine image
										// run over all the gride and view all the places of the mines
										for (int e = 0; e < mines.getHeight(); e++)
											for (int c = 0; c < mines.getWidth(); c++)
												if (mines.getIsMine(sIcone[e][c].x, sIcone[e][c].y) == true
														&& mines.getIsOpen(sIcone[e][c].x, sIcone[e][c].y) == false) {// if
																														// there
																														// is
																														// mine
																														// and
																														// it's
																														// closed
													if (sIcone[e][c] != b1)// put the grey mine image just for the other
																			// buttons
														sIcone[e][c].getSource().setGraphic(new ImageView(mine1));
													sIcone[e][c].getSource().setText("");
												}
										win.setText("Lost the game!");// if he try to open a mine so he lose
										win.setTextFill(Color.GOLD);// put color to the label
										b1.setText("");// remove the text of the button
									} else {
										mines.open(b1.x, b1.y);// open this place
										// run over all the places that opened with this click
										for (int e = 0; e < mines.getHeight(); e++)
											for (int c = 0; c < mines.getWidth(); c++)
												if (mines.getIsOpen(sIcone[e][c].x, sIcone[e][c].y) == true) {
													// if it's open put the returned value of get as a text in the
													// button
													sIcone[e][c].getSource()
															.setText(mines.get(sIcone[e][c].x, sIcone[e][c].y));
													sIcone[e][c].getSource().setTextFill(Color.DARKRED);
												}
										if (mines.isDone()) {
											win.setText("You Won");// if all the places that not mine are opened he wim
																	// the game
											win.setTextFill(Color.GOLD);
										}
									}
								}
							}
						});// if the button is on action call the putIcon handle
						b1.setMinSize(50, 50);
						b1.setMaxSize(50, 50);
					}
				grid.setPadding(new Insets(20));
			}
		}
		// on action save the integer value of it
		textField[0].setOnAction(e -> {// get the entered text
			j[0] = Integer.valueOf(textField[0].getText());
		});
		// on action save the integer value of it
		textField[1].setOnAction(e -> {// get the entered text
			j[1] = Integer.valueOf(textField[1].getText());
		});
		textField[2].setOnAction(new makeButton());// the last value entered then make the grid
		// initialization of the labels and hBoxes
		label[0].setText("width");
		hBox[0].getChildren().addAll(label[0], textField[0]);
		hBox[0].setSpacing(2);
		label[1].setText("height");
		hBox[1].getChildren().addAll(label[1], textField[1]);
		hBox[1].setSpacing(2);
		label[2].setText("mines");
		hBox[2].getChildren().addAll(label[2], textField[2]);
		hBox[2].setSpacing(2);
		win.setFont(new Font("Combaria", 15));
		vBox.getChildren().addAll(b, hBox[0], hBox[1], hBox[2], win);
		vBox.setSpacing(5);

		vBox.setPadding(new Insets(10));
		// put the input and the grid in the center
		vBox.setAlignment(Pos.CENTER);
		grid.setAlignment(Pos.CENTER);

		root.getChildren().addAll(vBox, grid);
		return root;
	}

	public static void main(String[] args) {
		launch(args);
	}
}
