package application;


import controlleur.Controleur;
import facade.ServiceImpl;
import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {

    public void start (Stage primaryStage) throws Exception{
        // init du modele : TODO
        ServiceImpl facadeParis  = new ServiceImpl();
        Controleur monControleur = new Controleur(primaryStage, facadeParis);}

    public static void main(String[] args) {
        launch(args);
    }



}
