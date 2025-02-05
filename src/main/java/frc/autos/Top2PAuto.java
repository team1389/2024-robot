// package frc.autos;

// import com.pathplanner.lib.path.PathPlannerPath;

// import edu.wpi.first.math.geometry.Pose2d;
// import edu.wpi.first.wpilibj.DriverStation;
// import edu.wpi.first.wpilibj2.command.Command;
// import edu.wpi.first.wpilibj2.command.Commands;
// import frc.util.AutoGenerator;
// import frc.util.PathPlannerBase;

// /** Add your docs here. */
// public class Top2PAuto extends AutoPaths{

//     @Override
//     public Command load(AutoGenerator autos) {
//         String p1Name = "close_top_piece_1";
//         PathPlannerPath p1 = PathPlannerPath.fromPathFile(p1Name);
//         var alliance = DriverStation.getAlliance();
    
//         Pose2d startingPose = null;
//         if (alliance.isPresent()) {
//             if (alliance.get() == DriverStation.Alliance.Red){
//                 startingPose = p1.flipPath().getPreviewStartingHolonomicPose();
//             } else {
//                 startingPose = p1.getPreviewStartingHolonomicPose();
//             }
//         } 
        
//         return Commands.sequence(
//             autos.scoringSequence(.95, 3000),   
//             autos.resetOdometry(startingPose),
//             autos.pathIntake(p1Name),
//             autos.scoringSequence(.9, 3500),
//             PathPlannerBase.followTrajectory(p1Name)
//         );
//     }
    
// }