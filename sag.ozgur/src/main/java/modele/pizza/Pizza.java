package modele.pizza;

public abstract class Pizza {
    private long id;
    private String demandeParticuliere;
    private double prix;

    static long lastId = 0;

    public Pizza(String demandeParticuliere, double prix) {
        this.id = ++lastId;
        this.demandeParticuliere = demandeParticuliere;
        this.prix = prix;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public double getPrix() {
        return prix;
    }

    public void setPrix(double prix) {
        this.prix = prix;
    }

    public String getDemandeParticuliere() {
        return demandeParticuliere;
    }

    public void setDemandeParticuliere(String demandeParticuliere) {
        this.demandeParticuliere = demandeParticuliere;
    }

    public static Pizza fabrique(String nomPizza, String demandeParticuliere) {
        switch (nomPizza) {
            case "Regina":
                return new Regina(demandeParticuliere);
            case "Calzone":
                return new Calzone(demandeParticuliere);
            case "Hawaienne":
                return new Hawaienne(demandeParticuliere);
        }
        return null;
    }
}
