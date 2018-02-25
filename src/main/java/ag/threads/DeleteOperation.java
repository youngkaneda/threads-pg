package ag.threads;

import java.sql.SQLException;

public class DeleteOperation implements Runnable {

    private final int id;
    private final Operation op;
    private final BlockingQueue monitor;
    private final BlockingChannel channel;
    private final boolean suspend;
    private final Object suspend_lock;

    public DeleteOperation(boolean suspend, Object suspend_lock, int id, Operation op, BlockingQueue se, BlockingChannel channel) {
        this.op = op;
        this.monitor = se;
        this.channel = channel;
        this.id = id;
        this.suspend = suspend;
        this.suspend_lock = suspend_lock;
    }

    public void run() {
        //como garantir um ID único???
        try {
            if(suspend)
                synchronized(suspend_lock) {suspend_lock.wait();}
            //aguardar a atualização
//            System.out.println("7:aguarando t1");
            monitor.attend();
            //operar
            op.delete(id);
//            System.out.println("8:excluindo " + id);
            //
            channel.unlock();
//            System.out.println("9:desbloquando");
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
