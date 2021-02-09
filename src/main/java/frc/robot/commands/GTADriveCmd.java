// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import java.util.function.BooleanSupplier;
import java.util.function.DoubleSupplier;


import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.DriveTrainSubsystem2019;

public class GTADriveCmd extends CommandBase {
  DriveTrainSubsystem2019 driveTrain;

  private final DoubleSupplier leftStick;
  private final DoubleSupplier rightStick;
  private final BooleanSupplier rightTrigger;
  private final BooleanSupplier leftTrigger;
  
  /** Creates a new GTADriveCmd. */
  public GTADriveCmd(DriveTrainSubsystem2019 driveTrain, DoubleSupplier leftStick, DoubleSupplier rightStick, BooleanSupplier rightTrigger, BooleanSupplier leftTrigger) {
    this.driveTrain = driveTrain;
    this.leftStick = leftStick;
    this.rightStick = rightStick;
    this.rightTrigger = rightTrigger;
    this.leftTrigger = leftTrigger;
    
    addRequirements(driveTrain);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {}

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    double powerToSet = 0;

    if (leftTrigger.getAsBoolean() && rightTrigger.getAsBoolean()) {
      powerToSet = 0;
    } else if (leftTrigger.getAsBoolean()) {
      powerToSet = -((rightStick.getAsDouble() + 1) / 2);
    } else if (rightTrigger.getAsBoolean()) {
      powerToSet = (rightStick.getAsDouble() + 1) / 2;
    }

    driveTrain.curveDrive(powerToSet, leftStick.getAsDouble(), Math.abs(powerToSet) < 0.02);
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {}

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}
