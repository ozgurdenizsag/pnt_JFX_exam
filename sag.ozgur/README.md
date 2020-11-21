
# Examen PnT : Livraisons de pizzas


## Contexte 

Vous lancez avec des amis une entreprise de livraison de pizzas à domicile. 
Pour l'ouverture de votre première boutique, vous devez développer une application 
qui permet aux membres du personnel de gérer les commandes et les livraisons de pizzas 
et qui permet également aux clients de passer des commandes et de suivre leur état.

Pour des raisons de sécurité, chaque personnel authentifié par un login 
et un mot de passe possède un certains nombre de rôles, 
représentés par des chaînes de caractère ("VENDEUR", "LIVREUR", "ADMIN"). 
Un personnel ayant le rôle de "LIVREUR" est autorisé à prendre un scooter et livrer les commandes de pizzas.  

Les clients n'ont pas besoin de s'authentifier.

Vous vous êtes répartis les tâches : Vous devez développer **une application en JFX** 
qui implémente au minimum les fonctionnalités décrites ci-dessous. 

## Connexion (ConnexionService)

La page de connexion permet à la fois aux clients et aux membres du personnel d'accèder 
aux fonctionnalités de l'application qui leur correspondent.

Les clients peuvent passer des précommandes et consulter leur état **sans avoir besoin de s'authentifier**.
On ajoutera simplement des liens ou des boutons pour accéder à ces fonctionnalités. 

Les membres du personnel devront s'authentifier de la manière suivante :
un membre du personnel saisit d'abord son login puis clique sur suivant. 
Si son login est inconnu, un message d'erreur est affiché ;
sinon dans la fenêtre suivante, il saisit son mot de passe puis clique sur connexion 
 ou sur précédent pour revenir à la fenêtre de saisie du login. 
Quand il valide la connexion, si le couple login/mot de passe n'est pas valide, 
on lui redemande de saisir de nouveau son mot de passe. 
Si le couple login/mot de passe est valide, le membre du personnel arrive sur un menu. 

## Menu du personnel

Le menu affiché doit comprendre trois sections intitulées «Menu admin», «Menu vendeur» et «Menu livreur».

Il n'est pas demandé de développer tous les boutons ou liens de chacune des sections du menu, 
mais seulement ceux de la section intitulée «Menu vendeur».
Les fonctionnalités correspondant à cette section sont détaillées ci-dessous.
Pour chacune des deux autres sections, vous ajouterez un bouton ou lien nommé "TODO".

Les boutons ou liens de chacune des sections ne seront activés que si le membre du personnel connecté 
possède les rôles respectifs associés à ces sections 
(respectivement "ADMIN", "VENDEUR" et "LIVREUR").
Par exemple, si le membre du personnel a les rôles "VENDEUR" et "LIVREUR", 
les boutons ou liens de la section «Menu admin» seront désactivés.

Un membre du personnel doit pouvoir aussi se déconnecter. 


## Fonctionnalités du vendeur (CommandeService)

Toutes les commandes possèdent un Statut : PRECOMMANDE, ATTENTE, PRETE, ENLIVRAISON, et LIVREE.

* Valider une précommande : à partir d'une liste de précommandes, un vendeur peut valider les 
précommandes de son choix. Une fois une précommande validée par un vendeur, elle change de statut et 
passe en ATTENTE.


## Fonctionnalités du client (CommandeOffLineService)

* Le client devra choisir toutes les pizzas qu'il souhaite commander avant de saisir ses informations 
  personnelles. Le client peut effectuer une précommande de pizzas en mentionnant son nom, son prénom, 
  son numéro de téléphone et son adresse. 
  Une fois la précommande passée, il obtient un ticket de suivi (classe Suivi) contenant l'identifiant de la commande 
  concernée et une chaîne aléatoire.
  
  Dans la méthode **Suivi precommander(String nom, String telephone, String adresse, Pizza... pizzas)**, 
  la notation **Pizza ... pizzas** indique que l'on passe un nombre variable de paramètres 
  de type Pizza. On peut passer les paramètres séparés par une virgule (ex: pizza1, pizza2, pizza3) 
  ou un tableau de type Pizza.
  
* Lorsqu'un client a passé une commande, il a obtenu un ticket de suivi. Il peut alors consulter 
  l'évolution de sa demande en fournissant : l'identifiant de sa commande et la chaîne aléatoire 
  inclus dans le ticket de suivi. 
  Si la chaîne correspond bien à sa commande alors la commande est retournée (contenant ainsi son statut). 

## Fonctionnalités diverses

A tout moment on doit pouvoir revenir au menu principal une fois connecté. 

## Quelques infos

Dans le modèle fourni, il existe quelques données initiales en base. 
Vous avez 3 membres du personnel existants : 
* login : admin et mdp : admin 
* login : vendeur et mdp : vendeur
* login : livreur et mdp : livreur

Admin a trois rôles : admin, vendeur et livreur. 
vendeur n'a qu'un seul rôle (le sien comme son nom l'indique) et 
livreur également. 

Il y a également plusieurs commandes en cours dans la base : 
3 en PRECOMMANDE, 1 ATTENTE, 2 PRETE, 1 ENLIVRAISON, 0 LIVREE

Façades : 
* Une façade de connexion : ConnexionService (fonctionnalités d'authentification plus celles relativement commune) 
* Une façade de gestion des commandes : CommandeService
* Une façade de gestion des pré-commandes : CommandeOffLineService
* L'implantation de toutes les façades se retrouve dans la classe : ServiceImpl


## Travail à rendre

Vous devez développer une application **JFX** couvrant les fonctionnalités spécifiées ci-dessus,
en **complétant ce projet git** qui comprend déjà le code source du modèle à utiliser, 
avec vos propres packages.

Votre travail sera à rendre sur **ce même repository git** à la fin de la séance.
