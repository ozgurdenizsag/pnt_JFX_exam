package facade;

import facade.erreurs.*;
import modele.commande.Commande;
import modele.commande.Suivi;
import modele.pizza.Pizza;

public interface CommandeOffLineService {

    /**
     *  Créer une pré-commande par un client
     * @param nom le nom de la personne qui fait la commande
     * @param telephone le téléphone de la personne qui fait la commande
     * @param adresse l'adresse de la personne qui fait la commande
     * @param pizzas la liste (une ou plusieurs) des pizzas commandées
     * @return le suivi de cette pré-commande
     */
    Suivi precommander(String nom, String telephone, String adresse, Pizza... pizzas);


    /**
     *  Récupère la (pré)commande d'un suivi en vérifiant le ticket
     * @param idCommande l'id de la précommande
     * @param ticket le ticket de vérification
     * @return la commande
     * @throws CommandeNotFoundException si l'id commande n'existe pas
     * @throws TicketInvalideException si le ticket n'est pas valide
     */
    Commande suiviParClient(long idCommande, String ticket) throws CommandeNotFoundException, TicketInvalideException;

}
