package models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import play.db.jpa.Model;

@Entity
public class Uloga extends Model {
	
	@Column (name="NAZIV_ULOGE")
	public String nazivUloge;
	

	public Uloga(String nazivUloge) {
		super();
		this.nazivUloge = nazivUloge;
	}

	public String getNazivUloge() {
		return nazivUloge;
	}

	public void setNazivUloge(String nazivUloge) {
		this.nazivUloge = nazivUloge;
	}

}
