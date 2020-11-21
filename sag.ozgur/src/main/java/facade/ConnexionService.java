package facade;

import facade.erreurs.MauvaisLoginPasswordException;
import facade.erreurs.UtilisateurDejaConnecteException;
import modele.personnes.Personnel;

import java.util.Collection;

public interface ConnexionService {

    /**
     *  ValiderLogin d'un membre du personnel
     * @param login le login de l'utilisateur
     * @param mdp le mot de passe de l'utilisateur
     * @return renvoie une clé d'authentification (String) unique à utiliser lors des appels à la facade
     * @throws MauvaisLoginPasswordException si le login/mot de passe ne sont pas valide
     * @throws UtilisateurDejaConnecteException si ce login est déjà connecté
     */
    String connexion(String login, String mdp) throws MauvaisLoginPasswordException, UtilisateurDejaConnecteException;

    /**
     *  Déconnexion d'un membre du personnel
     * @param authentification la clé d'authentification de l'utilisateur (celle fournie à la connexion)
     */
    void deconnexion(String authentification);

    /**
     * Liste des membres du personnel
     * Cette fonctionnalité peut être utile pour savoir si un individu est un membre
     * du personnel ou non.
     * @return renvoie la liste des membres du personnel enregistrés
     */
    Collection<Personnel> getListePersonnels();

    /**
     *  Cherche un membre du personnel par son login
     * @param login le login de l'utilisateur recherché
     * @return renvoie le Personnel ou null s'il n'existe pas
     */
    Personnel getPersonnelParLogin(String login);
}
