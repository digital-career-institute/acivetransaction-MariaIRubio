package exercise_activeTransaction;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class ActiveTransction {
	
	private static final String url = "jdbc:mysql://localhost:3306/new_database";
	private static final String username = "root";
	private static final String password = "1234";
	private static Connection myConn;
	
	public static void main(String[] args) {

		try {
			//Connection
			myConn = DriverManager.getConnection(url, username, password);
			
			//Set auto-commit to false
			myConn.setAutoCommit(false);
			
			boolean hasActiveTransactions = isCurrentActiveTransaction();
			System.out.println("Active transactions: " + hasActiveTransactions );
			
			try {
				if(myConn != null) {
					if(hasActiveTransactions) {
					myConn.rollback();
					System.out.println("Rolling back");
					}else {
						myConn.commit();
						System.out.println("Committing");
					}
				}
			}catch(SQLException rollbackException) {
				rollbackException.printStackTrace();
			}
			
		}catch(SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if(myConn != null) {
					myConn.close();
				}
			}catch(SQLException closeException) {
				closeException.printStackTrace();
			}
		}
	}
	
	private static boolean isCurrentActiveTransaction() throws SQLException {
		   
	    String sql = "SELECT COUNT(1) AS count FROM INFORMATION_SCHEMA.INNODB_TRX WHERE trx_mysql_thread_id = CONNECTION_ID()";
	    
	    try (Statement myStmt = myConn.createStatement();    	    
	    	 ResultSet myRs = myStmt.executeQuery(sql)){
	    	 if(myRs.next()) {
	    		 int count = myRs.getInt("count");
	 	    	return count > 0; // return true if there are actives transactions
	 	    }
	    }

		return false; //if there are not, then it will return false
	    
	}

}
