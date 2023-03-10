package org.firstinspires.ftc.teamcode.Autos.Auto_TrajectorySequences.MainAutos;

import com.acmerobotics.roadrunner.geometry.Pose2d;

import org.firstinspires.ftc.teamcode.MechanismTemplates.Arm;
import org.firstinspires.ftc.teamcode.MechanismTemplates.Claw;
import org.firstinspires.ftc.teamcode.MechanismTemplates.OdoPod;
import org.firstinspires.ftc.teamcode.MechanismTemplates.Slide;
import org.firstinspires.ftc.teamcode.MechanismTemplates.poleAlignment;
import org.firstinspires.ftc.teamcode.TeleOps.AprilTags.PowerPlay_AprilTagDetectionDeposit;
import org.firstinspires.ftc.teamcode.drive.SampleMecanumDrive;
import org.firstinspires.ftc.teamcode.trajectorysequence.TrajectorySequence;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.acmerobotics.roadrunner.trajectory.Trajectory;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

@Config
@Autonomous(name = "testAuto")
public class SPLINEAutoLeftSide extends PowerPlay_AprilTagDetectionDeposit{
    public static double endTangent1 = 40;
    public static double openingStrafe = 8;
	// [ARM]
		private Arm armControl;
		public static int armPosition = 0;

	// [SLIDES]
		private Slide slideControl;

	// [CLAW]
	    private Claw clawControl;

	//[POLEALIGNMENT]
		private poleAlignment alignmentControl;

	// [OPENING MOVE]
	public static double openingX = 37.7;
	public static double openingY = -1;

	// [MEDIUM JUNCTION]
	public static double mediumX1 = 45;
	public static double mediumY1 = -4.5;
	public static double mediumX2 = 45;
	public static double mediumY2 = -4.5;
	public static double mediumX3 = 45;
	public static double mediumY3 = -4.5;
	public static double mediumX4 = 45;
	public static double mediumY4 = -4.5;
	//public static double mediumX5 = 46.5;
	//public static double mediumY5 = -3.25;

	//Parking related variables
	public static double zone2X = 51;
	public static double zone2Y = 0;
	public static double zone1backwards =-22;
	public static double zone3forwards = -24; //-24.5

	// [CONE STACK]
	public static double xConeStack1 = 48.7;
	public static double yConeStack = 27.85;
	public static double xConeStack2 = 48.45;
	public static double xConeStack3 = 48.2;
	public static double xConeStack4 = 48;
	//public static double xConeStack5 = 52.7;
	public static double coneStackHeading = 89;
	//public static double coneStackForward = 8.7;

	// [OPENING MOVE --> MEDIUM JUNCTION]
	public static double openingHeading = 90;
	// [1] MEDIUM JUNCTION --> CONE STACK
	public static double coneStackHeading1 = 93;
	// [1] CONE STACK --> MEDIUM JUNCTION
	public static double mediumHeading1 = 220; // no end tangent for splineTo

	// [2] MEDIUM JUNCTION --> CONE STACK
	public static double coneStackHeading2 = 45;
	// [2] CONE STACK --> MEDIUM JUNCTION
	public static double mediumHeading2 = 229;

	// [3] MEDIUM JUNCTION --> CONE STACK
	public static double coneStackHeading3 = 45;
	// [3] CONE STACK --> MEDIUM JUNCTION
	public static double mediumHeading3 = 227;

	// [4] MEDIUM JUNCTION --> CONE STACK
	public static double coneStackHeading4 = 45;
	// [4] CONE STACK --> MEDIUM JUNCTION
	public static double mediumHeading4 = 229;//ALL OF THESE USED TO BE 230

	public void initialize(){
		armControl = new Arm(hardwareMap);
		slideControl = new Slide(hardwareMap);
		alignmentControl=new poleAlignment(hardwareMap);
	}

	public void scan(){
		super.runOpMode();
	}

	@Override
	public void runOpMode() {
		Pose2d startPose = new Pose2d(0, 0, Math.toRadians(180));
		SampleMecanumDrive bot = new SampleMecanumDrive(hardwareMap);
		bot.setPoseEstimate(startPose);
		initialize();

		TrajectorySequence junction = bot.trajectorySequenceBuilder(startPose)
				.UNSTABLE_addTemporalMarkerOffset(0, () -> {
					clawControl = new Claw(hardwareMap);
					OdoPod odoControl = new OdoPod(hardwareMap);
				})
				// claw close
				.waitSeconds(0.45)
				.UNSTABLE_addTemporalMarkerOffset(1, () -> {
					slideControl.setMidJunction();
					armControl.setExtake();
					clawControl.toggleWristRotate();
				})
				.lineToLinearHeading(new Pose2d(openingX, openingY, Math.toRadians(openingHeading)))
				.UNSTABLE_addTemporalMarkerOffset(0.15, () -> {
					slideControl.setIntakeOrGround(); // TODO change this to custom height since we'll be at the conestack height
				})

				.waitSeconds(0.25)
				.UNSTABLE_addTemporalMarkerOffset(0, () -> {
					clawControl.toggleOpenClose();
				})
				.waitSeconds(0.25)

				.UNSTABLE_addTemporalMarkerOffset(0, () -> {
					armControl.setIntake();
					clawControl.toggleWristRotate();
				})
				.waitSeconds(0.25)
				.UNSTABLE_addTemporalMarkerOffset(0, () -> {
					slideControl.setCustom(650);//600
				})
				//.splineTo(new Vector2d(xConeStack, yConeStack), Math.toRadians(coneStackHeading1))
				//.strafeRight(openingStrafe)

				//.splineToLinearHeading(new Pose2d(xConeStack, yConeStack, Math.toRadians(coneStackHeading1)), Math.toRadians(endTangent1))
				//.forward(coneStackForward)

				.lineToLinearHeading(new Pose2d(xConeStack1, -1, Math.toRadians(coneStackHeading)))
				.forward(yConeStack)

				.UNSTABLE_addTemporalMarkerOffset(0, () -> {
					alignmentControl.raiseServo();
					clawControl.toggleOpenClose();
				})
				.waitSeconds(0.2)
				.UNSTABLE_addTemporalMarkerOffset(0, () -> {
					slideControl.setCustom(1325);//1275
				})
				.waitSeconds(0.5)

				.UNSTABLE_addTemporalMarkerOffset(0.5, () -> {
					slideControl.setCustom(1050);//950
					armControl.setAutoExtake();
					clawControl.toggleWristRotate();
				})

				.setReversed(true)
				.splineTo(new Vector2d(mediumX1, mediumY1), Math.toRadians(mediumHeading1))
				.setReversed(false)
				.UNSTABLE_addTemporalMarkerOffset(0, () -> {
					slideControl.setIntakeOrGround();
				})
				.waitSeconds(0.2)
				.UNSTABLE_addTemporalMarkerOffset(0, () -> {
					clawControl.toggleOpenClose();
				})
				.waitSeconds(0.2)

				.UNSTABLE_addTemporalMarkerOffset(0, () -> {
					armControl.setIntake();
					clawControl.toggleWristRotate();
				})
				.waitSeconds(0.25)
				.UNSTABLE_addTemporalMarkerOffset(0, () -> {
					slideControl.setCustom(475);//475
				})
				//.splineTo(new Vector2d(xConeStack, yConeStack), Math.toRadians(coneStackHeading1))
				//.strafeRight(openingStrafe)

				//.splineToLinearHeading(new Pose2d(xConeStack, yConeStack, Math.toRadians(coneStackHeading1)), Math.toRadians(endTangent1))
				//.forward(coneStackForward)

				.lineToLinearHeading(new Pose2d(xConeStack2, -1, Math.toRadians(coneStackHeading)))
				.forward(yConeStack)
				.UNSTABLE_addTemporalMarkerOffset(0, () -> {
					clawControl.toggleOpenClose();
				})
				.waitSeconds(0.2)
				.UNSTABLE_addTemporalMarkerOffset(0, () -> {
					slideControl.setCustom(1275);//1275
				})
				.waitSeconds(0.5)

				.UNSTABLE_addTemporalMarkerOffset(0.5, () -> {
					slideControl.setCustom(1050);//900
					armControl.setAutoExtake();
					clawControl.toggleWristRotate();
				})
				.setReversed(true)
				.splineTo(new Vector2d(mediumX2, mediumY2), Math.toRadians(mediumHeading2))
				.setReversed(false)
				.UNSTABLE_addTemporalMarkerOffset(0, () -> {
					slideControl.setIntakeOrGround();
				})
				.waitSeconds(0.2)
				.UNSTABLE_addTemporalMarkerOffset(0, () -> {
					clawControl.toggleOpenClose();
				})
				.waitSeconds(0.2)
				.UNSTABLE_addTemporalMarkerOffset(0, () -> {
					armControl.setIntake();
					clawControl.toggleWristRotate();
				})
				.waitSeconds(0.25)
				.UNSTABLE_addTemporalMarkerOffset(0, () -> {
					slideControl.setCustom(350);//350
				})
				.lineToLinearHeading(new Pose2d(xConeStack3, -1, Math.toRadians(coneStackHeading)))
				.forward(yConeStack)
				.UNSTABLE_addTemporalMarkerOffset(0, () -> {
					clawControl.toggleOpenClose();
				})
				.waitSeconds(0.2)
				.UNSTABLE_addTemporalMarkerOffset(0, () -> {
					slideControl.setCustom(1275);//1275
				})
				.waitSeconds(0.5)

				.UNSTABLE_addTemporalMarkerOffset(0.5, () -> {
					slideControl.setCustom(1050);//900
					armControl.setAutoExtake();
					clawControl.toggleWristRotate();
				})
				.setReversed(true)
				.splineTo(new Vector2d(mediumX3, mediumY3), Math.toRadians(mediumHeading3))
				.setReversed(false)
				.UNSTABLE_addTemporalMarkerOffset(0, () -> {
					slideControl.setIntakeOrGround();
				})
				.waitSeconds(0.2)
				.UNSTABLE_addTemporalMarkerOffset(0, () -> {
					clawControl.toggleOpenClose();
				})
				.waitSeconds(0.2)
				.UNSTABLE_addTemporalMarkerOffset(0, () -> {
					armControl.setIntake();
					clawControl.toggleWristRotate();
				})
				.waitSeconds(0.25)
				.UNSTABLE_addTemporalMarkerOffset(0, () -> {
					slideControl.setCustom(225);//225

				})
				.lineToLinearHeading(new Pose2d(xConeStack4, -1, Math.toRadians(coneStackHeading)))
				.forward(yConeStack)
				.UNSTABLE_addTemporalMarkerOffset(0, () -> {
					clawControl.toggleOpenClose();
				})
				.waitSeconds(0.2)
				.UNSTABLE_addTemporalMarkerOffset(0, () -> {
					slideControl.setCustom(1275);//1275
				})
				.waitSeconds(0.5)

				.UNSTABLE_addTemporalMarkerOffset(0.5, () -> {
					slideControl.setCustom(1050);//900
					armControl.setAutoExtake();
					clawControl.toggleWristRotate();
				})
				.setReversed(true)
				.splineTo(new Vector2d(mediumX4, mediumY4), Math.toRadians(mediumHeading4))
				.setReversed(false)
				.UNSTABLE_addTemporalMarkerOffset(0, () -> {
					slideControl.setIntakeOrGround();
				})
				.waitSeconds(0.2)
				.UNSTABLE_addTemporalMarkerOffset(0, () -> {
					clawControl.toggleOpenClose();
				})
				.waitSeconds(0.2)
				.UNSTABLE_addTemporalMarkerOffset(0, () -> {
					armControl.setIntake();
					clawControl.toggleWristRotate();
				})
				.waitSeconds(0.25)
				.UNSTABLE_addTemporalMarkerOffset(0, () -> {
					alignmentControl.toggleAlignmentDevice();
					slideControl.setIntakeOrGround();
				})
				.waitSeconds(0.1)

				.addTemporalMarker(() -> {
				/*
					if(tagUse == 1){
						TrajectorySequence zoneOne = bot.trajectorySequenceBuilder(new Pose2d(mediumX4,mediumY4,Math.toRadians(mediumHeading4)))
								.lineToLinearHeading(new Pose2d(51 ,27, Math.toRadians(89)))
								.build();
						bot.followTrajectorySequenceAsync(zoneOne);
					}else if(tagUse == 2) {
						TrajectorySequence zoneTwo = bot.trajectorySequenceBuilder(new Pose2d(mediumX4,mediumY4,Math.toRadians(mediumHeading4)))
								.lineToLinearHeading(new Pose2d(51 ,0, Math.toRadians(89)))
								.build();
						bot.followTrajectorySequenceAsync(zoneTwo);
					}else{
						Trajectory zoneThree = bot.trajectoryBuilder(new Pose2d(mediumX4,mediumY4,Math.toRadians(mediumHeading4)))
								.lineToLinearHeading(new Pose2d(51 ,-23, Math.toRadians(89)))
								.build();
						bot.followTrajectoryAsync(zoneThree);
					}
					*/
					if(tagUse == 2) {
						TrajectorySequence zoneTwo = bot.trajectorySequenceBuilder(new Pose2d(mediumX4,mediumY4,Math.toRadians(mediumHeading4)))
								.lineToLinearHeading(new Pose2d(zone2X ,zone2Y, Math.toRadians(89)))
								.build();
						bot.followTrajectorySequenceAsync(zoneTwo);
					}
					else if(tagUse == 1){
						TrajectorySequence zoneOne = bot.trajectorySequenceBuilder(new Pose2d(mediumX4,mediumY4,Math.toRadians(mediumHeading4)))
								.lineToLinearHeading(new Pose2d(zone2X ,zone2Y, Math.toRadians(89)))
								.build();
						Trajectory backward1 = bot.trajectoryBuilder(new Pose2d(zone2X ,zone2Y, Math.toRadians(89)))
								.back(zone1backwards)
								.build();
						bot.followTrajectorySequenceAsync(zoneOne);
						bot.followTrajectoryAsync(backward1);
					}
					else{
						Trajectory zoneThree = bot.trajectoryBuilder(new Pose2d(mediumX4,mediumY4,Math.toRadians(mediumHeading4)))
								.lineToLinearHeading(new Pose2d(zone2X ,zone2Y, Math.toRadians(89)))
								.build();
						Trajectory forward3 = bot.trajectoryBuilder(new Pose2d(zone2X ,zone2Y, Math.toRadians(89)))
								.forward(zone3forwards)
								.build();
						bot.followTrajectoryAsync(zoneThree);
						bot.followTrajectoryAsync(forward3);
					}
				})
				.build();
		scan();
		waitForStart();
		bot.followTrajectorySequenceAsync(junction);

		while(opModeIsActive()  && !isStopRequested()){
			bot.update();
			armControl.update(telemetry);
			slideControl.update(telemetry);
		}
	}
}
// EAT CHICKEN ┻━┻ ︵ヽ(`Д´)ﾉ︵ ┻━┻