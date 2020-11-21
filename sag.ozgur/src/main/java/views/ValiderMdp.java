package views;

import controlleur.Controleur;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

public class ValiderMdp {
    
    public BorderPane saisiemdpRoot;
    public TextField mdp;


    private Controleur controleur;
    private Stage stage;

    public Stage getStage(){
        return this.stage;
    }

    public static ValiderMdp creerInstance(Controleur controleur, Stage stage) {
        URL location = ValiderMdp.class.getResource("/views/saisiemdp.fxml");
        FXMLLoader fxmlLoader = new FXMLLoader(location);
        Parent root = null;
        try {
            root = (Parent) fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        ValiderMdp view = fxmlLoader.getController();
        view.setStage(stage);
        view.setControleur(controleur);
        return view;
    }

    public void show() {
        stage.setTitle("ValiderLogin");
        Scene scene = new Scene(saisiemdpRoot, saisiemdpRoot.getPrefWidth(), saisiemdpRoot.getPrefHeight());
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

    public void validerMdp(ActionEvent actionEvent) {
       if (controleur.getPersonnel().getMdp().equals(mdp.getText())) {
           controleur.afficherMenu();
        } else {
           controleur.messageErreur("Mdp Erroné");
       }
    }

    public void pagePrecedenteLogin(ActionEvent actionEvent) {
        controleur.pagePrecedenteLogin();
    }
}
