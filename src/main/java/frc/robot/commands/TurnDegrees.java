package frc.robot.commands;

import java.util.function.DoubleSupplier;

import edu.wpi.first.wpilibj2.command.CommandBase;
import com.revrobotics.CANPIDController;
import com.revrobotics.ControlType;
import frc.robot.subsystems.DriveSubsystem;
import frc.robot.Constants.*;

/**
 * A command to drive the robot with joystick input (passed in as {@link DoubleSupplier}s). Written
 * explicitly for pedagogical purposes - actual code should inline a command this simple with {@link
 * edu.wpi.first.wpilibj2.command.RunCommand}.
 */
public class TurnDegrees extends CommandBase {
  private final DriveSubsystem m_drive;
  private double m_targetHeading;
  private double m_speed;
  private double m_rspeed, m_lspeed;
  private double m_initHeading;
  private double m_accumulatedHeading;
  private double m_turnRadius;
  private boolean m_finished = false;
  private int m_direction;
  private CANPIDController m_flSpeedPID;
  private CANPIDController m_frSpeedPID;
  private double outerSpeed;
  private double innerSpeed;
  private boolean reverse;

  /**
   * Creates a new DefaultDrive.
   *
   * @param subsystem The drive subsystem this command wil run on.
   * @param degrees   The control input for driving forwards/backwards
   * @param speed     The control input for turning
   */
  public TurnDegrees(DriveSubsystem subsystem, double degrees, double speed, int direction, double radius) {
      m_drive = subsystem;
      m_targetHeading = degrees;
      m_speed = speed;
      m_direction = direction;
      m_turnRadius = radius;
      System.out.println("Created");
      m_flSpeedPID = m_drive.getFrontLeftSparkMax().getPIDController();
      m_frSpeedPID = m_drive.getFrontRightSparkMax().getPIDController();
      m_drive.getBackLeftSparkMax().follow(m_drive.getFrontLeftSparkMax());
      m_drive.getBackRightSparkMax().follow(m_drive.getFrontRightSparkMax());
      m_flSpeedPID.setP(.00026);
      m_frSpeedPID.setP(.00026);
      m_flSpeedPID.setI(0);
      m_frSpeedPID.setI(0);
      m_flSpeedPID.setD(0);
      m_frSpeedPID.setD(0);
      addRequirements(m_drive);
  }

  @Override
  public void initialize() {

    System.out.println("Starting");
    m_drive.setBrake();


    //calculate the motor speeds required to turn a specific radius
    double robotRadius = .5*DriveConstants.kDriveWidth;
    double speedRatio = (m_turnRadius-robotRadius)/(m_turnRadius+robotRadius);
    outerSpeed = m_speed;
    innerSpeed = m_speed*speedRatio;

    System.out.println("outerSpeed: " + outerSpeed);
    System.out.println("innerSpeed: " + innerSpeed);

    
      
       //get the initial heading
       m_initHeading = m_drive.getGyro();

       //reset the accumulated gyro
       m_drive.resetCompleteRotations();
    }

    @Override
    public void execute() {

      //get the current heading
      m_accumulatedHeading = m_drive.getAcumulatedHeading();

      //if the goal is reached, stop and set the command to finished
      if(m_direction == DriveConstants.kRight && m_accumulatedHeading > m_targetHeading) {
        m_frSpeedPID.setReference(0, ControlType.kVoltage);
        m_flSpeedPID.setReference(0, ControlType.kVoltage);
        m_finished = true;
      }else if(m_direction == DriveConstants.kLeft && m_accumulatedHeading < m_targetHeading) {
        m_frSpeedPID.setReference(0, ControlType.kVoltage);
        m_flSpeedPID.setReference(0, ControlType.kVoltage);
        m_finished = true;
      } else if(m_direction == DriveConstants.kLeft) { //if the goal is not reached, keep the pid loop running
        m_frSpeedPID.setReference(outerSpeed * 5676 , ControlType.kVelocity);
        m_flSpeedPID.setReference(innerSpeed * 5676 , ControlType.kVelocity);
      } else if(m_direction == DriveConstants.kRight) {
        m_frSpeedPID.setReference(innerSpeed * 5676 , ControlType.kVelocity);
        m_flSpeedPID.setReference(outerSpeed * 5676 , ControlType.kVelocity);
      }
    }

    @Override
    public boolean isFinished() {
      return m_finished;
    }

    @Override
    public void end(boolean interrupted) {
      System.out.println("Done");
/*          m_drive.getFrontLeftSparkMax().setInverted(false);
          m_drive.getBackLeftSparkMax().setInverted(false);
          m_drive.getFrontRightSparkMax().setInverted(false);
          m_drive.getBackRightSparkMax().setInverted(false);
*/
      //m_drive.setCoast();
  
      m_drive.arcadeDrive(0, 0);
    }
}