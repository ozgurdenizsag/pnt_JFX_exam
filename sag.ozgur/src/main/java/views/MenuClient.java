package views;

import controlleur.Controleur;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import modele.commande.Commande;
import observer.Evenement;
import observer.Observateur;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

public class MenuClient implements Observateur {

    public BorderPane menuClientRoot;

    @Override
    public void notifier(Evenement event) {

    }


    private Controleur controleur;
    private Stage stage;

    public Stage getStage(){
        return this.stage;
    }


    public static MenuClient creerInstance(Controleur controleur, Stage stage) {
        URL location = MenuClient.class.getResource("/views/menuclient.fxml");
        FXMLLoader fxmlLoader = new FXMLLoader(location);
        Parent root = null;
        try {
            root = (Parent) fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        MenuClient view = fxmlLoader.getController();
        view.setStage(stage);
        view.setControleur(controleur);
        return view;
    }


    public void show() {
        stage.setTitle("Menu");
        Scene scene = new Scene(menuClientRoot, menuClientRoot.getPrefWidth(), menuClientRoot.getPrefHeight());
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


    public void retourAuMenuFromClient(){
        controleur.retourAuMenuFromClient();
    }

    public void passerCommande(ActionEvent actionEvent) {
        controleur.afficherPageSaisieCommande();
    }

    public void suiviCommande(ActionEvent actionEvent) {
    }
}
