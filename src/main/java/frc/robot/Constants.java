package frc.robot;

import edu.wpi.first.wpilibj.kinematics.DifferentialDriveKinematics;

public class Constants {
    // TODO: Set all of these using characterization tool
    public static final double ksVolts = 1.21;
    public static final double kvVoltSecondsPerMeter = 3.32;
    public static final double kaVoltSecondsSquaredPerMeter = 0.0438;
    public static final double kPDriveVel = 0.203;
    // TODO: Measure the horizontal distance between wheels and set trackwidth
    public static final double kTrackwidthMeters = 1.3025332494619235;
    public static final DifferentialDriveKinematics kDriveKinematics =
        new DifferentialDriveKinematics(kTrackwidthMeters);
    // TODO: We should set this - I'm leaving it at 3 for now bc I know our robot is capable of it
    public static final double kMaxSpeedMetersPerSecond = 3;
    public static final double kMaxAccelerationMetersPerSecondSquared = 3;
    // These are the values explicity recommended as correct for almost all bots
    public static final double kRamseteB = 2;
    public static final double kRamseteZeta = 0.7;
    // This is most likely correct - it's the distance in meters per encoder pulse
    public static final double distancePerPulse = 0.00193739;
}