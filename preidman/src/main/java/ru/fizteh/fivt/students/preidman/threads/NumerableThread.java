package ru.fizteh.fivt.students.preidman.threads;


public class NumerableThread {

    public static void main(String[] args) {

        new NumerableThread(Integer.valueOf(args[0]));

    }



    private final int kol;

    private Integer par;



    public class Numerator extends Thread {

        private int num;

        public Numerator(int x) {
            num = x;
        }

        @Override
        public final void run() {

            while (true) {

                synchronized (par) {

                    if (par.equals(num - 1)) {

                        System.out.println("Thread-" + num);
                        par++;

                        if (par.equals(kol))  par = 0;

                    }
                }

            }
        }
    }

    public NumerableThread(int numberOfThreads) {

        kol = numberOfThreads;

        par = 0;

        for (int i = 0; i < kol; ++i) {

            Thread count = new Numerator(i + 1);
            count.start();

        }
    }

}
