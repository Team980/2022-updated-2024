// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Conveyor extends SubsystemBase {
  /** Creates a new Conveyor. */
  private WPI_TalonSRX conveyor;
  
  public Conveyor() {
    conveyor = new WPI_TalonSRX(9);
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }

  public void up() {
    conveyor.set(-1);
  }

  public void down() {
    conveyor.set(1);
  }

  public void runConveyor(double speed){
    conveyor.set(speed);
  }

  public void stop() {
    conveyor.stopMotor();
  }
}
