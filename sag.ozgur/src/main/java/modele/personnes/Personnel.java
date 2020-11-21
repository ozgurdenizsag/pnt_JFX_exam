package modele.personnes;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class Personnel {

    private String nom;
    private String mdp;
    private Set<String> mesRoles;


    public Personnel(String nom, String mdp) {
        this.nom = nom;
        this.mdp = mdp;
        mesRoles = new HashSet<>();
    }

    public Personnel(String nom, String mdp, String... droits) {
        this.nom = nom;
        this.mdp = mdp;
        mesRoles = new HashSet<String>(Arrays.asList(droits));
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getMdp() {
        return mdp;
    }

    public void setMdp(String mdp) {
        this.mdp = mdp;
    }

    public void addRole(String role) { mesRoles.add(role);}
    public boolean hasRole(String role) {
        return mesRoles.contains(role);
    }

    public Collection<String> getRoles() {
        return mesRoles;
    }

    public void supprimerRole(String role) {
        mesRoles.remove(role);
    }
}
