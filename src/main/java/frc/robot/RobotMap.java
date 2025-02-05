package frc.robot;

import com.revrobotics.CANSparkBase.IdleMode;

import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.kinematics.SwerveDriveKinematics;
import edu.wpi.first.math.trajectory.TrapezoidProfile;
import edu.wpi.first.math.util.Units;

/**
 * define Hardware Ports in here
 */
public class RobotMap {


        // Distance between right and left wheels (meters)
        public static final double ROBOT_WIDTH = 0.498475;//19 5/8 in
        // Distance between front and back wheels (meters)
        public static final double ROBOT_LENGTH = 0.4953; //19.5 in

        // Note positive x is forward
        // Wheel order: FR, FL, BR, BL
        public static final SwerveDriveKinematics driveKinematics = new SwerveDriveKinematics(
                new Translation2d(ROBOT_LENGTH / 2, -ROBOT_WIDTH / 2),
                new Translation2d(ROBOT_LENGTH / 2, ROBOT_WIDTH / 2),
                new Translation2d(-ROBOT_LENGTH / 2, -ROBOT_WIDTH / 2),
                new Translation2d(-ROBOT_LENGTH / 2, ROBOT_WIDTH / 2));

    public static final class MotorPorts{
        public static final int SHOOT_BOTTOM = 11; 
        public static final int SHOOT_TOP = 10; 
        public static final int WRIST_MOTOR = 12; 
        public static final int INDEXER_MOTOR = 14; 
        public static final int INTAKE_MOTOR = 13;
        public static final int ELEVATOR_MOTOR = 16;

        public static final int WRIST_ENCODER = 8;
        public static final int ELEVATOR_ENCODER = 7;

        public static final int PIGEON = 18;

    }

    public static final class IntakeConstants {
        public static final double kDistanceWithoutNode = 46.7;
    }

    public static final class LightConstants {
        public static final int LED_PORT = 7;
        public static final int COUNT = 60;

    }

    public static final class ShooterConstants {
        //Shooter Parameters

        public static final double kTopRPM = 3000; //top
        public static final double kBottomRPM = 3000; //bottom
        
        public static final double kTop_P = .1;
        public static final double kTop_I = 0;//change
        public static final double kTop_D = 0;
        public static final double kTop_FF = 1.75;
        public static final double kTop_MIN_OUTPUT = 0.3;
        public static final double kTop_MAX_OUTPUT = 1;

        public static final double kBottom_P = .1;
        public static final double kBottom_I = 0;//change
        public static final double kBottom_D = 0;
        public static final double kBottom_FF = 1.75;
        public static final double kBottom_MIN_OUTPUT = 0.3;
        public static final double kBottom_MAX_OUTPUT = 1;

        public static final double kWrist_P = 0.055;
        public static final double kWrist_I = 0.01;
        public static final double kWrist_D = 0.0;

        public static final double kMinWristAngle = 0.8;
        public static final double kMaxWristAngle = 0.98;

        public static final double AprilTagHeight = 4.697916667; // height of the center of the speaker apriltag to ground (ft) //h2
        public static final double LimelightHeight = 26/12; // distance from the center of the limelight lens to ground //h1
        public static final double TagToSpeakerHeight = 1.896192542; // height from center of apriltag to center of speaker //s
        public static final double LimelightAngle = 22.5; // how many degrees back is the limelight rotated from perfectly vertical (deg) //a1

        public static final double SpeakerXDistfromCenter = 8.270621; 
        public static final double SpeakerYDistfromCenter = 1.442212;
        public static final double XOffset = 0.0254;
        public static final double YOffset = 0.0889;
        public static final double dis_LL_to_bumpers = 1.333;// ft


    }

    public static final class VisionConstants {
        //vision constants

        public static final double kLimelightHeight = 24.5; //Vertical distance in inches from center of robot to center of limelight 
        public static final double kSpeakerAprilTagHeight = 53.88; //Vertical distance in inches from floor to the central april tags on the speaker
    }

    public static final class DriveConstants {
    // Driving Parameters - Note that these are not the maximum capable speeds of
    // the robot, rather the allowed maximum speeds
    public static final double kMaxSpeedMetersPerSecond = 0.5; //10, //4.8, 5.74
    public static final double kMaxAngularSpeed = Math.PI; // radians per second 2*math.pi

    public static final double kDirectionSlewRate = .2; // radians per second //1.2, 10000
    public static final double kMagnitudeSlewRate = .2; // percent per second (1 = 100%) //1.8, 10000
    public static final double kRotationalSlewRate = .2; // percent per second (1 = 100%)

    public static final double kTrackRadius = Units.inchesToMeters(19.6 * Math.sqrt(2)/2);

    // Chassis configuration
    public static final double kTrackWidth = Units.inchesToMeters(19.625);
    // Distance between centers of right and left wheels on robot
    public static final double kWheelBase = Units.inchesToMeters(19.5);
    // Distance between front and back wheels on robot
    public static final SwerveDriveKinematics kDriveKinematics = new SwerveDriveKinematics(
        new Translation2d(kWheelBase / 2, kTrackWidth / 2),
        new Translation2d(kWheelBase / 2, -kTrackWidth / 2),
        new Translation2d(-kWheelBase / 2, kTrackWidth / 2),
        new Translation2d(-kWheelBase / 2, -kTrackWidth / 2));

    // Angular offsets of the modules relative to the chassis in radians
    public static final double kFrontLeftChassisAngularOffset = -Math.PI / 2;
    public static final double kFrontRightChassisAngularOffset = 0;
    public static final double kBackLeftChassisAngularOffset = Math.PI;
    public static final double kBackRightChassisAngularOffset = Math.PI / 2;

    // SPARK MAX CAN IDs
    public static final int kFrontLeftDrivingCanId = 2; //FL
    public static final int kRearLeftDrivingCanId = 4; //BL
    public static final int kFrontRightDrivingCanId = 6; //FR
    public static final int kRearRightDrivingCanId = 8; //BR

    public static final int kFrontLeftTurningCanId = 3; //FL
    public static final int kRearLeftTurningCanId = 5; //BL
    public static final int kFrontRightTurningCanId = 7; //FR
    public static final int kRearRightTurningCanId = 9; //BR

    public static final boolean kPigeonReversed = false;
  }

  public static final class ModuleConstants {
    // The MAXSwerve module can be configured with one of three pinion gears: 12T, 13T, or 14T.
    // This changes the drive speed of the module (a pinion gear with more teeth will result in a
    // robot that drives faster).
    public static final int kDrivingMotorPinionTeeth = 14;

    // Invert the turning encoder, since the output shaft rotates in the opposite direction of
    // the steering motor in the MAXSwerve Module.
    public static final boolean kTurningEncoderInverted = true;

    // Calculations required for driving motor conversion factors and feed forward
    public static final double kDrivingMotorFreeSpeedRps = VortexMotorConstants.kFreeSpeedRpm / 60;
    public static final double kWheelDiameterMeters = 0.07; //0.0762//7689, (FL - 0.07721) (FR - 0.07682) (BL - 0.07708) (BR - 0.07726)
    public static final double kWheelCircumferenceMeters = kWheelDiameterMeters * Math.PI;
    // 45 teeth on the wheel's bevel gear, 22 teeth on the first-stage spur gear, 15 teeth on the bevel pinion
    public static final double kDrivingMotorReduction = (45.0 * 22) / (kDrivingMotorPinionTeeth * 15);
    public static final double kDriveWheelFreeSpeedRps = (kDrivingMotorFreeSpeedRps * kWheelCircumferenceMeters)
        / kDrivingMotorReduction;

    public static final double kDrivingEncoderPositionFactor = (kWheelDiameterMeters * Math.PI)
        / kDrivingMotorReduction; // meters
    public static final double kDrivingEncoderVelocityFactor = ((kWheelDiameterMeters * Math.PI)
        / kDrivingMotorReduction) / 60.0; // meters per second

    public static final double shooterEncoderPositionFactor = (kWheelDiameterMeters * Math.PI)
        / kDrivingMotorReduction; // meters

    public static final double kTurningEncoderPositionFactor = (2 * Math.PI); // radians
    public static final double kTurningEncoderVelocityFactor = (2 * Math.PI) / 60.0; // radians per second

    public static final double kTurningEncoderPositionPIDMinInput = 0; // radians
    public static final double kTurningEncoderPositionPIDMaxInput = kTurningEncoderPositionFactor; // radians

    public static final double kDrivingP = 0.04;
    public static final double kDrivingI = 0;
    public static final double kDrivingD = 0;
    public static final double kDrivingFF = 1 / kDriveWheelFreeSpeedRps;
    public static final double kDrivingMinOutput = -1;
    public static final double kDrivingMaxOutput = 1;

    //     public static final double P_TURNING = 0.075; // PID constant
    //     public static final double I_TURNING = 0.000001; // PID Constant
    //     public static final double D_TURNING = 0.00025; // PID constant
    //     public static final double P_DRIVE = 0.25; // PID constant

    public static final double kTurningP = 1; //1
    public static final double kTurningI = 0; //0
    public static final double kTurningD = 0; //0
    public static final double kTurningFF = 0;
    public static final double kTurningMinOutput = -1;
    public static final double kTurningMaxOutput = 1;

    public static final IdleMode kDrivingMotorIdleMode = IdleMode.kBrake;
    public static final IdleMode kTurningMotorIdleMode = IdleMode.kBrake;

    public static final int kDrivingMotorCurrentLimit = 50; // amps
    public static final int kTurningMotorCurrentLimit = 20; // amps
  }

  public static final class OIConstants {
    public static final int kDriverControllerPort = 0;
    public static final double kDriveDeadband = 0.05;
  }

  public static final class AutoConstants {
    public static final double kMaxSpeedMetersPerSecond = 3;
    public static final double kMaxAccelerationMetersPerSecondSquared = 3;
    public static final double kMaxAngularSpeedRadiansPerSecond = Math.PI;
    public static final double kMaxAngularSpeedRadiansPerSecondSquared = Math.PI;

    public static final double kPXController = 1;
    public static final double kPYController = 1;
    public static final double kPThetaController = 1;

    // Constraint for the motion profiled robot angle controller
    public static final TrapezoidProfile.Constraints kThetaControllerConstraints = new TrapezoidProfile.Constraints(
        kMaxAngularSpeedRadiansPerSecond, kMaxAngularSpeedRadiansPerSecondSquared);
  }

  public static final class VortexMotorConstants {
    public static final double kFreeSpeedRpm = 6784; //5676 for neo
  }

  
}