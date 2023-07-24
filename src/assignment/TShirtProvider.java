package assignment;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Scanner;
import Utils.DataBaseUtil;
import Utils.TShirtUtil;

public class TShirtProvider {

	public static void main(String[] args) throws SQLException, IOException {
		DataBaseUtil dataBaseUtil = new DataBaseUtil();
		TShirtUtil tshirtprovider = new TShirtUtil();
		Integer exit=1;
		dataBaseUtil.start();
		while(exit==1){ 
			//This Method will update Database at configurable time
			
			Scanner colorScanner = new Scanner(System.in);  // Create a Scanner object  
			System.out.println("Enter Color");   
			String color = colorScanner.nextLine(); 
			Scanner sizeScanner = new Scanner(System.in);  // Create a Scanner object  
			System.out.println("Enter Size");  
			String size = sizeScanner.nextLine();   
			Scanner genderScanner = new Scanner(System.in);  // Create a Scanner object  
			System.out.println("Enter Gender");   
			String gender = genderScanner.nextLine(); 
			Scanner opPrefScanner = new Scanner(System.in);  // Create a Scanner object  
			System.out.println("Enter Preference"); 
			String opPref = opPrefScanner.nextLine();  
			tshirtprovider.getMyTShirt(color, size, gender, opPref);
			Scanner e=new Scanner(System.in); 
			System.out.println("Type any character to exit, 1 to continue searching"); 
			try {  
				exit = Integer.parseInt(e.nextLine());  
			}catch (Exception e1){   
				exit=0;   }
			
	}	
		System.out.println("See you Again!!!"); 
		dataBaseUtil.stop();	
	}

}
