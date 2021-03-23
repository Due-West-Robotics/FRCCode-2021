// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.Joystick;
import frc.robot.Constants.*;
import frc.robot.subsystems.CameraSubsystem;
import frc.robot.subsystems.DriveSubsystem;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import frc.robot.commands.*;
import frc.robot.commands.Autonomous.AutoNav.*;
import frc.robot.commands.Autonomous.GalacticSearch.*;
import frc.robot.commands.Autonomous.*;
import frc.robot.subsystems.IntakeSubsystem;

/**
 * This class is where the bulk of the robot should be declared. Since Command-based is a
 * "declarative" paradigm, very little robot logic should actually be handled in the {@link Robot}
 * periodic methods (other than the scheduler calls). Instead, the structure of the robot (including
 * subsystems, commands, and button mappings) should be declared here.
 */
public class RobotContainer {
  // The robot's subsystems
  private final DriveSubsystem m_robotDrive = new DriveSubsystem();
  private final IntakeSubsystem m_intakeSubsystem = new IntakeSubsystem();
  private final CameraSubsystem m_cameraSubsystem = new CameraSubsystem();

  // The autonomous routines

  // A simple auto routine that drives forward a specified distance, and then stops.
  /*private final Command m_simpleAuto =
      new DriveDistance(
          AutoConstants.kAutoDriveDistanceInches, AutoConstants.kAutoDriveSpeed, m_robotDrive);

 /* // A complex auto routine that drives forward, drops a hatch, and then drives backward.
  private final Command m_complexAuto = new ComplexAuto(m_robotDrive, m_hatchSubsystem);
*/
  //A chooser for autonomous commands
  SendableChooser<Command> m_chooser = new SendableChooser<>();


  // The driver's controller
  GenericHID m_driverController = new Joystick(0);

  private final Command m_arcadeDrive = new JoystickDrive(m_robotDrive,
  () -> m_driverController.getY(GenericHID.Hand.kLeft),
  () -> m_driverController.getX(GenericHID.Hand.kRight));

  private final Command m_defaultDrive = new DefaultDrive(m_robotDrive);

  private final Command myBarrelRacing = new BarrelRacing(m_robotDrive);
  private final Command myBouncePath = new BouncePath(m_robotDrive);
  private final Command mySlalomPath = new SlalomPath(m_robotDrive);
  private final Command myTestCommand = new CameraAndIntake(m_robotDrive, m_cameraSubsystem, m_intakeSubsystem);

  private Command GalacticAChooser() {
    Command pathABlue = new PathABlue(m_robotDrive);
    Command pathARed = new PathARed(m_robotDrive);
    Command defaultDrive = new DefaultDrive(m_robotDrive);
    
    if (m_cameraSubsystem.GetTargetHorizontalOffset() > -4 && m_cameraSubsystem.GetTargetHorizontalOffset() < 4 && m_cameraSubsystem.GetTargetHorizontalOffset() != 0){
      return pathARed;
    }
    else if (m_cameraSubsystem.GetTargetHorizontalOffset() == 0) {
      return pathABlue;}
    else {
      return defaultDrive;
    }
  }

  private Command GalacticBChooser() {
    Command pathBBlue = new PathBBlue(m_robotDrive);
    Command pathBRed = new PathBRed(m_robotDrive);
    Command defaultDrive = new DefaultDrive(m_robotDrive);

    if (m_cameraSubsystem.GetTargetHorizontalOffset() < 0){
      return pathBRed;
    }
    else if (m_cameraSubsystem.GetTargetHorizontalOffset() == 0) {
      return pathBBlue;}
    else {
      return defaultDrive;
    }
  }

  /** The container for the robot. Contains subsystems, OI devices, and commands. */
  public RobotContainer() {
    // Configure the button bindings
    configureButtonBindings();

    // Configure default commands
    // Set the default drive command to split-stick arcade drive
    m_robotDrive.setDefaultCommand(
        // A split-stick arcade command, with forward/backward controlled by the left
        // hand, and turning controlled by the right.
        new JoystickDrive(
            m_robotDrive,
            () -> m_driverController.getY(GenericHID.Hand.kLeft),
            () -> m_driverController.getX(GenericHID.Hand.kRight)));

    /*m_intakeSubsystem.setDefaultCommand(
      // A split-stick arcade command, with forward/backward controlled by the left
      // hand, and turning controlled by the right.

      new DefaultIntake(
          m_intakeSubsystem));
    */

   // Add commands to the autonomous command chooser
    m_chooser.setDefaultOption("Barrel Racing", myBarrelRacing);
    m_chooser.addOption("Bounce Path", myBouncePath);
    m_chooser.addOption("Slalom Path", mySlalomPath);
    //m_chooser.addOption("Galactic Search Path A", GalacticAChooser);
    //m_chooser.addOption("Galactic Search Path B", GalacticBChooser);

    //Put the chooser on the dashboard
    Shuffleboard.getTab("Autonomous").add(m_chooser);
  }

  /**
   * Use this method to define your button->command mappings. Buttons can be created by
   * instantiating a {@link GenericHID} or one of its subclasses ({@link
   * edu.wpi.first.wpilibj.Joystick} or {@link XboxController}), and then passing it to a {@link
   * edu.wpi.first.wpilibj2.command.button.JoystickButton}.
   */
  private void configureButtonBindings() {
    // Grab the hatch when the 'A' button is pressed.
    new JoystickButton(m_driverController, OIConstants.kButtonIntakeOn).whenPressed(new StartIntake(m_intakeSubsystem));
    new JoystickButton(m_driverController, OIConstants.kButtonIntakeOff).whenPressed(new StopIntake(m_intakeSubsystem));
    new JoystickButton(m_driverController, OIConstants.kButtonIntakeReverse).whenPressed(new ReverseIntake(m_intakeSubsystem));
    }
    



  public Command getAutonomousCommand() {
    return m_chooser.getSelected();
  }
   /**
   * Use this to pass the autonomous command to the main {@link Robot} class.
   *
   * @return the command to run in autonomous
   */

  public void arcadeDrive() {
    m_arcadeDrive.schedule();
  }

  public void resetGyro(){
    m_robotDrive.resetGyro();
  }

  public double getGyro(){
    return m_robotDrive.getGyro();
  }

  public void calibrateGyro() {
    m_robotDrive.calibrateGyro();
  }

  public void encoderInit(){
    m_robotDrive.setDistancePerPulse();
  }

  public void resetEncoders() {
    m_robotDrive.resetEncoders();
  }
  public Command testCommand() {
    Command testCommand = new AutoTest(m_robotDrive);
    return testCommand;
  }
}