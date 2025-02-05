package frc.robot;

// import com.pathplanner.lib.auto.NamedCommands;
// import com.pathplanner.lib.commands.PathPlannerAuto;

import frc.command.*;
import frc.robot.RobotMap.OIConstants;
import frc.subsystems.*;
// import frc.util.AutoSelector;
import edu.wpi.first.math.MathUtil;
import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.RunCommand;
import edu.wpi.first.wpilibj2.command.button.Trigger;


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
    

    
    
    public final IndexerSubsystem indexerSubsystem = new IndexerSubsystem();
    public final IntakeSubsystem intakeSubsystem = new IntakeSubsystem();
    public final LightsSubsystem lightSubsystem = new LightsSubsystem();
    public final ShooterSubsystem shooterSubsystem = new ShooterSubsystem();
    public final ElevatorSubsystem elevatorSubsystem = new ElevatorSubsystem();
    public final LimelightVisionSubsystem limeLightVisionSubsystem = new LimelightVisionSubsystem();
    public final DriveSubsystem drivetrainSubsystem = new DriveSubsystem(limeLightVisionSubsystem);
    public final PhotonVisionSubsystem photonVisionSubsystem = new PhotonVisionSubsystem();
    private final SendableChooser<Command> m_chooser = new SendableChooser<>();

    // public AutoSelector autoSelector = new AutoSelector(drivetrainSubsystem, indexerSubsystem, intakeSubsystem, shooterSubsystem);
    
    SendableChooser<Command> chooser = new SendableChooser<>();

    public OI() {

        initControllers();

        

        manipAButton.whileTrue(new ShootCmd(intakeSubsystem, indexerSubsystem, shooterSubsystem, drivetrainSubsystem, limeLightVisionSubsystem));

        manipBButton.whileTrue(new OverridePreShootCmd(indexerSubsystem,intakeSubsystem, shooterSubsystem));

        manipRightBumper.onTrue(new AmpCmd(intakeSubsystem, indexerSubsystem));

        manipFullscreen.whileTrue(new PreAmpCmd(indexerSubsystem, intakeSubsystem));
        // manipGoogle.whileTrue(new ShootAmpCmd(indexerSubsystem, intakeSubsystem));

        // manipXButton.whileTrue(new HoldPositionCmd(shooterSubsystem));

        manipXButton.whileTrue(new AutoSetWristCmd(shooterSubsystem, limeLightVisionSubsystem.calculateShooterAngle(), limeLightVisionSubsystem));

        manipLeftBumper.whileTrue(new TeleopAlignShootCmd(intakeSubsystem, indexerSubsystem, shooterSubsystem, drivetrainSubsystem, limeLightVisionSubsystem));
      //  .onFalse(new PreShootCmd(indexerSubsystem, intakeSubsystem, shooterSubsystem));

        manipLeftTrigger.onTrue(new IntakeCmd(intakeSubsystem, limeLightVisionSubsystem));
        manipRightTrigger.whileTrue(new Outtake(intakeSubsystem));

        manipStadia.onTrue(new HoldElevator(elevatorSubsystem));
      //  manipStadia.whileTrue(new ShootSeq(intakeSubsystem, indexerSubsystem, shooterSubsystem, drivetrainSubsystem, limeLightVisionSubsystem));

        manipEllipsisButton.whileTrue(new MoveShooterCmd(shooterSubsystem));
        manipMenuButton.whileTrue(new MoveShooterDownCmd(shooterSubsystem));     

        // manipYButton.whileTrue(new SetPowerCmd(shooterSubsystem));

 
      //  shooterSubsystem.setDefaultCommand(new HoldPositionCmd(shooterSubsystem));
        elevatorSubsystem.setDefaultCommand(new ManualElevatorCmd(elevatorSubsystem, () -> -getManipRightY()));
        
      //  shooterSubsystem.setDefaultCommand(new ManualWristCmd(shooterSubsystem, () -> -getManipLeftY()));


        drivetrainSubsystem.setDefaultCommand(
        // The left stick controls translation of the robot.
        // Turning is controlled by the X axis of the right stick.
        new RunCommand(
            () -> drivetrainSubsystem.drive(
                -MathUtil.applyDeadband(driveController.getRawAxis(1)*0.1, OIConstants.kDriveDeadband),
                -MathUtil.applyDeadband(driveController.getRawAxis(0)*0.1, OIConstants.kDriveDeadband),
                -MathUtil.applyDeadband(driveController.getRawAxis(3), OIConstants.kDriveDeadband),
                () -> !driveLeftBumper.getAsBoolean(), true, () -> manipGoogle.getAsBoolean(),
                () -> driveLeftTrigger.getAsBoolean(), () -> driveRightTrigger.getAsBoolean(), () -> manipLeftBumper.getAsBoolean()),
            drivetrainSubsystem));

        // Press A button -> zero gyro heading
        driveAButton.onTrue(new InstantCommand(() -> drivetrainSubsystem.zeroHeading()));

        // Press X button -> set X to not slide
        driveXButton.onTrue(new InstantCommand(() -> drivetrainSubsystem.setX()));

        driveYButton.onTrue(new InstantCommand(() -> {lightSubsystem.isRainbowing = true;}));

        // NamedCommands.registerCommand("Shoot", new ShootCmd(intakeSubsystem,indexerSubsystem,shooterSubsystem,drivetrainSubsystem,limeLightVisionSubsystem));
        // NamedCommands.registerCommand("Amp", new AmpCmd(intakeSubsystem,indexerSubsystem));
        // NamedCommands.registerCommand("Intake", new IntakeCmd(intakeSubsystem, limeLightVisionSubsystem));
        // NamedCommands.registerCommand("RampShoot", new RunShoot(shooterSubsystem));
        // NamedCommands.registerCommand("SetWrist:.95", new ManualSetWrist(shooterSubsystem, .95));
        // NamedCommands.registerCommand("SetWrist:.92", new ManualSetWrist(shooterSubsystem, .92));
        // NamedCommands.registerCommand("SetWrist:.9", new ManualSetWrist(shooterSubsystem, .9));
        // NamedCommands.registerCommand("PreShoot", new PreShootCmd(indexerSubsystem, intakeSubsystem, shooterSubsystem));
        // NamedCommands.registerCommand("OverridePreShoot", new OverridePreShootCmd(indexerSubsystem, intakeSubsystem, shooterSubsystem));
        // NamedCommands.registerCommand("AutoAlignShoot.95", new AlignShootCmdTwo(intakeSubsystem, indexerSubsystem, shooterSubsystem, drivetrainSubsystem, limeLightVisionSubsystem, .95));
        // NamedCommands.registerCommand("AutoAlignShoot.90", new AlignShootCmdTwo(intakeSubsystem, indexerSubsystem, shooterSubsystem, drivetrainSubsystem, limeLightVisionSubsystem, .9));
        // NamedCommands.registerCommand("AutoAlignShoot.94", new AlignShootCmdTwo(intakeSubsystem, indexerSubsystem, shooterSubsystem, drivetrainSubsystem, limeLightVisionSubsystem, .92));
        // NamedCommands.registerCommand("AutoAlignShoot.93", new AlignShootCmdTwo(intakeSubsystem, indexerSubsystem, shooterSubsystem, drivetrainSubsystem, limeLightVisionSubsystem, .905));
        // NamedCommands.registerCommand("AlignShoot.95", new AlignShootCmd3(intakeSubsystem, indexerSubsystem, shooterSubsystem, drivetrainSubsystem, limeLightVisionSubsystem, .96));


        
        // autoChooser = AutoBuilder.buildAutoChooser();

        // SmartDashboard.putData("Auto Chooser", autoChooser);

      //  final Command frontSpeaker2P = new FrontOfSpeaker2PieceAuto(intakeSubsystem, indexerSubsystem, shooterSubsystem, limeLightVisionSubsystem, drivetrainSubsystem);
      //  final Command quickBalanceCone = new QuickBalanceCone(drivetrain, arm, intake, autoMap);

       // chooser.addOption("front speaker 2P", frontSpeaker2P);
       // chooser.addOption("right blue", quickBalanceCone);

       SmartDashboard.putData("Auto choices", chooser);
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

        manipStadia = new JoystickButton(manipController, 11);

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

    private double getManipLeftBumper(){
        return manipController.getRawAxis(4);
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

    /**
   * Use this to pass the autonomous command to the main {@link Robot} class.
   *
   * @return the command to run in autonomous
   */
  // WHEN CHANGING THE AUTO NAME HERE, REMEMBER TO CHANGE THE AUTO NAME IN DRIVESUBSYSTEM (BOTTOM LINES)
  //   public Command getAutonomousCommand() {
  //     //  return new PathPlannerAuto("bottom clear");
  //       // return new PathPlannerAuto("4 piece close center");
  //       // return new PathPlannerAuto("Quick 4 piece close");
  //     //  return new RightSideRedAuto(intakeSubsystem, indexerSubsystem, shooterSubsystem, limeLightVisionSubsystem, drivetrainSubsystem);
  //       // return new RightSideRedAuto(intakeSubsystem, indexerSubsystem, shooterSubsystem, limeLightVisionSubsystem, drivetrainSubsystem);
  //       // return new FrontOfSpeaker2PieceAuto(intakeSubsystem, indexerSubsystem, shooterSubsystem, limeLightVisionSubsystem, drivetrainSubsystem);
  //       // return new TheOnePiece(intakeSubsystem, indexerSubsystem, shooterSubsystem, limeLightVisionSubsystem, drivetrainSubsystem);
  //       // PathPlannerPath path = PathPlannerPath.fromPathFile("Test Run one");

  //       // Create a path following command using AutoBuilder. This will also trigger event markers.
  //       // return AutoBuilder.followPath(path);
  //       // return autoSelector.getSelected();
  // }

}