package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;

// import frc.robot.Robot;

public class TeleOpCMD extends CommandBase {
  private CommandBase TankDriveCMD = null;

  protected CommandBase getTankDriveCMD() {
    if (TankDriveCMD == null) {
      TankDriveCMD = new DriveTrainCMDS.TankDrive();
    }
    return TankDriveCMD;
  }

  @Override
  public void execute() {
    System.out.println("teleop1");
    // double left = -Robot.oi.stick.getY();
    // double right = -Robot.oi.stick.getThrottle();
    CommandBase driveTrainCMD = getTankDriveCMD();
    driveTrainCMD.schedule();
    System.out.println("teleop");
  }

  @Override
  public boolean isFinished() {
    return false;
  }

  @Override
  public void end(boolean interrupted) {
    TankDriveCMD.cancel();
  }
}
