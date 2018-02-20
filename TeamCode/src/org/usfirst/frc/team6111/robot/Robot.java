/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.usfirst.frc.team6111.robot;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Spark;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.Victor;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.CameraServer;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class Robot extends IterativeRobot {
	private DifferentialDrive m_robotDrive = new DifferentialDrive(new Spark(0), new Spark(1));
	private Joystick gamepad1 = new Joystick(0);
	private Joystick gamepad2 = new Joystick(1);
	private Timer m_timer = new Timer();
	private Victor IntakeL = new Victor(2);
	private Victor IntakeR = new Victor(3);
	private Spark Arm = new Spark(4);
	public boolean flip = true;
	public double speed = 1.0;
	
	public void Forward(double speed) {
		m_robotDrive.tankDrive(speed, speed);
	}

	public void Right(double speed) {
		m_robotDrive.tankDrive(speed, 0);
	}

	public void Left(double speed) {
		m_robotDrive.tankDrive(0, speed);
	}

	public void armLaunch(double power) {
		Arm.set(power);
	}

	/**
	 * This function is run when the robot is first started up and should be used
	 * for any initialization code.
	 */
	@Override
	public void robotInit() {
		CameraServer.getInstance().startAutomaticCapture();
	}

	/**
	 * This function is run once each time the robot enters autonomous mode.
	 */
	@Override
	public void autonomousInit() {
		m_timer.reset();
		m_timer.start();
	}

	/**
	 * This function is called periodically during autonomous.
	 */
	@Override
	public void autonomousPeriodic() {
String gameData;
		gameData = DriverStation.getInstance().getGameSpecificMessage();
		if (gameData.length() > 0) {
			if (gameData.charAt(0) == 'L') {
				// Put left auto code here

				if (m_timer.get() < 1.0) {
					Forward(.6);
				} else if (m_timer.get() < 1.55) {
					Left(0.75);
				} else if (m_timer.get() < 3.1) {
					Forward(0.6);
				} else if (m_timer.get() < 3.7){
					Right(.75);
				}
				else if(m_timer.get() < 5.4) {
					Forward(.6);
				}
				else if (m_timer.get() < 6.4) {
					armLaunch(1.0);
				}
				else if (m_timer.get() < 9.4) {
					armLaunch(-.2);
				}
				else {
					m_robotDrive.tankDrive(0, 0);
					Arm.set(0);
				}

			} else {
				// Put right auto code here
				if (m_timer.get() < 1.0) {
					Forward(.6);
				} else if (m_timer.get() < 1.55) {
					Right(0.75);
				} else if (m_timer.get() < 3.05) {
					Forward(0.6);
				} else if (m_timer.get() < 3.55){
					Left(.75);
				} else if (m_timer.get() < 4.8) {
					Forward(.6);
				}
				else if (m_timer.get() < 5.8) {
					armLaunch(1.0);
				}
				else if (m_timer.get() < 8.8) {
					armLaunch(-.2);
				}
				else {
					m_robotDrive.tankDrive(0, 0);
					Arm.set(0);
				}
			}
		}

	}

	/**
	 * This function is called once each time the robot enters teleoperated mode.
	 */
	@Override
	public void teleopInit() {
	}

	/**
	 * This function is called periodically during teleoperated mode.
	 */
	@Override
	public void teleopPeriodic() {
/**
		 * The "speed" variable is used to throttle speed. When Driver 1
		 * needs more precise control, he'll signal Driver 2 to lower the slider(3) on the Joystick. 
		 * He can raise the slider to increase speed again.
		 * .
		 */
		speed = 1.0 - (gamepad2.getRawAxis(3)*0.5);

		/**
		 * The "flip" variable allows us to toggle which side of the robot is front.
		 * When Driver 1 wants the arm side to be front, he'll signal Driver 2 to hit
		 * the button 11. Button 12 will toggle it back.
		 */
		if (gamepad2.getRawButton(12)) {
			flip = false;
		}
		if (gamepad2.getRawButton(12)) {
			flip = true;
		}

		/**
		 * For some reason, the robot veers sharply to one side. Unfortunately, we don't
		 * have enough time before Bag Day to troubleshoot the issue mechanically, so we
		 * have to correct for it in the program.
		 */
		if (flip) {
			m_robotDrive.tankDrive(-gamepad1.getRawAxis(1) * speed, -gamepad1.getRawAxis(5) * speed * 0.95);
		} else {
			m_robotDrive.tankDrive(gamepad1.getRawAxis(5) * speed * 0.95, gamepad1.getRawAxis(1) * speed);
		}

		// Driver 1 controls the intake with Left & Right Bumpers.
		if (gamepad1.getRawButton(6)) {
			IntakeL.set(-.5);
			IntakeR.set(.5);
		} else if (gamepad1.getRawButton(5)) {
			IntakeL.set(.5);
			IntakeR.set(-.5);
		} else {
			IntakeL.set(0);
			IntakeR.set(0);
		}

		// Driver 2 controls the arm with the Joystick trigger (1) and button 2.
		if (gamepad2.getRawButton(2)) {
			Arm.set(-.4);
		} else if (gamepad2.getRawButton(1)) {
			Arm.set(1);
		} else {
			Arm.set(0);
		}
	}

	/**
	 * This function is called periodically during test mode.
	 */
	@Override
	public void testPeriodic() {

		/*
		 * The "speed" variable is used to emulate a 2-speed shifter. When Driver 1
		 * needs more precise control, he'll signal Driver 2 to hit the Left Bumper. The
		 * Right Bumper will toggle it back.
		 */
		if (gamepad2.getRawButton(5)) {
			speed = 0.5;
		}
		if (gamepad2.getRawButton(6)) {
			speed = 1.0;
		}

		/*
		 * The "flip" variable allows us to toggle which side of the robot is front.
		 * When Driver 1 wants the arm side to be front, he'll signal Driver 2 to hit
		 * the A button. The B button will toggle it back.
		 */
		if (gamepad2.getRawButton(1)) {
			flip = false;
		}
		if (gamepad2.getRawButton(2)) {
			flip = true;
		}

		/*
		 * For some reason, the robot veers sharply to one side. Unfortunately, we don't
		 * have enough time before Bag Day to troubleshoot the issue mechanically, so we
		 * have to correct for it in the program.
		 */
		if (flip) {
			m_robotDrive.tankDrive(-gamepad1.getRawAxis(1) * speed, -gamepad1.getRawAxis(5) * speed * 0.95);
		} else {
			m_robotDrive.tankDrive(gamepad1.getRawAxis(5) * speed * 0.95, gamepad1.getRawAxis(1) * speed);
		}

		// Driver 1 controls the intake with Left & Right Bumpers.
		if (gamepad1.getRawButton(6)) {
			IntakeL.set(-.5);
			IntakeR.set(.5);
		} else if (gamepad1.getRawButton(5)) {
			IntakeL.set(.5);
			IntakeR.set(-.5);
		} else {
			IntakeL.set(0);
			IntakeR.set(0);
		}

		// Driver 2 controls the arm with Left & Right Triggers
		if (gamepad2.getRawAxis(2) > 0) {
			Arm.set(-.4);
		} else if (gamepad2.getRawAxis(3) > 0) {
			Arm.set(1);
		} else {
			Arm.set(0);
		}
	}
}
