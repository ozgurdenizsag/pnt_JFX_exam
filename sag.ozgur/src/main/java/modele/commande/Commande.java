package modele.commande;

import modele.pizza.Pizza;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;

public class Commande {
    private long id;
    private String nom;
    private String telephone;
    private String adresse;
    private Collection<Pizza> pizzas;
    private Statut statut;
    private Optional<String> moyenPaiement;

    private static long lastId = 0;

    public Commande(String nom, String telephone, String adresse) {
        this.id = ++lastId;
        this.nom = nom;
        this.telephone = telephone;
        this.adresse = adresse;
        this.pizzas = new ArrayList<>();
        this.statut = Statut.ATTENTE;
        this.moyenPaiement = Optional.empty();
    }

    public long getId() {
        return id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public Collection<Pizza> getPizzas() {
        return pizzas;
    }

    public void addProduits(Pizza... pizzas) {
        this.pizzas.addAll(Arrays.asList(pizzas));
    }

    public Statut getStatut() {
        return statut;
    }

    public void setStatut(Statut statut) {
        this.statut = statut;
    }

    public void setStatut(String statut) {
        this.statut = Statut.valueOf(statut);
    }

    public Optional<String> getMoyenPaiement() {
        return moyenPaiement;
    }

    public void setMoyenPaiement(String moyenPaiement) {
        this.moyenPaiement = Optional.of(moyenPaiement);
    }
}
