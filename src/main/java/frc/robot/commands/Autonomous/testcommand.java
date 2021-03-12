// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.Autonomous;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.robot.subsystems.DriveSubsystem;
import frc.robot.subsystems.CameraSubsystem;
import frc.robot.Constants.*;
import frc.robot.commands.*;

public class testcommand extends SequentialCommandGroup {

  DriveSubsystem m_drive;
  CameraSubsystem m_camera;

  public testcommand(DriveSubsystem driveSubsystem, CameraSubsystem cameraSubsystem) {
    m_drive = driveSubsystem;
    m_camera = cameraSubsystem;

    addCommands(new CameraTurn(m_camera, m_drive));
  }
}
