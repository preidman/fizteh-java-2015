package ru.fizteh.fivt.students.preidman.threads;

import java.util.concurrent.Semaphore;
import java.util.Random;

public class RollingThread  implements Runnable {


    public static void main(String[] args) {

        new RollingThread (Integer.valueOf(args[0])).run();

    }



    private int countOfReady;

    private int numberOfThreads;
    private Responder[] students;
    private Integer numberOfYes;

    private Semaphore turn;
    private Boolean end;





    public RollingThread (int x) {

        numberOfYes = 0;
        end = false;
        numberOfThreads = x;

        turn = new Semaphore(numberOfThreads);

        try {

            turn.acquire(numberOfThreads);

        } catch (InterruptedException e) {

            e.printStackTrace();
            System.exit(-1);

        }


        students = new Responder[numberOfThreads];

        for (int i = 0; i < numberOfThreads; ++i) {

            students[i] = new Responder();

            try {

                students[i].answPls.acquire();

            } catch (InterruptedException e) {

                e.printStackTrace();
                System.exit(-1);

            }
        }
    }



    @Override
    public final void run() {

        boolean ready = false;

        for (int i = 0; i < numberOfThreads; ++i) {

            students[i].start();

        }

        while (!ready) {

            System.out.println("Are you ready?");
            goAnswer();

            try {

                turn.acquire(numberOfThreads);
                synchronized (numberOfYes) {

                    if (countOfReady == numberOfThreads) {

                        ready = true;
                        end = true;
                        goAnswer();

                    }

                    numberOfYes = 0;
                    countOfReady = 0;
                }

            } catch (InterruptedException e) {

                e.printStackTrace();

            }
        }
    }



    private void goAnswer() {

        for (int i = 0; i < numberOfThreads; ++i) {

            students[i].answPls.release();

        }

    }

    public class Responder extends Thread {

        private Random maybe = new Random();

        private Semaphore answPls = new Semaphore(1);

        @Override
        public final void run() {

            while (true) {

                try {

                    answPls.acquire();

                    synchronized (end) {

                        if (end) return;

                    }

                    boolean yes = true;

                    if (maybe.nextInt(10) == 0)  yes = false;

                    if (!yes) System.out.println(getName() + ": No");
                    else System.out.println(getName() + ": Yes");


                    synchronized (numberOfYes) {

                        numberOfYes++;
                        if (yes) countOfReady++;

                    }

                    turn.release();

                } catch (InterruptedException e) {

                    System.err.println(e.getMessage());

                }
            }
        }
    }
}