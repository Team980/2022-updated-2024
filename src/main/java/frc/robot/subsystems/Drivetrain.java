// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.ctre.phoenix.sensors.PigeonIMU;

import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.CounterBase.EncodingType;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.motorcontrol.MotorControllerGroup;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard; 

public class Drivetrain extends SubsystemBase {
  /** Creates a new Drivetrain. */
  private MotorControllerGroup leftDrive;
  private MotorControllerGroup rightDrive;
  private DifferentialDrive robotDrive;
  private Encoder leftEncoder;
  private Encoder rightEncoder;

  private PigeonIMU imu;
  private PigeonIMU.GeneralStatus generalStatus;
  private int imuErrorCode;
  private double [] ypr;
  

  public Drivetrain() {
    var collectorTalon = new WPI_TalonSRX(7);
    imu = new PigeonIMU(collectorTalon);
    generalStatus = new PigeonIMU.GeneralStatus();
    ypr = new double [3];

    var leftTop = new WPI_TalonSRX(3);
    var leftBack = new WPI_TalonSRX(1);
    var leftFront = new WPI_TalonSRX(5);
    leftTop.setInverted(true);
    leftDrive = new MotorControllerGroup(leftTop, leftBack, leftFront);
    leftTop.setNeutralMode(NeutralMode.Coast);
    leftBack.setNeutralMode(NeutralMode.Coast);
    leftFront.setNeutralMode(NeutralMode.Coast);

  
    leftEncoder = new Encoder(4, 5, false, EncodingType.k4X); //come back to false bit, switch if forward is negative and vise versa
    leftEncoder.setDistancePerPulse( (Math.PI / 3.0) / 2048.0 );

    var rightTop = new WPI_TalonSRX(4);
    var rightBack = new WPI_TalonSRX(2);
    var rightFront = new WPI_TalonSRX(6);
    rightTop.setInverted(true);

    rightDrive = new MotorControllerGroup(rightTop, rightBack, rightFront);
    rightDrive.setInverted(true);
    rightTop.setNeutralMode(NeutralMode.Coast);
    rightBack.setNeutralMode(NeutralMode.Coast);
    rightFront.setNeutralMode(NeutralMode.Coast);

    rightEncoder = new Encoder(6, 7, true, EncodingType.k4X); //come back to false bit, switch if forward is negative and vise versa
    rightEncoder.setDistancePerPulse( (Math.PI / 3.0) / 2048.0 );

    robotDrive = new DifferentialDrive(leftDrive, rightDrive);
  }

  public void driveRobot(double move, double turn) {
    if(turn > .85){
      turn = .85;
    }
      robotDrive.arcadeDrive(-move, turn);
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
    imuErrorCode = imu.getGeneralStatus(generalStatus).value;
    imu.getYawPitchRoll(ypr);
    SmartDashboard.putNumber("IMU Health", imuErrorCode);
    SmartDashboard.putNumber("IMU Yaw", ypr[0]);
    SmartDashboard.putNumber("Left Speed", leftEncoder.getRate() );
    SmartDashboard.putNumber("Right Speed", rightEncoder.getRate() );

    SmartDashboard.putNumber("Left Distance", leftEncoder.getDistance() );
    SmartDashboard.putNumber("Right Distance", rightEncoder.getDistance() );
  }

  public void stop(){
    leftDrive.stopMotor();
    rightDrive.stopMotor();
  }

  public double getLeftSpeed() {
    return leftEncoder.getRate();
  }

  public double getRightSpeed() {
    return rightEncoder.getRate();
  }

  public double getLeftDistance() {
    return leftEncoder.getDistance();
  }

  public double getRightDistance() {
    return rightEncoder.getDistance();
  } 

  public void resetEncoders(){
    leftEncoder.reset();
    rightEncoder.reset();
  }

  public int getIMUHealth(){
    return imuErrorCode;
  }

  public double [] getYPR() {
    return ypr;
  }

  public void resetYaw(double value){
    imu.setYaw(value);
  }
}