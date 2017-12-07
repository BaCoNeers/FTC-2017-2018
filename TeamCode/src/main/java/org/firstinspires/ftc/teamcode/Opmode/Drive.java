/* Copyright (c) 2017 FIRST. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted (subject to the limitations in the disclaimer below) provided that
 * the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice, this list
 * of conditions and the following disclaimer.
 *
 * Redistributions in binary form must reproduce the above copyright notice, this
 * list of conditions and the following disclaimer in the documentation and/or
 * other materials provided with the distribution.
 *
 * Neither the name of FIRST nor the names of its contributors may be used to endorse or
 * promote products derived from this software without specific prior written permission.
 *
 * NO EXPRESS OR IMPLIED LICENSES TO ANY PARTY'S PATENT RIGHTS ARE GRANTED BY THIS
 * LICENSE. THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package org.firstinspires.ftc.teamcode.Opmode;


import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.Configuration;
import org.firstinspires.ftc.teamcode.Classes.JewelDrop;

@TeleOp(name="Drive", group="Pushbot")

public class Drive extends LinearOpMode {

    /* Declare OpMode members. */
    private Configuration robot           = new Configuration();   // Use a Pushbot's hardware
    private boolean toggle = false;
    private boolean toMax = false;
    private boolean toMin = false;
    private double divider = 1;
    private double  Forward;
    private double  Turning;
    private double  oldTurning;
    private double oldForward;
    private boolean leftStickY;
    private boolean rightStickX;
    private JewelDrop jewel = new JewelDrop();



    @Override
    public void runOpMode() {


        /* Initialize the hardware variables.
         * The init() method of the hardware class does all the work here
         */
        robot.init(hardwareMap);

        // Send telemetry message to signify robot waiting;
        telemetry.addData("Say", "Hello Driver");    //
        telemetry.update();

        telemetry.setAutoClear(false);
        Telemetry.Item leftdrive = telemetry.addData("left gamepad" , "%12.3f", 0.0);
        Telemetry.Item rightdrive = telemetry.addData("right gamepad" , "%12.3f", 0.0);
        Telemetry.Item leftdrivebias = telemetry.addData("Left drive bias" , "%12.3f", 0.0);
        Telemetry.Item rightdrivebias = telemetry.addData("right drive bias" , "%12.3f", 0.0);

        // Wait for the game to start (driver presses PLAY)
        waitForStart();
//        jewel.Jewel(true, robot);
        // run until the end of the match (driver presses STOP)
        while (opModeIsActive()) {

            vec2 drive_bias = DriveController.GetDriveBias(gamepad1);

            leftdrive.setValue("%12.3f", gamepad1.left_stick_y);
            rightdrive.setValue("%12.3f",gamepad1.right_stick_x);
            leftdrivebias.setValue("%12.3f",0.1 * drive_bias.x);
            rightdrivebias.setValue("%12.3f",0.1 * drive_bias.y);

            telemetry.update();

            robot.leftDrive.setPower(0.1 * drive_bias.x);
            robot.rightDrive.setPower(0.1 * drive_bias.y);


            //lift
            robot.ymotion.setPower(gamepad2.right_stick_x * -1);
            robot.xmotion.setPower(  gamepad2.right_stick_y * -1);

            //grabber
            if(gamepad2.left_bumper){
                toMax = true;
            }
            if (toMax){
                robot.grabber.setPosition(1);
                if(robot.min.isPressed()){
                    robot.grabber.setPosition(0.5);
                    toMax = false;
                }
            }
            if(gamepad2.right_bumper){
                toMin = true;
            }
            if(toMin){
                robot.grabber.setPosition(0);
                if(robot.max.isPressed()){
                    robot.grabber.setPosition(0.5);
                    toMin = false;
                }
            }



//            //extendor
           robot.extentionUp.setPower(gamepad2.left_stick_y*-1);
           robot.extentionCross.setPower(gamepad2.left_stick_x);
            if (gamepad1.dpad_left){
                robot.picker.setPosition(0);
            }
            if(gamepad1.dpad_right){
                robot.picker.setPosition(1);
            }
            if(!gamepad1.dpad_right && !gamepad2.dpad_left){
                robot.picker.setPosition(0.5);
            }

            // Drive
            oldForward = Forward;
            oldTurning = Turning;
        }
    }


}

