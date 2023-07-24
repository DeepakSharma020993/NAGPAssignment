package Utils;


import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Properties;

import bean.TShirtBean;
import static Constants.TShirtConstant.*;

public class DataBaseUtil extends Thread {

	static Properties props=new Properties();
	
	public DataBaseUtil() {
		FileReader reader;
		try {
			reader = new FileReader(PROPERTYFILE_PATH);
			props.load(reader); 
		} catch (IOException e) {
			e.printStackTrace();
		}
		 
    }
	
	/**
	 * Method will Run the provided select query and return a ResultSet
	 */
	
	public static ResultSet executeSelectQuery(Connection conn , String query) {		
    
		Statement stmt  = null;
		ResultSet resultSet = null;
		try {		 
			 stmt= conn.createStatement();
			 resultSet = stmt.executeQuery(query);
		} catch (SQLException e) {
			e.printStackTrace();
		} 
		 return resultSet;
		
	}
	
	public void run() {
		String query ;
		List<TShirtBean> dataList;
		CSVUtil csvUtil = new CSVUtil();
		Connection conn;
		try {
			conn = DriverManager.getConnection(props.getProperty("dburl"), props.getProperty("username"), props.getProperty("password"));
			Statement stmt = conn.createStatement();
			while(true) {
				dataList = csvUtil.getDataListfromCSVFiles();
				for (TShirtBean tShirt : dataList ) {
					
					if(checkIfRecordExists(conn,tShirt)) {
						if(checkifRecordUpdated(stmt,tShirt)) {
						query = " Update "+TABLE_NAME+ " Set "+NAME+" = '"+tShirt.getName()+"' , "+COLOR+" = '"+tShirt.getColor()+"' , "+GENDER+" = '"+tShirt.getGender()+"' , "+SIZE+" = '"+tShirt.getSize()+"' , "+PRICE+" = "+tShirt.getPrice()+" , "+RATING+" = "+tShirt.getRating()+" , "+AVAILABILITY+" = '"+tShirt.getAvailability()+"' where "+ID+" = '"+tShirt.getId()+"'";	
						stmt.executeUpdate(query);
						}
					}else {
						query =" INSERT INTO "+TABLE_NAME+" ( "+ID+" , " +NAME+" , "+COLOR+" , "+GENDER+" , "+SIZE+" , "+PRICE+" , "+RATING+" , "+AVAILABILITY+" ) VALUES  ( '"+tShirt.getId()+"' , '"+tShirt.getName()+"' , '"+tShirt.getColor()+"' , '"+tShirt.getGender()+"' , '"+tShirt.getSize()+"' , "+tShirt.getPrice()+" , "+tShirt.getRating()+" , '"+tShirt.getAvailability()+"' )";
						stmt.executeUpdate(query);
					}
					
					
				}
				//System.out.println("Updating DB");
				Thread.sleep(Integer.valueOf(props.getProperty("dbpolltime")));
			}
		} catch (SQLException | InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public static Boolean checkIfRecordExists(Connection con,TShirtBean tShirt ) throws SQLException {

		Statement stmt = con.createStatement();
		String query = "Select count(*) from "+TABLE_NAME+" where "+ID+" = '"+tShirt.getId()+"'";
		ResultSet resultset =  stmt.executeQuery(query);
		resultset.next();
		if(resultset.getInt(1)==0) {
			return false;
		}else {
			return true;
		}

	  	
	}

	public Boolean checkifRecordUpdated(Statement stmt , TShirtBean tShirt) throws SQLException{
		String query = "Select * from "+TABLE_NAME+" where "+ID+" = '"+tShirt.getId()+"'";
		ResultSet resultset =  stmt.executeQuery(query);
		resultset.next();
		if(tShirt.getColor().equalsIgnoreCase(resultset.getString(COLOR)) && tShirt.getName().equalsIgnoreCase(resultset.getString(NAME)) && tShirt.getSize().equalsIgnoreCase(resultset.getString(SIZE)) && tShirt.getAvailability().equalsIgnoreCase(resultset.getString(AVAILABILITY))&& tShirt.getGender().equalsIgnoreCase(resultset.getString(GENDER)) && tShirt.getPrice() == resultset.getDouble(PRICE) && tShirt.getRating() == resultset.getDouble(RATING)){
			return false;
		}else {
			return true;
		}

	}
	
}
