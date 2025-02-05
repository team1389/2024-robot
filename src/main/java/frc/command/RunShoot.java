package frc.command;


import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import frc.subsystems.ShooterSubsystem;

public class RunShoot extends Command{
    ShooterSubsystem shooter;
    Timer timer = new Timer();

    public RunShoot(ShooterSubsystem shooter){
        this.shooter = shooter;
    }

    @Override
    public void initialize(){
        timer.reset();
        timer.start();
    }

    @Override
    public void execute(){
     //   SmartDashboard.putNumber("Shooter timer", timer.get());
        shooter.runShoot(7000);
    }

    @Override
    public void end(boolean interrupted){
        shooter.stop();
    }

    @Override
    public boolean isFinished(){
        return timer.get() > 3.5;
    }
}
