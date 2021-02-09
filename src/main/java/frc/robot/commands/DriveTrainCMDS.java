package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Robot;
import frc.robot.subsystems.DriveTrainSubsystem2019;

public class DriveTrainCMDS {
  public static class TankDrive extends CommandBase {

    double rightPower, leftPower;

    public TankDrive() {
      // Use requires() here to declare subsystem dependencies
      addRequirements(Robot.driveTrainSubsystem);
    }

    @Override
    public void execute() {
      Robot.driveTrainSubsystem.leftPower(Robot.oi.stick.getThrottle());
      Robot.driveTrainSubsystem.rightPower(Robot.oi.stick.getY());
    }

    @Override
    public boolean isFinished() {
      return false;
    }
  }

  public static class ToggleBrakeCommand extends CommandBase {
    DriveTrainSubsystem2019 drive;
    /** Creates a new ToggleBrakeCommand. */
    public ToggleBrakeCommand(DriveTrainSubsystem2019 drive) {
      this.drive = drive;
    }

    // Called when the command is initially scheduled.
    @Override
    public void initialize() {
      drive.toggleBrakemode();
    }

    // Called every time the scheduler runs while the command is scheduled.
    @Override
    public void execute() {}

    // Called once the command ends or is interrupted.
    @Override
    public void end(boolean interrupted) {
      drive.toggleBrakemode();
    }

    // Returns true when the command should end.
    @Override
    public boolean isFinished() {
      return false;
    }
  }

  public static class DisableRampingCommand extends CommandBase {
    DriveTrainSubsystem2019 driveTrain;
    /** Creates a new DisableRampingCommand. */
    public DisableRampingCommand(DriveTrainSubsystem2019 driveTrain) {
      // Use addRequirements() here to declare subsystem dependencies.
      this.driveTrain = driveTrain;
    }

    // Called when the command is initially scheduled.
    @Override
    public void initialize() {
      driveTrain.disableRamping();
    }

    // Called every time the scheduler runs while the command is scheduled.
    @Override
    public void execute() {}

    // Called once the command ends or is interrupted.
    @Override
    public void end(boolean interrupted) {
      driveTrain.enableRamping();
    }

    // Returns true when the command should end.
    @Override
    public boolean isFinished() {
      return false;
    }
  }
}