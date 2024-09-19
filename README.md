<h3 align="center">Laboratoire Maven</h3>


- **Cindy Bragdon** https://github.com/cindybragdon
- **Olivier Poirier** https://github.com/olivierpoirier

Pour builder:
    mvn clean install

Pour partir Spring Boot:
    mvn spring-boot:run
    

<!-- TABLE OF CONTENTS -->
<details>
  <summary>Table des matières</summary>
  <ol>
    <li>
      <a href="#à-propos-du-projet">À propos du projet</a>
    </li>
    <li><a href="#migrations-exigées">Migrations exigées</a></li>
    <li><a href="#bug-a-fixer">Bug a fixer</a></li>
    <li><a href="#couverture-de-tests-unitaires">Couverture de tests unitaires</a></li>
    <li><a href="#participation">Participation</a></li>
  </ol>
</details>



<!-- ABOUT THE PROJECT -->
## À propos du projet

Projet effectué dans le cadre du cours *Maintenance de logiciel* offert à la cinquième session du DEC Techniques de l'informatique au cégep Marie-Victorin.  Remis à l'enseignant Dominic Légaré.

Un ancien projet doit être migré sur un nouveau serveur. Étant donné l'âge du projet, les anciennes technologies s'y rattachant ne sont plus supportées. Une migration est nécessaire afin de maintenir le projet et vous assurer qu'il fonctionne toujours.

Le but de ce laboratoire est d'explorer les liens entre les librairies, dépôt local et de comprendre comment faire une migration simple d'un projet. Le projet choisit devra être mis à jour avec des librairies précises en fonction des requis de la migration.


<!-- MIGRATIONS -->
## Migrations exigées

À partir des versions actuelles dans le pom.xml, migrer celles-ci vers les versions ci-bas.

Spring Framework 6
Spring Boot 3.3.X
Java 17+
Webflux (optionel)


<!-- BUG -->
## Bug a fixer

Fixer le bug sur la compilation et exécution avec succès sur 2 requêtes GET et POST après le fix.

1. GET /rocket/{rocketid}
   ![image](https://github.com/user-attachments/assets/a8445f24-68d4-4444-bb4b-a41a76c248dc)

2. POST /rocket
    ![image](https://github.com/user-attachments/assets/b415b5dc-123b-4e68-a0ce-16b85456c030)


<p align="right">(<a href="#readme-top">back to top</a>)</p>



<!-- TEST -->
## Couverture de tests unitaires

Ajout de couverture de tests unitaires pour le code actuel. 



<!-- PARTICIPATION -->
## Participation

Chacune des parties prenantes doit participer également aux migrations, fix de bug et à l'utilisation correcte du gitflow

<p align="right">(<a href="#readme-top">back to top</a>)</p>




