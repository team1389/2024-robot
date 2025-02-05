// // Copyright (c) FIRST and other WPILib contributors.
// // Open Source Software; you can modify and/or share it under the terms of
// // the WPILib BSD license file in the root directory of this project.

// package frc.util;

// import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
// import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
// import edu.wpi.first.wpilibj2.command.Command;
// import frc.autos.AutoPaths;
// // import frc.autos.Top2PAuto;
// import frc.subsystems.DriveSubsystem;
// import frc.subsystems.IndexerSubsystem;
// import frc.subsystems.IntakeSubsystem;
// import frc.subsystems.ShooterSubsystem;


// /** Add your docs here. */
// public class AutoSelector {
//     private SendableChooser<AutoPaths> chooser = new SendableChooser<AutoPaths>();

//     AutoGenerator autos;

//     public AutoSelector(DriveSubsystem drivetrain, IndexerSubsystem indexer, IntakeSubsystem intake, ShooterSubsystem shooter){
//         autos = new AutoGenerator(drivetrain, indexer, intake, shooter);

//         // chooser.setDefaultOption("1P_Stay", new AUTO_JustShoot());
//         //  chooser.addOption("2P_Top", new Top2PAuto());
//         // chooser.addOption("2P_Middle", new AUTO_2P_Mid());

//         // chooser.addOption("3P_Top", new AUTO_3P_Top());
//         // chooser.addOption("3P_Middle", new AUTO_3P_Mid());
//         // chooser.addOption("4P_MidLine_3_1st", new AUTO_MidLine_FourP_One());
        
//         // chooser.addOption("4P_Top", new AUTO_4P_Top());

//         SmartDashboard.putData("Auto Selector", chooser);
//     }

//     public Command getSelected() {
//         return chooser.getSelected().load(autos);
//     }

// }