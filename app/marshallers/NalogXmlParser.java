package marshallers;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import models.AnalitikaIzvoda;
import models.Valuta;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import play.libs.XPath;

public class NalogXmlParser {
	
public static AnalitikaIzvoda parsexml(File xml) throws ParserConfigurationException, SAXException, IOException, ParseException{
		
		DocumentBuilderFactory dbfac = DocumentBuilderFactory.newInstance();
	    DocumentBuilder docBuilder = dbfac.newDocumentBuilder();
	    Document nalog = docBuilder.parse(xml);
	    Element root = nalog.getDocumentElement();
	    
	    String valutaStr = XPath.selectText("valuta", root);
	    
	    String duznik = XPath.selectText("duznik", root);
	    
	    String svrhaPlacanja = XPath.selectText("svrha_placanja", root);
	    
	    String poverilac = XPath.selectText("poverilac", root);
	    
	    String datumPrijemaStr = XPath.selectText("datum_prijema", root);
	    Date datumPrijema = new SimpleDateFormat("yyyy-MM-dd").parse(datumPrijemaStr);
	    
	    String datumValuteStr = XPath.selectText("datum_valute", root);
	    Date datumValute = new SimpleDateFormat("yyyy-MM-dd").parse(datumValuteStr);
	    
	    String racunDuznika = XPath.selectText("racun_duznika", root);

	    Integer modelZaduzenja = XPath.selectText("model_zaduzenja", root) != null ? Integer.parseInt(XPath.selectText("model_zaduzenja", root)) : null;
	   
	    String pozivNaBrojZaduzenja = XPath.selectText("poziv_na_broj_zaduzenja", root);
	    
	    String racunPoverioca = XPath.selectText("racun_poverioca", root);
	    
	    Integer modelOdobrenja = XPath.selectText("model_odobrenja", root) != null ? Integer.parseInt(XPath.selectText("model_odobrenja", root)) : null;
	    
	    String pozivNaBrojOdobrenja = XPath.selectText("poziv_na_broj_odobrenja", root);
	    
	    Boolean hitno = Boolean.parseBoolean(XPath.selectText("hitno", root));
	    
	    Long iznos = Long.parseLong(XPath.selectText("iznos", root));
	    
	    AnalitikaIzvoda ai = new AnalitikaIzvoda (
	    		datumPrijema, null, duznik, svrhaPlacanja, poverilac, datumPrijema, datumValute, racunDuznika, modelZaduzenja, pozivNaBrojZaduzenja, racunPoverioca, modelOdobrenja, pozivNaBrojOdobrenja, hitno, iznos, null, null, null);
	    
	    return ai;
	}

}
