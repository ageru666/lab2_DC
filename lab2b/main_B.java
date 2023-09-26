package lab2b;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Semaphore;

class Warehouse {
    private BlockingQueue<Integer> storage = new ArrayBlockingQueue<>(10);
    private int currentItem = 1;
    private Semaphore ivanovSemaphore = new Semaphore(1);
    private Semaphore petrovSemaphore = new Semaphore(0);
    private Semaphore nechiporchukSemaphore = new Semaphore(0);
    private final Object lock = new Object(); // Add an object lock

    public void store(int item) {
        try {
            ivanovSemaphore.acquire();
            synchronized (lock) {
                while (currentItem != item) {
                    lock.wait();
                }
                storage.put(item);
                System.out.println("Іванов виніс: " + item);
                currentItem++;
                lock.notifyAll();
            }
            petrovSemaphore.release();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public int retrieve() {
        try {
            petrovSemaphore.acquire();
            synchronized (lock) {
                while (storage.isEmpty()) {
                    lock.wait();
                }
                int item = storage.take();
                System.out.println("Петров вантажить: " + item);
                lock.notifyAll();
                nechiporchukSemaphore.release();
                return item;
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return -1;
        }
    }

    public int calculateValue(int item) {
        try {
            nechiporchukSemaphore.acquire();
            synchronized (lock) {
                System.out.println("Нечипорчук підраховує вартість: " + item);
                lock.notifyAll();
                ivanovSemaphore.release();
                return item * 10;
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return -1;
        }
    }
}


class Ivanov implements Runnable {
    private Warehouse warehouse;

    public Ivanov(Warehouse warehouse) {
        this.warehouse = warehouse;
    }

    @Override
    public void run() {
        try {
            for (int i = 1; i <= 10; i++) {
                warehouse.store(i);
                Thread.sleep(10);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}

class Petrov implements Runnable {
    private Warehouse warehouse;

    public Petrov(Warehouse warehouse) {
        this.warehouse = warehouse;
    }

    @Override
    public void run() {
        try {
            for (int i = 1; i <= 10; i++) {
                int item = warehouse.retrieve();
                Thread.sleep(10);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}

class Nechiporchuk implements Runnable {
    private Warehouse warehouse;

    public Nechiporchuk(Warehouse warehouse) {
        this.warehouse = warehouse;
    }

    @Override
    public void run() {
        try {
            for (int i = 1; i <= 10; i++) {
                warehouse.calculateValue(i);
                Thread.sleep(10);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}




public class main_B {
    public static void main(String[] args) throws InterruptedException {
        Warehouse warehouse = new Warehouse();

        Thread ivanovThread = new Thread(new Ivanov(warehouse));
        Thread petrovThread = new Thread(new Petrov(warehouse));
        Thread nechiporchukThread = new Thread(new Nechiporchuk(warehouse));

        ivanovThread.start();
        petrovThread.start();
        nechiporchukThread.start();

    }
}

