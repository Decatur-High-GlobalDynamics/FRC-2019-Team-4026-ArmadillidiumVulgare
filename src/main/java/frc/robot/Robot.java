/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.wpilibj.BuiltInAccelerometer;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Filesystem;
import edu.wpi.first.wpilibj.PowerDistributionPanel;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.controller.PIDController;
import edu.wpi.first.wpilibj.controller.RamseteController;
import edu.wpi.first.wpilibj.controller.SimpleMotorFeedforward;
import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import edu.wpi.first.wpilibj.interfaces.Accelerometer.Range;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.trajectory.Trajectory;
import edu.wpi.first.wpilibj.trajectory.TrajectoryUtil;
import frc.robot.commands.DriveTrainCMDS;
import frc.robot.commands.GTADriveCmd;
import frc.robot.subsystems.DriveTrainSubsystem2019;
import frc.robot.subsystems.VisionSystem;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.RamseteCommand;
import edu.wpi.first.wpilibj2.command.button.Button;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;

// import frc.robot.subsystems.GyroSubsystem;

/**
 * The VM is configured to automatically run this class, and to call the functions corresponding to
 * each mode, as described in the TimedRobot documentation. If you change the name of this class or
 * the package after creating this project, you must also update the build.gradle file in the
 * project.
 */
public class Robot extends TimedRobot {

  //public static DriveTrainSubsystemPractice driveTrainSubsystem = new DriveTrainSubsystemPractice();
  // DriveTrainSubsystemPractice();
  //public static IntakeSubsystem intakeSubsystem = new IntakeSubsystem();
  // public static DriveTrainSubsystem2018 driveTrainSubsystem = new DriveTrainSubsystem2018();
  public static DriveTrainSubsystem2019 driveTrainSubsystem = new DriveTrainSubsystem2019(false);

  // public static DriveTrainSubsystem2018 driveTrainSubsystem = new DriveTrainSubsystem2018();

  public static VisionSystem visionSystem = new VisionSystem();
  public static PowerDistributionPanel PDP = new PowerDistributionPanel(0);
  //public static FlipperSubsystem flipperSubsystem = new FlipperSubsystem();
  public static BuiltInAccelerometer Accelerometer = new BuiltInAccelerometer(Range.k8G);

  public static OI oi;

  CommandBase m_autonomousCommand;
  SendableChooser<Command> m_chooser = new SendableChooser<>();


  /**
   * This function is run when the robot is first started up and should be used for any
   * initialization code.
   */
  @Override
  public void robotInit() {

    oi = new OI();

    //oi.stick2Button5.whileHeld(new HatchGrabberCMDS.AutoPlaceHatch());

    m_chooser.setDefaultOption("Default Auto", getTrajCommandFromJSON("TestPathOne.wpilib.json"));
    // chooser.addOption("My Auto", new MyAutoCommand());
    // robotChooser.setDefaultOption("Main Bot", new DriveTrainSubsystem2019());
    // robotChooser.addOption("Pratice Bot", new DriveTrainSubsystemPractice());
    SmartDashboard.putData("Auto mode", m_chooser);
    // SmartDashboard.putData("Robot type", robotChooser);
    CameraServer.getInstance().startAutomaticCapture().setResolution(320, 240);

    // driveTrainSubsystem = new DriveTrainSubsystem2019();

  }

  /**
   * This function is called every robot packet, no matter the mode. Use this for items like
   * diagnostics that you want ran during disabled, autonomous, teleoperated and test.
   *
   * <p>This runs after the mode specific periodic functions, but before LiveWindow and
   * SmartDashboard integrated updating.
   */
  @Override
  public void robotPeriodic() {}

  /**
   * This function is called once each time the robot enters Disabled mode. You can use it to reset
   * any subsystem information you want to clear when the robot is disabled.
   */
  @Override
  public void disabledInit() {}

  @Override
  public void disabledPeriodic() {
    CommandScheduler.getInstance().run();
  }

  /**
   * This autonomous (along with the chooser code above) shows how to select between different
   * autonomous modes using the dashboard. The sendable chooser code works with the Java
   * SmartDashboard. If you prefer the LabVIEW Dashboard, remove all of the chooser code and
   * uncomment the getString code to get the auto name from the text box below the Gyro
   *
   * <p>You can add additional auto modes by adding additional commands to the chooser code above
   * (like the commented example) or additional comparisons to the switch structure below with
   * additional strings & commands.
   */
  @Override
  public void autonomousInit() {
    boolean test = m_chooser.getSelected() == null;
    //m_chooser.getSelected().schedule();
    //teleopInit();
    /*
     * String autoSelected = SmartDashboard.getString("Auto Selector",
     * "Default"); switch(autoSelected) { case "My Auto": autonomousCommand
     * = new MyAutoCommand(); break; case "Default Auto": default:
     * autonomousCommand = new ExampleCommand(); break; }
     */

    // schedule the autonomous command (example)

    // System.out.println(intakeSubsystem.rightIntakeMotor.getSelectedSensorVelocity(0));
  }

  /** This function is called periodically during autonomous. */
  @Override
  public void autonomousPeriodic() {
    teleopPeriodic();
  }

  @Override
  public void teleopInit() {
    // This makes sure that the autonomous stops running when
    // teleop starts running. If you want the autonomous to
    // continue until interrupted by another command, remove
    // this line or comment it out.
    if (m_autonomousCommand != null) {
      m_autonomousCommand.cancel();
    }
    Button rightTrigger = new JoystickButton(OI.stick, 8);
    Button leftTrigger = new JoystickButton(OI.stick, 7);
    Button leftBumper = new JoystickButton(OI.stick, 5);
    Button rightBumper = new JoystickButton(OI.stick, 6);
    driveTrainSubsystem.setDefaultCommand(new GTADriveCmd(driveTrainSubsystem, () -> OI.stick.getX(), () -> OI.stick.getThrottle(), () -> rightTrigger.get(), () -> leftTrigger.get()));
    rightBumper.whileHeld(new DriveTrainCMDS.ToggleBrakeCommand(driveTrainSubsystem));
    leftBumper.whileHeld(new DriveTrainCMDS.DisableRampingCommand(driveTrainSubsystem));
  }

  /** This function is called periodically during operator control. */
  @Override
  public void teleopPeriodic() {
    CommandScheduler.getInstance().run();
  }

  /** This function is called periodically during test mode. */
  @Override
  public void testPeriodic() {}

  public Command getTrajCommandFromJSON(String trajectoryJSON) {
    Trajectory traj = new Trajectory();
    try {
      Path dir = Filesystem.getDeployDirectory().toPath();
      Path trajPath = Filesystem.getDeployDirectory().toPath().resolve("paths/output/" + trajectoryJSON);
      traj = TrajectoryUtil.fromPathweaverJson(trajPath);
    } catch (IOException ex) {
      DriverStation.reportError("Unable to open trajectory: " + trajectoryJSON, ex.getStackTrace());
      return null;
    }

    return new RamseteCommand(
      traj, 
      driveTrainSubsystem::getPose, 
      new RamseteController(Constants.kRamseteB, Constants.kRamseteZeta), 
      new SimpleMotorFeedforward(Constants.ksVolts, Constants.kvVoltSecondsPerMeter, Constants.kaVoltSecondsSquaredPerMeter),
      Constants.kDriveKinematics, 
      driveTrainSubsystem::getWheelSpeeds, 
      new PIDController(Constants.kPDriveVel, 0, 0), 
      new PIDController(Constants.kPDriveVel, 0, 0),
      driveTrainSubsystem::tankDriveVolts,
      driveTrainSubsystem
    );
  }
}
