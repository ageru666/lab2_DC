package lab2a;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Forest {
    private volatile boolean foundWinnie = false;
    private MySynchronizer synchronizer = new MySynchronizer();
    private Lock lock = new ReentrantLock();
    private Condition winnieFound = lock.newCondition();

    public boolean isWinnieFound() {
        return foundWinnie;
    }

    public void waitForSignal() throws InterruptedException {
        lock.lock();
        try {
            while (!foundWinnie) {
                winnieFound.await();
            }
        } finally {
            lock.unlock();
        }
    }

    public void signal() {
        lock.lock();
        try {
            winnieFound.signalAll();
        } finally {
            lock.unlock();
        }
    }

    public synchronized void foundWinnie() {
        foundWinnie = true;
    }
}

