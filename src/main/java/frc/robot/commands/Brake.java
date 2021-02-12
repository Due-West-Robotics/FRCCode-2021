package frc.robot.commands;

import java.util.function.DoubleSupplier;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.DriveSubsystem;

/**
 * A command to drive the robot with joystick input (passed in as {@link DoubleSupplier}s). Written
 * explicitly for pedagogical purposes - actual code should inline a command this simple with {@link
 * edu.wpi.first.wpilibj2.command.RunCommand}.
 */
public class Brake extends CommandBase {
  private final DriveSubsystem m_drive;

  /**
   * Creates a new DefaultDrive.
   *
   * @param subsystem The drive subsystem this command wil run on.
   */
  public Brake(DriveSubsystem subsystem) {
      m_drive = subsystem;
      addRequirements(m_drive);
  }

  // Called when the command is initially scheduled.
    @Override
    public void initialize() {
        m_drive.setBrake();
    }

    @Override
    public void execute() {
    }

  
    // Returns true when the command should end.
    @Override
    public boolean isFinished() {
      return false;
    }
      
    // Called once the command ends or is interrupted.
    @Override
    public void end(boolean interrupted) {}
}