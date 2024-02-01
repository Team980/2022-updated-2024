// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.CounterBase.EncodingType;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Shooter extends SubsystemBase {
  /** Creates a new Shooter. */
  private WPI_TalonSRX flywheelTop;
  private WPI_TalonSRX flywheelBottom;
  private Encoder shooterEncoder; 

  public Shooter() {
    flywheelTop = new WPI_TalonSRX(10);
    flywheelBottom = new WPI_TalonSRX(11);

    shooterEncoder = new Encoder(8, 9, true, EncodingType.k4X);
    shooterEncoder.setDistancePerPulse(1.0 / 2048);
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
    SmartDashboard.putNumber("I Hate You (shooter encoder)", shooterEncoder.getRate());
  }

  public void manualOverride() {
    flywheelTop.set(-.16);
    flywheelBottom.set(-.16);
  }

  public void stop() {
    flywheelTop.stopMotor();
    flywheelBottom.stopMotor();
  }
}
