package org.usfirst.frc.team2194.robot.subsystems;

import org.usfirst.frc.team2194.robot.Robot;
import org.usfirst.frc.team2194.robot.Robot.DriveMode;
import org.usfirst.frc.team2194.robot.RobotMap;
import org.usfirst.frc.team2194.robot.RobotMotion.JoystickDrive;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.internal.HardwareTimer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 */
public class DriveTrain extends Subsystem {

	// Put methods for controlling this subsystem
	// here. Call these from Commands.
	double rightMultiplier;
	double maxMultiplier = .5;
	double minMultiplier = .35;
	double timeStamp = 0.0;

	public void initDefaultCommand() {
		// Set the default command for a subsystem here.
		setDefaultCommand(new JoystickDrive());
	}

	public void driveRobotJoystick(double leftSpeed, double rightSpeed) {
		rightMultiplier = minMultiplier + ((1 - Math.abs(leftSpeed)) * (maxMultiplier - minMultiplier));

		RobotMap.driveLeftFrontMotor.set(leftSpeed + (rightSpeed * rightMultiplier));
		RobotMap.driveLeftBackMotor.set(leftSpeed + (rightSpeed * rightMultiplier));
		RobotMap.driveRightFrontMotor.set(leftSpeed - (rightSpeed * rightMultiplier));
		RobotMap.driveRightBackMotor.set(leftSpeed - (rightSpeed * rightMultiplier));

	}
	
	public void driveRobotMecanum(double x, double y, double z) {
		rightMultiplier = minMultiplier + ((1 - Math.abs(y)) * (maxMultiplier - minMultiplier));

		RobotMap.drive.mecanumDrive_Cartesian(x, y, z * rightMultiplier, 0);

	}
	
	public void driveRobotMecanumGyro(double x, double y, double z) {
		rightMultiplier = minMultiplier + ((1 - Math.abs(y)) * (maxMultiplier - minMultiplier));

		RobotMap.drive.mecanumDrive_Cartesian(x, y, z * rightMultiplier, Robot.gyro.getGyroAngle());
	}
	
	public void driveRobotTank(double left, double right){
		RobotMap.drive.tankDrive(left, right, false);
	}
	
	public void driveRobotArcade(double left, double right){
		rightMultiplier = minMultiplier + ((1 - Math.abs(left)) * (maxMultiplier - minMultiplier));
		
		
		RobotMap.driveLeftFrontMotor.set(left + (right * rightMultiplier));
		RobotMap.driveLeftBackMotor.set(left + (right * rightMultiplier));
		RobotMap.driveRightFrontMotor.set(left - (right * rightMultiplier));
		RobotMap.driveRightBackMotor.set(left - (right * rightMultiplier));
//		RobotMap.drive.arcadeDrive(left, right * rightMultiplier);
	}

	public void driveRobotControlled(double speed, double comp) {
		RobotMap.driveLeftFrontMotor.set(speed + comp);
		RobotMap.driveLeftBackMotor.set(speed + comp);
		RobotMap.driveRightFrontMotor.set(speed - comp);
		RobotMap.driveRightBackMotor.set(speed - comp);

	}
	
	public void driveRobotControlledMecanumGyro(double x, double y, double z) {

		RobotMap.drive.mecanumDrive_Cartesian(x, y, z, Robot.gyro.getGyroAngle());
	}

	public void stopDrive() {
		RobotMap.driveLeftFrontMotor.set(0);
		RobotMap.driveLeftBackMotor.set(0);
		RobotMap.driveRightFrontMotor.set(0);
		RobotMap.driveRightBackMotor.set(0);
	}

	public void driveGyroStraight(double speed) {
		Robot.driveTrain.driveRobotControlled(speed, Robot.gyro.getTargetYawComp());
	}
	
	public void updateStatus(){
		
		
		if(Robot.driveMode == DriveMode.NONE  || Robot.driveMode == DriveMode.MECANUMGYRO)
		{
			if(Math.abs(RobotMap.driveLeftBackMotor.get()) > 0 || Math.abs(RobotMap.driveLeftFrontMotor.get()) > 0 || Math.abs(RobotMap.driveRightBackMotor.get()) > 0 || Math.abs(RobotMap.driveRightFrontMotor.get()) > 0)
			{
				Robot.mainLEDControl.updateDutyCycle(((float) 30)/100);
				Robot.leftLEDControl.updateDutyCycle(0.5 - (RobotMap.driveLeftFrontMotor.get()/2));
				Robot.rightLEDControl.updateDutyCycle(0.5 + (RobotMap.driveRightFrontMotor.get()/2));
				Robot.leftBackLEDControl.updateDutyCycle(0.5 - (RobotMap.driveLeftBackMotor.get()/2));
				Robot.rightBackLEDControl.updateDutyCycle(0.5 + (RobotMap.driveRightBackMotor.get()/2));
				timeStamp = Timer.getFPGATimestamp();
			}
			else
			{
				if((Timer.getFPGATimestamp() - timeStamp) > 5)
				{
					Robot.mainLEDControl.updateDutyCycle(((float) 10)/100);
				}
			}
		}
		else
		{
			double leftAverage = (RobotMap.driveLeftBackMotor.get() + RobotMap.driveLeftFrontMotor.get())/2;
			double rightAverage = (RobotMap.driveRightBackMotor.get() + RobotMap.driveRightFrontMotor.get())/2;
			
			if(Math.abs(leftAverage) > 0 || Math.abs(rightAverage) > 0)
			{
				Robot.mainLEDControl.updateDutyCycle(((float) 20)/100);
				Robot.leftLEDControl.updateDutyCycle(0.5 - (leftAverage/2));
				Robot.rightLEDControl.updateDutyCycle(0.5 + (rightAverage/2));
				timeStamp = Timer.getFPGATimestamp();
			}
			else
			{
				if((Timer.getFPGATimestamp() - timeStamp) > 5)
				{
					Robot.mainLEDControl.updateDutyCycle(((float) 10)/100);
				}
			}
		}
		
		
		
//		SmartDashboard.putNumber("time", Timer.getFPGATimestamp());
	}
}
