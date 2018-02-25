package ag.threads;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Scanner;

/**
 * Hello world!
 *
 */
public class App {

    private static volatile boolean suspend = false;
    private static final Object suspend_lock = new Object();

    public static void main(String[] args) throws SQLException, InterruptedException {
        //pg uri connection
        String pguri = "jdbc:postgresql://127.0.0.1:5432/mytestdb?user=postgres&password=equinox";
        Connection conn = DriverManager.getConnection(pguri);
        conn.setAutoCommit(true);
        //clear
//        Operation.clear(conn);
        //operation instance
        Operation op = new Operation(conn);
        BlockingChannel channel = new BlockingChannel();
        BlockingQueue monitor0 = new BlockingQueue(1);//0 to 1
        BlockingQueue monitor1 = new BlockingQueue(1);//1 to 2
        //threads
        long init = System.currentTimeMillis();
        for (int i = 0; i < 10000; i++) {
            //bloqueia se já tiver em uso
//            System.out.println("0:bloqueado");
            channel.lock();//<-- por que?
//            int id = Operation.getLastId(conn) + 1;
            int id = i;
//            int id = op.getID() + 1;
//            System.out.println("1:desbloqueado");
            Thread t0 = new Thread(new InsertOperation(suspend, suspend_lock, id, op, monitor0));
            Thread t1 = new Thread(new UpdateOperation(suspend, suspend_lock, id, op, monitor0, monitor1));
            Thread t2 = new Thread(new DeleteOperation(suspend, suspend_lock, id, op, monitor1, channel));
            t0.start();
            t1.start();
            t2.start();
            if (i < 1) {
                Thread thread = new Thread() {
                    @Override
                    public void run() {
                        int val = 0;
                        while (val != 3) {
                            Scanner scan = new Scanner(System.in);
                            val = scan.nextInt();
                            if (val == 1) {
                                suspend = true;
                                System.out.println("devia pausar");
                            } else if (val == 2) {
                                suspend = false;
                                synchronized (suspend_lock) {
                                    suspend_lock.notifyAll();
                                }
                            }
                        }
                    }
                };
                thread.start();
            }
        }
        long destroy = System.currentTimeMillis();
        System.out.println(destroy - init);
    }
}
