package marshallers;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import models.AnalitikaIzvoda;
import models.DnevnoStanjeRacuna;
import models.Racun;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class IzvodMarshaller {
	
	public static void createXml(List<DnevnoStanjeRacuna> dsrList, Racun racun, Date datumOd, Date datumDo) throws ParserConfigurationException, TransformerException {
		DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
		
		//root elements
		Document doc = docBuilder.newDocument();
		Element rootElement = doc.createElement("izvod_za_period");
		rootElement.setAttribute("datum-od", dateToStr(datumOd));
		rootElement.setAttribute("datum-do", dateToStr(datumDo));		
		doc.appendChild(rootElement);		
		
		//dnevna stanja racuna i nalozi		
		Element dnevnaStanja = doc.createElement("dnevna_stanja");
		Long koristUkupno = 0L;
		Long teretUkupno = 0L;
		
		for (DnevnoStanjeRacuna dsr : dsrList){
			if (dsr.datum.after(datumOd) && dsr.datum.before(datumDo)){
				Element dnevnoStanje = doc.createElement("promet_na_dan");
				dnevnoStanje.setAttribute("datum", dateToStr(dsr.datum));
			
				Element prethodnoStanje = doc.createElement("prethodno_stanje");
				prethodnoStanje.setTextContent(dsr.prethodnoStanje.toString());
				dnevnoStanje.appendChild(prethodnoStanje);			
			
				Element uKorist = doc.createElement("promet_u_korist");
				uKorist.setTextContent(dsr.prometUKorist.toString());
				dnevnoStanje.appendChild(uKorist);
				koristUkupno +=dsr.prometUKorist;
			
				Element naTeret = doc.createElement("promet_na_teret");
				naTeret.setTextContent(dsr.prometNaTeret.toString());
				dnevnoStanje.appendChild(naTeret);
				koristUkupno +=dsr.prometNaTeret;			
			
				Element novoStanje = doc.createElement("novo_stanje");
				novoStanje.setTextContent(dsr.novoStanje.toString());
				dnevnoStanje.appendChild(novoStanje);
			
				//nalozi
				Element naloziList = doc.createElement("analitike_izvoda");
				
				for (AnalitikaIzvoda ai : dsr.analitikaIzvoda){
					Element nalog = doc.createElement("nalog");
					nalog.setAttribute("id", ai.id.toString());
				
					Element duznik = doc.createElement("duznik_nalogodavac");
					duznik.setTextContent(ai.duznikNalogodavac);
					nalog.appendChild(duznik);
				
					Element svrhaPlacanja = doc.createElement("svrha_placanja");
					svrhaPlacanja.setTextContent(ai.svrhaPlacanja);
					nalog.appendChild(svrhaPlacanja);
				
					Element primalac = doc.createElement("primalac_poverilac");
					primalac.setTextContent(ai.poverilacPrimalac);
					nalog.appendChild(primalac);
				
					Element datumNaloga = doc.createElement("datum_naloga");
					datumNaloga.setTextContent(ai.datumPrijema.toString());
					nalog.appendChild(datumNaloga);
					
					Element racunDuznika = doc.createElement("racun_duznika");
					racunDuznika.setTextContent(ai.racunDuznika);
					nalog.appendChild(racunDuznika);
				
					Element modelZaduzenja = doc.createElement("model_zaduzenja");
					modelZaduzenja.setTextContent(ai.modelZaduzenja != null ? ai.modelZaduzenja.toString() : "");
					nalog.appendChild(modelZaduzenja);
				
					Element pozivNaBrojZaduzenja = doc.createElement("poziv_na_broj_zaduzenja");
					pozivNaBrojZaduzenja.setTextContent(ai.pozivNaBrojZaduzenja);
					nalog.appendChild(pozivNaBrojZaduzenja);
				
					Element racunPoverioca = doc.createElement("racun_poverioca");
					racunPoverioca.setTextContent(ai.racunPoverioca);
					nalog.appendChild(racunPoverioca);
				
					Element modelOdobrenja = doc.createElement("model_odobrenja");
					modelOdobrenja.setTextContent(ai.modelOdobrenja != null ? ai.modelOdobrenja.toString() : "");
					nalog.appendChild(modelOdobrenja);
				
					Element pozivNaBrojOdobrenja = doc.createElement("poziv_na_broj_odobrenja");
					pozivNaBrojOdobrenja.setTextContent(ai.pozivNaBrojOdobrenja);
					nalog.appendChild(pozivNaBrojOdobrenja);
				
					Element iznos = doc.createElement("iznos");
					iznos.setTextContent(ai.iznos.toString());
					nalog.appendChild(iznos);
				
					naloziList.appendChild(nalog);
				}
				dnevnoStanje.appendChild(naloziList);
			
				dnevnaStanja.appendChild(dnevnoStanje);
			}
		}
		
		//zaglavlje
		Element zaglavlje = doc.createElement("zaglavlje");
				
		Element sifraBanke = doc.createElement("sifra_banke");
		sifraBanke.setTextContent(racun.banka.sifraBanke);
		zaglavlje.appendChild(sifraBanke);
				
		Element brojRacuna = doc.createElement("broj_racuna");
		brojRacuna.setTextContent(racun.brojRacuna);
		zaglavlje.appendChild(brojRacuna);
				
		Element klijent = doc.createElement("klijent");
		klijent.setTextContent(racun.klijent.naziv);
		zaglavlje.appendChild(klijent);
				
		Element prethodnoStanjePeriod = doc.createElement("prethodno_stanje_za_period");
		prethodnoStanjePeriod.setTextContent(dsrList.get(0).prethodnoStanje.toString());
		zaglavlje.appendChild(prethodnoStanjePeriod);
				
		Element uKoristUkupno = doc.createElement("u_korist_ukupno");
		uKoristUkupno.setTextContent(koristUkupno.toString());
		zaglavlje.appendChild(uKoristUkupno);
				
		Element naTeretUkupno = doc.createElement("na_teret_ukupno");
		naTeretUkupno.setTextContent(teretUkupno.toString());
		zaglavlje.appendChild(naTeretUkupno);
				
		Element novoStanjePeriod = doc.createElement("novo_stanje_za_period");
		novoStanjePeriod.setTextContent(dsrList.get(dsrList.size()-1).novoStanje.toString());
		zaglavlje.appendChild(novoStanjePeriod);
				
		rootElement.appendChild(zaglavlje);
		rootElement.appendChild(dnevnaStanja);
		
		//upisi sadrzaj u xml
		TransformerFactory tf = TransformerFactory.newInstance();
		Transformer t = tf.newTransformer();
		t.setOutputProperty(OutputKeys.INDENT, "yes");
		t.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
		DOMSource src = new DOMSource(doc);
		StreamResult result = new StreamResult(new File("./exported/test-izvod.xml"));
				
		t.transform(src, result);
		
	}
	
	private static String dateToStr(Date d){
		DateFormat df = new SimpleDateFormat("dd-MM-yyyy");		
		return df.format(d);
	}

}
