package ag.threads;

import java.sql.SQLException;

public class InsertOperation implements Runnable {

    private final int id;
    private final Operation op;
    private final BlockingQueue monitor;
    private final boolean suspend;
    private final Object suspend_lock;

    public InsertOperation(boolean suspend, Object suspend_lock, int id, Operation op, BlockingQueue se) {
        this.op = op;
        this.monitor = se;
        this.id = id;
        this.suspend = suspend;
        this.suspend_lock = suspend_lock;
    }

    public void run() {
        //como garantir um ID único???
        try {
            if(suspend)
                synchronized(suspend_lock) {suspend_lock.wait();}
            //operar
            op.insert(id, "Ari " + id);
//            System.out.println("2:inserindo " + id);
            //notificar
            monitor.enqueue();
//            System.out.println("3:notificando t1");
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }
}
