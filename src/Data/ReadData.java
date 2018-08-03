package Data;

import utils.Util;

import java.io.File;
import java.io.FileInputStream;

import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ReadData {
	
	public static Sheet readExcel() throws Exception { 
		 File file =    new File(Util.getFilePath()+Util.getFileName());
		 FileInputStream inputStream = new FileInputStream(file);
		 Workbook guru99Workbook = new XSSFWorkbook(inputStream);
		 Sheet guru99Sheet = guru99Workbook.getSheet(Util.getSheetName());
		 return guru99Sheet;
	}

}
