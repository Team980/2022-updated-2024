// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.PneumaticsModuleType;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Collector extends SubsystemBase {
  private WPI_TalonSRX collect;
  private Solenoid deployRetract;
  private WPI_TalonSRX grabber;

  private AnalogInput ballDetector;
  private double distance;

  /** Creates a new Collector. */
  public Collector() {
    collect = new WPI_TalonSRX(7);
    deployRetract = new Solenoid(PneumaticsModuleType.CTREPCM, 1);
    grabber = new WPI_TalonSRX(8);

    ballDetector = new AnalogInput(0);
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
    distance = ballDetector.getVoltage();
  }
  
  public double getDistance(){
    return distance;
  }

  public void deployCollector(){
    deployRetract.set(true);
  }

  public void collectorDown(){
    deployRetract.set(true);
  }

  public void retractCollector(){
    deployRetract.set(false);
  }

  public void spinCollector(){
    collect.set(.8);
    grabber.set(1);
  }
  
  public void runCollector(double speed){
    if (speed >= .8){
      collect.set(.8);
    }
    else {
      collect.set(speed);
    }
    collect.set(speed);
    grabber.set(speed);
  }

  public void stop() {
    collect.stopMotor();
    grabber.stopMotor();
  }
}
