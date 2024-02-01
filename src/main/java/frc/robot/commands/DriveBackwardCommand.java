// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.Collector;
import frc.robot.subsystems.Drivetrain;

public class DriveBackwardCommand extends Command {
  private Drivetrain drivetrain;
  private Collector collector;
  private final double DISTANCE_TO_DRIVE = -3;//in ft
  private  int cyclesToStop;

  /** Creates a new DriveForwardCommand. */
  public DriveBackwardCommand(Drivetrain drivetrain, Collector collector) {
    // Use addRequirements() here to declare subsystem dependencies.
    this.drivetrain = drivetrain;
    this.collector = collector;
    addRequirements(drivetrain);
    addRequirements(collector);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    drivetrain.resetYaw(0);
    drivetrain.resetEncoders();
    collector.collectorDown();
    cyclesToStop = 200;
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    drivetrain.driveRobot(1, drivetrain.getYPR()[0]/30);
    cyclesToStop--;
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {}

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    if(drivetrain.getLeftDistance() <= DISTANCE_TO_DRIVE || drivetrain.getRightDistance() <= DISTANCE_TO_DRIVE || cyclesToStop <= 0) {
      return true;
    }
    return false;
  }
}
