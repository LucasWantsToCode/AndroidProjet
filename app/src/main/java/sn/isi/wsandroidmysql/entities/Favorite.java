package sn.isi.wsandroidmysql.entities;

public class Favorite {
    private int id;
    private Demandeur demandeur;
    private Offre offre;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Demandeur getDemandeur() {
        return demandeur;
    }

    public void setDemandeur(Demandeur demandeur) {
        this.demandeur = demandeur;
    }

    public Offre getOffre() {
        return offre;
    }

    public void setOffre(Offre offre) {
        this.offre = offre;
    }
}
