package lab2a;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class MySynchronizer {
    private final Lock lock = new ReentrantLock();
    private final Condition condition = lock.newCondition();
    private boolean isSignaled = false;

    public void waitForSignal() throws InterruptedException {
        lock.lock();
        try {
            while (!isSignaled) {
                condition.await();
            }
        } finally {
            lock.unlock();
        }
    }

    public void signal() {
        lock.lock();
        try {
            isSignaled = true;
            condition.signalAll();
        } finally {
            lock.unlock();
        }
    }
}
