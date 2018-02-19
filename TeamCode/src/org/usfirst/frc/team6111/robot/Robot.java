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

		// Drive for 2 seconds
		while (m_timer.get() < 2.4) {
			m_robotDrive.arcadeDrive(0.75, 0.27); // drive forwards half speed
		}
		m_robotDrive.arcadeDrive(0.0, 0.0); // stop robot

		m_timer.reset();

		// Turn Right
		while (m_timer.get() < 0.55) {
			m_robotDrive.tankDrive(.75, 0);
		}
		m_robotDrive.tankDrive(0.0, 0.0);

		m_timer.reset();

		// Launch Cube
		while (m_timer.get() < 1.0) {
			Arm.set(1);
		}

		Arm.set(0);
		// For the love of God, stop!!!
		while (m_timer.get() < 30.0) {
			m_robotDrive.arcadeDrive(0, 0);
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

		/**
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

		// Driver 2 controls the arm with Left & Right Triggers
		if (gamepad2.getRawAxis(2) > 0) {
			Arm.set(-.4);
		} else if (gamepad2.getRawAxis(3) > 0) {
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