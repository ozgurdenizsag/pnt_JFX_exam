package views;

import controlleur.Controleur;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import observer.Evenement;
import observer.Observateur;

import java.io.IOException;
import java.net.URL;

public class Menu implements Observateur {
    public BorderPane menuRoot;

    @Override
    public void notifier(Evenement event) {

    }


    private Controleur controleur;
    private Stage stage;

    public Stage getStage(){
        return this.stage;
    }

    public static Menu creerInstance(Controleur controleur, Stage stage) {
        URL location = Menu.class.getResource("/views/menu.fxml");
        FXMLLoader fxmlLoader = new FXMLLoader(location);
        Parent root = null;
        try {
            root = (Parent) fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Menu view = fxmlLoader.getController();
        view.setStage(stage);
        view.setControleur(controleur);
        return view;
    }

    public void show() {
        stage.setTitle("Menu");
        Scene scene = new Scene(menuRoot, menuRoot.getPrefWidth(), menuRoot.getPrefHeight());
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

    public void ouvreMenuAdmin(ActionEvent actionEvent) {
    }

    public void ouvreMenuVendeur(ActionEvent actionEvent) {
        controleur.afficherPageValiderCommandes();
    }

    public void ouvreMenuLivreur(ActionEvent actionEvent) {
    }
}
