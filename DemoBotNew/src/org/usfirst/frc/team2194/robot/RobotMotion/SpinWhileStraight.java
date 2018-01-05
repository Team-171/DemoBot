package org.usfirst.frc.team2194.robot.RobotMotion;

import org.usfirst.frc.team2194.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class SpinWhileStraight extends Command {
	
	double myStraightSpeed;
	double myRotationSpeed;
	int myRotations;
	boolean myclockwise;	
	double myTimeout;
	double lastAngle = 0;
	int currentRotation;

    public SpinWhileStraight(double straightSpeed, double rotationSpeed, int rotations, boolean clockwise, double timeout) {
        super(timeout);
        
        myStraightSpeed = straightSpeed;
        myRotationSpeed = rotationSpeed;
        myRotations = rotations;
        myclockwise = clockwise;
        myTimeout = timeout;
        
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	setTimeout(myTimeout);
    	Robot.gyro.resetGyro();
    	
    	if(!myclockwise)
    	{
    		myRotationSpeed = -myRotationSpeed;
    		lastAngle = 360;
    	}
    	
    	Robot.driveTrain.driveRobotControlledMecanumGyro(0, myStraightSpeed, myRotationSpeed);
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	double currentAngle = Robot.gyro.getGyroAngle() % 360;
    	
    	if(myclockwise)
    	{
    		if(lastAngle > 350 && currentAngle < 10)
    		{
    			currentRotation++;
    		}
    	}
    	else
    	{
    		if(lastAngle < 10 && currentAngle > 350)
    		{
    			currentRotation++;
    		}
    	}
    	
    	lastAngle = Robot.gyro.getGyroAngle() % 360;
    }

	@Override
	protected boolean isFinished() {
		// TODO Auto-generated method stub
		return (currentRotation == myRotations);
	}

    // Called once after timeout
    protected void end() {
    	Robot.driveTrain.stopDrive();
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    	end();
    }
}
