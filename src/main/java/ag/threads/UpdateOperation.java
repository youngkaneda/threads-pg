package ag.threads;

import java.sql.SQLException;

public class UpdateOperation implements Runnable {

    private final int id;
    private final Operation op;
    private final BlockingQueue monitor0;
    private final BlockingQueue monitor1;
    private final boolean suspend;
    private final Object suspend_lock;

    public UpdateOperation(boolean suspend, Object suspend_lock, int id, Operation op, BlockingQueue m0, BlockingQueue m1) {
        this.op = op;
        this.monitor0 = m0;
        this.monitor1 = m1;
        this.id = id;
        this.suspend = suspend;
        this.suspend_lock = suspend_lock;
    }

    public void run() {
        //como garantir um ID único???
        try {
            if(suspend)
                synchronized(suspend_lock) {suspend_lock.wait();}
            //aguardar a inserção
//            System.out.println("4:aguardando t0");
            monitor0.attend();
            //operar
            op.update(id);
//            System.out.println("5:atualizando " + id);
            //notificar
            monitor1.enqueue();
//            System.out.println("6:notificando t2");
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
