package frc.robot;


import edu.wpi.first.wpilibj.PowerDistribution;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import frc.util.SwerveTelemetry;

/**
 * Don't change the name of this class since the VM is set up to run this
 */
public class Robot extends TimedRobot {

    /**
     * Initialize all systems here as public & static.
     * Ex: public static System system = new System();
     */
    private OI oi;
    private Command autoCommand;
    SwerveTelemetry frontLeftTelemetry;
    SwerveTelemetry backLeftTelemetry;
    SwerveTelemetry frontRightTelemetry;
    SwerveTelemetry backRightTelemetry;
    PowerDistribution pdh;

  //  public static OI;


    @Override
    public void robotInit() {
        oi = new OI();
        oi.light.rainbow();

        frontLeftTelemetry = new SwerveTelemetry(oi.drivetrain.frontLeft);
        backLeftTelemetry = new SwerveTelemetry(oi.drivetrain.backLeft);
        frontRightTelemetry = new SwerveTelemetry(oi.drivetrain.frontRight);
        backRightTelemetry = new SwerveTelemetry(oi.drivetrain.backRight);
        
        pdh = new PowerDistribution();


    }

    /**
     * This function is called every robot packet, no matter the mode. Use
     * this for items like diagnostics that you want ran during disabled,
     * autonomous, teleoperated and test.
     */
    @Override
    public void robotPeriodic() {
        CommandScheduler.getInstance().run();
        double voltage = pdh.getVoltage();
        SmartDashboard.putNumber("Voltage", voltage);
    }


    @Override
    public void autonomousInit() {
        //Example of setting auto: Scheduler.getInstance().add(YOUR AUTO);
    }

    /**
     * This function is called periodically during autonomous.
     */
    @Override
    public void autonomousPeriodic() {
    }

    /**
     * This function is called periodically during operator control.
     */
    @Override
    public void teleopPeriodic() {
    }

    /**
     * This function is called periodically during test mode.
     */
    @Override
    public void testPeriodic() {
    }
}