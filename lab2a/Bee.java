package lab2a;
import java.util.Random;

public class Bee implements Runnable {
    private final int id;
    private final Forest forest;
    private final Random random = new Random();
    private boolean checkedWinnie = false;

    public Bee(int id, Forest forest) {
        this.id = id;
        this.forest = forest;
    }

    @Override
    public void run() {
        if (!checkedWinnie) {
            searchArea();
        } else {
            System.out.println("Бджола " + id + " повертається в вулик.");
        }
    }

    private void searchArea() {
        System.out.println("Бджола " + id + " розпочала пошук.");
        try {
            Thread.sleep(random.nextInt(500));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        if (!forest.isWinnieFound()) {
            if (random.nextDouble() < 0.2) {
                System.out.println("Бджола " + id + " знайшла Вінні-Пуха!");
                forest.foundWinnie();
                checkedWinnie = true;
                forest.signal();
            } else {
                System.out.println("Бджола " + id + " не знайшла Вінні-Пуха і повертається в вулик.");
            }
        } else {
            System.out.println("Бджола " + id + " повертається в вулик.");
        }
    }
}



