package modele.commande;

public class Suivi {

    private long idCommande;
    private String ticket;


    public Suivi(long idCommande, String ticket) {
        this.idCommande = idCommande;
        this.ticket = ticket;
    }

    public long getIdCommande() {
        return idCommande;
    }

    public void setIdCommande(long idCommande) {
        this.idCommande = idCommande;
    }

    public String getTicket() {
        return ticket;
    }

    public void setTicket(String ticket) {
        this.ticket = ticket;
    }
}
