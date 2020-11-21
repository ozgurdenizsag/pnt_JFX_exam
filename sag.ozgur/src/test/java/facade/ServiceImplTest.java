package facade;

import facade.erreurs.*;
import modele.commande.Commande;
import modele.commande.Scooter;
import modele.commande.Statut;
import modele.commande.Suivi;
import modele.pizza.Calzone;
import modele.pizza.Pizza;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static facade.AdminService.LIVREUR;
import static facade.AdminService.VENDEUR;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by Fred on 30/11/2016.
 */
public class ServiceImplTest {
    private ServiceImpl service;
    private String cleAdmin;
    private long idCommandeYo;
    private long idCommandefred;

    private long idCommandeBisounours;
    private  long idCommandeRantanplan;

    private Scooter scooter;

    @Before
    public void setUp() throws Exception {
        service = new ServiceImpl("vide");
        cleAdmin = service.connexion("admin","admin");
        // add users
        try {
            service.enregistrerNouvelUtilisateur(cleAdmin, "vendeur", "vendeur", VENDEUR);
            service.enregistrerNouvelUtilisateur(cleAdmin, "livreur", "livreur", LIVREUR);
            // add commandes

            idCommandeYo = service.creerCommande(cleAdmin,"yohan","0600000000","22 rue des narcisses, 45100 ORLEANS");
            service.ajouterPizzasCommande(cleAdmin, idCommandeYo,
                    service.demandePizza("Hawaienne","beaucoup d'ananas"),
                    service.demandePizza("Calzone","")
            );
            idCommandefred = service.creerCommande(cleAdmin,"fred","0612345678","2bis, rue des branquignoles, 45000 ORLEANS");
            service.ajouterPizzasCommande(cleAdmin, idCommandefred,
                    service.demandePizza("Regina","avec peperonni")
            );

            Pizza pizza = service.demandePizza("Regina","plein de trucs");
            Pizza pizza1 = service.demandePizza("Calzone","Aucun truc");


            Pizza pizza11= service.demandePizza("Regina","plein de trucs encore plus");
            Pizza pizza111 = service.demandePizza("Calzone","Aucun truc encore moins");


            Pizza pizza2 = service.demandePizza("Regina","plein de trucs encore encore plus");
            Pizza pizza12 = service.demandePizza("Calzone","Aucun truc encore encore moins");


            Suivi suivi =  service.precommander("Anais","060606060606","Middle of nowhere 1 ",pizza,pizza1);
            Suivi suivi1 =  service.precommander("Anthony","060606060607","Middle of nowhere 2",pizza11,pizza111);
            Suivi suivi2 =  service.precommander("Laure","060606060608","Middle of nowhere 3",pizza2,pizza12);




            /**
             * Commande crée puis prête
             */

            idCommandeBisounours = service.creerCommande(cleAdmin,"Bisounours","pas de telephone","Premier nuage à droite" );
            service.ajouterPizzasCommande(cleAdmin,idCommandeBisounours,pizza11,pizza12);
            service.preparerCommande(cleAdmin,idCommandeBisounours);



            /**
             * Commande crée puis prête et enfin en livraison mais pas encore livrée
             */


            idCommandeRantanplan = service.creerCommande(cleAdmin,"Rantanplan","pas de telephone","La niche à côté du saloon" );
            service.ajouterPizzasCommande(cleAdmin,idCommandeRantanplan,pizza11,pizza2);
            service.preparerCommande(cleAdmin,idCommandeRantanplan);
            scooter = service.prendreUnScooter(cleAdmin);
            service.prendreEnChargeCommandes(cleAdmin,scooter,idCommandeRantanplan);

        } catch (UtilisateurDejaExistantException e) {
            e.printStackTrace();
        } catch (CommandeNotFoundException e) {
            e.printStackTrace();
        }
    }

    @After
    public void tearDown() throws Exception {
        service.deconnexion(cleAdmin);
        service.resetAll();
    }

    @Test(expected = MauvaisLoginPasswordException.class)
    public void testConnexion() throws MauvaisLoginPasswordException, UtilisateurDejaConnecteException {
        service.connexion("admin","ERREUR");
    }
    @Test(expected = UtilisateurDejaConnecteException.class)
    public void testConnexion2() throws MauvaisLoginPasswordException, UtilisateurDejaConnecteException {
        service.connexion("admin","admin");
    }

    @Test
    public void testEnregistrementUtilisateur() throws UtilisateurDejaExistantException {
        service.enregistrerNouvelUtilisateur(cleAdmin, "new", "new", "USER");
    }

    @Test(expected = UtilisateurDejaExistantException.class)
    public void testEnregistrementUtilisateurException() throws UtilisateurDejaExistantException {
        service.enregistrerNouvelUtilisateur(cleAdmin, "admin", "new", "USER");
    }

    @Test
    public void testFabriquePizza() {
        Pizza pizza = service.demandePizza("","");
        assertThat(pizza).isNull();
    }
    @Test
    public void testFabriquePizza2() {
        Pizza pizza = service.demandePizza("Calzone","");
        assertThat(pizza).isOfAnyClassIn(Calzone.class);
    }

    @Test
    public void testCommandeParId() throws CommandeNotFoundException {
        Commande commande = service.getCommandeParId(cleAdmin,idCommandeYo );
        assertThat(commande).isNotNull();
        assertThat(commande.getId()).isEqualTo(idCommandeYo);
        assertThat(commande.getNom()).isEqualTo("yohan");
    }

    @Test(expected = CommandeNotFoundException.class)
    public void testCommandeParIdException() throws CommandeNotFoundException {
        service.getCommandeParId(cleAdmin,-1 );
    }

    @Test
    public void testCommande() throws CommandeNotFoundException {
        long id = service.creerCommande(cleAdmin, "babar", "0606060606", "1 rue du chateau, CELESTEVILLE");
        Commande commande = service.getCommandeParId(cleAdmin,id );
        assertThat(commande).isNotNull();
        assertThat(commande.getId()).isEqualTo(id);
        assertThat(commande.getStatut()).isEqualTo(Statut.ATTENTE);
    }

    @Test
    public void testPrendreScooter() throws MauvaisLoginPasswordException, UtilisateurDejaConnecteException, CleAuthentificationInvalideException {
        String cle = service.connexion("livreur","livreur");
        Scooter scooter = service.prendreUnScooter(cle);
        assertThat(scooter).isNotNull();
        assertThat(scooter.nbPizzasChargement()).isZero();
        assertThat(scooter.resteCommandeALivrer()).isFalse();
    }

    @Test
    public void testChargerScooter() throws MauvaisLoginPasswordException, UtilisateurDejaConnecteException, CleAuthentificationInvalideException, ScooterTropChargeException, CommandeNotFoundException {
        String cle = service.connexion("livreur","livreur");
        Scooter scooter = service.prendreUnScooter(cle);
        service.preparerCommande(cleAdmin,idCommandefred);
        service.preparerCommande(cleAdmin,idCommandeYo);

        service.prendreEnChargeCommandes(cle,scooter,idCommandefred, idCommandeYo);
        assertThat(scooter).isNotNull();
        assertThat(scooter.nbPizzasChargement()).isEqualTo(3);
        assertThat(scooter.resteCommandeALivrer()).isTrue();
    }

    @Test
    public void testLivraionScooter() throws MauvaisLoginPasswordException, UtilisateurDejaConnecteException, CleAuthentificationInvalideException, ScooterTropChargeException, CommandeNotFoundException, ScooterVideException, PasDeScooterException {
        String cle = service.connexion("livreur","livreur");
        Scooter scooter = service.prendreUnScooter(cle);
        service.preparerCommande(cleAdmin,idCommandefred);
        service.preparerCommande(cleAdmin,idCommandeYo);

        service.prendreEnChargeCommandes(cle,scooter,idCommandefred, idCommandeYo);
        service.livreCommande(cle, idCommandefred, "CB");
        Commande commande = service.getCommandeParId(cleAdmin,idCommandefred);
        assertThat(scooter).isNotNull();
        assertThat(scooter.nbPizzasChargement()).isEqualTo(2);
        assertThat(scooter.resteCommandeALivrer()).isTrue();
        assertThat(scooter.commandesRestantALivrer().size()).isEqualTo(1);

        assertThat(commande.getStatut()).isEqualTo(Statut.LIVREE);
        assertThat(commande.getMoyenPaiement().get()).isEqualTo("CB");
    }



    @Test
    public void testCommandeOffLine() {
        Pizza pizza = service.demandePizza("Regina","plein de trucs");
        Pizza pizza1 = service.demandePizza("Calzone","Aucun truc");

       Suivi suivi =  service.precommander("Boichut","060606060606","Middle of nowhere",pizza,pizza1);

       assertThat(suivi).isNotNull();
    }




    @Test
    public void testSuiviCommande() throws CommandeNotFoundException, TicketInvalideException  {

        Pizza pizza = service.demandePizza("Regina","plein de trucs");
        Pizza pizza1 = service.demandePizza("Calzone","Aucun truc");

        Suivi suivi =  service.precommander("Boichut","060606060606","Middle of nowhere",pizza,pizza1);

        Commande commande = service.suiviParClient(suivi.getIdCommande(),suivi.getTicket());

        assertThat(commande).isNotNull();
        assertThat(commande.getId()).isEqualTo(suivi.getIdCommande());
    }



    @Test(expected = CommandeNotFoundException.class)
    public void testSuiviCommandeKO1() throws CommandeNotFoundException, TicketInvalideException {

        Pizza pizza = service.demandePizza("Regina","plein de trucs");
        Pizza pizza1 = service.demandePizza("Calzone","Aucun truc");

        Suivi suivi =  service.precommander("Boichut","060606060606","Middle of nowhere",pizza,pizza1);

        Commande commande = service.suiviParClient(-1,suivi.getTicket());
    }



    @Test(expected = TicketInvalideException.class)
    public void testSuiviCommandeKO2() throws CommandeNotFoundException, TicketInvalideException {

        Pizza pizza = service.demandePizza("Regina","plein de trucs");
        Pizza pizza1 = service.demandePizza("Calzone","Aucun truc");

        Suivi suivi =  service.precommander("Boichut","060606060606","Middle of nowhere",pizza,pizza1);

        Commande commande = service.suiviParClient(suivi.getIdCommande(),suivi.getTicket()+"aa");

    }



    @Test(expected = TicketInvalideException.class)

    public void testSuiviCommandeKO3() throws CommandeNotFoundException, TicketInvalideException {

        Pizza pizza = service.demandePizza("Regina","plein de trucs");
        Pizza pizza1 = service.demandePizza("Calzone","Aucun truc");

        Suivi suivi =  service.precommander("Boichut","060606060606","Middle of nowhere",pizza,pizza1);

        Pizza pizza2 = service.demandePizza("Regina","plein de trucs");
        Pizza pizza3 = service.demandePizza("Calzone","Aucun truc");

        Suivi suivi1 =  service.precommander("Moal","070707070707","Somewhere else in Middle of nowhere",pizza2,pizza3);


        Commande commande = service.suiviParClient(suivi.getIdCommande(),suivi1.getTicket());

    }



    @Test
    public void testValiderPrecommande() throws CleAuthentificationInvalideException, CommandeNotFoundException, TicketInvalideException {
        Pizza pizza = service.demandePizza("Regina","plein de trucs");
        Pizza pizza1 = service.demandePizza("Calzone","Aucun truc");

        Suivi suivi =  service.precommander("Boichut","060606060606","Middle of nowhere",pizza,pizza1);

        service.validerPrecommande(cleAdmin,suivi.getIdCommande());
        Commande commande = service.suiviParClient(suivi.getIdCommande(), suivi.getTicket());
        assertThat(commande.getStatut()==Statut.ATTENTE);
    }




    @Test
    public void getCommandesParStatutENLIVRAISON() {

        assertThat(service.getCommandesParStatut(cleAdmin,Statut.ENLIVRAISON).size()).isEqualTo(1);
    }



    @Test
    public void getCommandesParStatutPRETE() {

        assertThat(service.getCommandesParStatut(cleAdmin,Statut.PRETE).size()).isEqualTo(1);
    }

    @Test
    public void getCommandesParStatutATTENTE() {

        assertThat(service.getCommandesParStatut(cleAdmin,Statut.ATTENTE).size()).isEqualTo(2);
    }


    @Test
    public void getCommandesParStatutPRECOMMANDE() {

        assertThat(service.getCommandesParStatut(cleAdmin,Statut.PRECOMMANDE).size()).isEqualTo(3);
    }



    @Test
    public void prendreUnScooter() {

        assertThat(scooter).isNotNull();
    }



    @Test(expected = CommandeNotFoundException.class)
    public void prendreEnChargeCommandesKO() throws CommandeNotFoundException, CleAuthentificationInvalideException, ScooterTropChargeException {
        service.prendreEnChargeCommandes(cleAdmin,scooter,idCommandeRantanplan);
    }



    @Test
    public void prendreEnChargeCommandes() throws CommandeNotFoundException, CleAuthentificationInvalideException, ScooterTropChargeException {
        service.prendreEnChargeCommandes(cleAdmin,scooter,idCommandeBisounours);
    }


    @Test(expected = CommandeNotFoundException.class)
    public void livreCommandeKO() throws PasDeScooterException, ScooterVideException, CommandeNotFoundException, CleAuthentificationInvalideException {
        service.livreCommande(cleAdmin,idCommandeBisounours,"CB");

    }

    @Test
    public void livreCommande() throws PasDeScooterException, ScooterVideException, CommandeNotFoundException, CleAuthentificationInvalideException {
        service.livreCommande(cleAdmin,idCommandeRantanplan,"CB");

    }



    @Test
    public void resteALivrer() throws CleAuthentificationInvalideException, PasDeScooterException {
        assertThat(service.resteALivrer(cleAdmin).size()).isEqualTo(1);

    }


    @Test(expected = PasDeScooterException.class)
    public void resteALivrerKO() throws CleAuthentificationInvalideException, PasDeScooterException, MauvaisLoginPasswordException, UtilisateurDejaConnecteException {
        String cle = service.connexion("livreur","livreur");
        service.resteALivrer(cle);

    }


    @Test(expected = AccesRefuseException.class)
    public void resteALivrerKO1() throws CleAuthentificationInvalideException, PasDeScooterException, MauvaisLoginPasswordException, UtilisateurDejaConnecteException {
        String cle = service.connexion("vendeur","vendeur");
        service.resteALivrer(cle);

    }






}