package models;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import play.db.jpa.Model;

@Entity
public class PermisijaUloge extends Model {
	
	@ManyToOne
	public Permisija permisija;
	
	@ManyToOne
	public Uloga uloga;

	public PermisijaUloge(Permisija permisija, Uloga uloga) {
		super();
		this.permisija = permisija;
		this.uloga = uloga;
	}

	public Permisija getPermisija() {
		return permisija;
	}

	public void setPermisija(Permisija permisija) {
		this.permisija = permisija;
	}

	public Uloga getUloga() {
		return uloga;
	}

	public void setUloga(Uloga uloga) {
		this.uloga = uloga;
	}
	
}
