
package org.usfirst.frc.team2194.robot;

import edu.wpi.cscore.UsbCamera;
import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.ControllerPower;
import edu.wpi.first.wpilibj.DigitalOutput;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.PIDSourceType;
import edu.wpi.first.wpilibj.PWM;
import edu.wpi.first.wpilibj.PWM.PeriodMultiplier;
import edu.wpi.first.wpilibj.PowerDistributionPanel;
import edu.wpi.first.wpilibj.SPI;
import edu.wpi.first.wpilibj.SerialPort;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj.networktables.NetworkTable;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.vision.VisionThread;

import org.usfirst.frc.team2194.robot.commands.ExampleCommand;
import org.usfirst.frc.team2194.robot.subsystems.DriveTrain;
import org.usfirst.frc.team2194.robot.subsystems.ExampleSubsystem;
import org.usfirst.frc.team2194.robot.subsystems.Gyro;

import com.kauailabs.navx.frc.AHRS;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class Robot extends IterativeRobot {

	public static final ExampleSubsystem exampleSubsystem = new ExampleSubsystem();
	public static OI oi;
	public static Gyro gyro;
	public static DriveTrain driveTrain;
	public static AHRS imu;
	private VisionThread hookVisionThread;
	UsbCamera gearHookCamera;
	public static DriveMode driveMode;
	public static DigitalOutput mainLEDControl;
	public static DigitalOutput leftLEDControl;
	public static DigitalOutput rightLEDControl;
	public static DigitalOutput leftBackLEDControl;
	public static DigitalOutput rightBackLEDControl;
	public static PowerDistributionPanel PDP;

	public static final int IMG_WIDTH = 320;
	public static final int IMG_HEIGHT = 240;
	public static NetworkTable dashboardTable;

	double i = 1;
	Command autonomousCommand;
	SendableChooser<Command> chooser = new SendableChooser<>();
	
	public enum DriveMode {
		NONE("none"),
		MECANUMGYRO("mecanumGyro"),
		TANK("tank"),
		ARCADE("arcade"),
		GYRO("gyro"),
		VISION("vision");
		private String driveName;
		private DriveMode(String name) { 
            this.driveName = name; 
        } 
        
        @Override 
        public String toString(){ 
            return driveName; 
        } 
	}
	
	/**
	 * This function is run when the robot is first started up and should be
	 * used for any initialization code.
	 */
	@Override
	public void robotInit() {
		RobotMap.init();
		driveMode = DriveMode.NONE;
		oi = new OI();
		gyro = new Gyro();
		driveTrain = new DriveTrain();
		PDP = new PowerDistributionPanel();
		mainLEDControl = new DigitalOutput(0);
		leftLEDControl = new DigitalOutput(1);
		rightLEDControl = new DigitalOutput(2);
		leftBackLEDControl = new DigitalOutput(3);
		rightBackLEDControl = new DigitalOutput(4);
		
		mainLEDControl.setPWMRate(2000);
		leftLEDControl.setPWMRate(2000);
		rightLEDControl.setPWMRate(2000);
		leftBackLEDControl.setPWMRate(2000);
		rightBackLEDControl.setPWMRate(2000);
		
		mainLEDControl.enablePWM(((float) 10)/100);
		leftLEDControl.enablePWM(0);
		rightLEDControl.enablePWM(0);
		leftBackLEDControl.enablePWM(0);
		rightBackLEDControl.enablePWM(0);

		chooser.addDefault("Default Auto", new ExampleCommand());
		chooser.addObject("Default Auto1", new ExampleCommand());
		chooser.addObject("Default Auto2", new ExampleCommand());
		chooser.addObject("Default Auto3", new ExampleCommand());
		chooser.addObject("Default Auto4", new ExampleCommand());
		chooser.addObject("Default Auto5", new ExampleCommand());
		// chooser.addObject("My Auto", new MyAutoCommand());
		SmartDashboard.putData("Auto mode", chooser);

		gearHookCamera = CameraServer.getInstance().startAutomaticCapture(0);
		gearHookCamera.setResolution(IMG_WIDTH, IMG_HEIGHT);

		// serial = new SerialPort(9600, SerialPort.Port.kUSB1);

		try {
			imu = new AHRS(SPI.Port.kMXP);
			// imu = new AHRS(SerialPort.Port.kUSB1);
			imu.setPIDSourceType(PIDSourceType.kDisplacement);

		} catch (Exception ex) {
			DriverStation.reportError("Error instantiating navX-MXP:  " + ex.getMessage(), true);
		}
		if (imu != null) {
			LiveWindow.addSensor("IMU", "Gyro", imu);
		}
		Timer.delay(3);

		imu.reset();
		
		dashboardTable = NetworkTable.getTable("data");

		// hookVisionThread = new VisionThread(gearHookCamera, new
		// GripPipeline(), hookpipeline);
	}

	/**
	 * This function is called once each time the robot enters Disabled mode.
	 * You can use it to reset any subsystem information you want to clear when
	 * the robot is disabled.
	 */
	@Override
	public void disabledInit() {
		// serial.writeString("hi");
//		test.updateDutyCycle(((float) 10)/255);
	}

	@Override
	public void disabledPeriodic() {
		Scheduler.getInstance().run();
		updateStatus();
	}

	/**
	 * This autonomous (along with the chooser code above) shows how to select
	 * between different autonomous modes using the dashboard. The sendable
	 * chooser code works with the Java SmartDashboard. If you prefer the
	 * LabVIEW Dashboard, remove all of the chooser code and uncomment the
	 * getString code to get the auto name from the text box below the Gyro
	 *
	 * You can add additional auto modes by adding additional commands to the
	 * chooser code above (like the commented example) or additional comparisons
	 * to the switch structure below with additional strings & commands.
	 */
	@Override
	public void autonomousInit() {
		autonomousCommand = chooser.getSelected();
//		mainLEDControl.updateDutyCycle(((float) 30)/255);
		/*
		 * String autoSelected = SmartDashboard.getString("Auto Selector",
		 * "Default"); switch(autoSelected) { case "My Auto": autonomousCommand
		 * = new MyAutoCommand(); break; case "Default Auto": default:
		 * autonomousCommand = new ExampleCommand(); break; }
		 */

		// schedule the autonomous command (example)
		if (autonomousCommand != null)
			autonomousCommand.start();
	}

	/**
	 * This function is called periodically during autonomous
	 */
	@Override
	public void autonomousPeriodic() {
		Scheduler.getInstance().run();
		updateStatus();
	}

	@Override
	public void teleopInit() {
//		mainLEDControl.updateDutyCycle(((float) 20)/255);
		// This makes sure that the autonomous stops running when
		// teleop starts running. If you want the autonomous to
		// continue until interrupted by another command, remove
		// this line or comment it out.
		if (autonomousCommand != null)
			autonomousCommand.cancel();
	}

	/**
	 * This function is called periodically during operator control
	 */
	@Override
	public void teleopPeriodic() {
		
//		
//		i = i-0.01;
//		
//		if(i<0)
//		{
//			i=1;
//		}
		Scheduler.getInstance().run();
		updateStatus();
	}

	/**
	 * This function is called periodically during test mode
	 */
	@Override
	public void testPeriodic() {
//		mainLEDControl.updateDutyCycle(((float) 40)/255);
		LiveWindow.run();
	}

	public void updateStatus() {
		SmartDashboard.putString("Drive Mode", driveMode.toString());
//		SmartDashboard.putNumber("Lencoder", RobotMap.leftEncoder.getDistance());
//		SmartDashboard.putNumber("Rencoder", RobotMap.rightEncoder.getDistance());

		gyro.updateStatus();
		driveTrain.updateStatus();
		
		
		
		dashboardTable.putNumber("sensors/gyro/angle", gyro.getGyroAngle());
		
		dashboardTable.putNumber("power/motor/Front/Left", PDP.getCurrent(3));
		dashboardTable.putNumber("power/motor/Back/Left", PDP.getCurrent(0));
		dashboardTable.putNumber("power/motor/Front/Right", PDP.getCurrent(2));
		dashboardTable.putNumber("power/motor/Back/Right", PDP.getCurrent(1));
		dashboardTable.putNumber("power/rio", ControllerPower.getInputCurrent());
		dashboardTable.putNumber("power/leds", PDP.getCurrent(4));
		
		
        dashboardTable.putNumber("motors/motor/Front/Left", RobotMap.driveLeftFrontMotor.get());
        dashboardTable.putNumber("motors/motor/Back/Left", RobotMap.driveLeftBackMotor.get());
        dashboardTable.putNumber("motors/motor/Front/Right", RobotMap.driveRightFrontMotor.get());
        dashboardTable.putNumber("motors/motor/Back/Right", RobotMap.driveRightBackMotor.get());
        
        dashboardTable.putNumber("match/time", Timer.getMatchTime());
        dashboardTable.putNumber("info/gyro/x", imu.getDisplacementX());
        dashboardTable.putNumber("info/gyro/y", imu.getDisplacementY());
	}
}
