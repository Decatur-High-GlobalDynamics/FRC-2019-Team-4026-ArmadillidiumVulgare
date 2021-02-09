  package frc.robot.subsystems;

import com.kauailabs.navx.frc.AHRS;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public abstract class DriveTrain extends SubsystemBase {
  public abstract void leftPower(double requestedPower);

  public abstract void rightPower(double requestedPower);

  public abstract void periodic();

  protected abstract void initDefaultCommand();

  public abstract void stop();

  public abstract void keepDriveStraight(
      double leftDriveVel, double rightDriveVel, double targetAngle);

  public abstract void dumbDriveStraight(double power);

  public abstract int getEncoderLeft();

  public abstract int getEncoderRight();

  public abstract void resetEncoders();

  public static final double TICKS_PER_INCH = 13.3333333333333;

  public abstract double getAngle();

  public AHRS navx;
}
