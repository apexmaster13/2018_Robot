package robot.commands;

import edu.wpi.first.wpilibj.command.CommandGroup;
import robot.Robot;
import robot.commands.drive.ArcCommand;
import robot.commands.drive.DriveDistanceCommand;
import robot.commands.drive.DriveTankCommand;
import robot.commands.drive.RotateToAngleCommand;
import robot.oi.AutoSelector;
import robot.oi.GameData;

/**
 *
 */
public class AutonomousCommand extends CommandGroup {

	public static final char LEFT 				= 'L';
	public static final char RIGHT 				= 'R';
	public static final char CENTER 			= 'C';
	public static final String ROBOT_LEFT   	= "Robot Left";
	public static final String ROBOT_CENTER 	= "Robot Center";
	public static final String ROBOT_RIGHT  	= "Robot Right";
	public static final String SWITCH 			= "Switch";
	public static final String SCALE 			= "Scale";
	public static final String CROSS  			= "Cross";
	public static final String NONE  			= "None";

	public AutonomousCommand() {
		//getting info
		String robotStartPosition 	= AutoSelector.getRobotStartPosition();
		String firstAction 			= AutoSelector.getRobotFirstAction();
		String secondAction 		= AutoSelector.getRobotSecondAction();
		char closeSwitch 			= GameData.getCloseSwitch();
		char scale 					= GameData.getScale();


		// Print out the user selection and Game config for debug later
		System.out.println("Auto Command Configuration");
		System.out.println("--------------------------");
		System.out.println("Robot Position : " + robotStartPosition);
		System.out.println("First Action   : " + firstAction);
		System.out.println("Second Action  : " + secondAction);
		System.out.println("Close Switch   : " + closeSwitch);
		System.out.println("Scale		   : " + scale);

		//overrides
		//System.out.println("Statring Overrides");

		if (robotStartPosition.equals(ROBOT_CENTER) && !firstAction.equals(SWITCH)) {
			firstAction = SWITCH;
			System.out.println("Center start must do switch as first action. Overriding first action to SWITCH");
		}
		if (robotStartPosition.equals(ROBOT_RIGHT) && firstAction.equals(SWITCH) && closeSwitch == LEFT) {
			firstAction = CROSS;
			System.out.println("Switch is not on our side.  Overriding first action to CROSS");
		}
		if (robotStartPosition.equals(ROBOT_LEFT) && firstAction.equals(SWITCH) && closeSwitch == RIGHT){
			firstAction = CROSS;
			System.out.println("Switch is not on our side. Overriding first action to CROSS");
		}

		//run the auto
		if (robotStartPosition.equals(ROBOT_LEFT)) {
			//robot starts to the left
			//first action
			if (firstAction.equals(SWITCH)) {
				//switch action is selected
				addSequential(new DriveDistanceCommand(160, 0, 1.0, 3.0, false));
				addSequential(new RotateToAngleCommand(90, 0.5));
			}

			else if (firstAction.equals(SCALE)) {
				//scale action is selected

				if (scale == LEFT){
					//scale is on the left side
					addSequential(new DriveDistanceCommand(320, 0, 1.0, 3.0, false));
				}
				else{
					System.out.println("scale is on the right side");
					addSequential(new DriveDistanceCommand(160, 0, 1.0, 3.0, false));
					addSequential(new ArcCommand(100, 0, 80, 1.0));
					addSequential(new DriveDistanceCommand(180, 80, 1.0, 5.0, false));
					addSequential(new ArcCommand(100, 80, 10, 1.0));
				}
			}

			else{
				//cross action is selected
				addSequential(new DriveDistanceCommand(160, 0, 1.0, 3.0, false));
			}

			//System.out.println("Starting second action");

			if (secondAction.equals(SWITCH)) {

				//System.out.println("Switch action selected");

				if (closeSwitch == LEFT){
					//System.out.println("Executing left switch command");
				}
				else{
					//System.out.println("Executing right switch command");	
				}
			}

			else if (secondAction.equals(SCALE)) {
				if (scale == LEFT){
					//System.out.println("Executing left scale command");
					addSequential(new DriveDistanceCommand(320, 0, 1.0, 3.0, false));
				}
				else{
					//System.out.println("Executing right scale command");	
					addSequential(new ArcCommand(100, 0, 80, 1.0));
					addSequential(new DriveDistanceCommand(180, 80, 1.0, 5.0, false));
					addSequential(new ArcCommand(100, 80, 10, 1.0));
				}
			}
			else{
				System.out.println("No second action");
			}
		}

		else if (robotStartPosition.equals(ROBOT_RIGHT)) {
			//robot starts to the right
			//first action
			if (firstAction.equals(SWITCH)) {
				//switch action is selected

				addSequential(new DriveDistanceCommand(160, 0, 1.0, 3.0, false));
				addSequential(new RotateToAngleCommand(270, 0.5));
			}

			else if (firstAction.equals(SCALE)) {
				//scale action is selected

				if (scale == RIGHT){
					//scale is on the right side
					addSequential(new DriveDistanceCommand(320, 0, 1.0, 3.0, false));
				}
				else{
					//scale is on the left side
					addSequential(new ArcCommand(100, 0, 280, 1.0));
					addSequential(new DriveDistanceCommand(180, 280, 1.0, 5.0, false));
					addSequential(new ArcCommand(100, 280, 350, 1.0));
				}
			}

			else{
				//cross action is selected
				addSequential(new DriveDistanceCommand(160, 0, 1.0, 3.0, false));
			}

			//System.out.println("Starting second action");

			if (secondAction.equals(SWITCH)) {

				//System.out.println("Switch action selected");

				if (closeSwitch == RIGHT){
					//System.out.println("Executing right switch command");
				}
				else{
					//System.out.println("Executing left switch command");	
				}
			}

			else if (secondAction.equals(SCALE)) {
				if (scale == RIGHT){
					//System.out.println("Executing left scale command");
				}
				else{
					//System.out.println("Executing right scale command");	
					addSequential(new ArcCommand(100, 0, 280, 1.0));
					addSequential(new DriveDistanceCommand(180, 280, 1.0, 5.0, false));
					addSequential(new ArcCommand(100, 280, 350, 1.0));
				}
			}
			else{
				//System.out.println("No second action");
			}

		}

		else{

			//System.out.println("Robot is in the center");
			//System.out.println("Starting first action");

			if (firstAction.equals(SWITCH)) {

				//System.out.println("Switch action selected");

				if (closeSwitch == LEFT){

					//System.out.println("Executing left switch command");
					//addSequential(new DriveTankCommand(50, 0.25, 1.0, 2, false));
					addSequential(new ArcCommand(100, 0, 310, 1.0));
					addSequential(new DriveDistanceCommand(20, 310, 1.0, 3.0, false));
					//addSequential(new DriveTankCommand(50, 1.0, 0.25, 2, true));
					addSequential(new ArcCommand(140, 310, 350, 1.0));
				}
				else{
					//System.out.println("Executing right switch command");	
					addSequential(new ArcCommand(80, 0, 45, 1.0));
					addSequential(new DriveDistanceCommand(25, 310, 1.0, 3.0, false));
					addSequential(new ArcCommand(120, 45, 0, 1.0));
				}
			}

			//System.out.println("Starting second action");

			if (secondAction.equals(SWITCH)) {

				//System.out.println("Switch action selected");

				if (closeSwitch == LEFT){
					//System.out.println("Executing left switch command");
				}
				else{
					//System.out.println("Executing right switch command");	
				}
			}

			else if (secondAction.equals(SCALE)) {
				if (scale == LEFT){
					//System.out.println("Executing left scale command");
					if (closeSwitch == LEFT) {
						addSequential(new DriveDistanceCommand(160, 0, 1.0, 3.0, false));
					}
					else{
						addSequential(new ArcCommand(100, 0, 280, 1.0));
						addSequential(new DriveDistanceCommand(180, 280, 1.0, 5.0, false));
						addSequential(new ArcCommand(100, 280, 350, 1.0));
					}
				}
				else{
					//System.out.println("Executing right scale command");	
					if (closeSwitch == RIGHT) {
						addSequential(new DriveDistanceCommand(160, 0, 1.0, 3.0, false));
					}
					else{
						addSequential(new ArcCommand(100, 0, 80, 1.0));
						addSequential(new DriveDistanceCommand(180, 80, 1.0, 5.0, false));
						addSequential(new ArcCommand(100, 80, 10, 1.0));
					}
				}
			}
			else{
				//System.out.println("No second action");
			}
		}	
	}
}
