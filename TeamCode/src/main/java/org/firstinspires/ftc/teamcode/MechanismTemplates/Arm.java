package org.firstinspires.ftc.teamcode.MechanismTemplates;

import com.acmerobotics.dashboard.config.Config;
import com.arcrobotics.ftclib.controller.PIDFController;
import com.arcrobotics.ftclib.hardware.motors.Motor;
import com.qualcomm.robotcore.hardware.AnalogInput;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;

@Config
public class Arm{
    private  PIDFController armPIDF;
    private Motor armMotor;
    private final AnalogInput sensor;

//    Declaring and Initializing PIDF values
//    public static double armKp = 0.003;
//    public static double armKi = 0.000001;
//    public static double armKd = 0.000005;
//    public static double armKf = 0.0000001;

    // New PID values
    public static double armKpUp = 0.008; // old is 0.012
    public static double armKpDown = 0.0015;
    public static double armKi = 0.00001;
    public static double armKd = 0.00007;
    public static double armKf = 0;
    public static double EXTAKE_POS = 285; // 180 old val; in degrees of absolute encoder
    public static double INTAKE_POS = 42; // 65 old val
    public static double AUTOEXTAKE_POS = 288; //169

    // Initially set to 0 because we only want the claw to move when given input from the controller
    // initializing the targetPos value to a greater positive value would cause the update() method to
    // immediately start moving the arm since a difference between the current motor encoder position
    // and the target position is created (error).
    private double targetPos = 65;

    public Arm(HardwareMap hardwareMap){
        armMotor = new Motor(hardwareMap, "ARM", Motor.GoBILDA.RPM_84); // pin 1 control hub
        armMotor.setZeroPowerBehavior(Motor.ZeroPowerBehavior.BRAKE);
        armMotor.setRunMode(Motor.RunMode.VelocityControl);
        armPIDF = new PIDFController(armKpUp, armKi, armKd, armKf);
        sensor = hardwareMap.analogInput.get("ARM_ENC"); // encoder port 1 on control hub
    }

    public double getArmPosition(){
        // ENCODER TICKS
        // return 2.5 * 480 * ((sensor.getVoltage() - 0.3520574787720445) - 0.5);
        // DEGREES
          return (sensor.getVoltage()/3.3) *360;
    }

    public void update(Telemetry telemetry){
        double correction = armPIDF.calculate(getArmPosition(), targetPos);
        telemetry.addData("Correction: ", correction);
        telemetry.addData("Target Position: ", targetPos);
        telemetry.addData("Motor Position: ", getArmPosition());
        telemetry.update();
        armMotor.set(correction); // sets a PID-tuned voltage for the arm motor
    }

    public void setExtake(){
        armPIDF.setPIDF(armKpUp, armKi, armKd, armKf);
        targetPos = EXTAKE_POS;
    }

    public void setAutoExtake(){
       armPIDF.setPIDF(armKpUp, armKi, armKd, armKf);
       targetPos = AUTOEXTAKE_POS;
       }

    public void setIntake(){
        armPIDF.setPIDF(armKpDown, armKi, armKd, armKf);
        targetPos = INTAKE_POS;
    }

    public void setCustom(int custom){
        armPIDF.setPIDF(armKpUp, armKi, armKd, armKf);
        targetPos=custom;
    }
}