package robot.subsystems;

import com.torontocodingcollective.sensors.encoder.TEncoder;
import com.torontocodingcollective.sensors.gyro.TAnalogGyro;
import com.torontocodingcollective.sensors.gyro.TNavXGyro;
import com.torontocodingcollective.sensors.ultrasonic.TUltrasonicSensor;
import com.torontocodingcollective.speedcontroller.TCanSpeedController;
import com.torontocodingcollective.speedcontroller.TCanSpeedControllerType;
import com.torontocodingcollective.subsystem.TGryoDriveSubsystem;

import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import robot.RobotConst;
import robot.RobotMap;
import robot.commands.drive.DefaultChassisCommand;

/**
 *
 */
public class ChassisSubsystem extends TGryoDriveSubsystem {

	public TUltrasonicSensor ultrasonicSensor = 
			new TUltrasonicSensor(RobotMap.ULTRASONIC_SENSOR_ANALOG_PORT);

	private Solenoid shifter = new Solenoid(RobotMap.SHIFTER_PNEUMATIC_PORT);

	public boolean LOW_GEAR = false;
	public boolean HIGH_GEAR = true;

	private boolean turboEnabled = false;

	public ChassisSubsystem() {

		// Uncomment this block to use CAN based speed controllers
				// Uncomment this constructor to use PWM based Speed controllers
				super(	
						(RobotConst.robot == 1311 ? new TNavXGyro() : new TAnalogGyro(0)),
						//			new TAnalogGyro(0),
						new TCanSpeedController(
								TCanSpeedControllerType.TALON_SRX, 
								RobotMap.LEFT_DRIVE_MOTOR_CAN_ADDRESS,  
								RobotConst.LEFT_MOTOR_ORIENTATION,  
								RobotMap.LEFT_DRIVE_FOLLOWER_CAN_ADDRESS), 
						new TCanSpeedController(
								TCanSpeedControllerType.TALON_SRX, 
								RobotMap.RIGHT_DRIVE_MOTOR_CAN_ADDRESS, 
								RobotConst.RIGHT_MOTOR_ORIENTATION, 
								RobotMap.RIGHT_DRIVE_FOLLOWER_CAN_ADDRESS),
						RobotConst.DRIVE_GYRO_PID_KP,
						RobotConst.DRIVE_GYRO_PID_KI);

				// Get the encoders attached to the CAN bus speed controller.
				TEncoder leftEncoder  = ((TCanSpeedController) super.leftMotor) .getEncoder();
				TEncoder rightEncoder = ((TCanSpeedController) super.rightMotor).getEncoder();

				super.setEncoders(
						leftEncoder,  RobotConst.LEFT_ENCODER_ORIENTATION,
						rightEncoder, RobotConst.RIGHT_ENCODER_ORIENTATION,
						RobotConst.DRIVE_SPEED_PID_KP,
						RobotConst.MAX_LOW_GEAR_SPEED);

	}

	@Override
	public void init() {

		if (RobotConst.robot == 1321) {
			TAnalogGyro gyro = (TAnalogGyro) super.gyro;
			gyro.setSensitivity(0.0017);

			ultrasonicSensor.calibrate(RobotConst.ULTRASONIC_VOLTAGE_20IN, RobotConst.ULTRASONIC_VOLTAGE_40IN,
					RobotConst.ULTRASONIC_VOLTAGE_80IN);
		}
		disableTurbo();
	};

	// Initialize the default command for the Chassis subsystem.
	@Override
	public void initDefaultCommand() {
		setDefaultCommand(new DefaultChassisCommand());
	}

	// ********************************************************************************************************************
	// Turbo routines
	// ********************************************************************************************************************
	public void enableTurbo() {
		turboEnabled = true;
		setMaxEncoderSpeed(RobotConst.MAX_HIGH_GEAR_SPEED);
		shifter.set(HIGH_GEAR);
	}

	public void disableTurbo() {
		turboEnabled = false;
		setMaxEncoderSpeed(RobotConst.MAX_LOW_GEAR_SPEED);
		shifter.set(LOW_GEAR);
	}

	public boolean isTurboEnabled() {
		return turboEnabled;
	}

	// ********************************************************************************************************************
	// Update the SmartDashboard
	// ********************************************************************************************************************
	// Periodically update the dashboard and any PIDs or sensors
	@Override
	public void updatePeriodic() {
		

		super.updatePeriodic();

		SmartDashboard.putBoolean("Turbo Enabled", turboEnabled);
	}

}
