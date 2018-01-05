package org.usfirst.frc.team2194.robot;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.buttons.Button;
import edu.wpi.first.wpilibj.buttons.JoystickButton;

import org.usfirst.frc.team2194.robot.Gyro.GyroDriveMode;
import org.usfirst.frc.team2194.robot.RobotMotion.NoDriveMode;
import org.usfirst.frc.team2194.robot.commands.ArcadeDrive;
import org.usfirst.frc.team2194.robot.commands.ResetEncoders;
import org.usfirst.frc.team2194.robot.commands.TankDrive;

/**
 * This class is the glue that binds the controls on the physical operator
 * interface to the commands and command groups that allow control of the robot.
 */
public class OI {

	public Joystick joystick1;
	public Joystick joystick2;
	public Joystick gamepad;
	public static JoystickButton gyroDriveMode;
	public static JoystickButton noDriveMode;
	public static JoystickButton resetEncoders;
	public static JoystickButton tankDriveMode;
	public static JoystickButton arcadeDriveMode;
	
	public OI()
	{
//		joystick1 = new Joystick(2);
//		joystick2 = new Joystick(1);
		
		gamepad = new Joystick(0);
		
//		resetEncoders = new JoystickButton(joystick1, 3);
//		resetEncoders.whenPressed(new ResetEncoders());
//
		gyroDriveMode = new JoystickButton(gamepad, 3);
		gyroDriveMode.whenPressed(new GyroDriveMode());
		
		gyroDriveMode = new JoystickButton(gamepad, 2);
		gyroDriveMode.whenPressed(new NoDriveMode());
		
		tankDriveMode = new JoystickButton(gamepad, 4);
		tankDriveMode.whenPressed(new TankDrive());
		
		arcadeDriveMode = new JoystickButton(gamepad, 5);
		arcadeDriveMode.whenPressed(new ArcadeDrive());
	}
	//// CREATING BUTTONS
	// One type of button is a joystick button which is any button on a
	//// joystick.
	// You create one by telling it which joystick it's on and which button
	// number it is.
	// Joystick stick = new Joystick(port);
	// Button button = new JoystickButton(stick, buttonNumber);

	// There are a few additional built in buttons you can use. Additionally,
	// by subclassing Button you can create custom triggers and bind those to
	// commands the same as any other Button.

	//// TRIGGERING COMMANDS WITH BUTTONS
	// Once you have a button, it's trivial to bind it to a button in one of
	// three ways:

	// Start the command when the button is pressed and let it run the command
	// until it is finished as determined by it's isFinished method.
	// button.whenPressed(new ExampleCommand());

	// Run the command while the button is being held down and interrupt it once
	// the button is released.
	// button.whileHeld(new ExampleCommand());

	// Start the command when the button is released and let it run the command
	// until it is finished as determined by it's isFinished method.
	// button.whenReleased(new ExampleCommand());
}
