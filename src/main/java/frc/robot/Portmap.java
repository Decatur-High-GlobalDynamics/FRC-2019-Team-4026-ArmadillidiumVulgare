package frc.robot;

public class Portmap {
  // Motor controllers on CAN
  public static final int LEFTDRIVETALON = 1;
  public static final int LEFTDRIVEVICTOR = 6;
  public static final int RIGHTDRIVETALON = 2;
  public static final int RIGHTDRIVEVICTOR = 7;
  public static final int HATCHGRABBER = 5;
  public static final int LEFTFLIPPER = 3;
  public static final int RIGHTFLIPPER = 4;

  // Solenoids
  public static final int GRABBER_RELEASED = 4;
  public static final int GRABBER_GRAB = 6;
  public static final int GRABBER_STOWED = 2;
  public static final int GRABBER_UP = 3;
  public static final int CAMERA_IN = 0;
  public static final int CAMERA_OUT = 1;

  // Digital Inputs
  // Limit switches
  public static final int HATCH_SENSOR_SWITCH1 = 7;
  //Not Connected
  public static final int HATCH_SENSOR_SWITCH2 = 8;
  //Not Connected
  public static final int FLIPPER_REVERSE_LIMIT = 5;

  public static final int FLIPPER_FOREWARD_LIMIT = 0;
  // Encoders
  public static final int RIGHT_ENCODER_1 = 1;
  public static final int RIGHT_ENCODER_2 = 2;
  public static final int LEFT_ENCODER_1 = 3;
  public static final int LEFT_ENCODER_2 = 4;

  // Analog sensors
  public static final int GYRO = 0;
  public static final int ULTRASONIC = 1;

  public static double clipOneToOne(double val) {
    val = Math.min(1, val);
    val = Math.max(-1, val);
    return val;
  }
}
