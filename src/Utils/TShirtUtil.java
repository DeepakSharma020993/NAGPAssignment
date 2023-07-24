package Utils;

import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;

import Utils.DataBaseUtil;
import bean.TShirtBean;
import static Constants.TShirtConstant.*;
public class TShirtUtil{
	private static Properties props=new Properties();
	private static FileReader reader;
	public TShirtUtil() {
		try {
			reader = new FileReader(PROPERTYFILE_PATH);
			props.load(reader); 
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void getMyTShirt(String color , String size , String gender , String outputPreference) {
		Connection conn = null;
		List<TShirtBean> sortedTshirtList = null;
		try {
			conn = DriverManager.getConnection(props.getProperty("dburl"), props.getProperty("username"), props.getProperty("password"));
			ResultSet resultset  = DataBaseUtil.executeSelectQuery(conn,prepareSelectQuery(color,size,gender));		
			List<TShirtBean> tShirtList  = getTshirtListFromDBResponse(resultset);
			if(outputPreference.equalsIgnoreCase(RATING)) {
				sortedTshirtList = tShirtList.stream().sorted(Comparator.comparingDouble(TShirtBean::getRating).thenComparing(Comparator.comparingDouble(TShirtBean::getPrice))).collect(Collectors.toList());
			}else if(outputPreference.equalsIgnoreCase(PRICE)) {
				sortedTshirtList = tShirtList.stream().sorted(Comparator.comparingDouble(TShirtBean::getPrice).thenComparing(Comparator.comparingDouble(TShirtBean::getRating))).collect(Collectors.toList());
			}else {
				System.out.println(" Invaid sorting preference choice. You will get unsorted result");
				sortedTshirtList= tShirtList;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {}

			}
		}


		showMessageToUser(sortedTshirtList,outputPreference);


	}

	private String prepareSelectQuery(String color , String size , String gender) {

		return "Select * from "+TABLE_NAME+" where "+COLOR+" = '"+color+"' and "+SIZE+" = '"+size+"' and "+GENDER+" = '"+gender+"'";
	}


	private List<TShirtBean> getTshirtListFromDBResponse(ResultSet result){
		List<TShirtBean> dataList = new ArrayList<>();
		try {
			result.next();
			while(result.next()) {
				TShirtBean tShirtBean = new TShirtBean();
				tShirtBean.setId(result.getString(ID));
				tShirtBean.setColor(result.getString(COLOR));
				tShirtBean.setGender(result.getString(GENDER));
				tShirtBean.setSize(result.getString(SIZE));
				tShirtBean.setName(result.getString(NAME));
				tShirtBean.setPrice(result.getDouble(PRICE));
				tShirtBean.setRating((result.getDouble(RATING)));
				tShirtBean.setAvailability(result.getString(AVAILABILITY));
				dataList.add(tShirtBean);			
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return dataList;

	}

	private void showMessageToUser(List<TShirtBean> tshirtList, String outputPreference) {
		int count = 1;
		if(tshirtList.size() == 0) {
			System.out.println(NO_TSHIRT_MSG);
		}else {
			System.out.println("We have "+tshirtList.size()+" tshirt  as per  your filter");
			System.out.println("");
			if(outputPreference.equalsIgnoreCase("rating")) {
				System.out.println("Displaying Result as per sorted rating");
				System.out.println("");
			}else if(outputPreference.equalsIgnoreCase("price")) {
				System.out.println("Displaying Result as per sorted price");
				System.out.println("");
			}
		}

		for(TShirtBean tShirt : tshirtList) {
			System.out.println("------------ TShirt "+count+"----------");
			System.out.println("Name of the Product-- "+tShirt.getName());
			System.out.println("Color of the Product-- "+tShirt.getColor());
			System.out.println("Gender type  of the Product-- "+tShirt.getGender());
			System.out.println("Size of the Product-- "+tShirt.getSize());
			System.out.println("Price of the Product-- "+tShirt.getPrice());
			System.out.println("Rating of the Product-- "+tShirt.getRating());
			System.out.println("Availability of the Product-- "+tShirt.getAvailability());
			System.out.println("");
			System.out.println("");
			count++;


		}

		System.out.println("------------ !!!!!!HAPPY SHOPPING!!!!!!!!!!!----------");

	}
}
