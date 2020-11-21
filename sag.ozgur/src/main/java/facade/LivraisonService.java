package facade;

import facade.erreurs.*;
import modele.commande.Commande;
import modele.commande.Scooter;
import modele.commande.Statut;

import java.util.Collection;

public interface LivraisonService {
    /**
     *  Récupère toutes les commandes ayant le statut recherché
     * @param authentification la clé unique du personnel connecté qui appelle la méthode
     * @param statut le statut (ATTENTE, LIVREE, ...) des commandes recherchées
     * @return la liste des commandes qui ont ce statut
     */
    Collection<Commande> getCommandesParStatut(String authentification, Statut statut);

    /**
     *  Récupèrer un Scooter pour faire la livraison
     * @param authentification la clé unique du personnel connecté qui appelle la méthode
     * @return le scooter ayant une capacité en nombre de pizzas qu'il peut transporter au maximum
     */
    Scooter prendreUnScooter(String authentification) throws CleAuthentificationInvalideException;

    /**
     * Prendre en charge des commandes pour la livraison
     * @param authentification la clé unique du personnel connecté qui appelle la méthode
     * @param scooter le scooter utilisé pour faire la livraison des commandes
     * @param idCommandes la liste des ids d'une ou plusieurs commandes à charger dans le scooter pour la livraison.
     *                    les commandes concernées passent dans l'étant ENLIVRAISON
     * @throws ScooterTropChargeException si la capacité du scooter en nb de pizzas est dépassée
     * @throws CleAuthentificationInvalideException si la cle authentification n'est pas valide
     * @throws CommandeNotFoundException si l'un des ids des commandes n'existe pas
     */
    void prendreEnChargeCommandes(String authentification, Scooter scooter, long... idCommandes)
            throws ScooterTropChargeException, CommandeNotFoundException, CleAuthentificationInvalideException;

    /**
     *  Livre la commande numero no sur le scooter
     * @param authentification la clé unique du personnel connecté qui appelle la méthode
     * @param idCommande l'id de la commande que l'on livre. La commande concernée passe alors dans l'état LIVREE
     * @param paiement le moyen de paiement lors de la livraison
     * @throws ScooterVideException si toutes les commandes ont été livrées : le scooter est donc vide
     * @throws CleAuthentificationInvalideException si la cle authentification n'est pas valide
     * @throws PasDeScooterException si on tente de livrer AVANT d'avoir récupéré un scooter
     */
    void livreCommande(String authentification, long idCommande, String paiement)
            throws CleAuthentificationInvalideException, CommandeNotFoundException, ScooterVideException, PasDeScooterException;

    /**
     *  Donne la liste des commandes restant à livrer
     * @param authentification la clé unique du personnel connecté qui appelle la méthode
     * @throws CleAuthentificationInvalideException si la cle authentification n'est pas valide
     * @throws PasDeScooterException si on tente de livrer AVANT d'avoir récupéré un scooter
     */
    Collection<Commande> resteALivrer(String authentification) throws CleAuthentificationInvalideException, PasDeScooterException;
}
