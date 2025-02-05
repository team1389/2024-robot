package frc.command;

import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.subsystems.DriveSubsystem;
import frc.subsystems.IndexerSubsystem;
import frc.subsystems.IntakeSubsystem;
import frc.subsystems.LimelightVisionSubsystem;
import frc.subsystems.ShooterSubsystem;

public class TeleopAlignShootCmd extends SequentialCommandGroup{
    public TeleopAlignShootCmd(IntakeSubsystem intakeSubsystem, IndexerSubsystem indexerSubsystem, ShooterSubsystem shooterSubsystem, DriveSubsystem driveSubsystem, LimelightVisionSubsystem limelightVisionSubsystem){
        addCommands(
            Commands.parallel(
              new AutoSetWristCmd(shooterSubsystem, limelightVisionSubsystem.toEncoderVal(), limelightVisionSubsystem),
                    Commands.sequence(
                        new TeleopAutoShootPIDCmd(shooterSubsystem, 5000, limelightVisionSubsystem),//, //limelightVisionSubsystem.rpmTableForShoot()
                        new PreShootCmd(indexerSubsystem, intakeSubsystem, shooterSubsystem)
            )
            )
        );
    }

}
