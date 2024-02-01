// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import java.util.ArrayList;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import io.github.pseudoresonance.pixy2api.Pixy2;
import io.github.pseudoresonance.pixy2api.Pixy2CCC;
import io.github.pseudoresonance.pixy2api.Pixy2CCC.Block;
import io.github.pseudoresonance.pixy2api.links.SPILink;

public class Finder extends SubsystemBase {
  /** Creates a new Finder. */
  private Pixy2 pixy;
  private int ballCount;
  private int sig = Pixy2CCC.CCC_SIG1;
  private ArrayList<Block> balls;

  public Finder() {//blue is true, red is false
    pixy = Pixy2.createInstance(new SPILink());
    pixy.init();
    balls = new ArrayList<Block>();
  }

  @Override
  public void periodic() {
    ballCount = pixy.getCCC().getBlocks(false, sig, 11);
    SmartDashboard.putNumber("targets found", ballCount);
    SmartDashboard.putBoolean("alliance color", sig == Pixy2CCC.CCC_SIG2);
    //debugging code
    balls = pixy.getCCC().getBlockCache();
    /*balls = pixy.getCCC().getBlockCache();
    for(int index = 0; index < balls.size(); index++){
      SmartDashboard.putNumber("target width" + index, balls.get(index).getWidth());
      SmartDashboard.putNumber("target location" + index, balls.get(index).getX() - 157);
    }*/
    // This method will be called once per scheduler run
  }

  public int[] findClosestCargo(){
    int[] dize = new int[2];//[0]-157 to 157
    if(ballCount == 0){
      dize[0] = 160;//designated did not find a ball
      return dize;
    }
    //ArrayList<Block> balls = pixy.getCCC().getBlockCache();
    Block closestBall = balls.get(0);
    for(int index = 1; index<balls.size(); index++){
      if(balls.get(index).getWidth()>closestBall.getWidth()){
        closestBall = balls.get(index);
      }//end if
    }//end for
    dize[0]=closestBall.getX()-157;
    dize[1]=closestBall.getWidth();
    SmartDashboard.putNumber("largest x location", dize[0]);
    SmartDashboard.putNumber("largest width", dize[1]);
    return dize;
  }//end findClosestCargo

  public void setColor(boolean color){
    if(color){
      sig = Pixy2CCC.CCC_SIG2;//blue
    }
    else{
      sig = Pixy2CCC.CCC_SIG1;//red
    }
  }
}//end subsystem
