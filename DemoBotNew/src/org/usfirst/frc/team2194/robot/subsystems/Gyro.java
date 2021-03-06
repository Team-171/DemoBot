package org.usfirst.frc.team2194.robot.subsystems;

import org.usfirst.frc.team2194.robot.Robot;

import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 */
public class Gyro extends Subsystem {

	private static final double Kp = .01;
	private static final double Ki = 0.0;
	private static final double Kd = 0.0;

	public boolean gyroState = true;

	public double targetAngle;
	private double targetError;
	public double gyroKp = .05;
    // Put methods for controlling this subsystem
    // here. Call these from Commands.

    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        //setDefaultCommand(new MySpecialCommand());
    }
    
	public void resetGyro() {
		Robot.imu.reset();
	}

	public double getGyroAngle() {
		double angle = Robot.imu.getAngle();
		
		if(angle < 0)
		{
			angle += 360;
		}
		
		return angle;
	}
	
	public double GetTargetAngle(){
		return this.targetAngle;
	}

	public float getGyroYaw() {
		return Robot.imu.getYaw();
	}

	public double getGyroYawComp() {
		return (getGyroYaw() * gyroKp);
	}

	public void setTargetAngle(double targetAngle) {
		this.targetAngle = targetAngle;
	}

	public double getTargetError() {
		targetError = getGyroAngle() - targetAngle;
//		if (Math.abs(targetError) < 180)
			return -targetError;
//		else if (targetError >= 180)
//			return 360 - targetError;
//		else
//			return 360 + targetError;
	}

	public double getTargetYawComp() {
		if ((Robot.gyro.getTargetError() * gyroKp) >= .1) {
			return .1;
		} else if (Robot.gyro.getTargetError() <= -.1) {
			return -.1;
		} else {
			return getTargetError() * gyroKp;
		}
	}
	
	public void updateStatus(){
		SmartDashboard.putNumber("Gyro Angle", getGyroAngle());
		SmartDashboard.putNumber("Gyro Target Angle", GetTargetAngle());
		SmartDashboard.putNumber("Gyro Error", getTargetYawComp());
		SmartDashboard.putNumber("Gyro Target Error", getTargetError());
	}
}

