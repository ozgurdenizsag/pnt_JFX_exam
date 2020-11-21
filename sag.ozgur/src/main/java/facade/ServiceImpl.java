package facade;


import facade.erreurs.*;
import io.jsonwebtoken.*;
import modele.commande.Scooter;
import modele.commande.Commande;
import modele.commande.Statut;
import modele.commande.Suivi;
import modele.personnes.Personnel;
import modele.pizza.*;

import java.util.*;
import java.util.stream.Collectors;


public class ServiceImpl implements ConnexionService, CommandeService, LivraisonService, AdminService, CommandeOffLineService {
    private static final String SECRET_KEY =  "ma cle secrete";
    private static Map<Long, Commande> commandes = new HashMap<>();
    private static Map<String, Personnel> utilisateurs = new TreeMap<String, Personnel>();
    private static Map<String, Personnel> clesConnectes = new TreeMap<>();
    private static Map<String,Scooter> scooterUtilises = new TreeMap<>();

    public ServiceImpl() {
        // initialise un admin
        Personnel admin = new Personnel("admin", "admin", ADMIN, VENDEUR, LIVREUR);
        utilisateurs.put(admin.getNom(), admin);
        String cleAdmin = genereCleUnique();
        clesConnectes.put(cleAdmin, admin);
        initBaseDeDonnees(cleAdmin);
    }

    // constructeur pour les Tests Unitaires
    ServiceImpl(String contexte) {
        // initialise un admin
        Personnel admin = new Personnel("admin", "admin", ADMIN, VENDEUR, LIVREUR);
        utilisateurs.put(admin.getNom(), admin);
        // pas d'init de la BD : @Before tests
    }
    void resetAll() {
        commandes = new HashMap<>();
        utilisateurs = new TreeMap<String, Personnel>();
        clesConnectes = new TreeMap<>();
        Personnel admin = new Personnel("admin", "admin", ADMIN, VENDEUR, LIVREUR);
        utilisateurs.put(admin.getNom(), admin);
    }

    private void initBaseDeDonnees(String cleAdmin) {
        // add users
        try {
            enregistrerNouvelUtilisateur(cleAdmin, "vendeur", "vendeur", VENDEUR);
            enregistrerNouvelUtilisateur(cleAdmin, "livreur", "livreur", LIVREUR);
        // add commandes

        long yo = creerCommande(cleAdmin,"boichut","0600000000","22 rue des narcisses, 45100 ORLEANS");
        ajouterPizzasCommande(cleAdmin, yo,
                demandePizza("Hawaienne","beaucoup d'ananas"),
                demandePizza("Calzone","")
                );
        long fred = creerCommande(cleAdmin,"moal","0612345678","2bis, rue des branquignoles, 45000 ORLEANS");
            ajouterPizzasCommande(cleAdmin, fred,
                    demandePizza("Regina","avec peperonni")
            );
            preparerCommande(cleAdmin,fred);


            Pizza pizza = demandePizza("Regina","plein de trucs");
            Pizza pizza1 = demandePizza("Calzone","Aucun truc");


            Pizza pizza11= demandePizza("Regina","plein de trucs encore plus");
            Pizza pizza111 = demandePizza("Calzone","Aucun truc encore moins");


            Pizza pizza2 = demandePizza("Regina","plein de trucs encore encore plus");
            Pizza pizza12 = demandePizza("Calzone","Aucun truc encore encore moins");


            precommander("Anais","060606060606","Middle of nowhere 1 ",pizza,pizza1);
            precommander("Anthony","060606060607","Middle of nowhere 2",pizza11,pizza111);
            precommander("Laure","060606060608","Middle of nowhere 3",pizza2,pizza12);


            /**
             * Commande crée puis prête
             */

            long bisounours = creerCommande(cleAdmin,"Bisounours","pas de telephone","Premier nuage à droite" );
            ajouterPizzasCommande(cleAdmin,bisounours,pizza11,pizza12);
            preparerCommande(cleAdmin,bisounours);



            /**
             * Commande crée puis prête et enfin en livraison mais pas encore livrée
             */


            long rantanplan = creerCommande(cleAdmin,"Rantanplan","pas de telephone","La niche à côté du saloon" );
            ajouterPizzasCommande(cleAdmin,rantanplan,pizza11,pizza2);
            preparerCommande(cleAdmin,rantanplan);
            Scooter scooter = prendreUnScooter(cleAdmin);
            prendreEnChargeCommandes(cleAdmin,scooter,rantanplan);




        } catch (UtilisateurDejaExistantException e) {
            e.printStackTrace();
        } catch (CommandeNotFoundException e) {
            e.printStackTrace();
        } catch (CleAuthentificationInvalideException e) {
            e.printStackTrace();
        } catch (ScooterTropChargeException e) {
            e.printStackTrace();
        }

        deconnexion(cleAdmin);
    }

    @Override
    public void enregistrerNouvelUtilisateur(String cleUtilisateur, String login, String motDePasse, String... droits) throws UtilisateurDejaExistantException {
        if (utilisateurs.containsKey(login)) {
            throw new UtilisateurDejaExistantException();
        }
        Personnel personnel = new Personnel(login,motDePasse,droits);
        utilisateurs.put(login, personnel);
    }

    private String genereCleUnique() {
        return UUID.randomUUID().toString();
    }

    private void checkDroit(String cleUser, String droit) throws CleAuthentificationInvalideException {
        if (cleUser==null || cleUser.isEmpty()) throw new CleAuthentificationInvalideException();
        if (!clesConnectes.containsKey(cleUser)) throw new NonConnecteException();
        if (!clesConnectes.get(cleUser).hasRole(droit)) throw new AccesRefuseException();
    }


    @Override
    public long creerCommande(String authentification, String nom, String telephone, String adresse) {
        Commande commande = new Commande(nom, telephone, adresse);
        commandes.put(commande.getId(), commande);
        return commande.getId();
    }

    @Override
    public Pizza demandePizza(String nomPizza, String demandeParticuliere) {
        return Pizza.fabrique(nomPizza,demandeParticuliere);
    }

    @Override
    public void ajouterPizzasCommande(String authentification, long idCommande, Pizza... pizzas) throws CommandeNotFoundException {
        Commande commande = commandes.get(idCommande);
        if (commande==null) {
            throw new CommandeNotFoundException();
        }
        commande.addProduits(pizzas);
    }

    @Override
    public Commande getCommandeParId(String authentification, long idCommande) throws CommandeNotFoundException {
        if (!clesConnectes.containsKey(authentification)) {
            throw new NonConnecteException();
        }

        if (!commandes.containsKey(idCommande)) {
            throw new CommandeNotFoundException();
        }
        return commandes.get(idCommande);
    }

    @Override
    public Collection<Commande> getCommandesParStatut(String authentification, Statut statut) {
        return commandes.values().stream().filter(c->c.getStatut().equals(statut)).collect(Collectors.toList());
    }

    @Override
    public Collection<Personnel> getListePersonnels() {
        return utilisateurs.values();
    }

    @Override
    public Personnel getPersonnelParLogin(String login) {
        return utilisateurs.get(login);
    }

    @Override
    public void supprimerDroitUtilisateur(String cleUtilisateur, String login, String role) throws CleAuthentificationInvalideException {
        checkDroit(cleUtilisateur, ADMIN);

            utilisateurs.get(login).supprimerRole(role);
    }

    @Override
    public void supprimerUtilisateur(String cleUtilisateur, String login) throws CleAuthentificationInvalideException {
        checkDroit(cleUtilisateur, ADMIN);
        utilisateurs.remove(login);
    }

    @Override
    public void changerMotDePasseUtilisateur(String cleUtilisateur, String login, String mdp) throws CleAuthentificationInvalideException {
        checkDroit(cleUtilisateur, ADMIN);
        utilisateurs.get(login).setMdp(mdp);
    }


    @Override
    public String connexion(String login, String mdp) throws MauvaisLoginPasswordException, UtilisateurDejaConnecteException {
        if (utilisateurs.containsKey(login)&&utilisateurs.get(login).getMdp().equals(mdp)) {
            Personnel personnel = utilisateurs.get(login);
            if (clesConnectes.values().contains(personnel)) {
                throw new UtilisateurDejaConnecteException();
            }
            String cle = genereCleUnique();
            clesConnectes.put(cle, personnel);
            return cle;
        }
        throw new MauvaisLoginPasswordException();
    }

    @Override
    public void deconnexion(String authentification) {
        if (clesConnectes.containsKey(authentification)) {
            clesConnectes.remove(authentification);
        } else {
            throw new NonConnecteException();
        }
    }

    @Override
    public void ajouterDroitUtilisateur(String cleUtilisateur, String utilisateurConcerne, String role) throws CleAuthentificationInvalideException {
        checkDroit(cleUtilisateur,ADMIN);

        Personnel user = utilisateurs.get(utilisateurConcerne);
        user.addRole(role);
    }

    @Override
    public void prendreEnChargeCommandes(String authentification, Scooter scooter, long... idCommandes) throws ScooterTropChargeException, CommandeNotFoundException, CleAuthentificationInvalideException {
        checkDroit(authentification,LIVREUR);
        for(long idComande:idCommandes) {
            Commande commande = getCommandeParId(authentification, idComande);
            if (commande.getStatut()==Statut.PRETE) {
                scooter.chargerCommande(commande);
            }
            else  {
                throw new CommandeNotFoundException();
            }
        }
    }

    @Override
    public Scooter prendreUnScooter(String authentification) throws CleAuthentificationInvalideException {
        checkDroit(authentification,LIVREUR);
        Scooter scooter = new Scooter();
        scooterUtilises.put(authentification,scooter);
        return scooter;
    }

    @Override
    public void livreCommande(String authentification, long idCommande, String paiement)
            throws CleAuthentificationInvalideException, CommandeNotFoundException, ScooterVideException, PasDeScooterException {
        checkDroit(authentification,LIVREUR);
        Scooter scooter = scooterUtilises.get(authentification);
        if (scooter==null) {
            throw new PasDeScooterException();
        }
        scooter.livrerCommande(idCommande,paiement);
    }

    @Override
    public void preparerCommande(String authentification, long idCommande) throws CleAuthentificationInvalideException, CommandeNotFoundException {
        checkDroit(authentification,VENDEUR);
        if (!commandes.containsKey(idCommande)) {
            throw new CommandeNotFoundException();
        }
        Commande commande = commandes.get(idCommande);
        commande.setStatut(Statut.PRETE);
    }

    @Override
    public void validerPrecommande(String authentification, long idCommande) throws CleAuthentificationInvalideException, CommandeNotFoundException {
        checkDroit(authentification,VENDEUR);
        if (!commandes.containsKey(idCommande)) {
            throw new CommandeNotFoundException();
        }
        Commande commande = commandes.get(idCommande);
        commande.setStatut(Statut.ATTENTE);
    }

    private static String compileInfos(Commande commande) {
        return commande.getNom()+commande.getTelephone()+commande.getAdresse()+commande.getId();
    }

    @Override
    public Suivi precommander(String nom, String telephone, String adresse, Pizza... pizzas) {
        Commande commande = new Commande(nom,telephone, adresse);
        commande.setStatut(Statut.PRECOMMANDE);
        commande.addProduits(pizzas);
        commandes.put(commande.getId(),commande);

        Claims claims = Jwts.claims().setSubject(compileInfos(commande));
        String token = Jwts.builder()
                .setClaims(claims)
                .signWith(SignatureAlgorithm.HS512, SECRET_KEY.getBytes())
                .compact();

        Suivi suivi = new Suivi(commande.getId(),token);

        return suivi;
    }

    @Override
    public Commande suiviParClient(long idCommande, String ticket) throws CommandeNotFoundException, TicketInvalideException {

        Commande commande = commandes.get(idCommande);
        if (Objects.isNull(commande)) {
            throw new CommandeNotFoundException();
        }

        try {
            Jws<Claims> jwsClaims = Jwts.parser()
                    .setSigningKey(SECRET_KEY.getBytes())
                    .parseClaimsJws(ticket);
            String infos = jwsClaims.getBody().getSubject();

            if (infos.equals(compileInfos(commande))) {
                return commande;
            } else {
                throw new TicketInvalideException();
            }
        }
        catch (SignatureException e) {
            throw new TicketInvalideException();
        }
    }

    @Override
    public Collection<Commande> resteALivrer(String authentification) throws CleAuthentificationInvalideException, PasDeScooterException {
        checkDroit(authentification,LIVREUR);
        if (Objects.isNull(scooterUtilises.get(authentification))) {
            throw new PasDeScooterException();
        }

        return scooterUtilises.get(authentification).commandesRestantALivrer();
    }
}
