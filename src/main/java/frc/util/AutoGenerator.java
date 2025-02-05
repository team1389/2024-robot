// package frc.util;

// // import com.pathplanner.lib.auto.AutoBuilder;
// // import com.pathplanner.lib.util.HolonomicPathFollowerConfig;
// // import com.pathplanner.lib.util.PIDConstants;
// // import com.pathplanner.lib.util.ReplanningConfig;

// import edu.wpi.first.math.geometry.Pose2d;
// import edu.wpi.first.wpilibj.DriverStation;
// import edu.wpi.first.wpilibj2.command.Command;
// import edu.wpi.first.wpilibj2.command.InstantCommand;
// import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
// import edu.wpi.first.wpilibj2.command.RunCommand;
// import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
// import edu.wpi.first.wpilibj2.command.WaitCommand;
// import frc.command.IntakeCmd;
// import frc.command.PreShootCmd;
// import frc.robot.RobotMap;
// import frc.subsystems.DriveSubsystem;
// import frc.subsystems.IndexerSubsystem;
// import frc.subsystems.IntakeSubsystem;
// import frc.subsystems.LimelightVisionSubsystem;
// import frc.subsystems.ShooterSubsystem;

// public class AutoGenerator {
//     private DriveSubsystem drivetrain;
//     IndexerSubsystem indexer;
//     IntakeSubsystem intake;
//     ShooterSubsystem shooter; 
//     LimelightVisionSubsystem limelight;

  
//   public AutoGenerator(DriveSubsystem drivetrain, IndexerSubsystem indexer, IntakeSubsystem intake, ShooterSubsystem shooter) {
//     this.drivetrain = drivetrain;
//     this.indexer = indexer;
//     this.intake = intake;
//     this.shooter = shooter;
    
//     // AutoBuilder.configureHolonomic(drivetrain::getPose, drivetrain::resetPose, drivetrain::getChassisSpeeds, drivetrain::driveRobotRelative,
//     //  new HolonomicPathFollowerConfig(new PIDConstants(2.55, 0.00008,0.0003), new PIDConstants(5.0, 0,0), RobotMap.DriveConstants.kMaxSpeedMetersPerSecond, RobotMap.DriveConstants.kTrackRadius, new ReplanningConfig())
//     // , ()->{
//     //   var alliance = DriverStation.getAlliance();
//     //                 if (alliance.isPresent()) {
//     //                     return alliance.get() == DriverStation.Alliance.Red;
//     //                 }
//     //                 return false;
//     // }, drivetrain);


//   //  registerAllCommands();
    
//   }

//   public Command resetOdometry(Pose2d pose){
//       return new InstantCommand(
//         // () -> drivetrain.resetOdometry(pose)
//       );
//     }


//     public Command scoringSequence(){
//       return new RunCommand(()->shooter.runShoot(3000), shooter)
//       .until(()->shooter.isAtTargetRPM(2500))
//      // .andThen(new PreShootCmd(indexer, intake, shooter))
//       .andThen(new RunCommand(()->indexer.moveToShoot()))
//       .alongWith(new InstantCommand(()->intake.runIntake()));
//     }

//     public Command shootingSeq(double setpoint, int rpm){
//       return new SequentialCommandGroup(
//         new InstantCommand(()->shooter.setWrist(setpoint)),
//         new WaitCommand(.25),
//         new RunCommand(()->shooter.runShoot(rpm), shooter)
//       .until(()->shooter.isAtTargetRPM(rpm-500))
//       .andThen(new PreShootCmd(indexer, intake, shooter))
//       ); 
//     }

//     public Command scoringSequence(double setpoint, int rpm){
//       return new SequentialCommandGroup(
//         new InstantCommand(()->shooter.setWrist(setpoint)),
//         new WaitCommand(.25),
//         new RunCommand(()->shooter.runShoot(rpm), shooter)
//       .until(()->shooter.isAtTargetRPM(rpm-500))
//       .andThen(new RunCommand(()->indexer.moveToShoot()))
//       .alongWith(new InstantCommand(()->intake.runIntake()))
//       );
//     }

//      public Command scoringSequence(double setpoint, int rpm, double delay){
//       return new SequentialCommandGroup(
//         new InstantCommand(()->shooter.setWrist(setpoint)),
//         new WaitCommand(delay),
//         new RunCommand(()->shooter.runShoot(rpm), shooter)
//       .until(()->shooter.isAtTargetRPM(rpm-500))
//       .andThen(new RunCommand(()->indexer.moveToShoot()))
//       .alongWith(new InstantCommand(()->intake.runIntake()))
//       );
//     }

//     //TODO: Test
//     public Command pathIntake(String path){
//       return new ParallelCommandGroup(
//         new IntakeCmd(intake,limelight), //runIntake(),
//         PathPlannerBase.followTrajectory(path)
//       );
//     }


//     // public Command runIntake(){
//     //   return new ParallelCommandGroup(
//     //     new InstantCommand(()->pivot.goToAngle(75)),
//     //     new InstantCommand(()->index.starttimer()),
//     //     new RunCommand(()->index.setMotorSpeed(Constants.Intake.kIndexSpeed), index),
//     //     new RunCommand(()->intake.setMotorSpeed(Constants.Intake.kIntakingSpeed))).until(
//     //       ()->index.CurrentLimitSpike()).andThen(
//     //     new RunCommand(()->index.setMotorSpeed(0.1)).withTimeout(0.025).andThen(new ParallelCommandGroup(
//     //       new InstantCommand(()->index.setMotorSpeed(0)),
//     //       new InstantCommand(()->intake.setMotorSpeed(0))))
//     //   );
//     // }

//     // public Command stopIntake(){
//     //   return new ParallelCommandGroup(
//     //     new InstantCommand(()->indexer.stop()),
//     //     new InstantCommand(()->intake.stop())
//     //   );
//     // }

//     // public Command stopAll(){
//     //   return new ParallelCommandGroup(
//     //     new InstantCommand(()->indexer.stop()),
//     //     new InstantCommand(()->intake.stop()),
//     //     new InstantCommand(()->shooter.stop())
//     //   );
//     // }

//     // public Command setPivotSetpoint(double setpoint){
//     //   return new InstantCommand(()->pivot.setPivotSetpoint(setpoint));
//     // }

//     // public Command dumpAuto(){
//     //   return new ParallelCommandGroup(
//     //     setPivotSetpoint(Constants.Pivot.kSpeakerAngleSP), 
//     //     new CMD_Shoot(shooter, index, drivetrain, intake, pivot)
//     //     );
//     // }
// }
