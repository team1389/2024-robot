package frc.robot;

import java.util.HashMap;

import org.photonvision.PhotonCamera;

import com.pathplanner.lib.auto.AutoBuilder;
import com.pathplanner.lib.auto.NamedCommands;
import com.pathplanner.lib.commands.PathPlannerAuto;
import com.pathplanner.lib.path.PathPlannerPath;

import frc.command.*;
import frc.robot.RobotMap.OIConstants;
import frc.subsystems.*;
import frc.util.DPadButton;
import frc.util.DPadButton.Direction;
import edu.wpi.first.math.MathUtil;
import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.RunCommand;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import edu.wpi.first.wpilibj2.command.button.Trigger;

import com.revrobotics.CANSparkBase.IdleMode;

public class OI {


    private GenericHID driveController;
    private Trigger driveRightBumper, driveLeftBumper;
    private Trigger driveAButton;
    private Trigger driveXButton;
    private Trigger driveBButton;
    private Trigger driveYButton;
    private Trigger driveRightTrigger;
    private Trigger driveLeftTrigger;



    private XboxController manipController;
    private Trigger manipEllipsisButton;
    private Trigger manipMenuButton;
    private Trigger manipFullscreen;
    private Trigger manipStadia;
    private Trigger manipGoogle;


    private Trigger manipRightBumper;
    private Trigger manipLeftBumper;
    private Trigger manipRightTrigger;
    private Trigger manipLeftTrigger;

    private Trigger manipAButton;
    private Trigger manipBButton;
    private Trigger manipXButton;
    private Trigger manipYButton;

    private Trigger manipUp;
    private Trigger manipLeft;
    private Trigger manipDown;
    private Trigger manipRight;
    

    public final DriveSubsystem drivetrain = new DriveSubsystem();
    
    public final Indexer indexer = new Indexer();

    public final Intake intake = new Intake();
    public final Lights light = new Lights();
    public final Shooter shooter = new Shooter();
    public final Elevator elevator = new Elevator();
    public final LimelightVision limeLightVision = new LimelightVision();
    public final Vision vision = new Vision();

    public OI() {

        initControllers();
        manipAButton.onTrue(new RunIntake(intake).andThen(new InstantCommand(() -> light.setColor(0, 128, 255))));
       // manipBButton.whileTrue(new RunIndexer(indexer, false));
        manipBButton.whileTrue(new RunIndexerAmp(indexer, false));
       // manipYButton.whileTrue(new IndexAndShoot(indexer, intake));
        //  manipYButton.whileTrue(new ShootToSpeaker(shooter, indexer, intake));
       //  manipYButton.onTrue(new ContinueIntake(intake).alongWith(new RunIndexer(indexer, true).alongWith(new Shoot(shooter, intake)))); //TODO
        // manipEllipsisButton.whileTrue(new RunIndexer(indexer, true)); // indexer to amp
        manipXButton.onTrue(new RunElevatorUp(elevator).andThen(new RunIntake(intake)).alongWith(new RunIndexer(indexer, false)));
         manipLeftTrigger.whileTrue(new RunIntake(intake));
        //  manipLeftTrigger.onTrue(new SetElevator(elevator, .605)); //.605 //TODO
        //  manipRightTrigger.onTrue(new SetElevator(elevator, .55)); //.511 //TODO
       // manipLeftTrigger.onTrue(new RunIntake(intake));
         manipRightTrigger.whileTrue(new RunOuttake(intake));
        // manipMenuButton.whileTrue(new RunOuttake(intake));
        manipLeftBumper.whileTrue(new Shoot(shooter, intake).alongWith(new HoldPosition(shooter)));
        manipRightBumper.whileTrue(new RunIntake(intake).alongWith(new RunIndexer(indexer, true)));
        // manipYButton.whileTrue(new AlignShooter(shooter, shooter));
       // manipGoogle.onTrue(new InstantCommand(() -> shooter.setTargetAngle(shooter.getWristPos())).alongWith(new InstantCommand(() -> shooter.holdPosition())));
        manipGoogle.whileTrue(new SetWrist(shooter));
        manipEllipsisButton.whileTrue(new MoveShooter(shooter));
        manipMenuButton.whileTrue(new MoveShooterDown(shooter));
        // manipStadia.whileTrue(new AutoAlign(drivetrain, limeLightVision));

        // Cool new way to make a drive command by passing in Suppliers for the
        // joysticks
        // drivetrain.setDefaultCommand(new TeleOpDrive(
        //         drivetrain,
        //         () -> getDriveLeftY(),
        //         () -> getDriveLeftX(),
        //         () -> getDriveRightX(),
        //         () -> getDriveRightY(),
        //         () -> getDriveLeftBumper(), // By default be in field oriented
        //         () -> !getDriveRightBumper(), // Slow function
        //         () -> driveXButton.getAsBoolean(), // Hold x position
        //         () -> driveRightTrigger.getAsBoolean(),
        //         () -> driveRightTrigger.getAsBoolean(),//auto alignment
        //         () -> driveController.getRawAxis(5),
        //         limeLightVision) // flip
        // );

            drivetrain.setDefaultCommand(
        // The left stick controls translation of the robot.
        // Turning is controlled by the X axis of the right stick.
        new RunCommand(
            () -> drivetrain.drive(
                -MathUtil.applyDeadband(driveController.getRawAxis(1), OIConstants.kDriveDeadband),
                -MathUtil.applyDeadband(driveController.getRawAxis(0), OIConstants.kDriveDeadband),
                -MathUtil.applyDeadband(driveController.getRawAxis(3), OIConstants.kDriveDeadband),
                true, true),
            drivetrain));

        
        // shooter.setDefaultCommand(new ManualWrist(shooter, () -> -getManipLeftY()));
        shooter.setDefaultCommand(new HoldPosition(shooter));
        elevator.setDefaultCommand(new ManualElevator(elevator, () -> -getManipRightY()));
        


        // Press A button -> zero gyro headingq
        driveAButton.onTrue(new InstantCommand(() -> drivetrain.zeroHeading()));

        // Press X button -> set X to not slide
        driveXButton.onTrue(new InstantCommand(() -> drivetrain.setX()));

        driveYButton.onTrue(new InstantCommand(() -> {light.isRainbowing = true;}));

        NamedCommands.registerCommand("Shoot", new Shoot(shooter, intake));
        NamedCommands.registerCommand("IndexerToShooter", new RunIndexer(indexer, true));
        NamedCommands.registerCommand("IndexerToAmp", new RunIndexer(indexer, false));
        NamedCommands.registerCommand("RunIntake", new RunIntake(intake));
        
        getAutonomousCommand();

        // Create a path following command using AutoBuilder. This will also trigger event markers.
    }

    /**
     * Initialize JoystickButtons and Controllers
     */
    private void initControllers() {
        driveController = new XboxController(0);
        manipController = new XboxController(1);

        manipAButton = new JoystickButton(manipController,1);
        manipBButton = new JoystickButton(manipController, 2); 
        manipXButton = new JoystickButton(manipController, 3);
        manipYButton = new JoystickButton(manipController, 4);

        manipRightBumper = new JoystickButton(manipController, 6);
        manipRightTrigger = new JoystickButton(manipController, 12);
        manipLeftTrigger = new JoystickButton(manipController, 13);
        manipLeftBumper = new JoystickButton(manipController, 5);

        manipMenuButton = new JoystickButton(manipController, 10);
        manipFullscreen = new JoystickButton(manipController, 15);
        manipGoogle = new JoystickButton(manipController, 14);
        manipEllipsisButton = new JoystickButton(manipController, 9);

        driveRightBumper = new JoystickButton(driveController, 6);
        driveRightTrigger = new JoystickButton(driveController, 12);
        driveLeftTrigger = new JoystickButton(driveController, 13);
        driveLeftBumper = new JoystickButton(driveController, 5);


        driveAButton = new JoystickButton(driveController, 1);
        driveBButton = new JoystickButton(driveController, 2); 
        driveXButton = new JoystickButton(driveController, 3); 
        driveYButton = new JoystickButton(driveController, 4); 


    }

    private double getManipLeftY() {
        return manipController.getRawAxis(1);
    }
    
    private double getManipRightY() {
        return manipController.getRawAxis(4);
    }

    private double getDriveLeftX() {
        return driveController.getRawAxis(0);
    }
    
    private double getDriveLeftY() {
        return -driveController.getRawAxis(1);
    }
    
    private double getDriveRightX() {
        return -driveController.getRawAxis(3); 
    }
    
    private double getDriveRightY() {
        return driveController.getRawAxis(4); 
    }
    
    private boolean getDriveLeftBumper() {
        return !driveController.getRawButton(5);
    }
    
    private boolean getDriveRightBumper() {
        return !driveController.getRawButton(6);
    }

    public Command getAutonomousCommand() {
    return new PathPlannerAuto("Top Drive Shoot");
  }


}