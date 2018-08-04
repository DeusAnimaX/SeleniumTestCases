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
		 Workbook workbook = new XSSFWorkbook(inputStream);
		 Sheet sheet = workbook.getSheet(Util.getSheetName());
		 return sheet;
	}

}
