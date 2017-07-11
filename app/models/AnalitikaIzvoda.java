package models;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import play.db.jpa.Model;

@Entity
public class AnalitikaIzvoda extends Model {
	
	@Column(name="DATUM_ANALITIKE")
	public Date datumAnalitike;
	
	@Column(name="SMER", length=1)
	public String smer;
	
	@Column(name="DUZNIK_NALOGODAVAC", length = 256)
	public String duznikNalogodavac;
	
	@Column(name="SVRHA_PLACANJA", length = 256)
	public String svrhaPlacanja;
	
	@Column(name="POVERILAC_PRIMALAC", length = 256)
	public String poverilacPrimalac;
	
	@Column(name="DATUM_PRIJEMA")
	public Date datumPrijema;
	
	@Column(name="DATUM_VALUTE")
	public Date datumValute;
	
	@Column(name="RACUN_DUZNIKA", length = 18)
	public String racunDuznika;
	
	@Column(name="MODEL_ZADUZENJA")
	public Integer modelZaduzenja;
	
	@Column(name="POZIV_NA_BROJ_ZADUZENJA", length = 20)
	public String pozivNaBrojZaduzenja;
	
	@Column(name="RACUN_POVERIOCA", length = 18)
	public String racunPoverioca;
	
	@Column(name="MODEL_ODOBRENJA")
	public Integer modelOdobrenja;
	
	@Column(name="POZIV_NA_BROJ_ODOBRENJA", length = 20)
	public String pozivNaBrojOdobrenja;
	
	@Column(name="HITNO")
	public boolean hitno;
	
	@Column(name="IZNOS")
	public Long iznos;
	
	@Column(name="TIP_GRESKE")
	public Integer tipGreske;
	
	@Column(name="STATUS", length=1)
	public String status;

	@ManyToOne
	public DnevnoStanjeRacuna dnevnoStanjeRacuna;
	
	@OneToMany(mappedBy = "analitikaIzvoda", cascade=CascadeType.ALL)
	public List<StavkaPrenosa> stavkaPrenosa;
	
	public AnalitikaIzvoda(Date datumAnalitike, String smer, String duznikNalogodavac, String svrhaPlacanja, String poverilacPrimalac, Date datumPrijema, Date datumValute, String racunDuznika, Integer modelZaduzenja, String pozivNaBrojZaduzenja, String racunPoverioca, Integer modelOdobrenja, String pozivNaBrojOdobrenja, boolean hitno, Long iznos, Integer tipGreske, String status, DnevnoStanjeRacuna dnevnoStanjeRacuna) {
		
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
