// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.PneumaticsModuleType;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Climb extends SubsystemBase {
  private DoubleSolenoid climber;

  /** Creates a new Climb. */
  public Climb() {
    climber = new DoubleSolenoid(PneumaticsModuleType.CTREPCM, 2, 3);
    climber.set(Value.kReverse);
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
    SmartDashboard.putBoolean("climber extended", isExtended() );
  }

  public boolean isExtended() {
    return climber.get() == Value.kForward;
  }

  public void extend() {
    climber.set(Value.kForward);
  }
  
  public void retract() {
    climber.set(Value.kReverse);
  }
}
