package modele.commande;

import facade.erreurs.CommandeNotFoundException;
import facade.erreurs.ScooterTropChargeException;
import facade.erreurs.ScooterVideException;
import modele.commande.Commande;
import modele.commande.Statut;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public class Scooter {
    private final int capacite;
    private List<Commande> chargement = new ArrayList<>();

    public Scooter(int capacite) {
        this.capacite = capacite;
    }

    public Scooter() {
        // capacité aléatoire entre 4 et 10 pizzas
        this((int)(Math.random() * 7 + 4));
    }

    public int nbPizzasChargement() {
        return chargement.stream().map(c->c.getPizzas().size()).reduce(0,Integer::sum);
    }

    public void chargerCommande(Commande commande) throws ScooterTropChargeException {
        if (nbPizzasChargement()+commande.getPizzas().size()>capacite) {
            throw new ScooterTropChargeException();
        }
        chargement.add(commande);
        commande.setStatut(Statut.ENLIVRAISON);
    }

    public boolean resteCommandeALivrer() {
        return !chargement.isEmpty();
    }

    public void livrerCommande(long idCommande, String paiement) throws ScooterVideException, CommandeNotFoundException {
        if (chargement.isEmpty()) {
            throw new ScooterVideException();
        }
        for(Commande commande:chargement) {
            if (commande.getId()==idCommande) {
                commande.setStatut(Statut.LIVREE);
                commande.setMoyenPaiement(paiement);
                chargement.remove(commande);
                return;
            }
        }
        throw new CommandeNotFoundException();
    }

    public Collection<Commande> commandesRestantALivrer() {
        return chargement;
    }
}
