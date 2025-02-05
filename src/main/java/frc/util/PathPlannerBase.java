// package frc.util;

// import edu.wpi.first.wpilibj2.command.Command;
// import frc.robot.OI;
// import frc.robot.Robot;
// import frc.subsystems.DriveSubsystem;

// import com.pathplanner.lib.auto.AutoBuilder;
// import com.pathplanner.lib.path.PathConstraints;
// import com.pathplanner.lib.path.PathPlannerPath;

// /** Util class for all Path Planner auto builders/generators */
// public class PathPlannerBase {

//    //OI oi = new OI();
// //  static final DriveSubsystem drivetrain = oi.drivetrain;
//   static final PathConstraints constraints = new PathConstraints(.5, 0.5, 1, 0.5);

//   public Command followPathCommand(String pathName){
//     PathPlannerPath path = PathPlannerPath.fromPathFile(pathName);


//     return AutoBuilder.pathfindThenFollowPath(path, constraints);
//   }

//   public static Command followTrajectory(String PathName){
//     PathPlannerPath path = PathPlannerPath.fromPathFile(PathName);
//     return AutoBuilder.pathfindThenFollowPath(path, constraints);
//   }
// }