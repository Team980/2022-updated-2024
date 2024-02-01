// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.wpilibj.GenericHID;
//import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.XboxController;
//import edu.wpi.first.wpilibj.XboxController.Button;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.commands.AimAuto;
import frc.robot.commands.AimTele;
import frc.robot.commands.BallSeeker;
import frc.robot.commands.CollectCargoCommand;
import frc.robot.commands.DriveBackwardCommand;
import frc.robot.commands.FireCargoAuto;
import frc.robot.commands.FireCargoRange;
import frc.robot.commands.TurnRobot;
import frc.robot.subsystems.Climb;
import frc.robot.subsystems.Collector;
import frc.robot.subsystems.Conveyor;
import frc.robot.subsystems.Drivetrain;
import frc.robot.subsystems.Finder;
import frc.robot.subsystems.Shifter;
//import frc.robot.subsystems.Shooter;
import frc.robot.subsystems.Targeting;
import frc.robot.subsystems.VelocityControlledShooter;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.RunCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.button.CommandJoystick;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
//import edu.wpi.first.wpilibj2.command.button.JoystickButton;
//import edu.wpi.first.wpilibj2.command.button.POVButton;

/**
 * This class is where the bulk of the robot should be declared. Since Command-based is a
 * "declarative" paradigm, very little robot logic should actually be handled in the {@link Robot}
 * periodic methods (other than the scheduler calls). Instead, the structure of the robot (including
 * subsystems, commands, and button mappings) should be declared here.
 */
public class RobotContainer {
  // The robot's subsystems and commands are defined here...

  /** The container for the robot. Contains subsystems, OI devices, and commands. */
  private final Drivetrain drivetrain = new Drivetrain();
  private final Shifter shifter  = new Shifter (drivetrain);
  private final VelocityControlledShooter shooter = new VelocityControlledShooter();
  //private final Shooter shooter = new Shooter();
  private final Collector collector = new Collector();
  private final Conveyor conveyor = new Conveyor();
  private final Climb climber = new Climb();
  private final Finder finder = new Finder();
  private final Targeting targeting = new Targeting();

  private final CommandXboxController xbox = new CommandXboxController(2);

  private final CommandJoystick wheel = new CommandJoystick(0);
  private final CommandJoystick throttle = new CommandJoystick(1);
  private final CommandJoystick prajBox = new CommandJoystick(4);
  private final DriveBackwardCommand driveBackwardCommand = new DriveBackwardCommand(drivetrain, collector);
  private final SequentialCommandGroup shootTaxi = new SequentialCommandGroup(
    new FireCargoAuto(shooter, conveyor, true, targeting, collector) , 
    new DriveBackwardCommand(drivetrain, collector)
  );
  private final SequentialCommandGroup shootFindShootDR = new SequentialCommandGroup(
    new FireCargoAuto(shooter, conveyor, true, targeting, collector) , 
    new DriveBackwardCommand(drivetrain, collector) , 
    new TurnRobot(drivetrain, -77) , 
    new CollectCargoCommand(collector, drivetrain, conveyor, false) , 
    new TurnRobot(drivetrain, 77) , 
    new AimAuto(drivetrain, targeting),
    new FireCargoAuto(shooter, conveyor, false, targeting, collector)
  );
  private final SequentialCommandGroup shootFindShootPixyRed = new SequentialCommandGroup(
    new FireCargoAuto(shooter, conveyor, true, targeting, collector) , 
    new DriveBackwardCommand(drivetrain, collector) , 
    new TurnRobot(drivetrain, -70) , 
    new BallSeeker(drivetrain, finder, false) , //false is red
    new CollectCargoCommand(collector, drivetrain, conveyor, true) , 
    new TurnRobot(drivetrain, 70) , 
    new AimAuto(drivetrain, targeting),
    new FireCargoAuto(shooter, conveyor, false, targeting, collector)
  );
  private final SequentialCommandGroup shootFindShootPixyBlue = new SequentialCommandGroup(
    new FireCargoAuto(shooter, conveyor, true, targeting, collector) , 
    new DriveBackwardCommand(drivetrain, collector) , 
    new TurnRobot(drivetrain, -70) , 
    new BallSeeker(drivetrain, finder, true) , //true is blue
    new CollectCargoCommand(collector, drivetrain, conveyor, true) , 
    new TurnRobot(drivetrain, 70) , 
    new AimAuto(drivetrain, targeting),
    new FireCargoAuto(shooter, conveyor, false, targeting, collector)
  );

  private final SequentialCommandGroup pixyOtherDirectionRed = new SequentialCommandGroup(
    new FireCargoAuto(shooter, conveyor, true, targeting, collector) , 
    new DriveBackwardCommand(drivetrain, collector) , 
    new TurnRobot(drivetrain, 50) , 
    new BallSeeker(drivetrain, finder, false) , //false is red
    new CollectCargoCommand(collector, drivetrain, conveyor, true) , 
    new TurnRobot(drivetrain, -50) , 
    new AimAuto(drivetrain, targeting),
    new FireCargoAuto(shooter, conveyor, false, targeting, collector)
  );
  private final SequentialCommandGroup pixyOtherDirectionBlue = new SequentialCommandGroup(
    new FireCargoAuto(shooter, conveyor, true, targeting, collector) , 
    new DriveBackwardCommand(drivetrain, collector) , 
    new TurnRobot(drivetrain, 50) , 
    new BallSeeker(drivetrain, finder, true) , //true is blue
    new CollectCargoCommand(collector, drivetrain, conveyor, true) , 
    new TurnRobot(drivetrain, -50) , 
    new AimAuto(drivetrain, targeting),
    new FireCargoAuto(shooter, conveyor, false, targeting, collector)
  );

  SendableChooser<Command> autoChooser = new SendableChooser<Command>();

  public RobotContainer() {
    autoChooser.setDefaultOption("Shoot and Taxi", shootTaxi);
    autoChooser.addOption("Seek Red", shootFindShootPixyRed);
    autoChooser.addOption("Seek Blue", shootFindShootPixyBlue);
    autoChooser.addOption("No Pixy", shootFindShootDR);
    autoChooser.addOption("Opposite Direction Red", pixyOtherDirectionRed);
    autoChooser.addOption("Opposite Direction Blue", pixyOtherDirectionBlue);
    autoChooser.addOption("Taxi Only", driveBackwardCommand);

    SmartDashboard.putData(autoChooser);

    drivetrain.setDefaultCommand(new RunCommand(
      () -> drivetrain.driveRobot(throttle.getY(), wheel.getX()), 
      drivetrain
      ));
    shifter.setDefaultCommand(new RunCommand(shifter::setLowGear, shifter) );

    conveyor.setDefaultCommand(new RunCommand(
      () -> conveyor.runConveyor(xbox.getRightY()), 
      conveyor
      ));

      collector.setDefaultCommand(new RunCommand(
      () -> collector.runCollector(xbox.getLeftY()),
      collector
      ));
    
      shooter.enable();

    // Configure the button bindings
    configureButtonBindings();
  }

  /**
   * Use this method to define your button->command mappings. Buttons can be created by
   * instantiating a {@link GenericHID} or one of its subclasses ({@link
   * edu.wpi.first.wpilibj.Joystick} or {@link XboxController}), and then passing it to a {@link
   * edu.wpi.first.wpilibj2.command.button.JoystickButton}.
   */
  private void configureButtonBindings() {
    throttle.button(4).onTrue(new RunCommand(shifter::setHighGear, shifter) );
    throttle.button(3).onTrue(new RunCommand(shifter::setLowGear, shifter) );
    throttle.button(5).onTrue(new RunCommand(
      () -> shifter.autoShift(),
      shifter
      ));

    /*new JoystickButton(throttle, 4).whenPressed(new RunCommand(shifter::setHighGear, shifter) );
    new JoystickButton(throttle, 3).whenPressed(new RunCommand(shifter::setLowGear, shifter) );
    new JoystickButton(throttle, 5).whenPressed(new RunCommand(
      () -> shifter.autoShift(),
      shifter
      ));*/
    // new JoystickButton(throttle, 7).whenPressed(new FireCargoAuto(shooter, conveyor, true, targeting, collector));
    // new JoystickButton(throttle, 8).whenPressed(new DriveBackwardCommand(drivetrain, collector));
    // new JoystickButton(throttle, 9).whenPressed(new TurnRobot(drivetrain, -70));
    // new JoystickButton(throttle, 10).whenPressed(new BallSeeker(drivetrain, finder , false));
    // new JoystickButton(throttle, 11).whenPressed(new CollectCargoCommand(collector, drivetrain, conveyor, false));//switch to false for dead reckoning
    // new JoystickButton(throttle, 12).whenPressed(new TurnRobot(drivetrain, 90));
    throttle.button(1).whileTrue(new AimTele(drivetrain, targeting));
    //new JoystickButton(throttle, 1).whileHeld(new AimTele(drivetrain, targeting));
    // new JoystickButton(throttle, 2).whenPressed(new FireCargoAuto(shooter, conveyor, false, targeting, collector));

    xbox.a().onTrue(new InstantCommand(collector::deployCollector, collector));
    xbox.y().onTrue(new InstantCommand(collector::retractCollector, collector));
    xbox.x().onTrue(new RunCommand(shooter::manualOverride, shooter));
    xbox.start().onTrue(new FireCargoRange(shooter, targeting));
    xbox.back().onTrue(new InstantCommand(shooter::stopMotor, shooter));
    xbox.b().onTrue(new RunCommand(shooter::lowGoal, shooter));
    xbox.pov(0).onTrue(new InstantCommand(climber::extend, climber));
    xbox.pov(180).onTrue(new InstantCommand(climber::retract, climber));

    /*new JoystickButton(xBox, Button.kA.value).whenPressed(new InstantCommand(collector::deployCollector, collector));
    new JoystickButton(xBox, Button.kY.value).whenPressed(new InstantCommand(collector::retractCollector, collector));
    new JoystickButton(xBox, Button.kX.value).whenPressed(new RunCommand(shooter::manualOverride, shooter));
    new JoystickButton(xBox, Button.kStart.value).whenPressed(new FireCargoRange(shooter, targeting));
    new JoystickButton(xBox, Button.kBack.value).whenPressed(new InstantCommand(shooter::stopMotor, shooter));
    new JoystickButton(xBox, Button.kB.value).whenPressed(new RunCommand(shooter::lowGoal, shooter));
    new POVButton(xBox, 0).whenPressed(new InstantCommand(climber::extend, climber));
    new POVButton(xBox, 180).whenPressed(new InstantCommand(climber::retract, climber));*/


  }

  /**
   * Use this to pass the autonomous command to the main {@link Robot} class.
   *
   * @return the command to run in autonomous
   */
  public Command getAutonomousCommand() {
    // An ExampleCommand will run in autonomous
    return autoChooser.getSelected();
  }
}
