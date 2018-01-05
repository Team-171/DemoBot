package org.usfirst.frc.team2194.robot;

import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.PIDSourceType;
import edu.wpi.first.wpilibj.PowerDistributionPanel;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.TalonSRX;

/**
 * The RobotMap is a mapping from the ports sensors and actuators are wired into
 * to a variable name. This provides flexibility changing wiring, makes checking
 * the wiring easier and significantly reduces the number of magic numbers
 * floating around.
 */
public class RobotMap {
	// For example to map the left and right motors, you could define the
	// following variables to use with your drivetrain subsystem.
	// public static int leftMotor = 1;
	// public static int rightMotor = 2;

	// If you are using multiple modules, make sure to define both the port
	// number and the module. For example you with a rangefinder:
	// public static int rangefinderPort = 1;
	// public static int rangefinderModule = 1;

	public static TalonSRX driveLeftFrontMotor;
	public static TalonSRX driveLeftBackMotor;
	public static TalonSRX driveRightFrontMotor;
	public static TalonSRX driveRightBackMotor;
	public static Encoder leftEncoder;
	public static Encoder rightEncoder;
	public static RobotDrive drive;
	public static PowerDistributionPanel pdp;
	
	public static void init(){
		driveLeftFrontMotor = new TalonSRX(3);		
		driveLeftBackMotor = new TalonSRX(0);
		
		driveRightFrontMotor = new TalonSRX(2);
		driveRightBackMotor = new TalonSRX(1);
		driveRightFrontMotor.setInverted(true);
		driveRightBackMotor.setInverted(true);
		
//		leftEncoder = new Encoder(2, 3);
//		leftEncoder.setDistancePerPulse(0.16362461737446839783659600954581);
//		leftEncoder.setPIDSourceType(PIDSourceType.kDisplacement);
//		leftEncoder.setSamplesToAverage(10);
//		leftEncoder.setReverseDirection(true);
//		leftEncoder.setMaxPeriod(.5);
		
//		rightEncoder = new Encoder(4, 5);
//		rightEncoder.setDistancePerPulse(0.07539822368615503772310344119871);
//		rightEncoder.setPIDSourceType(PIDSourceType.kDisplacement);
//		rightEncoder.setSamplesToAverage(10);
//		rightEncoder.setMaxPeriod(.5);
		
		drive = new RobotDrive(driveLeftFrontMotor, driveLeftBackMotor, driveRightFrontMotor, driveRightBackMotor);
		drive.setSafetyEnabled(false);

		pdp = new PowerDistributionPanel(1);
	}
}
