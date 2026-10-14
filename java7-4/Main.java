import java.util.LinkedList;

class PC {
    private final LinkedList<Integer> list = new LinkedList<>();
    private final int capacity = 2;
    private int nextValue = 0;

    public void produce() {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                synchronized (list) {
                    while (list.size() == capacity) {
                        list.wait();
                    }

                    int value = nextValue++;
                    list.add(value);
                    System.out.println("Producer produced-" + value);
                    list.notify();
                }
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return;
            }
        }
    }

    public void consume() {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                synchronized (list) {
                    while (list.isEmpty()) {
                        list.wait();
                    }

                    int value = list.removeFirst();
                    System.out.println("Consumer consumed-" + value);
                    list.notify();
                }
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return;
            }
        }
    }
}

public class Main {
    public static void main(String[] args) throws InterruptedException {
        PC pc = new PC();
        Thread producer = new Thread(pc::produce, "Producer");
        Thread consumer = new Thread(pc::consume, "Consumer");

        producer.start();
        consumer.start();

        producer.join();
        consumer.join();
    }
}
