package Utils;

import static Constants.TShirtConstant.PROPERTYFILE_PATH;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import bean.TShirtBean;

public class CSVUtil {
	
	private static Properties props=new Properties();
	private static FileReader reader;
	public CSVUtil() {
		try {
			reader = new FileReader(PROPERTYFILE_PATH);
			props.load(reader); 
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Method will parse all the csv files at provided path and return in form of List
	 */
	
	public  List<TShirtBean> getDataListfromCSVFiles() {

		File directory = new File(props.getProperty("dataFilePath"));
		BufferedReader reader = null;
		List<TShirtBean> dataList = new ArrayList<>();
		String[] totalFileAr = directory.list();
		for(int i=0;i<totalFileAr.length;i++) {		
			try {
				reader = new BufferedReader(new FileReader(props.getProperty("dataFilePath")+"\\"+totalFileAr[i]));
				 
				String linetext = null;
				reader.readLine();
				while((linetext=reader.readLine())!=null) {
					String[] data = linetext.split("\\|");
					TShirtBean tShirtBean = new TShirtBean();
					tShirtBean.setId(data[0]);
					tShirtBean.setName(data[1]);
					tShirtBean.setColor(data[2]);
					tShirtBean.setGender(data[3]);
					tShirtBean.setSize(data[4]);
					tShirtBean.setPrice(Double.valueOf(data[5]));
					tShirtBean.setRating(Double.valueOf(data[6]));
					tShirtBean.setAvailability(data[7]);
					dataList.add(tShirtBean);

				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}finally {
				if(reader != null) {
					try {
						reader.close();
					} catch (IOException e) {}
										
				}
			}
			
		}

		return dataList;

	}
}
