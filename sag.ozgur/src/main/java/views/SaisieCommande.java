package views;

import controlleur.Controleur;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import modele.commande.Commande;
import modele.pizza.Pizza;
import observer.Evenement;
import observer.Observateur;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

public class SaisieCommande implements Observateur {

    public BorderPane saisieCommandeRoot;
    public ListView listePizza;
    public ListView listeChoixPizza;
    public TextField nom;
    public TextField prenom;
    public TextField numtel;
    public TextField adresse;
    public String pizzaSelection;


    private ArrayList<String> allPizza = new ArrayList<>();
    private ArrayList<String> mesChoix = new ArrayList<>();

    @Override
    public void notifier(Evenement event) {

    }


    private Controleur controleur;
    private Stage stage;

    public Stage getStage(){
        return this.stage;
    }


    public static SaisieCommande creerInstance(Controleur controleur, Stage stage) {
        URL location = SaisieCommande.class.getResource("/views/saisiecommande.fxml");
        FXMLLoader fxmlLoader = new FXMLLoader(location);
        Parent root = null;
        try {
            root = (Parent) fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        SaisieCommande view = fxmlLoader.getController();
        view.setStage(stage);
        view.setControleur(controleur);
        view.setListePizza();
        view.afficherPizza();

        return view;
    }

    private void setListePizza() {
        allPizza.add("Regina");
        allPizza.add("Calzone");
        allPizza.add("Hawaienne");
    }

    private void afficherPizza() {
        listePizza.getItems().clear();
        for (String p: allPizza) {
            listePizza.getItems().add(p);
        }
    }


    public void show() {
        stage.setTitle("Menu");
        Scene scene = new Scene(saisieCommandeRoot, saisieCommandeRoot.getPrefWidth(), saisieCommandeRoot.getPrefHeight());
        stage.setScene(scene);
        stage.show();
    }


    public void setControleur(Controleur controleur) {
        this.controleur = controleur;
    }

    public Controleur getControleur() {
        return controleur;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void close() {
        stage.close();
    }




    public void ajouterPizza(ActionEvent actionEvent) {
        pizzaSelection = (String) listePizza.getSelectionModel().getSelectedItem();

        mesChoix.add(pizzaSelection);
        mettreAJour();
    }

    private void mettreAJour() {
        AfficherMesChoix();
    }

    private void AfficherMesChoix() {
        listeChoixPizza.getItems().clear();
        for (String p: mesChoix) {
            listeChoixPizza.getItems().add(p);
        }
    }

    public void validerCommande(ActionEvent actionEvent) {
    }
}
