package lab2a;

public class Main_A {
    public static void main(String[] args) {
        int numberOfBees = 10;
        Forest forest = new Forest();

        for (int i = 0; i < numberOfBees; i++) {
            Thread beeThread = new Thread(new lab2a.Bee(i, forest));
            beeThread.start();
        }


        try {
            forest.waitForSignal();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("Вінні-Пух знайдений та покараний!");
    }
}