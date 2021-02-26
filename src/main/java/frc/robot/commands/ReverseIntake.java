package frc.robot.commands;

import frc.robot.subsystems.IntakeSubsystem;
import edu.wpi.first.wpilibj2.command.CommandBase;
  
public class ReverseIntake extends CommandBase {
  
  private final IntakeSubsystem m_intake;

  /**
   * Creates a new DriveDistance.
   *
   * @param intake The drive subsystem on which this command will run
   */
  public ReverseIntake(IntakeSubsystem intake) {
    m_intake = intake;
    addRequirements(m_intake);
  }

  @Override
  public void initialize() {
    m_intake.reverse();
  }

  @Override
  public void execute() {
  }

  @Override
  public void end(boolean interrupted) {
  }

  @Override
  public boolean isFinished() {
    return true;
  }
}
