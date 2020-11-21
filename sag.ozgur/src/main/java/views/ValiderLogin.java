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

public class ValiderLogin {


    public BorderPane connexionRoot;
    public TextField id;
    public Label msgErreur;
    private Controleur controleur;
    private Stage stage;

    public Stage getStage(){
        return this.stage;
    }

    public static ValiderLogin creerInstance(Controleur controleur, Stage stage) {
        URL location = ValiderLogin.class.getResource("/views/saisielogin.fxml");
        FXMLLoader fxmlLoader = new FXMLLoader(location);
        Parent root = null;
        try {
            root = (Parent) fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        ValiderLogin view = fxmlLoader.getController();
        view.setStage(stage);
        view.setControleur(controleur);
        return view;
    }

    public void show() {
        stage.setTitle("ValiderLogin");
        Scene scene = new Scene(connexionRoot, connexionRoot.getPrefWidth(), connexionRoot.getPrefHeight());
        stage.setScene(scene);
        stage.show();
    }

    public void validerLogin(ActionEvent actionEvent) {
        try {
            controleur.validerLogin(id.getText());

        } catch (NullPointerException e){
            controleur.messageErreur("Login Erroné !");
        }
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

    public void passerCommande(ActionEvent actionEvent) {
        controleur.afficherPageCommande();
    }
}
