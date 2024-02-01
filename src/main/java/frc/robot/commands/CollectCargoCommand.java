// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.Collector;
import frc.robot.subsystems.Conveyor;
import frc.robot.subsystems.Drivetrain;

public class CollectCargoCommand extends Command {
  //private Finder finder;
  private Collector collector;
  private Drivetrain drivetrain;
  private Conveyor conveyor;
  private double distanceToDrive;
  private int cyclesToStop;

  /** Creates a new CollectCargoCommand. */
  public CollectCargoCommand(Collector collector, Drivetrain drivetrain, Conveyor conveyor , boolean withPixy) {
    //this.finder = finder;
    this.collector = collector;
    this.drivetrain = drivetrain;
    this.conveyor = conveyor;
    if(withPixy){
      distanceToDrive = 2;
    }
    else{
      distanceToDrive = 3;
    }

    // Use addRequirements() here to declare subsystem dependencies.
    //addRequirements(finder);
    addRequirements(collector);
    addRequirements(drivetrain);
    addRequirements(conveyor);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    drivetrain.resetYaw(0);
    drivetrain.resetEncoders();
    cyclesToStop = 150;
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    collector.spinCollector();
    if(drivetrain.getLeftDistance() <= 3 || drivetrain.getRightDistance() <= 3){
      drivetrain.driveRobot(-0.7, drivetrain.getYPR()[0]/30); 
    }
    else{
      drivetrain.stop();
    }
    conveyor.up();
    cyclesToStop--;
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    drivetrain.stop();
    collector.stop();
    conveyor.stop();
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() { 
     if(cyclesToStop <= 0) {
       return true;
     } 
    return false;
  }
}
