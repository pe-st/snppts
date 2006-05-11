import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicInteger;

public class SantaClaus {
    private volatile boolean kidsStillBelieveInSanta = true;
    private final Semaphore disbelief = new Semaphore(0);
    private final static int END_OF_FAITH = 2012;
    private AtomicInteger year = new AtomicInteger(2006);
    private static Random generator = new Random();

    private final Semaphore queueElves;
    private final CyclicBarrier threeElves;
    private final CyclicBarrier elvesLeaveOffice;
    private final CyclicBarrier allReindeers;
    private final Semaphore santa;

    class Reindeer implements Runnable {
        int id;

        Reindeer(int id) { this.id = id; }

        public void run() {
            while (kidsStillBelieveInSanta) {
                try {
                    // wait until christmas comes
                    Thread.sleep(900 + generator.nextInt(200));

                    // only all reindeers together can wake Santa
                    int waitIndex = allReindeers.await();
                    // the last reindeer to pass the barrier acts for all
                    if (waitIndex == 0) {
                        santa.acquire();
                        System.out.println("Delivery for Christmas " + year);
                        if (year.incrementAndGet() == END_OF_FAITH)
                        {
                            kidsStillBelieveInSanta = false;
                            disbelief.release();
                        }
                        santa.release();
                    }
                } catch (InterruptedException e) {
                    // thread interrupted for program cleanup
                } catch (BrokenBarrierException e) {
                    // another thread in the barrier was interrupted
                }
            }
            System.out.println("Reindeer " + id + " retires");
        }
    }

    class Elf implements Runnable {
        int id;

        Elf(int id) { this.id = id; }

        public void run() {
            try {
                Thread.sleep(generator.nextInt(2000));

                while (kidsStillBelieveInSanta) {
                    // no more than three elves fit into Santa's office
                    queueElves.acquire();
                    System.out.println("Knocking : Elf " + id);

                    // wait until three elves have a problem
                    int waitIndex = threeElves.await();

                    // the third elf acts for all three
                    if (waitIndex == 0)
                        santa.acquire();

                    // wait until all elves have understood Santas solutions
                    Thread.sleep(generator.nextInt(500));
                    System.out.println("Toy R&D : Elf " + id);
                    elvesLeaveOffice.await();

                    if (waitIndex == 0)
                        santa.release();
                    queueElves.release();

                    // manufacture toys until a problem emerges
                    Thread.sleep(generator.nextInt(2000));
                }
            } catch (InterruptedException e) {
                // thread interrupted for program cleanup
            } catch (BrokenBarrierException e) {
                // another thread in the barrier was interrupted
            }
            System.out.println("Elf " + id + " retires");
        }
    }

    class BarrierMessage implements Runnable {
        String msg;
        BarrierMessage(String msg) { this.msg = msg; }
        public void run() {
            System.out.println(msg);
        }
    }

    public SantaClaus() {
        santa = new Semaphore(1);
        queueElves = new Semaphore(3, true);    // use a fair semaphore
        threeElves = new CyclicBarrier(3,
                new BarrierMessage("--- Three elves are knocking ---"));
        elvesLeaveOffice = new CyclicBarrier(3,
                new BarrierMessage("---- Elvish problems solved ----"));
        allReindeers = new CyclicBarrier(9, new Runnable() {
            public void run() {
                System.out.println("=== Reindeer reunion for Christmas " + year +" ===");
            }});

        ArrayList<Thread> threads = new ArrayList<Thread>();
        for (int i = 0; i < 10; ++i)
            threads.add(new Thread(new Elf(i)));
        for (int i = 0; i < 9; ++i)
            threads.add(new Thread(new Reindeer(i)));
        Iterator<Thread> iter = threads.iterator();
        while (iter.hasNext())
            iter.next().start();

        try {
            // wait until !kidsStillBelieveInSanta
            disbelief.acquire();
            System.out.println("Faith has vanished from the world");
            iter = threads.iterator();
            while (iter.hasNext())
                iter.next().interrupt();
            iter = threads.iterator();
            while (iter.hasNext())
                iter.next().join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("The End of Santa Claus");
    }

    /**
     * @param args
     */
    public static void main(String[] args) {
        new SantaClaus();
    }
}
