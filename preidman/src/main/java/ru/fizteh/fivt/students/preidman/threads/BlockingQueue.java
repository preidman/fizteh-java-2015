package ru.fizteh.fivt.students.preidman.threads;

import java.util.concurrent.*;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.locks.*;
import java.util.ArrayList;


public class BlockingQueue<T> {

    private final int volume;
    private Lock l;

    private Condition takeCond;
    private Condition putCond;

    private Queue<T> queueBl;

    public BlockingQueue(int maxSize) {

        volume = maxSize;
        queueBl = new LinkedList<>();

        l = new ReentrantLock();

        takeCond = l.newCondition();
        putCond = l.newCondition();

    }

    public final void offer(List<T> newqueueBl) throws InterruptedException, TimeoutException {

        offer(newqueueBl, 0);

    }

    public final void offer(List<T> newqueueBl, long t) throws InterruptedException, TimeoutException {

        l.lock();

        try {

            long time = TimeUnit.MILLISECONDS.toNanos(t);

            boolean isAdd = true;


            while ((t == 0 || time > 0) && queueBl.size() + newqueueBl.size() > volume) {

                if (t == 0) putCond.await();
                else {

                    time = putCond.awaitNanos(time);
                    if (time < 0) isAdd = false;

                }

            }


            if (isAdd) {

                queueBl.addAll(newqueueBl);

                if (newqueueBl.size() > 0) takeCond.signal();

            } else  throw new TimeoutException();

        } finally {

            l.unlock();

        }
    }


    public final List<T> take(int count) throws InterruptedException {

        return take(count, 0);

    }

    public final List<T> take(int kol, long t) throws InterruptedException {

        l.lock();

        try {

            long time = TimeUnit.MILLISECONDS.toNanos(t);

            while (queueBl.size() < kol && (t == 0 || time > 0)) {

                if (t == 0) takeCond.await();
                else {

                    time = takeCond.awaitNanos(time);
                    if (time < 0) return null;

                }

            }

            List<T> tmp = new ArrayList<>();

            for (int i = 0; i < kol; ++i) {

                tmp.add(queueBl.poll());

            }

            if (kol > 0) putCond.signal();

            return tmp;

        } finally {

            l.unlock();

        }
    }

}