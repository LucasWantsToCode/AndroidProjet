package sn.isi.wsandroidmysql.entities;

public class Offre {
    private int id;
    private String dateO;
    private String libelle;
    private Entreprise entreprise;
    private Domaine domaine;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDateO() {
        return dateO;
    }

    public void setDateO(String dateO) {
        this.dateO = dateO;
    }

    public String getLibelle() {
        return libelle;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public Entreprise getEntreprise() {
        return entreprise;
    }

    public void setEntreprise(Entreprise entreprise) {
        this.entreprise = entreprise;
    }

    public Domaine getDomaine() {
        return domaine;
    }

    public void setDomaine(Domaine domaine) {
        this.domaine = domaine;
    }
}
