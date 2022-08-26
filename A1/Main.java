/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.util.Random;

public class Main {

    public static void main(String[] args) {
        int x = 0, y = 0, success = 0;
        boolean result;
        Robot robot = new Robot(x, y);
        Maze maze = new Maze();

        for (int i = 0; i < 1000000; i++) {
            result = maze.findExit(robot, maze);
            robot.reset();
            maze.reset();
            if (result) {
                success++;
            }
        }
        System.out.println("The Monte Carlo simulation result of one million runs:");
        System.out.println("No. of successful escape: " + success);
        System.out.printf("Success Rate P: %.3f\n", success / 1000000.0);
    }

    private static class Robot { // define the behaviours of the robot

        public int positionX, positionY;

        Robot(int x, int y) {
            positionX = x;
            positionY = y;
        }

        void reset() {
            positionX = 0;
            positionY = 0;
        }
    }

    private static class Maze { // define the behaviours of the maze

        private final int[][] prevMove;

        Maze() {
            prevMove = new int[7][7];
        }

        boolean checkPrevPos(int x, int y) { // The new position must not have been visited
            if (prevMove[x][y] == 1) {
                return true;
            } else {
                return false;
            }
        }

        static boolean checkMazeBounds(int x, int y) { // The new position must be within the boundaries of the maze.
            if (x > 6 || y > 6 || x < 0 || y < 0) {
                return true;
            } else {
                return false;
            }
        }

        void reset() {
            for (int i = 0; i < 7; i++) {
                for (int j = 0; j < 7; j++) {
                    prevMove[i][j] = 0;
                }
            }
        }

        static boolean findExit(Robot robot, Maze maze) {
            int pointing = 0; // 0 -> East, 1 -> South, 2-> West, 3->North.
            char direction;
            boolean checkMaze = true, left = true, right = true, forward = true;
            maze.prevMove[0][0] = 1;
            Random rand = new Random();
            while (true) {
                do {
                    if (forward == false && left == false && right == false) {
                        return false;
                    }
                    direction = 'S';
                    while (direction == 'S') {
                        int chance = rand.nextInt(100);
                        if (chance < 60) {
                            direction = 'F';
                            forward = false;
                        } else if (chance >= 60 && chance < 80) {
                            direction = 'L';
                            left = false;
                        } else if (chance >= 80 && chance < 100) {
                            direction = 'R';
                            right = false;
                        }
                    }
                    if (pointing % 4 == 0) {
                        switch (direction) {
                            case 'F':
                                robot.positionX++;
                                if (checkMazeBounds(robot.positionX, robot.positionY)) { // check bounds
                                    robot.positionX--; // go back to the previous position
                                    checkMaze = false;
                                } else if (maze.checkPrevPos(robot.positionX, robot.positionY)) { // check previous move
                                    robot.positionX--;
                                    checkMaze = false;
                                } else {
                                    checkMaze = true;
                                }
                                break;

                            case 'L':
                                robot.positionY--;
                                if (checkMazeBounds(robot.positionX, robot.positionY)) {
                                    robot.positionY++;
                                    checkMaze = false;
                                } else if (maze.checkPrevPos(robot.positionX, robot.positionY)) { 
                                    robot.positionY++;
                                    checkMaze = false;
                                } else {
                                    checkMaze = true;
                                }
                                break;

                            case 'R':
                                robot.positionY++;
                                if (checkMazeBounds(robot.positionX, robot.positionY)) {
                                    robot.positionY--;
                                    checkMaze = false;
                                } else if (maze.checkPrevPos(robot.positionX, robot.positionY)) { 
                                    robot.positionY--;
                                    checkMaze = false;
                                } else {
                                    checkMaze = true;
                                }
                                break;
                        }
                    }

                    if (pointing % 4 == 1) {
                        switch (direction) {
                            case 'F':
                                robot.positionY++;
                                if (checkMazeBounds(robot.positionX, robot.positionY)) {
                                    robot.positionY--;
                                    checkMaze = false;
                                } else if (maze.checkPrevPos(robot.positionX, robot.positionY)) { 
                                    robot.positionY--;
                                    checkMaze = false;
                                } else {
                                    checkMaze = true;
                                }
                                break;

                            case 'L':
                                robot.positionX++;
                                if (checkMazeBounds(robot.positionX, robot.positionY)) {
                                    robot.positionX--;
                                    checkMaze = false;
                                } else if (maze.checkPrevPos(robot.positionX, robot.positionY)) { 
                                    robot.positionX--;
                                    checkMaze = false;
                                } else {
                                    checkMaze = true;
                                }
                                break;

                            case 'R':
                                robot.positionX--;
                                if (checkMazeBounds(robot.positionX, robot.positionY)) {
                                    robot.positionX++;
                                    checkMaze = false;
                                } else if (maze.checkPrevPos(robot.positionX, robot.positionY)) { 
                                    robot.positionX++;
                                    checkMaze = false;
                                } else {
                                    checkMaze = true;
                                }
                                break;
                        }
                    }

                    if (pointing % 4 == 2) {
                        switch (direction) {
                            case 'F':
                                robot.positionX--;
                                if (checkMazeBounds(robot.positionX, robot.positionY)) {
                                    robot.positionX++;
                                    checkMaze = false;
                                } else if (maze.checkPrevPos(robot.positionX, robot.positionY)) { 
                                    robot.positionX++;
                                    checkMaze = false;
                                } else {
                                    checkMaze = true;
                                }
                                break;

                            case 'L':
                                robot.positionY++;
                                if (checkMazeBounds(robot.positionX, robot.positionY)) {
                                    robot.positionY--;
                                    checkMaze = false;
                                } else if (maze.checkPrevPos(robot.positionX, robot.positionY)) { 
                                    robot.positionY--;
                                    checkMaze = false;
                                } else {
                                    checkMaze = true;
                                }
                                break;

                            case 'R':
                                robot.positionY--;
                                if (checkMazeBounds(robot.positionX, robot.positionY)) {
                                    robot.positionY++;
                                    checkMaze = false;
                                } else if (maze.checkPrevPos(robot.positionX, robot.positionY)) { 
                                    robot.positionY++;
                                    checkMaze = false;
                                } else {
                                    checkMaze = true;
                                }
                                break;
                        }
                    }

                    if (pointing % 4 == 3) {
                        switch (direction) {
                            case 'F':
                                robot.positionY--;
                                if (checkMazeBounds(robot.positionX, robot.positionY)) {
                                    robot.positionY++;
                                    checkMaze = false;
                                } else if (maze.checkPrevPos(robot.positionX, robot.positionY)) { 
                                    robot.positionY++;
                                    checkMaze = false;
                                } else {
                                    checkMaze = true;
                                }
                                break;

                            case 'L':
                                robot.positionX--;
                                if (checkMazeBounds(robot.positionX, robot.positionY)) {
                                    robot.positionX++;
                                    checkMaze = false;
                                } else if (maze.checkPrevPos(robot.positionX, robot.positionY)) { 
                                    robot.positionX++;
                                    checkMaze = false;
                                } else {
                                    checkMaze = true;
                                }
                                break;

                            case 'R':
                                robot.positionX++;
                                if (checkMazeBounds(robot.positionX, robot.positionY)) {
                                    robot.positionX--;
                                    checkMaze = false;
                                } else if (maze.checkPrevPos(robot.positionX, robot.positionY)) { 
                                    robot.positionX--;
                                    checkMaze = false;
                                } else {
                                    checkMaze = true;
                                }
                                break;
                        }
                    }

                    if (robot.positionX == 6 && robot.positionY == 6) { // success
                        return true;
                    }
                } while (!checkMaze);

                maze.prevMove[robot.positionX][robot.positionY] = 1;
                forward = true;
                left = true;
                right = true;
                if (direction == 'R') {
                    pointing++;
                }
                if (direction == 'L') {
                    if (pointing % 4 == 0) {
                        pointing = 3;
                    } else {
                        pointing--;
                    }
                }
            }
        }
    }
}

