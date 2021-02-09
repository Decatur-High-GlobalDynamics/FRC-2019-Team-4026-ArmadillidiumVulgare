package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.BaseMotorController;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import edu.wpi.first.wpilibj.AnalogGyro;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Sendable;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.geometry.Pose2d;
import edu.wpi.first.wpilibj.geometry.Rotation2d;
import edu.wpi.first.wpilibj.kinematics.DifferentialDriveOdometry;
import edu.wpi.first.wpilibj.kinematics.DifferentialDriveWheelSpeeds;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpiutil.math.MathUtil;
import frc.robot.Portmap;
import frc.robot.Robot;
import frc.robot.commands.DriveTrainCMDS;
import frc.robot.Constants;

public class DriveTrainSubsystem2019 extends DriveTrain {
  public final double TICKS_PER_INCH = 13 + (1/3);
  final WPI_TalonSRX rightDriveMotorTalon;
  final WPI_TalonSRX leftDriveMotorTalon;
  final BaseMotorController rightDriveMotorVictor;
  final BaseMotorController leftDriveMotorVictor;
  final DifferentialDrive drive;

  Encoder rightEncoder;
  Encoder leftEncoder;

  static double MAXPOWERCHANGE = .16 ;

  public AnalogGyro navx;

  private final DifferentialDriveOdometry odometry;

  private boolean brakeMode = true;

  // These three are brought over from differential drive in order to bring over it's curvature
  // drive code
  private double m_quickStopAccumulator;
  private double m_quickStopThreshold;
  private double m_quickStopAlpha;

  @Override
  protected void initDefaultCommand() {
    setDefaultCommand(new DriveTrainCMDS.TankDrive());
  }

  public DriveTrainSubsystem2019(boolean practice) {
  //  System.out.println();

    navx = new AnalogGyro(Portmap.GYRO);

    // This assumes the robot will start backwards at the beginning of the match.
    // navx.setAngleAdjustment(180);

    rightEncoder = new Encoder(Portmap.RIGHT_ENCODER_1, Portmap.RIGHT_ENCODER_2, false);
    leftEncoder = new Encoder(Portmap.LEFT_ENCODER_1, Portmap.LEFT_ENCODER_2, true);

    rightDriveMotorTalon = new WPI_TalonSRX(Portmap.RIGHTDRIVETALON);
    leftDriveMotorTalon = new WPI_TalonSRX(Portmap.LEFTDRIVETALON);
    if (practice) {
      rightDriveMotorVictor = new WPI_TalonSRX(Portmap.RIGHTDRIVEVICTOR);
      leftDriveMotorVictor = new WPI_TalonSRX(Portmap.LEFTDRIVEVICTOR);
    }
    else {
      rightDriveMotorVictor = new VictorSPX(Portmap.RIGHTDRIVEVICTOR);
      leftDriveMotorVictor = new VictorSPX(Portmap.LEFTDRIVEVICTOR);
    }
    // rightDriveMotorVictor.follow(rightDriveMotorTalon);
    // leftDriveMotorVictor.follow(leftDriveMotorTalon);
    // rightDriveMotorTalon.setInverted(true);
    rightDriveMotorTalon.setNeutralMode(NeutralMode.Brake);
    leftDriveMotorTalon.setNeutralMode(NeutralMode.Brake);
    rightDriveMotorVictor.setNeutralMode(NeutralMode.Brake);
    leftDriveMotorVictor.setNeutralMode(NeutralMode.Brake);
    rightDriveMotorTalon.setInverted(true);
    rightDriveMotorVictor.setInverted(true);
    rightDriveMotorVictor.follow(rightDriveMotorTalon);
    leftDriveMotorVictor.follow(leftDriveMotorTalon);

    leftEncoder.setDistancePerPulse(Constants.distancePerPulse);
    rightEncoder.setDistancePerPulse(Constants.distancePerPulse);

    navx.calibrate();
    resetEncoders();

    drive = new DifferentialDrive(leftDriveMotorTalon, rightDriveMotorTalon);

    odometry = new DifferentialDriveOdometry(getAngleRot2d());
  }

  @Override
  public void periodic() {
    updateSmartDashboard();

    odometry.update(getAngleRot2d(), leftEncoder.getDistance(), rightEncoder.getDistance());
  }

  public void leftPower(double requestedPower) {
    double currentPower = leftDriveMotorTalon.get();
    double newPower; 
    if (requestedPower < currentPower) {
      newPower = Math.max(requestedPower, currentPower - MAXPOWERCHANGE);
    } else if (requestedPower > currentPower) {
      newPower = Math.min(requestedPower, currentPower + MAXPOWERCHANGE);
    } else {
      newPower = requestedPower;
    }
    // System.out.println(
    //  String.format(
    //    "Left:requestedPower: %.2f, resultingPower: %.2f, currentPower: %.2f",
    //  requestedPower, newPower, currentPower));
    leftDriveMotorTalon.set(newPower);
  }

  public void rightPower(double requestedPower) {
    double currentPower = rightDriveMotorTalon.get();
    double newPower;
    if (requestedPower < currentPower) {
      newPower = Math.max(requestedPower, currentPower - MAXPOWERCHANGE);
    } else if (requestedPower > currentPower) {
      newPower = Math.min(requestedPower, currentPower + MAXPOWERCHANGE);
    } else {
      newPower = requestedPower;
    }
    // System.out.println(
    //  String.format(
    //    "Right:requestedPower: %.2f, resultingPower: %.2f, currentPower: %.2f",
    //  requestedPower, newPower, currentPower));
    rightDriveMotorTalon.set(newPower);
  }

  // Function to stop instantly
  public void stop() {
    rightDriveMotorTalon.set(0);
    leftDriveMotorTalon.set(0);
  }

  private void updateSmartDashboard() {
    Sendable dataForGyro = navx;
    Sendable pdp = Robot.PDP;
    // SmartDashboard.putBoolean("NAVX CONNECTED", navx.isConnected());
    SmartDashboard.putData("Gyro", dataForGyro);
    SmartDashboard.putData("PDP", pdp);
    // SmartDashboard.putNumber("Heading", navx.getAngle());
    // SmartDashboard.putNumber("Right Encoder", rightEncoder.get());
    // SmartDashboard.putNumber("Left Encoder", leftEncoder.get());
  }

  public void dumbDriveStraight(double power) {
    rightPower(power);
    leftPower(power);
  }
  /*
  * Please note that powers must be negative to move foreward, this is a side effect of the controller axies being reversed
  * from what would be accepted.
  */
  public void keepDriveStraight(double leftDriveVel, double rightDriveVel, double targetAngle) {
    System.out.println("Drive straight " + leftDriveVel);
    double error = 0, correctionFactor;
    error = targetAngle - navx.getAngle();
    correctionFactor = (error / 75.0);

    // todo - best practice - conditions on a separate line should be
    // wrapped in brackets
    if (leftDriveVel > 0.9) leftDriveVel = 0.9;
    else if (leftDriveVel < -0.9) leftDriveVel = -0.9;

    // todo - best practice - conditions on a separate line should be
    // wrapped in brackets
    if (rightDriveVel > 0.9) rightDriveVel = 0.9;
    else if (rightDriveVel < -0.9) rightDriveVel = -0.9;

    if (targetAngle > (navx.getAngle() - 0.5) || targetAngle < (navx.getAngle() + 0.5)) {
      rightPower(((rightDriveVel) - correctionFactor));
      leftPower((leftDriveVel + correctionFactor));
    } else {
      rightPower(rightDriveVel);
      leftPower(leftDriveVel);
    }
  }

  

  public double getAngle() {
    return navx.getAngle();
  }

  public int getEncoderRight() {
    return rightEncoder.get();
  }

  public int getEncoderLeft() {
    return leftEncoder.get();
  }

  public void resetEncoders() {
    rightEncoder.reset();
    leftEncoder.reset();
  }

  public DifferentialDriveWheelSpeeds getWheelSpeeds() {
    return new DifferentialDriveWheelSpeeds(leftEncoder.getRate(), rightEncoder.getRate());
  }

  public Rotation2d getAngleRot2d() {
    return navx.getRotation2d();
  } 

  public Pose2d getPose() {
    return odometry.getPoseMeters();
  }

  public void tankDriveVolts(double leftVolts, double rightVolts) {
    leftDriveMotorTalon.setVoltage(leftVolts);
    rightDriveMotorTalon.setVoltage(-rightVolts);
    drive.feed();
  }

// This is literally just the curvature drive mode from differntial drive - the differnce is that
  // it feeds it to our management system for motor power instead, which means ramping
  // and max power change apply
  public void curveDrive(double speed, double rotation, boolean turnInPlace) {
    speed = MathUtil.clamp(speed, -1.0, 1.0);

    rotation = MathUtil.clamp(rotation, -1.0, 1.0);

    double angularPower;
    boolean overPower;

    if (turnInPlace) {
      if (Math.abs(speed) < m_quickStopThreshold) {
        m_quickStopAccumulator =
            (1 - m_quickStopAlpha) * m_quickStopAccumulator
                + m_quickStopAlpha * MathUtil.clamp(rotation, -1.0, 1.0) * 2;
      }
      overPower = true;
      angularPower = rotation;
    } else {
      overPower = false;
      angularPower = Math.abs(speed) * rotation - m_quickStopAccumulator;

      if (m_quickStopAccumulator > 1) {
        m_quickStopAccumulator -= 1;
      } else if (m_quickStopAccumulator < -1) {
        m_quickStopAccumulator += 1;
      } else {
        m_quickStopAccumulator = 0.0;
      }
    }

    double leftMotorOutput = speed + angularPower;
    double rightMotorOutput = speed - angularPower;

    // If rotation is overpowered, reduce both outputs to within acceptable range
    if (overPower) {
      if (leftMotorOutput > 1.0) {
        rightMotorOutput -= leftMotorOutput - 1.0;
        leftMotorOutput = 1.0;
      } else if (rightMotorOutput > 1.0) {
        leftMotorOutput -= rightMotorOutput - 1.0;
        rightMotorOutput = 1.0;
      } else if (leftMotorOutput < -1.0) {
        rightMotorOutput -= leftMotorOutput + 1.0;
        leftMotorOutput = -1.0;
      } else if (rightMotorOutput < -1.0) {
        leftMotorOutput -= rightMotorOutput + 1.0;
        rightMotorOutput = -1.0;
      }
    }

    // Normalize the wheel speeds
    double maxMagnitude = Math.max(Math.abs(leftMotorOutput), Math.abs(rightMotorOutput));
    if (maxMagnitude > 1.0) {
      leftMotorOutput /= maxMagnitude;
      rightMotorOutput /= maxMagnitude;
    }

    leftPower(leftMotorOutput);
    rightPower(rightMotorOutput);
  }

  public void toggleBrakemode() {
    if (brakeMode) {
      leftDriveMotorTalon.setNeutralMode(NeutralMode.Coast);
      leftDriveMotorVictor.setNeutralMode(NeutralMode.Coast);
      rightDriveMotorTalon.setNeutralMode(NeutralMode.Coast);
      rightDriveMotorVictor.setNeutralMode(NeutralMode.Coast);
    }
    if (!brakeMode) {
      leftDriveMotorTalon.setNeutralMode(NeutralMode.Brake);
      leftDriveMotorVictor.setNeutralMode(NeutralMode.Brake);
      rightDriveMotorTalon.setNeutralMode(NeutralMode.Brake);
      rightDriveMotorVictor.setNeutralMode(NeutralMode.Brake);
    }
    brakeMode = !brakeMode;
  }

  public void enableRamping() {
    MAXPOWERCHANGE = 0.16;
  }

  public void disableRamping() {
    MAXPOWERCHANGE = 2;
  }

}
