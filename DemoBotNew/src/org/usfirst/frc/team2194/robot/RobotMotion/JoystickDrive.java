package org.usfirst.frc.team2194.robot.RobotMotion;

import org.usfirst.frc.team2194.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class JoystickDrive extends Command {

	public JoystickDrive() {
		// Use requires() here to declare subsystem dependencies
		// eg. requires(chassis);
		requires(Robot.driveTrain);
	}

	// Called just before this Command runs the first time
	protected void initialize() {
	}

	// Called repeatedly when this Command is scheduled to run
	protected void execute() {
		double deadBand = 0.35;

		switch (Robot.driveMode) {
		case NONE:
			Robot.driveTrain.driveRobotMecanum(getOutput(deadBand, 1, -Robot.oi.gamepad.getX()), getOutput(deadBand, 1, -Robot.oi.gamepad.getY()), 
					getOutput(deadBand, 1, -Robot.oi.gamepad.getRawAxis(4)));
			break;
			
		case MECANUMGYRO:
			Robot.driveTrain.driveRobotMecanumGyro(getOutput(deadBand, 1, -Robot.oi.gamepad.getX()), getOutput(deadBand, 1, -Robot.oi.gamepad.getY()), 
					getOutput(deadBand, 1, -Robot.oi.gamepad.getRawAxis(4)));
			break;
			
		case TANK:
			Robot.driveTrain.driveRobotTank(getOutput(deadBand, 1, -Robot.oi.gamepad.getY()), getOutput(deadBand, 1, -Robot.oi.gamepad.getRawAxis(5)));
			break;
			
		case ARCADE:
			Robot.driveTrain.driveRobotArcade(getOutput(deadBand, 1, Robot.oi.gamepad.getY()), getOutput(deadBand, 1, -Robot.oi.gamepad.getRawAxis(4)));
			break;

		case GYRO:
			Robot.driveTrain.driveGyroStraight(getOutput(deadBand, 1, -Robot.oi.joystick1.getY()));
			break;

		case VISION:

			break;
		}
	}

	// Make this return true when this Command no longer needs to run execute()
	protected boolean isFinished() {
		return false;
	}

	// Called once after isFinished returns true
	protected void end() {
	}

	// Called when another command which requires one or more of the same
	// subsystems is scheduled to run
	protected void interrupted() {
	}

	public double getOutput(double deadband, double maxOutput, double axis) {
		double output;
		if (Math.abs(axis) < deadband) {
			output = 0;
		} else {
			double motorOutput = (((Math.abs(axis) - deadband) / (1 - deadband)) * (axis / Math.abs(axis)));
			output = motorOutput * maxOutput;
		}
		return output;
	}
}
