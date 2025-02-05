package frc.robot;


import edu.wpi.first.wpilibj.PowerDistribution;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import edu.wpi.first.wpilibj2.command.ScheduleCommand;
import frc.subsystems.DriveSubsystem;
import frc.subsystems.ShooterSubsystem;

/**
 * Don't change the name of this class since the VM is set up to run this
 */
public class Robot extends TimedRobot {

    private Command m_autonomousCommand;


    /**
     * Initialize all systems here as public & static.
     * Ex: public static System system = new System();
     */
    public static OI oi = new OI();
    private Command autoCommand;
    private DriveSubsystem drivetrain;
    private ShooterSubsystem shooter;
    // SwerveTelemetry frontLeftTelemetry;
    // SwerveTelemetry backLeftTelemetry;
    // SwerveTelemetry frontRightTelemetry;
    // SwerveTelemetry backRightTelemetry;
    PowerDistribution pdh;




    @Override
    public void robotInit() {
        oi.lightSubsystem.rainbow();


        // frontLeftTelemetry = new SwerveTelemetry(oi.drivetrain.frontLeft);
        // backLeftTelemetry = new SwerveTelemetry(oi.drivetrain.backLeft);
        // frontRightTelemetry = new SwerveTelemetry(oi.drivetrain.frontRight);
        // backRightTelemetry = new SwerveTelemetry(oi.drivetrain.backRight);
        
        pdh = new PowerDistribution();
        //DO NOT RE-ADD PIGEON CONFIG
        oi.shooterSubsystem.resetWristPos();

    }

    /**
     * This function is called every robot packet, no matter the mode. Use
     * this for items like diagnostics that you want ran during disabled,
     * autonomous, teleoperated and test.
     */
    @Override
    public void robotPeriodic() {
        CommandScheduler.getInstance().run();
        // oi.robotPeriodic();
        double voltage = pdh.getVoltage();
        // SmartDashboard.putNumber("Voltage", voltage);

        // SmartDashboard.putNumber("Wrist Current", pdh.getCurrent(10));
        // SmartDashboard.putNumber("Elevator Current", pdh.getCurrent(13));
        // SmartDashboard.putNumber("Shooter Left Current", pdh.getCurrent(11));
        // SmartDashboard.putNumber("Shooter Right Current", pdh.getCurrent(14));
        // SmartDashboard.putNumber("Indexer Current", pdh.getCurrent(17));
        // SmartDashboard.putNumber("Intake Current", pdh.getCurrent(16));


        // SmartDashboard.putNumber("BR Drive Current", pdh.getCurrent(1));
        // SmartDashboard.putNumber("BL Drive Current", pdh.getCurrent(12));
        // SmartDashboard.putNumber("FL Drive Current", pdh.getCurrent(8));
        // SmartDashboard.putNumber("FR Drive Current", pdh.getCurrent(5));


        // SmartDashboard.putNumber("FR Turn Current", pdh.getCurrent(6));
        // SmartDashboard.putNumber("FL Turn Current", pdh.getCurrent(9));
        // SmartDashboard.putNumber("BL Turn Current", pdh.getCurrent(2));
        // SmartDashboard.putNumber("BR Turn Current", pdh.getCurrent(0));

        // SmartDashboard.putNumber("Total Current", pdh.getTotalCurrent());

     //   SmartDashboard.putNumber("Shooter voltage", pdh.);

    }

      /** This function is called once each time the robot enters Disabled mode. */
    @Override
    public void disabledInit() {}

    @Override
    public void disabledPeriodic() {}


    @Override
    public void autonomousInit() {
        // m_autonomousCommand = oi.getAutonomousCommand();
       // schedule the autonomous command (example)
        if (m_autonomousCommand != null) {
            m_autonomousCommand.schedule();
        }
    }

    /**
     * This function is called periodically during autonomous.
     */
    @Override
    public void autonomousPeriodic() {
        // CommandScheduler.getInstance().run();

    }

    @Override
    public void teleopInit() {
    //     SwerveTelemetry frontLeftTelemetry = new SwerveTelemetry(oi.drivetrain.frontLeft);
    //     SwerveTelemetry backLeftTelemetry = new SwerveTelemetry(oi.drivetrain.backLeft);
    //     SwerveTelemetry frontRightTelemetry = new SwerveTelemetry(oi.drivetrain.frontRight);
    //     SwerveTelemetry backRightTelemetry = new SwerveTelemetry(oi.drivetrain.backRight);
    //     SendableRegistry.add(frontLeftTelemetry, "Swerve");

    //    // SmartDashboard.putNumber("FL angular", frontLeftTelemetry.get);
       
    //     SendableRegistry.addLW(frontLeftTelemetry, "FL Swerve");
    //     SendableRegistry.addLW(backLeftTelemetry, "BL Swerve");
    //     SendableRegistry.addLW(frontRightTelemetry, "FR Swerve");
    //     SendableRegistry.addLW(backRightTelemetry, "BR Swerve");

    //     oi.drivetrain.frontLeft.resetEncoders();
    //     oi.drivetrain.backLeft.resetEncoders();
    //     oi.drivetrain.frontRight.resetEncoders();
    //     oi.drivetrain.backRight.resetEncoders();
        
       // oi.drivetrain.setAngleAdjustment(0);

    }

    /**
     * This function is called periodically during operator control.
     */
    @Override
    public void teleopPeriodic() {
        // SmartDashboard.putNumber("FL angle", Math.toDegrees(frontLeftTelemetry.getAngle()));
        // SmartDashboard.putNumber("FL speed", frontLeftTelemetry.getSpeed());

        // SmartDashboard.putNumber("BL angle", Math.toDegrees(backLeftTelemetry.getAngle()));
        // SmartDashboard.putNumber("BL speed", backLeftTelemetry.getSpeed());

        // SmartDashboard.putNumber("FR angle", Math.toDegrees(frontRightTelemetry.getAngle()));
        // SmartDashboard.putNumber("FR speed", frontRightTelemetry.getSpeed());

        // SmartDashboard.putNumber("BR angle", Math.toDegrees(backRightTelemetry.getAngle()));
        // SmartDashboard.putNumber("BR speed", backRightTelemetry.getSpeed());
    }

    /**
     * This function is called periodically during test mode.
     */
    @Override
    public void testPeriodic() {
    }
}