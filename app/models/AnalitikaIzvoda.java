package models;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import play.db.jpa.Model;

@Entity
public class AnalitikaIzvoda extends Model {
	
	@Column(name="DATUM_ANALITIKE")
	public String datumAnalitike;
	
	@Column(name="SMER", length=1)
	public String smer;
	
	@Column(name="DUZNIK_NALOGODAVAC")
	public String duznikNalogodavac;
	
	@Column(name="SVRHA_PLACANJA")
	public String svrhaPlacanja;
	
	@Column(name="POVERILAC_PRIMALAC")
	public String poverilacPrimalac;
	
	@Column(name="DATUM_PRIJEMA")
	public String datumPrijema;
	
	@Column(name="DATUM_VALUTE")
	public String datumValute;
	
	@Column(name="RACUN_DUZNIKA")
	public String racunDuznika;
	
	@Column(name="MODEL_ZADUZENJA")
	public int modelZaduzenja;
	
	@Column(name="POZIV_NA_BROJ_ZADUZENJA")
	public String pozivNaBrojZaduzenja;
	
	@Column(name="RACUN_POVERIOCA")
	public String racunPoverioca;
	
	@Column(name="MODEL_ODOBRENJA")
	public int modelOdobrenja;
	
	@Column(name="POZIV_NA_BROJ_ODOBRENJA")
	public String pozivNaBrojOdobrenja;
	
	@Column(name="HITNO")
	public boolean hitno;
	
	@Column(name="IZNOS")
	public float iznos;
	
	@Column(name="TIP_GRESKE")
	public int tipGreske;
	
	@Column(name="STATUS", length=1)
	public String status;

	@ManyToOne
	public DnevnoStanjeRacuna dnevnoStanjeRacuna;
	
	public AnalitikaIzvoda(String datumAnalitike, String smer, String duznikNalogodavac, String svrhaPlacanja, String poverilacPrimalac, String datumPrijema, String datumValute, String racunDuznika, int modelZaduzenja, String pozivNaBrojZaduzenja, String racunPoverioca, int modelOdobrenja, String pozivNaBrojOdobrenja, boolean hitno, float iznos, int tipGreske, String status, DnevnoStanjeRacuna dnevnoStanjeRacuna) {
		
		super();
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
