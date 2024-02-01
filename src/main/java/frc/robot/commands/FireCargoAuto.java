// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.Collector;
import frc.robot.subsystems.Conveyor;
import frc.robot.subsystems.Targeting;
import frc.robot.subsystems.VelocityControlledShooter;

public class FireCargoAuto extends Command {
  /** Creates a new FireCargo. */
  private VelocityControlledShooter shooter;
  private Conveyor conveyor;
  private Targeting targeting;
  private Collector collector; 

  private int timeCounter;
  private boolean initialShot;
  private double spinToRange;

  public FireCargoAuto(VelocityControlledShooter shooter, Conveyor conveyor, boolean initialShot, Targeting targeting, Collector collector) {
    this.shooter = shooter;
    this.conveyor = conveyor;
    this.initialShot = initialShot;
    this.targeting = targeting;
    this.collector = collector;
    // Use addRequirements() here to declare subsystem dependencies.
    addRequirements(shooter);
    addRequirements(conveyor);
    addRequirements(collector);
    
    
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    timeCounter = 0;
    collector.deployCollector();
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    if(initialShot) {
      shooter.setSetpoint(36);
      conveyor.runConveyor(-1);
    }
    else {
      targeting.ledOn(true);
      spinToRange = 1.89 * targeting.calcRange() + 24;
      shooter.setSetpoint(spinToRange);
      conveyor.runConveyor(-1);
      collector.runCollector(1);
    }
    timeCounter++;
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    shooter.stopMotor();
    conveyor.stop();
    collector.stop();
    targeting.ledOn(false);
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    if(timeCounter >= 100 && initialShot) {
      return true;
    }
    else if(timeCounter >= 300){
      return true;
    }
    return false;
  }
}
