package marshallers;

import java.io.File;
import java.util.ArrayList;
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
import models.MedjubankarskiPrenos;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class MTMarshaller {
	
	public static void createXml(MedjubankarskiPrenos mp, List<AnalitikaIzvoda> nalozi) throws ParserConfigurationException, TransformerException {
		DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
		
		System.out.println("TU SAM");
		
		//root elements
		Document doc = docBuilder.newDocument();
		Element rootElement = doc.createElement("MT-" + mp.vrstaPoruke);
		doc.appendChild(rootElement);
		
		//zaglavlje
		Element zaglavlje = doc.createElement("zaglavlje");
		
		Element swiftBankeDuz = doc.createElement("swift_banke_duznika");
		swiftBankeDuz.setTextContent(mp.bankaPosiljalac.swiftKod);
		zaglavlje.appendChild(swiftBankeDuz);
		
		Element obracunRacBankeDuz = doc.createElement("obracunski_racun_banke_duznika");
		obracunRacBankeDuz.setTextContent(mp.bankaPosiljalac.obracunskiRacun);
		zaglavlje.appendChild(obracunRacBankeDuz);
		
		Element swiftBankePrim = doc.createElement("swift_banke_poverioca");
		swiftBankePrim.setTextContent(mp.bankaPrimalac.swiftKod);
		zaglavlje.appendChild(swiftBankePrim);
		
		Element obracunRacBankePrim = doc.createElement("obracunski_racun_banke_poverioca");
		obracunRacBankePrim.setTextContent(mp.bankaPrimalac.obracunskiRacun);
		zaglavlje.appendChild(obracunRacBankePrim);
		
		Element ukupanIznos = doc.createElement("ukupan_iznos");
		ukupanIznos.setTextContent(mp.ukupanIznos.toString());
		zaglavlje.appendChild(ukupanIznos);
		
		//Prestpostavimo da su datumi valute i prijema isti
		Date datum = findLastDate(nalozi);
		
		Element datumValute = doc.createElement("datum_valute");
		datumValute.setTextContent(datum.toString());
		zaglavlje.appendChild(datumValute);
		
		Element datumPrijema = doc.createElement("datum_prijema");
		datumPrijema.setTextContent(datum.toString());
		zaglavlje.appendChild(datumPrijema);		
		
		rootElement.appendChild(zaglavlje);
		
		///////////////////////////////////
		//nalozi
		Element naloziList = doc.createElement("nalozi");
		
		for (AnalitikaIzvoda ai : nalozi){
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
			modelZaduzenja.setTextContent(ai.modelZaduzenja.toString());
			nalog.appendChild(modelZaduzenja);
			
			Element pozivNaBrojZaduzenja = doc.createElement("poziv_na_broj_zaduzenja");
			pozivNaBrojZaduzenja.setTextContent(ai.pozivNaBrojZaduzenja);
			nalog.appendChild(pozivNaBrojZaduzenja);
			
			Element racunPoverioca = doc.createElement("racun_poverioca");
			racunPoverioca.setTextContent(ai.racunPoverioca);
			nalog.appendChild(racunPoverioca);
			
			Element modelOdobrenja = doc.createElement("model_odobrenja");
			modelOdobrenja.setTextContent(ai.modelOdobrenja.toString());
			nalog.appendChild(modelOdobrenja);
			
			Element pozivNaBrojOdobrenja = doc.createElement("poziv_na_broj_odobrenja");
			pozivNaBrojOdobrenja.setTextContent(ai.pozivNaBrojOdobrenja);
			nalog.appendChild(pozivNaBrojOdobrenja);
			
			Element iznos = doc.createElement("iznos");
			iznos.setTextContent(ai.iznos.toString());
			nalog.appendChild(iznos);
			
			naloziList.appendChild(nalog);
		}
			
		rootElement.appendChild(naloziList);
		
		//upisi sadrzaj u xml
		TransformerFactory tf = TransformerFactory.newInstance();
		Transformer t = tf.newTransformer();
		t.setOutputProperty(OutputKeys.INDENT, "yes");
		t.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
		DOMSource src = new DOMSource(doc);
		StreamResult result = new StreamResult(new File("./exported/test.xml"));
		
		t.transform(src, result);
	}
	
	private static Date findLastDate(List<AnalitikaIzvoda> nalozi) {
		List<Date> dates = new ArrayList<Date>();
		for (AnalitikaIzvoda ai : nalozi) {
			dates.add(ai.datumPrijema);
		}
		final Date maxDate = dates.stream().max(Date::compareTo).get();
		return maxDate;
	}

}
