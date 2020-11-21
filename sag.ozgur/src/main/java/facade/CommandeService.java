package facade;

import facade.erreurs.CleAuthentificationInvalideException;
import facade.erreurs.CommandeNotFoundException;
import modele.commande.Commande;
import modele.commande.Statut;
import modele.pizza.Pizza;

import java.util.Collection;

public interface CommandeService {
    /**
     *  Créer une commande
     * @param authentification la clé unique du personnel connecté qui appelle la méthode
     * @param nom le nom de la personne qui fait la commande
     * @param telephone le téléphone de la personne qui fait la commande
     * @param adresse l'adresse de la personne qui fait la commande
     * @return l'id de la commande créée
     */
    long creerCommande(String authentification, String nom, String telephone, String adresse);

    /**
     *  Créer une pizza
     * @param nomPizza le nom de la pizza à fabriquer
     * @param demandeParticuliere l'éventuelle demande particulière pour cette pizza ("sans champignons", ....)
     * @return la pizza
     */
    Pizza demandePizza(String nomPizza, String demandeParticuliere);

    /**
     *  Ajoute une pizza dans une commande
     * @param authentification la clé unique du personnel connecté qui appelle la méthode
     * @param idCommande l'id de la commande dans laquelle on veut ajouter des pizzas
     * @param pizzas la liste (une ou plusieurs objets Pizza) des pizzas à ajouter à la commande
     * @throws CommandeNotFoundException si l'idCommande n'existe pas
     */
    void ajouterPizzasCommande(String authentification, long idCommande, Pizza... pizzas) throws CommandeNotFoundException;

    /**
     *  Récupère une commande par son id
     * @param authentification la clé unique du personnel connecté qui appelle la méthode
     * @param idCommande l'id de la commande recherchée
     * @throws CommandeNotFoundException si l'idCommande n'existe pas
     * @return la commande
     */
    Commande getCommandeParId(String authentification, long idCommande) throws CommandeNotFoundException;

    /**
     *  Récupère toutes les commandes ayant un statut recherché
     * @param authentification la clé unique du personnel connecté qui appelle la méthode
     * @param statut le statut (ATTENTE, LIVREE, ...) des commandes recherchées
     * @return la liste des commandes qui ont ce statut
     */
    Collection<Commande> getCommandesParStatut(String authentification, Statut statut);

    /**
     *  Lance la préparation d'une commande (prépare toutes les pizzas de la commande)
     *  le statut de la commande passe à PRETE
     * @param authentification la clé unique du personnel connecté qui appelle la méthode
     * @param idCommande l'id de la commande recherchée
     * @throws CleAuthentificationInvalideException si la cle authentification n'est pas valide
     * @throws CommandeNotFoundException si l'idCommande n'existe pas
     */
    void preparerCommande(String authentification, long idCommande) throws CleAuthentificationInvalideException, CommandeNotFoundException;

    /**
     *  Valide une precommande effectuée par un client
     *  le statut de la commande passe à ATTENTE
     * @param authentification la clé unique du personnel connecté qui appelle la méthode
     * @param idCommande l'id de la (pre)commande à valider
     * @throws CleAuthentificationInvalideException si la cle authentification n'est pas valide
     * @throws CommandeNotFoundException si l'idCommande n'existe pas
     */
    void validerPrecommande(String authentification, long idCommande) throws CleAuthentificationInvalideException, CommandeNotFoundException;

}
