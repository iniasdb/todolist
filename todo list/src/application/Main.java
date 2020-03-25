package application;
	
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;


public class Main extends Application {
	
	static ArrayList<String> tasks = new ArrayList<String>();
	static File file = new File("C:\\todolist/db.txt");
	
	@Override
	public void start(Stage primaryStage) throws IOException, FileNotFoundException {
		PrintWriter pw = new PrintWriter(file);
		
		primaryStage.setTitle("To-do list");
		
		try {
			
			primaryStage.getIcons().add(new Image(getClass().getResourceAsStream( "todo.png" ))); 
			
		} catch (Exception e) {
			System.out.println(e);
		}
		
		GridPane grid = new GridPane();
		grid.setAlignment(Pos.CENTER);
		grid.setHgap(10);
		grid.setVgap(10);
		grid.setPadding(new Insets(25, 25, 25, 25));
		
		Scene scene = new Scene(grid, 400, 300);
		primaryStage.setResizable(false);
		
		Text text = new Text("To-do list");
		text.setFont(Font.font("Tahoma", FontWeight.NORMAL, 30));
		grid.add(text, 0, 0);
		
		// shows if task is added to list or if field is empty
		Label task = new Label("Task");
		grid.add(task, 0, 1);
		
		TextField taskField = new TextField();
		grid.add(taskField, 1, 1);
		
		Button view = new Button("view");
		Button add = new Button("add");
		HBox hbBtn = new HBox(10);
		hbBtn.setAlignment(Pos.CENTER);
		hbBtn.getChildren().add(view);
		hbBtn.getChildren().add(add);
		grid.add(hbBtn, 1, 2);
		
		Text status = new Text();
		grid.add(status, 1, 3);
		
		//second stage (view tasks)
		
		Stage viewTasks = new Stage();
		
		viewTasks.setTitle("tasks");
		GridPane grid2 = new GridPane();
		grid2.setAlignment(Pos.CENTER);
		grid2.setHgap(10);
		grid2.setVgap(10);
		grid2.setPadding(new Insets(25, 25, 25, 25));
		
		Scene scene2 = new Scene(grid2, 400, 450);
		viewTasks.setResizable(false);
		
		Button done = new Button("Done");
		Button notDone = new Button("Not done");
		Button remove = new Button("Remove");
		Button back	= new Button("Back");
		done.setMinSize(100, 25);
		notDone.setMinSize(100, 25);
		remove.setMinSize(100, 20);
		back.setMinSize(100, 25);
		VBox vbBtn = new VBox(10);
		vbBtn.setAlignment(Pos.CENTER);
		vbBtn.autosize();
		vbBtn.getChildren().add(done);
		vbBtn.getChildren().add(notDone);
		vbBtn.getChildren().add(remove);
		vbBtn.getChildren().add(back);
		grid2.add(vbBtn, 1, 0);
		
		scene.setOnKeyPressed(ke -> {
			KeyCode key = ke.getCode();
			
			if (key.equals(KeyCode.ENTER) && primaryStage.isShowing()) {
				add.fire();
				ke.consume();
			} else if (key.equals(KeyCode.V) && primaryStage.isShowing()) {
				view.fire();
				ke.consume();
			}
		});
		
		add.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent e) {
				String task = taskField.getText();
				
				// checks if field is empty
				
				if (task.isEmpty()) {
					status.setText("Niets ingevuld");
					status.setFill(Color.RED);
				} else {
					taskField.clear();
					tasks.add(task);
					status.setText("toegevoegd");
					status.setFill(Color.GREEN);
				}			
			}
		});
		
		view.setOnAction(new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent e) {
				primaryStage.hide();
				viewTasks.show();
				status.setText(null);				// resets status text
				
				ListView<String> list = new ListView<String>();
				list.setMinSize(250, 275);
								
				for (int i = 0; i < tasks.size(); i++) {
					list.getItems().add(tasks.get(i));				//adds tasks from array list to list view
				}
				
				//event listener list
				
				list.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() { 
					
				    @Override
				    public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				        int selectedId = list.getSelectionModel().getSelectedIndex();		//stores the id of the selected item
				        
						done.setOnAction(new EventHandler<ActionEvent>() {

							@Override
							public void handle(ActionEvent e) {		
								
								try {
									String task = list.getItems().get(selectedId);
									
									if (task.length() < 8 || !task.substring(0, 8).equals("KLAAR:  ")) {		// adds tag
										list.getItems().set(selectedId, "KLAAR:  " + task);
										tasks.set(selectedId, "KLAAR:  " + task);
									}
									
								} catch (Exception ex) {
									
								}
								
							}
							
						});
						
						notDone.setOnAction(new EventHandler<ActionEvent>() {
							
							@Override
							public void handle(ActionEvent event) {
								try {									
									tasks.set(selectedId, list.getItems().get(selectedId).substring(8, list.getItems().get(selectedId).length()));		//removes tag
									list.getItems().set(selectedId, list.getItems().get(selectedId).substring(8, list.getItems().get(selectedId).length()));
								} catch (Exception e) {
									
								}
							}
						});
						
						remove.setOnAction(new EventHandler<ActionEvent>() {
							
							@Override
							public void handle(ActionEvent event) {
								try {
									tasks.remove(selectedId);
									list.getItems().remove(selectedId);
								}catch (Exception e) {
									
								}
							}
						});
				    }
				});
				
		        HBox hbox = new HBox(list);  //places list view inside h box
		        grid2.add(hbox, 0, 0);
			}
		});
		
		back.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent e) {
				viewTasks.hide();
				primaryStage.show();
			}
			
		});
		
		primaryStage.setOnCloseRequest(event -> {
			
			//writes tasks to db.txt file
			
			for (int i = 0; i < tasks.size(); i++) {
				pw.println(tasks.get(i));
			}
			
			pw.close();
		});
		
		
		viewTasks.setOnCloseRequest(event -> {
			
			//opens main window
			
			primaryStage.show();
		});

		viewTasks.setScene(scene2);
		
		primaryStage.setScene(scene);
		primaryStage.show();
		
	}
	
	public static void leesDb() throws FileNotFoundException {
		Scanner sc = new Scanner(file);
		
		while (sc.hasNext()) {
			tasks.add(sc.nextLine());
		}
		
		sc.close();
	}
	
	public static void main(String[] args) throws FileNotFoundException {
		leesDb();
		launch(args);
	}
}
