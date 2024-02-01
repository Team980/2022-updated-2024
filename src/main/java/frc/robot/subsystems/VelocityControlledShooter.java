// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.controller.SimpleMotorFeedforward;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.CounterBase.EncodingType;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.PIDSubsystem;

public class VelocityControlledShooter extends PIDSubsystem {
  /** Creates a new VelocityControlledShooter. */
  private final double SHOOTER_RPS_MAX = 60; 
  private final double SHOOTER_FEEDFORWARD_KS = 0.05; 
  private final double SHOOTER_FEEDFORWARD_KV = 12.0 / SHOOTER_RPS_MAX;

  private WPI_TalonSRX flywheelTop;
  private WPI_TalonSRX flywheelBottom;
  private Encoder shooterEncoder; 

  private SimpleMotorFeedforward shooterFF;

  public VelocityControlledShooter() {
    super(
        // The PIDController used by the subsystem
        new PIDController(1, 0, 0));

    flywheelTop = new WPI_TalonSRX(10);
    flywheelTop.setInverted(true);
    flywheelBottom = new WPI_TalonSRX(11);
    flywheelBottom.setInverted(true);

    shooterEncoder = new Encoder(8, 9, true, EncodingType.k4X);
    shooterEncoder.setDistancePerPulse(1.0 / 2048);

    shooterFF = new SimpleMotorFeedforward(SHOOTER_FEEDFORWARD_KS, SHOOTER_FEEDFORWARD_KV);

  }

  @Override
  public void useOutput(double output, double setpoint) {
    // Use the output here
    flywheelTop.setVoltage(output + shooterFF.calculate(setpoint));
    flywheelBottom.setVoltage(output + shooterFF.calculate(setpoint));
  }

  @Override
  public double getMeasurement() {
    // Return the process variable measurement here
    SmartDashboard.putNumber("shooter rps", shooterEncoder.getRate());
    return shooterEncoder.getRate();
  }

  public void manualOverride() {
    SmartDashboard.putNumber("Manual shooter rps", shooterEncoder.getRate());
    disable();
    double throttle = 0.25;
    if(Math.abs(throttle) > 0.1) {
      flywheelTop.set(throttle);
      flywheelBottom.set(throttle);
    } 
    else {
      flywheelBottom.stopMotor();
      flywheelTop.stopMotor();
    }
  }

  public void stopMotor() {
    enable();
    setSetpoint(0);
  }

  public void lowGoal() {
    setSetpoint(10);
  }
}
