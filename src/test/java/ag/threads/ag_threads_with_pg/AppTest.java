package ag.threads.ag_threads_with_pg;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import ag.threads.Operation;
import junit.framework.TestCase;


public class AppTest extends TestCase {
	
	public void testOperations() throws SQLException{
		//
		String pguri = "jdbc:postgresql://192.168.99.100:5432/db?user=postgres&password=123456";
		//
		//Driver.register();
		Connection conn = DriverManager.getConnection(pguri);
		//
		Operation op = new Operation(conn);
		op.insert(1, "Ari");
		op.update(1);
		op.delete(1);
	}
	
}
