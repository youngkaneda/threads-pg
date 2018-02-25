package ag.threads;

public class BlockingChannel {

    private int state = 0;
    private int count = 0;
    private Object lock = new Object();

    public void lock() throws InterruptedException {
        synchronized (lock) {
            if (state == 1) {
                lock.wait();
            } else {
                state = 1;
            }
        }
    }

    public void unlock() {
        synchronized (lock) {
            lock.notify();
        }
    }

}
