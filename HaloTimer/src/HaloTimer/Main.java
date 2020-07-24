package HaloTimer;
	
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import java.util.HashMap;


public class Main extends Application {
	
	private final String[] LEVEL_NAMES = {"Dawn", "Requiem", "Foreunner", "Infinity", "Reclaimer", "Shutdown", "Composer", "Midnight"};
	private final short NUM_LEVELS = 8;
	
	@Override
	public void start(Stage primaryStage) 
	{
		try 
		{
			//HashMaps used to store all TextField nodes so they can be accessed
			HashMap<String, TextField> minutesMap = new HashMap<>();
			HashMap<String, TextField> secondsMap = new HashMap<>();
			
			
			//Creates the initial containers to hold all of the nodes on the form
			GridPane grid = new GridPane();
			Scene scene = new Scene(grid, 400, 500);
			
			VBox inputs = new VBox();
			inputs.setSpacing(20);
			
			//Creates labels that title each column
			HBox titles = new HBox();
			titles.setSpacing(30);
			
			Label lblLevelNames = new Label("Levels");
			lblLevelNames.setPrefWidth(100);
			lblLevelNames.setFont(Font.font("Serif", 20));
			
			Label lblMinutes = new Label("Minutes");
			lblMinutes.setPrefWidth(100);
			lblMinutes.setFont(Font.font("Serif", 20));
			
			Label lblSeconds = new Label("Seconds");
			lblSeconds.setPrefWidth(100);
			lblSeconds.setFont(Font.font("Serif", 20));
			
			titles.getChildren().addAll(lblLevelNames, lblMinutes, lblSeconds);
			inputs.getChildren().add(titles);
			
			
			//Creates the rows for the users inputs
			for (int i = 0; i < NUM_LEVELS; i++)
			{
				HBox row = new HBox();
				row.setSpacing(30);
				
				//Creates labels for the level times
				Label lbl = new Label(LEVEL_NAMES[i]);
				lbl.setPrefWidth(100);
				lbl.setFont(Font.font("Serif", 20));
				
				//Creates textfields for the minutes
				TextField txtMin = new TextField();
				minutesMap.put(LEVEL_NAMES[i], txtMin);
				txtMin.setPrefWidth(100);
				txtMin.setAlignment(Pos.CENTER);
				
				//Creates textfields for the level seconds
				TextField txtSec = new TextField();
				secondsMap.put(LEVEL_NAMES[i],  txtSec);
				txtSec.setPrefWidth(100);
				txtSec.setAlignment(Pos.CENTER);

				row.getChildren().addAll(lbl, txtMin, txtSec);	
				inputs.getChildren().add(row);
			}
			
			
			//Creates Row where the final time is displayed
			HBox finalTimeRow = new HBox();
			finalTimeRow.setSpacing(30);
			
			Label finalPrompt = new Label("Final Time:");
			finalPrompt.setPrefWidth(100);
			finalPrompt.setFont(Font.font("Serif", 20));
			
			TextField finalTime = new TextField();
			finalTime.setEditable(false);
			finalTime.setPrefWidth(230);
			finalTime.setAlignment(Pos.CENTER);
			finalTime.setFont(Font.font("Serif", 16));
			//minutesMap.put("Final", finalTime);
			
			finalTimeRow.getChildren().addAll(finalPrompt, finalTime);
			inputs.getChildren().add(finalTimeRow);
			
			
			//Creates the row of buttons that calc the final time and clear the textfields
			HBox formButtons = new HBox();
			
			Button btnCalc = new Button("Calculate");
			btnCalc.setPrefWidth(200);
			btnCalc.setFont(Font.font("Serif", 20));
			
			Button btnClear = new Button("Clear");
			btnClear.setPrefWidth(200);
			btnClear.setFont(Font.font("Serif", 20));
			
			formButtons.getChildren().addAll(btnCalc, btnClear);
			inputs.getChildren().add(formButtons);		
			
			
			//Sets up ActionEvents on the calc and clear buttons
			btnCalc.setOnAction((event) ->{
				calculate(minutesMap, secondsMap, finalTime);
			});
			
			btnClear.setOnAction((event) ->{
				clear(minutesMap, secondsMap, finalTime);
			});
			
			
			//Setting constraints on all containers and adding nodes to the grid
			GridPane.setConstraints(inputs, 0, 0);
			grid.getChildren().addAll(inputs);
			
			
			//Display the form
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setTitle("Halo 4 IGT Calculator");
			primaryStage.setScene(scene);
			primaryStage.show();
		} 
		catch(Exception e) 
		{
			e.printStackTrace();
		}
	}
	
	
	/*
	 * Has the task of being the action for the calculate button. Checks if all of the inputs are valid and if so
	 * calls the add function to get the final time.
	 */
	private void calculate(HashMap<String, TextField> minMap, HashMap<String, TextField> secMap, TextField txtTime)
	{
		int[] minArr = new int[NUM_LEVELS];
		int[] secArr = new int[NUM_LEVELS];
		
		if (checkValidString(minMap) && checkValidString(secMap) && checkValidInteger(minMap, minArr) && checkValidInteger(secMap, secArr))
		{
			String finalRunTime = add(minArr, secArr);
			txtTime.setText(finalRunTime);
		}
		else
		{
			txtTime.setText("Invalid Inputs");
		}
	}
	
	
	/*
	 * Calls the method that adds the time together and is responsible for returning the string that is the 
	 * combination of all of the times added together. 
	 */
	private String add(int[] minArr, int[] secArr)
	{
		String finalRunTime;
		int hourCount = 0;
		int extraMinuteCount = 0;
		int totalMinutes = 0;
		int totalSeconds = 0;
		
		//Calculates the total number of seconds
		for (int i = 0; i < secArr.length; i++)
		{
			totalSeconds += secArr[i];
			
			if (totalSeconds >= 60)
			{
				totalSeconds = totalSeconds - 60;
				extraMinuteCount++;
			}
		}
		
		totalMinutes = extraMinuteCount;
		
		//Calculates the total number of minutes and hours
		for (int i = 0; i < minArr.length; i++)
		{			
			totalMinutes += minArr[i];
			
			if (totalMinutes >= 60)
			{
				totalMinutes = totalMinutes - 60;
				hourCount++;
			}
		}
		
		finalRunTime = String.valueOf(hourCount) + ":" + String.valueOf(totalMinutes) + ":" + String.valueOf(totalSeconds); 
		return finalRunTime;
	}
	
	
	/*
	 * Has the task of validating the string that is input by the user. If the ascii values are not of numerical symbols
	 * the function will return false.
	 */
	private boolean checkValidString(HashMap<String, TextField> map)
	{	
		for (String key : map.keySet())
		{	
			//Checks if the string is empty
			if (map.get(key).getText().isEmpty())
				return false;
			
			//Checks the ascii values to correspond to a numeric value
			for (int i = 0; i < map.get(key).getText().length(); i++)
			{	
				if (map.get(key).getText().charAt(i) < 48 || map.get(key).getText().charAt(i) > 57)
					return false;
			}
		}
		
		return true;
	}
	
	
	/*
	 * Method has the task of validating the integer that is input by the user. If the value is greater
	 * than 59 or less than 0, that is not a valid measure of time and the method will return false.
	 */
	private boolean checkValidInteger(HashMap<String, TextField> map, int[] timesArr)
	{
		int tmp;
		int i = 0;
		
		for (String key : map.keySet())
		{
			tmp = Integer.parseInt(map.get(key).getText());
			
			if (tmp < 0 || tmp > 59)
				return false;
			
			timesArr[i] = tmp;
			i++;
		}
		
		return true;
	}
	
	
	/*
	 * Completes the task of setting all of the text fields on the form to a blank state
	 */
	private void clear(HashMap<String, TextField> minMap, HashMap<String, TextField> secMap, TextField txtTime)
	{
		clear(minMap);
		clear(secMap);
		txtTime.setText("");
	}
	
	
	/*
	 * Helper function of the clear() that iterates the hashmaps that the controls are stored in
	 * and sets all of the textfields to enpty strings.
	 */
	private void clear(HashMap<String, TextField> map)
	{
		for (String key : map.keySet())
		{
			map.get(key).setText("");
		}	
	}
	
	
	public static void main(String[] args) 
	{
		launch(args);
	}
}
