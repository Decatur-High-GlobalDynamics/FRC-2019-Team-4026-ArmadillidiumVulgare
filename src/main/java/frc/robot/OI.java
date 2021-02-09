/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj2.command.button.*;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;

// import frc.robot.commands.*;

/**
 * This class is the glue that binds the controls on the physical operator interface to the commands
 * and command groups that allow control of the robot.
 */
public class OI {
  //// CREATING BUTTONS
  // One type of button is a joystick button which is any button on a
  //// joystick.
  // You create one by telling it which joystick it's on and which button
  // number it is.
  public static Joystick stick = new Joystick(0);
  public static Joystick stick2 = new Joystick(1);
  public static Button stick1Button2 = new JoystickButton(stick, 2);
  public static Button stick2Button4 = new JoystickButton(stick2, 4);
  public static Button stick1Button8 = new JoystickButton(stick, 8);
  public static Button stick2Button1 = new JoystickButton(stick2, 1);
  public static Button stick2Button2 = new JoystickButton(stick2, 2);
  public static Button stick2Button7 = new JoystickButton(stick2, 7);
  public static Button stick2Button8 = new JoystickButton(stick2, 8);
  public static Button stick2Button3 = new JoystickButton(stick2, 3);
  public static Button stick2Button9 = new JoystickButton(stick2, 9);
  public static Button stick1Button6 = new JoystickButton(stick, 6);

  public static Button stick1Button5 = new JoystickButton(stick, 5);
  public static Button stick2Button10 = new JoystickButton(stick2, 10);
  public static Button stick2Button6 = new JoystickButton(stick2, 6);
  public static Button stick2Button5 = new JoystickButton(stick2, 5);


  // There are a few additional built in buttons you can use. Additionally,
  // by subclassing Button you can create custom triggers and bind those to
  // commands the same as any other Button.
  public OI() {}

  //// TRIGGERING COMMANDS WITH BUTTONS
  // Once you have a button, it's trivial to bind it to a button in one of
  // three ways:

  // Start the command when the button is pressed and let it run the command
  // until it is finished as determined by it's isFinished method.

  // Run the command while the button is being held down and interrupt it once
  // the button is released.
  // button.whileHeld(new ExampleCommand());

  // Start the command when the button is released and let it run the command
  // until it is finished as determined by it's isFinished method.
  // button.whenReleased(new ExampleCommand());
}
