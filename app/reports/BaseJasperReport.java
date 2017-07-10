package reports;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Map;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.export.JExcelApiExporter;
import net.sf.jasperreports.engine.JRExporter;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.view.JasperViewer;
import net.sf.jasperreports.engine.JRExporter;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperCompileManager;
import play.*;
import sun.misc.IOUtils;
import sun.nio.ch.IOUtil;

@SuppressWarnings("deprecation")
public class BaseJasperReport {
  //here should be jrxml and jasper files, 
  //you can generate them with iReports(I'm using netbeans plugin)
  // http://jasperforge.org/projects/ireport 
  static String REPORT_DEFINITION_PATH = "./app/reports/";

  public static InputStream generateReport(String reportDefFile, Map reportParams) {
	  OutputStream os = new ByteArrayOutputStream();
	  Connection conn = null;
	    String url = "jdbc:mysql://91.226.241.219:3306/";
	    String dbName = "c0PlayBaza";
	    String driver = "com.mysql.jdbc.Driver";
	    String userName = "c0test";
	    String password = "123test321";
	    
	  try {
		  
		  Class.forName(driver).newInstance();
	      conn = DriverManager.getConnection(url+dbName,userName,password);
		  
		  JasperPrint jp = JasperFillManager.fillReport(REPORT_DEFINITION_PATH + reportDefFile + ".jasper", reportParams, conn);
		  JasperExportManager.exportReportToPdfStream(jp, os);
		  conn.close();
	  } catch (Exception e) {
		e.printStackTrace();
	}
      return new ByteArrayInputStream(((ByteArrayOutputStream) os).toByteArray());
  }
}