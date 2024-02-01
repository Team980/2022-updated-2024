// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.Targeting;
import frc.robot.subsystems.VelocityControlledShooter;

public class FireCargoRange extends Command {
  /** Creates a new FireCargo. */
  private VelocityControlledShooter shooter;
  private Targeting targeting;

  private double spinToRange;

  public FireCargoRange(VelocityControlledShooter shooter, Targeting targeting) {
    this.shooter = shooter;
    this.targeting = targeting;

    // Use addRequirements() here to declare subsystem dependencies.
    addRequirements(shooter);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {}

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    spinToRange = 1.89 * targeting.calcRange() + 24;
    shooter.setSetpoint(spinToRange);
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    shooter.stopMotor();
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}
