package frc.robot.commands;

import edu.wpi.first.wpilibj.command.Command;
import frc.robot.Portmap;
import frc.robot.Robot;
import jaci.pathfinder.Pathfinder;
import jaci.pathfinder.PathfinderFRC;
import jaci.pathfinder.Trajectory;
import jaci.pathfinder.followers.EncoderFollower;

public class DriveTrainCMDS {
  public static class Turn extends Command {
    private float distance;
    private boolean direction;
    // private float goal;
    // private float startingYaw;
    private double startingAngle;
    private final double MAX_TURN_SPEED = 0.4;

    // Dist is in degrees. If direct is true turn right else turn left
    public Turn(float dist, boolean direct) {
      System.out.println("Constructor Called");
      requires(Robot.driveTrainSubsystem);
      // startingYaw = Robot.gyroSubsystem.getRotation();
      startingAngle = Robot.driveTrainSubsystem.getAngle();
      distance = dist;
      direction = direct;
      // This conditional operator adds to the starting yaw when turning right and vice versa when
      // left
      // goal = direction ? startingYaw + distance : startingYaw - distance;
      // If you go past
      // if (goal < -180) {
      // Calculate by how much
      //    float past = Math.abs(goal - (-180)) % 360;
      // And figure out where you should be.
      //    goal = 180 - past;
      // }
      // if (goal > 180) {
      //    float past = Math.abs(goal - 180) % 360;
      //    goal = -(180 - past);
      // }
    }

    @Override
    protected void execute() {
      System.out.println("Executing");
      if (direction) {
        Robot.driveTrainSubsystem.leftPower(MAX_TURN_SPEED);
        Robot.driveTrainSubsystem.rightPower(-MAX_TURN_SPEED);
      } else {
        Robot.driveTrainSubsystem.leftPower(-MAX_TURN_SPEED);
        Robot.driveTrainSubsystem.rightPower(MAX_TURN_SPEED);
      }
    }

    @Override
    protected boolean isFinished() {
      // TODO: Confirm that getAngle just counts up. If it doesn't it will be a lot harder and goal
      // will be required.
      // Read the documentation, I have confirmed -Walden
      return (float) Math.abs(Robot.driveTrainSubsystem.getAngle() - startingAngle) > distance;
    }

    @Override
    protected void end() {
      System.out.println("ending");
      Robot.driveTrainSubsystem.stop();
    }

    @Override
    protected void interrupted() {
      System.out.println("Interrupted");
      end();
    }
  }

  public static class TankDrive extends Command {

    double rightPower, leftPower;

    public TankDrive() {
      // Use requires() here to declare subsystem dependencies
      requires(Robot.driveTrainSubsystem);
    }

    protected void initialize() {}

    @Override
    protected void execute() {
      Robot.driveTrainSubsystem.leftPower(Robot.oi.stick.getThrottle());
      Robot.driveTrainSubsystem.rightPower(Robot.oi.stick.getY());
    }

    @Override
    protected boolean isFinished() {
      return false;
    }
  }

  public static class DriveStraight extends Command {
    double targetAngle;
    double power;

    public DriveStraight() {
      // Use requires() here to declare subsystem dependencies
      requires(Robot.driveTrainSubsystem);
    }

    protected void initialize() {
      targetAngle = Robot.driveTrainSubsystem.getAngle();
      // targetAngle = Robot.driveTrainSubsystem.navx.getAngle();
    }

    @Override
    protected void execute() {

      // Power when driving straight is the averaging of the stick values
      power = (Robot.oi.stick.getThrottle() + Robot.oi.stick.getY()) / 2;
      // Robot.driveTrainSubsystem.keepDriveStraight(power, power, targetAngle);
      Robot.driveTrainSubsystem.dumbDriveStraight(power);
    }

    @Override
    protected boolean isFinished() {
      return false;
    }
  }

  public static class ArcadeDriveCMD extends Command {
    double rightPower, leftPower;

    public ArcadeDriveCMD() {
      // Use requires() here to declare subsystem dependencies
      requires(Robot.driveTrainSubsystem);
    }

    protected void initialize() {}

    @Override
    protected void execute() {
      rightPower = Robot.oi.stick.getY() - Robot.oi.stick.getZ() * .75;
      leftPower = Robot.oi.stick.getY() + Robot.oi.stick.getZ() * .75;
      rightPower = Portmap.clipOneToOne(rightPower);
      leftPower = Portmap.clipOneToOne(leftPower);
      Robot.driveTrainSubsystem.leftPower(leftPower);
      Robot.driveTrainSubsystem.rightPower(rightPower);
    }

    @Override
    protected boolean isFinished() {
      return false;
    }
  }

  public static class DriveStraightDist extends Command {

    private double heading, ticks, maxPower, minPower;
    private double averageEncoders;
    private boolean isFinished;

    public DriveStraightDist(
        double distanceInch, double maxPowerVal, double minPowerVal, double headingVal) {
      requires(Robot.driveTrainSubsystem);
      ticks = distanceInch * Robot.driveTrainSubsystem.TICKS_PER_INCH;
      heading = headingVal;
      maxPower = maxPowerVal;
      minPower = minPowerVal;
    }

    @Override
    public void initialize() {
      Robot.driveTrainSubsystem.resetEncoders();
    }

    public void execute() {
      System.out.println("Max power " + maxPower);
      averageEncoders =
          (Robot.driveTrainSubsystem.getEncoderLeft() + Robot.driveTrainSubsystem.getEncoderRight())
              / 2;
      double error = ticks - averageEncoders;
      if (error > 1000) {
        Robot.driveTrainSubsystem.keepDriveStraight(-maxPower, -maxPower, heading);
      } else if (error > 15) {
        Robot.driveTrainSubsystem.keepDriveStraight(-minPower, -minPower, heading);
      } else if (error < 15) {
        Robot.driveTrainSubsystem.stop();
        isFinished = true;
      }
      System.out.println("Drive Error" + error);
    }

    @Override
    public boolean isFinished() {
      return isFinished;
    }
  }

  public static class DriveToRightHatchCMD extends Command {
    private double targetAngle, distance;
    private boolean isFinished = false;

    public DriveToRightHatchCMD() {
      requires(Robot.driveTrainSubsystem);
    }

    @Override
    protected void initialize() {
      isFinished = false;
    }

    @Override
    protected void execute() {
      targetAngle = Robot.visionSystem.hatch1.getTargetHeading();
      double power = (Robot.oi.stick.getThrottle() + Robot.oi.stick.getY()) / 2;
      if (power != -100) {
        Robot.driveTrainSubsystem.keepDriveStraight(power, power, targetAngle);
      } else {
        isFinished = true;
        Robot.driveTrainSubsystem.stop();
      }
    }

    @Override
    protected boolean isFinished() {
      return isFinished;
    }
  }

  public static class FollowPath extends Command {
    Trajectory leftTraj;
    Trajectory rightTraj;
    EncoderFollower leftFollow;
    EncoderFollower rightFollow;
    //TODO: Fix these numbers
    final double maxSpeed = 3.772241849;
    final int ticksPerRev = 4096;
    //Diameter in meters
    final double wheelDiameter = (6.0 / 12.0) / 3.2808;


    public FollowPath(Path path) {
      leftTraj = path.leftTraj;
      rightTraj = path.rightTraj;
      requires(Robot.driveTrainSubsystem);
    }

    @Override
    protected void initialize() {
      leftFollow = new EncoderFollower(leftTraj);
      rightFollow = new EncoderFollower(rightTraj);

      leftFollow.configureEncoder(Robot.driveTrainSubsystem.getEncoderLeft(), ticksPerRev, wheelDiameter);
      rightFollow.configureEncoder(Robot.driveTrainSubsystem.getEncoderRight(), ticksPerRev, wheelDiameter);

      leftFollow.configurePIDVA(1.0, 0.0, 0.0, 1 / maxSpeed, 0);
      rightFollow.configurePIDVA(1.0, 0.0, 0.0, 1 / maxSpeed, 0);
    }

    @Override
    protected void execute() {
      if (leftFollow.isFinished() && rightFollow.isFinished()) {
        Robot.driveTrainSubsystem.stop();
      }
      else {
        double left_speed = leftFollow.calculate(Robot.driveTrainSubsystem.getEncoderLeft());
        double right_speed = rightFollow.calculate(Robot.driveTrainSubsystem.getEncoderRight());
        double heading = Robot.driveTrainSubsystem.getAngle();
        double desired_heading = Pathfinder.r2d(leftFollow.getHeading());
        double heading_difference = Pathfinder.boundHalfDegrees(desired_heading - heading);
        double turn =  0.8 * (-1.0/80.0) * heading_difference;
        Robot.driveTrainSubsystem.leftPower(left_speed + turn);
        Robot.driveTrainSubsystem.rightPower(right_speed - turn);
      }
    }

    @Override
    protected boolean isFinished() {
      return leftFollow.isFinished() && rightFollow.isFinished();
    }
  }

  public static class Path {
    Trajectory leftTraj;
    Trajectory rightTraj;

    public Path(Trajectory left, Trajectory right) {
      leftTraj = left;
      rightTraj = right;
    }
  }

  public static class AutoLineUpToNinety extends Command {
    private double target;
    // Proportional gain
    private double p;
    // derivative gain
    private double d;
    // integral gain
    private double i;
    private double lastError;
    private double sumError;
    private double error;

    public AutoLineUpToNinety(double p, double d, double i) {
      requires(Robot.driveTrainSubsystem);
      this.p = p;
      this.d = d;
      this.i = i;
    }

    @Override
    protected void initialize() {
      double current = Robot.driveTrainSubsystem.getAngle();
      if (current > 315 || current <= 45) {
        target = 0;
      } else if (current > 45 && current <= 135) {
        target = 90;
      } else if (current > 135 && current <= 225) {
        target = 180;
      } else if (current > 225 && current <= 315) {
        target = 270;
      }
      lastError = 0;
      sumError = 0;
    }

    @Override
    protected void execute() {
      double current = Robot.driveTrainSubsystem.getAngle();
      error = target - current;
      if (target == 0 && current > 180) {
        error = 360 - current;
      }
      if (Math.abs(error) <= 0.5) {
        Robot.driveTrainSubsystem.stop();
      } else {
        sumError += error;
        double goalPower = p * (error) + d * ((error) - lastError) + i * sumError;
        goalPower = Math.min(1, Math.max(-1, goalPower));
        lastError = error;
        Robot.driveTrainSubsystem.rightPower(-goalPower);
        Robot.driveTrainSubsystem.leftPower(goalPower);
      }
    }

    @Override
    protected boolean isFinished() {
      return Math.abs(error) <= 0.5;
    }
  }
}
