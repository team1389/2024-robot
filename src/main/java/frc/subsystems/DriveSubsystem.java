// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.subsystems;

import java.util.function.Supplier;

import com.ctre.phoenix6.hardware.Pigeon2;
import edu.wpi.first.apriltag.AprilTagFieldLayout;
import edu.wpi.first.math.estimator.SwerveDrivePoseEstimator;
import edu.wpi.first.math.filter.SlewRateLimiter;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.math.kinematics.SwerveDriveKinematics;
import edu.wpi.first.math.kinematics.SwerveDriveOdometry;
import edu.wpi.first.math.kinematics.SwerveModulePosition;
import edu.wpi.first.math.kinematics.SwerveModuleState;
import edu.wpi.first.math.util.Units;
import edu.wpi.first.util.WPIUtilJNI;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.smartdashboard.Field2d;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.OI;
import frc.robot.Robot;
import frc.robot.RobotMap;
import frc.robot.RobotMap.DriveConstants;
import frc.util.LimelightHelpers;
import frc.util.SwerveUtils;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class DriveSubsystem extends SubsystemBase {
    private static final double MAX_DRIVE_SPEED = Units.feetToMeters(10);


     public final SwerveDrivePoseEstimator poseEstimator;

     private final LimelightVisionSubsystem limelightVisionSubsystem;
            
    private static final double TRACK_WIDTH_X = Units.inchesToMeters(24.0);
    
    private static final double TRACK_WIDTH_Y = Units.inchesToMeters(24.0);

    public double alignTx;
    
    private final Field2d m_field = new Field2d();
    private final OI oi = Robot.oi;
    
    private SwerveDriveKinematics kinematics = new SwerveDriveKinematics(getModuleTranslations());

  // Create MAXSwerveModules
  private final MAXSwerveModuleSubsystem frontLeft = new MAXSwerveModuleSubsystem(
      DriveConstants.kFrontLeftDrivingCanId,
      DriveConstants.kFrontLeftTurningCanId,
      DriveConstants.kFrontLeftChassisAngularOffset);

  private final MAXSwerveModuleSubsystem frontRight = new MAXSwerveModuleSubsystem(
      DriveConstants.kFrontRightDrivingCanId,
      DriveConstants.kFrontRightTurningCanId,
      DriveConstants.kFrontRightChassisAngularOffset);

  private final MAXSwerveModuleSubsystem backLeft = new MAXSwerveModuleSubsystem(
      DriveConstants.kRearLeftDrivingCanId,
      DriveConstants.kRearLeftTurningCanId,
      DriveConstants.kBackLeftChassisAngularOffset);

  private final MAXSwerveModuleSubsystem backRight = new MAXSwerveModuleSubsystem(
      DriveConstants.kRearRightDrivingCanId,
      DriveConstants.kRearRightTurningCanId,
      DriveConstants.kBackRightChassisAngularOffset);

    private MAXSwerveModuleSubsystem[] modules = new MAXSwerveModuleSubsystem[]{frontLeft, frontRight, backLeft, backRight};
    private SwerveModuleState[] moduleStates = getModuleStates();
    public AprilTagFieldLayout at_field;

  // The gyro sensor
  //  private final ADIS16470_IMU m_gyro = new ADIS16470_IMU();
  private final Pigeon2 pigeon = new Pigeon2(RobotMap.MotorPorts.PIGEON, "rio");

  // Slew rate filter variables for controlling lateral acceleration
  private double m_currentRotation = 0.0;
  private double m_currentTranslationDir = 0.0;
  private double m_currentTranslationMag = 0.0;

  private SlewRateLimiter m_magLimiter = new SlewRateLimiter(DriveConstants.kMagnitudeSlewRate);
  private SlewRateLimiter m_rotLimiter = new SlewRateLimiter(DriveConstants.kRotationalSlewRate);
  private double m_prevTime = WPIUtilJNI.now() * 1e-6;

  Pose2d pose = new Pose2d();
  public boolean commandAlign;

  SwerveDriveOdometry m_odometry;


  /** Creates a new DriveSubsystem. */
  public DriveSubsystem(LimelightVisionSubsystem limelightVisionSubsystem){
      // double x = getAutoStart().getX();
      // double y = getAutoStart().getY();
      // Rotation2d theta = getAutoStart().getRotation();

    //   m_odometry = new SwerveDriveOdometry(
    //   DriveConstants.kDriveKinematics,
    //   Rotation2d.fromDegrees(-pigeon.getAngle()),
    //   new SwerveModulePosition[] {
    //       frontLeft.getPosition(),
    //       frontRight.getPosition(),
    //       backLeft.getPosition(),
    //       backRight.getPosition()
    //   },
    //   // new Pose2d(x, y, theta));
    //   // new Pose2d(1.38,5.57,new Rotation2d(0)));

    // commandAlign = false;
    poseEstimator = new SwerveDrivePoseEstimator(RobotMap.DriveConstants.kDriveKinematics,
            new Rotation2d(0), getModulePositions(), new Pose2d());
    // AutoBuilder.configureHolonomic(
    //         this::getPose, // Robot pose supplier
    //         this::resetPose, // Method to reset odometry (will be called if your auto has a starting pose)
    //         () -> kinematics.toChassisSpeeds(getModuleStates()), // ChassisSpeeds supplier. MUST BE ROBOT RELATIVE
    //         this::driveRobotRelative, // Method that will drive the robot given ROBOT RELATIVE ChassisSpeeds
    //         new HolonomicPathFollowerConfig( // HolonomicPathFollowerConfig, this should likely live in your Constants class
    //                 new PIDConstants(5.0, 0.0, 0.0), // Translation PID constants
    //                 new PIDConstants(5.0, 0.0, 0.0), // Rotation PID constants
    //                 4.5, // Max module speed, in m/s 
    //                 0.4, // Drive base radius in meters. Distance from robot center to furthest module.
    //                 new ReplanningConfig() // Default path replanning config. See the API for the options here
    //         ),
    //         () -> {
    //           // Boolean supplier that controls when the path will be mirrored for the red alliance
    //           // This will flip the path being followed to the red side of the field.
    //           // THE ORIGIN WILL REMAIN ON THE BLUE SIDE

    //           var alliance = DriverStation.getAlliance();
    //           if (alliance.isPresent()) {
    //             return alliance.get() == DriverStation.Alliance.Red;
    //           }
    //           return false;
    //         },
    //         this // Reference to this subsystem to set requirements
    // );
    this.limelightVisionSubsystem = limelightVisionSubsystem;  
  }

  @Override
  public void periodic() {

    //limelight
    alignTx = LimelightHelpers.getTX("");
    // Update the odometry in the periodic block
    m_odometry.update(
        Rotation2d.fromDegrees(-pigeon.getAngle()),
        new SwerveModulePosition[] {
            frontLeft.getPosition(),
            frontRight.getPosition(),
            backLeft.getPosition(),
            backRight.getPosition()
        });
       // m_field.setRobotPose(m_poseEstimator.getEstimatedPosition());
    modules = new MAXSwerveModuleSubsystem[]{frontLeft, frontRight, backLeft, backRight};


    // SmartDashboard.putNumber("Robot Speed", modules[0].getVelocityDrive());
    // SmartDashboard.putNumber("Robot Heading", getHeading());
    // SmartDashboard.putNumber("Pigeon Angle", -pigeon.getAngle());
    // SmartDashboard.putNumber("Pigeon yaw", pigeon.getYaw().getValueAsDouble());
    // SmartDashboard.putNumber("Pigeon pitch", pigeon.getPitch().getValueAsDouble());
    // SmartDashboard.putNumber("Pigeon roll", pigeon.getRoll().getValueAsDouble());
    // SmartDashboard.putString("Robot Location", getPose().getTranslation().toString());
    // SmartDashboard.putNumber("Turn rate", getTurnRate());

    m_field.setRobotPose(getPose());
    // This method will be called once per scheduler run
    // SmartDashboard.putNumber("rotation", getPose().getRotation().getDegrees());

    // SmartDashboard.putNumber("robot pose X", getPose().getX());
    // SmartDashboard.putNumber("robot pose Y", getPose().getY());

    //SmartDashboard.putNumber("Speed", m_poseEstimator);
    // SmartDashboard.putData("Field", m_field);
    // SmartDashboard.putNumberArray(
    //     "Odometry",
    //     new double[] {getPose().getX(), getPose().getY(), getPose().getRotation().getDegrees()});

    // SmartDashboard.putNumber("Robot Speed", modules[0].getVelocityDrive());
    // SmartDashboard.putNumber("FL Speed", modules[0].getVelocityDrive());
    // SmartDashboard.putNumber("FR Speed", modules[1].getVelocityDrive());
    // SmartDashboard.putNumber("BL Speed", modules[2].getVelocityDrive());
    // SmartDashboard.putNumber("BR Speed", modules[3].getVelocityDrive());
    
    // SmartDashboard.putNumber("FL angle velocity", modules[0].getVelocitySteer());
    // SmartDashboard.putNumber("FR angle velocity", modules[1].getVelocitySteer());
    // SmartDashboard.putNumber("BL angle velocity", modules[2].getVelocitySteer());
    // SmartDashboard.putNumber("BR angle velocity", modules[3].getVelocitySteer());

   // SmartDashboard.putNumber("State", modules[0].getState());



  }

  /**
   * Returns the currently-estimated pose of the robot.
   *
   * @return The pose.
   */
  public Pose2d getPose() {
    return m_odometry.getPoseMeters();
  }

  public void toggleCommandAlign(){
    commandAlign = !commandAlign;
  }

  /**
   * Resets the odometry to the specified pose.
   *
   * @param pose The pose to which to set the odometry.
   */
  // public void resetOdometry(Pose2d pose) {
  //   // double x = getAutoStart().getX();
  //   // double y = getAutoStart().getY();
  //   // Rotation2d theta = getAutoStart().getRotation();
  //   m_odometry.resetPosition(
  //       Rotation2d.fromDegrees(-pigeon.getAngle()),
  //       new SwerveModulePosition[] {
  //           frontLeft.getPosition(),
  //           frontRight.getPosition(),
  //           backLeft.getPosition(),
  //           backRight.getPosition()
  //       },
  //       // new Pose2d(x, y, theta));
  // }

  /**
   * Method to drive the robot using joystick info.
   *
   * @param xSpeed        Speed of the robot in the x direction (forward).
   * @param ySpeed        Speed of the robot in the y direction (sideways).
   * @param rot           Angular rate of the robot.
   * @param fieldRelative Whether the provided x and y speeds are relative to the
   *                      field.
   * @param rateLimit     Whether to enable rate limiting for smoother control.
   */
  public void drive(double xSpeed, double ySpeed, double rot, Supplier<Boolean> fieldRelative, boolean rateLimit, Supplier<Boolean> isAutoAlign, Supplier<Boolean> slow, Supplier<Boolean> BOOST, Supplier<Boolean> IsMadyannThrowing) {
    
    double xSpeedCommanded;
    double ySpeedCommanded;

    if (rateLimit) {
      // Convert XY to polar for rate limiting
      double inputTranslationDir = Math.atan2(ySpeed, xSpeed);
      double inputTranslationMag = Math.sqrt(Math.pow(xSpeed, 2) + Math.pow(ySpeed, 2));

      // Calculate the direction slew rate based on an estimate of the lateral acceleration
      double directionSlewRate;
      if (m_currentTranslationMag != 0.0) {
        directionSlewRate = Math.abs(DriveConstants.kDirectionSlewRate / m_currentTranslationMag);
      } else {
        directionSlewRate = 500.0; //some high number that means the slew rate is effectively instantaneous
      }
      

      double currentTime = WPIUtilJNI.now() * 1e-6;
      double elapsedTime = currentTime - m_prevTime;
      double angleDif = SwerveUtils.AngleDifference(inputTranslationDir, m_currentTranslationDir);
      if (angleDif < 0.45*Math.PI) {
        m_currentTranslationDir = SwerveUtils.StepTowardsCircular(m_currentTranslationDir, inputTranslationDir, directionSlewRate * elapsedTime);
        m_currentTranslationMag = m_magLimiter.calculate(inputTranslationMag);
      }
      else if (angleDif > 0.85*Math.PI) {
        if (m_currentTranslationMag > 1e-4) { //some small number to avoid floating-point errors with equality checking
          // keep currentTranslationDir unchanged
          m_currentTranslationMag = m_magLimiter.calculate(0.0);
        }
        else {
          m_currentTranslationDir = SwerveUtils.WrapAngle(m_currentTranslationDir + Math.PI);
          m_currentTranslationMag = m_magLimiter.calculate(inputTranslationMag);
        }
      }
      else {
        m_currentTranslationDir = SwerveUtils.StepTowardsCircular(m_currentTranslationDir, inputTranslationDir, directionSlewRate * elapsedTime);
        m_currentTranslationMag = m_magLimiter.calculate(0.0);
      }
      m_prevTime = currentTime;
      
      xSpeedCommanded = m_currentTranslationMag * Math.cos(m_currentTranslationDir);
      ySpeedCommanded = m_currentTranslationMag * Math.sin(m_currentTranslationDir);
      m_currentRotation = m_rotLimiter.calculate(rot);


    } else {
      xSpeedCommanded = xSpeed;
      ySpeedCommanded = ySpeed;
      m_currentRotation = rot;
    }



    // Convert the commanded speeds into the correct units for the drivetrain
    double xSpeedDelivered = xSpeedCommanded * DriveConstants.kMaxSpeedMetersPerSecond;
    double ySpeedDelivered = ySpeedCommanded * DriveConstants.kMaxSpeedMetersPerSecond;
    double rotDelivered = m_currentRotation * DriveConstants.kMaxAngularSpeed;

    if(slow.get()){
      xSpeedDelivered = xSpeedCommanded * DriveConstants.kMaxSpeedMetersPerSecond * .3;
      ySpeedDelivered = ySpeedCommanded * DriveConstants.kMaxSpeedMetersPerSecond * .3;
      rotDelivered = m_currentRotation * DriveConstants.kMaxAngularSpeed * .3;
    }
    if(BOOST.get()){
      xSpeedDelivered = xSpeedCommanded * DriveConstants.kMaxSpeedMetersPerSecond * 1.5;
      ySpeedDelivered = ySpeedCommanded * DriveConstants.kMaxSpeedMetersPerSecond * 1.5;
      rotDelivered = m_currentRotation * DriveConstants.kMaxAngularSpeed * 1.5;
    }
    if(isAutoAlign.get() || IsMadyannThrowing.get()){
      xSpeedDelivered = xSpeedCommanded * DriveConstants.kMaxSpeedMetersPerSecond * .5;
      ySpeedDelivered = ySpeedCommanded * DriveConstants.kMaxSpeedMetersPerSecond * .5;
      rotDelivered = m_currentRotation * DriveConstants.kMaxAngularSpeed * .5;
    }

    //add offset for blue and red
    if(alignTx != 0){
      if(isAutoAlign.get() || commandAlign){
        rotDelivered = -(0.1 * alignTx) + Math.toRadians( 25); 
      }
    }
    

    var swerveModuleStates = DriveConstants.kDriveKinematics.toSwerveModuleStates(
        fieldRelative.get()
            ? ChassisSpeeds.fromFieldRelativeSpeeds(xSpeedDelivered, ySpeedDelivered, rotDelivered, Rotation2d.fromDegrees(-pigeon.getAngle()))
            : new ChassisSpeeds(xSpeedDelivered, ySpeedDelivered, rotDelivered)
             );
            
            SwerveDriveKinematics.desaturateWheelSpeeds(
        swerveModuleStates, DriveConstants.kMaxSpeedMetersPerSecond);
    frontLeft.setDesiredState(swerveModuleStates[0]);
    frontRight.setDesiredState(swerveModuleStates[1]);
    backLeft.setDesiredState(swerveModuleStates[2]);
    backRight.setDesiredState(swerveModuleStates[3]);
  }

  /**
   * Sets the wheels into an X formation to prevent movement.
   */
  public void setX() {
    frontLeft.setDesiredState(new SwerveModuleState(0, Rotation2d.fromDegrees(45)));
    frontRight.setDesiredState(new SwerveModuleState(0, Rotation2d.fromDegrees(-45)));
    backLeft.setDesiredState(new SwerveModuleState(0, Rotation2d.fromDegrees(-45)));
    backRight.setDesiredState(new SwerveModuleState(0, Rotation2d.fromDegrees(45)));
  }

  /**
   * Sets the swerve ModuleStates.
   *
   * @param desiredStates The desired SwerveModule states.
   */
  public void setModuleStates(SwerveModuleState[] desiredStates) {
    SwerveDriveKinematics.desaturateWheelSpeeds(
        desiredStates, DriveConstants.kMaxSpeedMetersPerSecond);
    frontLeft.setDesiredState(desiredStates[0]);
    frontRight.setDesiredState(desiredStates[1]);
    backLeft.setDesiredState(desiredStates[2]);
    backRight.setDesiredState(desiredStates[3]);
  }

  /** Resets the drive encoders to currently read a position of 0. */
  public void resetEncoders() {
    frontLeft.resetEncoders();
    backLeft.resetEncoders();
    frontRight.resetEncoders();
    backRight.resetEncoders();
  }

  /** Zeroes the heading of the robot. */
  public void zeroHeading() {
    pigeon.reset();
    pigeon.setYaw(0);
  }

  /**
   * Returns the heading of the robot.
   *
   * @return the robot's heading in degrees, from -180 to 180
   */
  public double getHeading() {
    return Rotation2d.fromDegrees(-pigeon.getAngle()).getDegrees() % 360 - 180;
  }

  /**
   * Returns the turn rate of the robot.
   *
   * @return The turn rate of the robot, in degrees per second
   */
  public double getTurnRate() {
    return pigeon.getRate() * (DriveConstants.kPigeonReversed ? -1.0 : 1.0);
  }

  public SwerveModuleState[] getModuleStates() {
    SwerveModuleState[] states = new SwerveModuleState[modules.length];
    for (int i = 0; i < modules.length; i++) {
      states[i] = modules[i].getState();
    }
    return states;
  }

  // public void driveFieldRelative(ChassisSpeeds fieldRelativeSpeeds){
  //   driveRobotRelative(ChassisSpeeds.fromFieldRelativeSpeeds(fieldRelativeSpeeds, getPose2d().getRotation()));
  // }

  public SwerveModulePosition[] getModulePositions() {
    return new SwerveModulePosition[] {
        frontRight.getPosition(),
        frontLeft.getPosition(),
        backRight.getPosition(),
        backLeft.getPosition()
    };
}

public void resetPose(Pose2d kms) {
  poseEstimator.resetPosition(getRotation2d(), getModulePositions(), kms);
}

    public Rotation2d getRotation2d() {
        return Rotation2d.fromDegrees(getHeading());
    }
// public void setModuleStates(SwerveModuleState[] desiredStates) {
//     // Normalize to within robot max speed
//     SwerveDriveKinematics.desaturateWheelSpeeds(desiredStates, DriveConstants.MAX_METERS_PER_SEC);
    
//     frontRight.setDesiredState(desiredStates[0], true);
//     frontLeft.setDesiredState(desiredStates[1], true);
//     backRight.setDesiredState(desiredStates[2], true);
//     backLeft.setDesiredState(desiredStates[3], true);
    
// }

public void driveRobotRelative(ChassisSpeeds speeds){
 ChassisSpeeds discreteSpeeds = ChassisSpeeds.discretize(speeds, 0.02);
 SwerveModuleState[] setpointStates = kinematics.toSwerveModuleStates(discreteSpeeds);
 SwerveDriveKinematics.desaturateWheelSpeeds(setpointStates, MAX_DRIVE_SPEED);
 setModuleStates(setpointStates);
}

      public static Translation2d[] getModuleTranslations() {
        return new Translation2d[] {
          new Translation2d(TRACK_WIDTH_X / 2.0, TRACK_WIDTH_Y / 2.0),
          new Translation2d(TRACK_WIDTH_X / 2.0, -TRACK_WIDTH_Y / 2.0),
          new Translation2d(-TRACK_WIDTH_X / 2.0, TRACK_WIDTH_Y / 2.0),
          new Translation2d(-TRACK_WIDTH_X / 2.0, -TRACK_WIDTH_Y / 2.0)
        };
      }

      public ChassisSpeeds getChassisSpeeds(){
        return this.kinematics.toChassisSpeeds(getModuleStates());
      }

      // private Pose2d getAutoStart(){
      //   return PathPlannerAuto.getStaringPoseFromAutoFile("3 middle close piece");
      // }
      //WHEN CHANGING THE AUTO NAME, REMEMBER TO CHANGE THE NAME IN OI AS WELL (DON'T BE ZACH)
      //  private Pose2d getAutoStart(){
      //     // PathPlannerPath jerry = PathPlannerAuto.getPathGroupFromAutoFile("Quick 4 piece close").get(0);
      //   // PathPlannerPath jerry = PathPlannerAuto.getPathGroupFromAutoFile("3 middle close piece").get(0);
      //   // PathPlannerPath jerry = PathPlannerAuto.getPathGroupFromAutoFile("4 piece close center").get(0);
      //   // PathPlannerPath jerry = PathPlannerAuto.getPathGroupFromAutoFile("bottom clear").get(0);
      //   // PathPlannerPath jerry = PathPlannerAuto.getPathGroupFromAutoFile("1 m back").get(0);
      //   var alliance = DriverStation.getAlliance();
      //   if(alliance.isPresent()){
      //     SmartDashboard.putBoolean("in isPresent", true);
      //     if (alliance.get()==DriverStation.Alliance.Red) {
      //       SmartDashboard.putBoolean("in get red", true);
      //           return jerry.flipPath().getPreviewStartingHolonomicPose();
      //         }
      //         else {
      //           SmartDashboard.putBoolean("in get blue", true);
      //           return jerry.getPreviewStartingHolonomicPose();
      //         } 
      //   } else {
      //     SmartDashboard.putBoolean("in isPresent", false);
      //     return jerry.flipPath().getPreviewStartingHolonomicPose();
      //   }
              
      // }
}
/*
 * four hundred lines!
 */