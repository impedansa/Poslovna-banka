package controllers;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;
import java.util.Arrays;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.IvParameterSpec;

import play.mvc.Controller;

public class AES {
	
	public static SecretKey generateKey() {
		//TODO: Generisati i vratiti AES kljuc duzine koju diktira najbolja praksa
		//Iako je generalno najbolja praksa koriscenje 256 bitnog kljuca zbog vece otpornosti na brute force napad,
		//AES-GCM koji radi sa 128 bitnim blokovima(moze da radi i za manje vrednosti, ali se preporucuje da se uzme najveca, odnostno 128) 
		//ne pravi razliku izmedju 128 bit kljuca i 256 bit kljuca.
		//Odnosno sa stanovista bezbednosti, oba kljuca pruzaju isti nivo bezbednosti. 256 bitni kljuc trosi vise 
		// CPU vremena, pa je bolje koristiti 128 bitni kljuc.
		// Drugi razlog za koriscenje ovog kljuca je to sto Firefox i Chrome(ium) koriste NSS koji ne podrzava 
		// AES256 GCM. Poslednji podaci o ovome su iz 2015/2016, tako da postoji mogucnost da je doslo do nekih 
		// promena u medjuvremenu.
		KeyGenerator KeyGen = null;
		try {
			KeyGen = KeyGenerator.getInstance("AES");
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    KeyGen.init(128);	
	    SecretKey key = KeyGen.generateKey();
		
        return key;
	}
	

	public static byte[] encrypt(String plainText, SecretKey key) {
		//TODO: Sifrovati otvoren tekst uz pomoc tajnog kljuca koristeci konfiguraciju AES algoritma koju diktira najbolja praksa
		//AES algoritam podrzava veliki broj rezima rada, u zavisnosti od toga u kojim uslovima se koristi, svaki 
		// ima svoje prednosti i mane. Na ovom liku (http://stackoverflow.com/questions/1220751/how-to-choose-an-aes-encryption-mode-cbc-ecb-ctr-ocb-cfb)
		// u okviru komentara moze se videti sazet pregled vecine rezima koje AES podrzava.
		//Dva najcesce koriscena rezima su CBC i CTR. Ovde koriscen AES-GCM zasnovan je na CTR rezimu i koristi 
		//GHASH za pruzanje autentifikacije. Upravo ovo i to sto ne zahteva padding stiti od padding oracle napada.
		//Padding koji omogucava padding oracle napad ovde nije potreban zato sto AES-CTR rezim funkcionise
		//tako sto pretvara blok sifre u stream sifre i samim tim padding nije potreban.
		//CBC rezim koji zahteva padding je podlozan ovim napadim, medjutim uvodjenje autentifikacije sluzi kao odbrana
		//od ove vrste napada. Ovo jeste jedna vrsta resenja, ali AES-GCM definitivno ima prednost kada je ovaj napad
		// u pitanju. Takodje, obezbedjuje paralelizam kako pri enkriptovanju, tako i pri dekriptovanju,
		//dok CBC podrzava paralelizam samo prilikom dekriptovanja.
		
		try {
			SecureRandom r;
			byte[] iv;
			//SHA1 predstavlja hes funkciju, a PRNG pseudo random number generator. 
			//Koriscenje SHA1PRNG skoro je nemoguce da dobijemo istu vrednost dva puta.
			//Ovde je to vrlo vazno za kreiranje IV-a. IV kod AES-GCM ne mora da bude random vrednost
			//(moze se koristiti i counter),ali je neophodno da bude jedinstvena.
			//Jedinstven IV obezbedjuje da se nikada ne pojavi isti par key+IV.
			//Ukoliko dodje do pojave istog para, verovatnoca uspesnog napada se znatno povecava.
			//Propisana velicina IV-a za AES-GCM je 12 bajtova. Podrzane su i vece vrednosti od 12, ali to dovodi
			//do dodatnog preracunavanja i smatra se da to samo dodatno slabi algoritam.
			//Zato se strogo preporucuje koriscenje bas ovde vrednosti.
			r = SecureRandom.getInstance("SHA1PRNG");
			iv = new byte[12]; r.nextBytes(iv);
			Cipher c;
			c = Cipher.getInstance("AES/GCM/NoPadding");
			//U GCMParameterSpec kao prvi parametar specificira se duzina taga.
			//Ova duzina moze biti u rasponu 64-128 bitova.
			//Posto je AES-GCM zavisan od duzine taga, preporucuje se koriscenje najveceg taga.
			//96 bita predstavalja neki minimum ukoliko se sifruje velika kolicina teksta.
			c.init(Cipher.ENCRYPT_MODE, key , new GCMParameterSpec(128, iv));
			byte[] ciphertext  = c.doFinal(plainText.getBytes());
			byte[] mg = new byte[12 + ciphertext.length];
			System.arraycopy(iv, 0, mg, 0, 12);
			System.arraycopy(ciphertext, 0, mg, 12, ciphertext.length);
		 
			return mg;
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			e.printStackTrace();
		} catch (InvalidAlgorithmParameterException e) {
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			e.printStackTrace();
		} catch (BadPaddingException e) {
			e.printStackTrace();
		}
		return null;
		  
		  
	}
	
	public static byte[] decrypt(byte[] cipherText, SecretKey key) {
		//TODO: Desifrovati sifru uz pomoc tajnog kljuca koristeci konfiguraciju AES algoritma koju diktira najbolja praksa
		 try {
			 //Vrednost 28 predstavlja zbir duzine IV(12) i taga(16).
			 if (cipherText.length > 28) {
		     byte[] iv = Arrays.copyOfRange(cipherText, 0, 12);
		     byte[] es = Arrays.copyOfRange(cipherText, 12, cipherText.length);
		  
		 
		     Cipher c = Cipher.getInstance("AES/GCM/NoPadding");
		     c.init(Cipher.DECRYPT_MODE, key , new GCMParameterSpec(128, iv));
		 
		     byte[] plainText = c.doFinal(es);
		     return plainText;
			 }
		} catch (InvalidKeyException e) {
					e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
					e.printStackTrace();
		} catch (NoSuchPaddingException e) {
					e.printStackTrace();
		} catch (InvalidAlgorithmParameterException e) {
					e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
					e.printStackTrace();
		} catch (BadPaddingException e) {
					e.printStackTrace();
		}
		return null;
		
	} 
	
	/*	public static byte[] encrypt(String plainText, SecretKey key) {
	//TODO: Sifrovati otvoren tekst uz pomoc tajnog kljuca koristeci konfiguraciju AES algoritma koju diktira najbolja praksa
	
	Cipher aesCipher = null;
	
	try {
		aesCipher=Cipher.getInstance("AES/CBC/PKCS5Padding");	//PKCS7 padding, zove se PKCS5 u Javi
	} catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	
	byte[] iv = new byte[16];	//uvek 128 bita zbog velicine bloka koja se koristi u AES
	SecureRandom random = new SecureRandom();
	random.nextBytes(iv);
	IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);
	
	try {
		aesCipher.init(Cipher.ENCRYPT_MODE, key, ivParameterSpec);
	} catch (InvalidKeyException | InvalidAlgorithmParameterException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	
	byte[] cipherText = null;
	
	try {
		cipherText = aesCipher.doFinal(plainText.getBytes());
	} catch (IllegalBlockSizeException | BadPaddingException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	
	byte[] concat = new byte[iv.length + cipherText.length];	//prosledjujem iv pre sifrata
	System.arraycopy(iv, 0, concat, 0, iv.length);
	System.arraycopy(cipherText, 0, concat, iv.length, cipherText.length);
	
	return concat;
}

public static byte[] decrypt(byte[] cipherText, SecretKey key) {
	//TODO: Desifrovati sifru uz pomoc tajnog kljuca koristeci konfiguraciju AES algoritma koju diktira najbolja praksa
	
	byte[] iv = Arrays.copyOfRange(cipherText, 0, 16);
	
	byte[] text = Arrays.copyOfRange(cipherText, 16, cipherText.length);
	
	IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);
	
	Cipher aesCipher = null;
	try {
		aesCipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
	} catch (NoSuchAlgorithmException | NoSuchPaddingException e1) {
		// TODO Auto-generated catch block
		e1.printStackTrace();
	}
	
	try {
		aesCipher.init(Cipher.DECRYPT_MODE, key, ivParameterSpec);
	} catch (InvalidKeyException | InvalidAlgorithmParameterException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	
	byte[] plainText = null;
	
	try {
		plainText = aesCipher.doFinal(text);
	} catch (IllegalBlockSizeException | BadPaddingException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	
	return plainText;
} */

}
