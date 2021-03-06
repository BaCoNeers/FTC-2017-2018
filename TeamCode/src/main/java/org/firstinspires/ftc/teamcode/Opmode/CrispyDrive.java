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
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.Configuration;
import org.firstinspires.ftc.teamcode.CrispyConfig;
import org.firstinspires.ftc.teamcode.util.MovingAverageTimer;

@TeleOp(name = "TrolleyDrive - Standard", group = "Pushbot")

public class CrispyDrive extends LinearOpMode {

    /* Declare OpMode members. */
    private CrispyConfig robot = new CrispyConfig();   // Use a Pushbot's hardware



    @Override
    public void runOpMode() {


        /* Initialize the hardware variables.
         * The init() method of the hardware class does all the work here
         */
        robot.init(hardwareMap);

        // Send telemetry message to signify robot waiting;
        telemetry.addData("Say", "Hello Driver");    //
        telemetry.update();

        long startTime;
        long estimatedTime;

        double CurrentLeftPower = 0;
        double CurrentRightPower = 0;

        telemetry.setAutoClear(false);
        Telemetry.Item toggel = telemetry.addData("Toggel", "%12.3f", 0.0);
        Telemetry.Item elaspedtime = telemetry.addData("elaspedtime", "%12.3f", 0.0);
        Telemetry.Item leftPower = telemetry.addData("leftpower", "%12.3f",0.0);
        Telemetry.Item rightPower = telemetry.addData("rightpower", "%12.3f",0.0);

        DriveController.SetupTelemetry(telemetry);
        startTime = System.nanoTime();

        MovingAverageTimer timer = new MovingAverageTimer(1000);

        // Wait for the game to start (driver presses PLAY)
        waitForStart();
        // run until the end of the match (driver presses STOP)
        while (opModeIsActive()) {
            timer.update();
            telemetry.update();
            double max_inc = .250*timer.loopTime()/1000.0;

            elaspedtime.setValue(timer.loopTime());

            CurrentLeftPower = getPower((gamepad1.right_trigger - gamepad1.left_trigger)  + gamepad1.right_stick_x,CurrentLeftPower, max_inc);
            CurrentRightPower = getPower((gamepad1.right_trigger - gamepad1.left_trigger)   - gamepad1.right_stick_x,CurrentRightPower, max_inc);

            rightPower.setValue(CurrentRightPower);
            leftPower.setValue(CurrentLeftPower);

            robot.leftDrive.setPower(CurrentLeftPower) ;
            robot.rightDrive.setPower(CurrentRightPower );
            timer.average();



            startTime = System.nanoTime();

        }
    }

    public double getPower(double desiredPower,double currentPower, double maxInc){

        if (Math.abs(currentPower-desiredPower) < maxInc){
            return desiredPower;
        }

        double sign = Math.signum(desiredPower);

        if (desiredPower > 0) {
            if (desiredPower > currentPower) {
                currentPower = Math.min(maxInc + currentPower, desiredPower);
            } else if (desiredPower < currentPower) {
                currentPower = Math.max(currentPower-maxInc, desiredPower);
            }
        } else {
            if (currentPower < desiredPower) {
                currentPower = Math.min(maxInc + currentPower, desiredPower);
            } else if (currentPower > desiredPower) {
                currentPower = Math.max(currentPower - maxInc, desiredPower);
            }

        }

        return currentPower;
    }


}


