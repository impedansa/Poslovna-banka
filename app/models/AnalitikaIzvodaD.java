package models;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

public class AnalitikaIzvodaD {
	
	public Long id;
	
	
	public Date datumAnalitike;
	
	
	public String smer;
	
	
	public String duznikNalogodavac;
	
	
	public String svrhaPlacanja;
	
	
	public String poverilacPrimalac;
	

	public Date datumPrijema;
	
	
	public Date datumValute;
	
	
	public String racunDuznika;
	
	
	public int modelZaduzenja;
	
	
	public String pozivNaBrojZaduzenja;
	
	
	public String racunPoverioca;
	
	
	public int modelOdobrenja;
	
	
	public String pozivNaBrojOdobrenja;
	
	
	public boolean hitno;
	
	
	public long iznos;
	
	
	public int tipGreske;
	
	
	public String status;

	
	public DnevnoStanjeRacuna dnevnoStanjeRacuna;


	public AnalitikaIzvodaD(Long id, Date datumAnalitike, String smer, String duznikNalogodavac, String svrhaPlacanja,
			String poverilacPrimalac, Date datumPrijema, Date datumValute, String racunDuznika, int modelZaduzenja,
			String pozivNaBrojZaduzenja, String racunPoverioca, int modelOdobrenja, String pozivNaBrojOdobrenja,
			boolean hitno, long iznos, int tipGreske, String status, DnevnoStanjeRacuna dnevnoStanjeRacuna) {
		super();
		this.id = id;
		this.datumAnalitike = datumAnalitike;
		this.smer = smer;
		this.duznikNalogodavac = duznikNalogodavac;
		this.svrhaPlacanja = svrhaPlacanja;
		this.poverilacPrimalac = poverilacPrimalac;
		this.datumPrijema = datumPrijema;
		this.datumValute = datumValute;
		this.racunDuznika = racunDuznika;
		this.modelZaduzenja = modelZaduzenja;
		this.pozivNaBrojZaduzenja = pozivNaBrojZaduzenja;
		this.racunPoverioca = racunPoverioca;
		this.modelOdobrenja = modelOdobrenja;
		this.pozivNaBrojOdobrenja = pozivNaBrojOdobrenja;
		this.hitno = hitno;
		this.iznos = iznos;
		this.tipGreske = tipGreske;
		this.status = status;
		this.dnevnoStanjeRacuna = dnevnoStanjeRacuna;
	}
	
	

}
