package facade;

import facade.erreurs.CleAuthentificationInvalideException;
import facade.erreurs.UtilisateurDejaExistantException;

/**
 * interface d'administration des utilisateurs du système
 *
 * pour appeler ces méthodes, l'utilisateur connecté (cleUtilisateur) doit avoir le droit "ADMIN"
 *
 */
public interface AdminService {
    String ADMIN = "ADMIN";
    String VENDEUR = "VENDEUR";
    String LIVREUR = "LIVREUR";

    /**
     *  Enregistrement d'un nouvel utilisateur
     * @param cleUtilisateur la clé unique de l'utilisateur connecté qui appelle la méthode
     * @param login le login du nouvel utilisateur
     * @param motDePasse le mode de passe du nouvel utilisateur
     * @param droits la liste des droits (chaînes de caractères) du nouvel utilisateur
     * @throws UtilisateurDejaExistantException s'il y a déjà un utilisateur avec ce login enregistré
     * @throws CleAuthentificationInvalideException si la cleUtilisateur n'est pas valide
     */
    void enregistrerNouvelUtilisateur(String cleUtilisateur, String login, String motDePasse, String... droits)
            throws CleAuthentificationInvalideException,UtilisateurDejaExistantException;

    /**
     *  Ajouter un droit à un utilisateur
     * @param cleUtilisateur la clé unique de l'utilisateur connecté qui appelle la méthode
     * @param login le login de l'utilisateur
     * @param droit le droit à ajouter à l'utilisateur
     * @throws CleAuthentificationInvalideException si la cleUtilisateur n'est pas valide
     */
    void ajouterDroitUtilisateur(String cleUtilisateur, String login, String droit) throws CleAuthentificationInvalideException;

    /**
     *  Supprimer un droit à un utilisateur
     * @param cleUtilisateur la clé unique de l'utilisateur connecté qui appelle la méthode
     * @param login le login de l'utilisateur
     * @param droit le droit à supprimer à l'utilisateur
     * @throws CleAuthentificationInvalideException si la cleUtilisateur n'est pas valide
     */
    void supprimerDroitUtilisateur(String cleUtilisateur, String login, String droit) throws CleAuthentificationInvalideException;

    /**
     *  Changer le mot de passe d'un utilisateur
     * @param cleUtilisateur la clé unique de l'utilisateur connecté qui appelle la méthode
     * @param login le login de l'utilisateur
     * @param mdp le nouveau mot de passe de l'utilisateur
     * @throws CleAuthentificationInvalideException si la cleUtilisateur n'est pas valide
     */
    void changerMotDePasseUtilisateur(String cleUtilisateur, String login, String mdp) throws CleAuthentificationInvalideException;

    /**
     *  Supprime un utilisateur
     * @param cleUtilisateur la clé unique de l'utilisateur connecté qui appelle la méthode
     * @param login le login de l'utilisateur à supprimer
     * @throws CleAuthentificationInvalideException si la cleUtilisateur n'est pas valide
     */
    void supprimerUtilisateur(String cleUtilisateur, String login) throws CleAuthentificationInvalideException;

}
