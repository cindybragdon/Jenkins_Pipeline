<a name="readme-top"></a>
<h2 align="center">Laboratoires, cours Maintenance de Logiciels</h2>
<h3 align="center">Remis à l'enseignant, monsieur Dominic Légaré</h3>
<h3 align="center">Réalisé par : Cindy Bragdon https://github.com/cindybragdon</h3>
<h3 align="center">12 octobre 2024</h3>

---
<!-- TABLE OF CONTENTS -->
<details>
  <summary>Table des matières PROJETS DE SESSION</summary>
  <ol>
    <li><a href="#laboratoire-maven-projet-initial">Laboratoire Maven Projet Initial</a></li>
    <li><a href="#laboratoire-pipeline-jenkins-projet-actuel">Laboratoire Pipeline Jenkins Projet Actuel</a></li>
    
  </ol>
</details>
 

---
<a name="maven-top"></a>
<!-- LABORATOIRE MAVEN PROJET INITIAL-->
<h2 align="center">Laboratoire Maven Projet Initial</h2>


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
   ![image](https://github.com/user-attachments/assets/7e9ed283-6abc-46bc-8f48-70fedc63657b)


2. POST /rocket
    ![image](https://github.com/user-attachments/assets/238c3aa1-f898-4371-a35a-3745c69cdc2a)



<p align="right">(<a href="#maven-top">Retour au debut de Maven</a>)</p>



<!-- TEST -->
## Couverture de tests unitaires

Ajout de couverture de tests unitaires pour le code actuel. 



<!-- PARTICIPATION -->
## Participation

Chacune des parties prenantes doit participer également aux migrations, fix de bug et à l'utilisation correcte du gitflow


<p align="right">(<a href="#maven-top">Retour au debut de Maven</a>)</p>
---
---
<a name="jenkins-top"></a>
<!-- LABORATOIRE PIPELINE JENKINS PROJET ACTUEL-->
<h2 align="center">Laboratoire Pipeline Jenkins Projet Actuel</h2>

- **Cindy Bragdon** https://github.com/cindybragdon


<!-- TABLE OF CONTENTS -->
<details open>
  <summary>Table des matières</summary>
  <ol>
    <li>
      <a href="#a-propos-du-projet-pipeline">A propos du projet Jenkins</a>
    </li>
    <li><a href="#technologies-utilisees">Technologies utilisees</a></li>
    <li><a href="#migrations-exigees-pipeline">Migrations Exigees Pipeline</a></li>
    <li><a href="#couverture-de-tests-accrue">Couverture de tests accrue</a></li>
    <li><a href="#mecanisme-arret-de-build-docker">Mecanisme arret de build Docker</a></li>
    <li><a href="#acces-a-jenkins-et-nexus">Acces a Jenkins et Nexus</a></li>
    <li><a href="#variables-environnement">Variables environnement</a></li>
  </ol>
</details>

---

<!-- A PROPOS DU PROJET PIPELINE-->
## A propos du projet pipeline

Le but de ce laboratoire est d'explorer les liens entre le projet Maven réalisé dans le laboratoire précédent, le dépôt Nexus, et de comprendre comment faire un projet de déploiement en continu avec Jenkins.
![image](https://github.com/user-attachments/assets/55d51410-2d9c-447b-8445-4ae8798ffd02)

---

<!-- TECHNOLOGIES UTILISEES -->
## Technologies utilisees

- ![Java](https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=java&logoColor=white)
- ![Jacoco](https://img.shields.io/badge/Jacoco-5C2D91?style=for-the-badge&logo=java&logoColor=white)
- ![Jenkins](https://img.shields.io/badge/Jenkins-2C5263?style=for-the-badge&logo=jenkins&logoColor=white)
- ![Maven](https://img.shields.io/badge/Maven-C71A36?style=for-the-badge&logo=apache-maven&logoColor=white)
- ![Spring Boot](https://img.shields.io/badge/Spring%20Boot-6DB33F?style=for-the-badge&logo=spring-boot&logoColor=white)
- ![IntelliJ IDEA](https://img.shields.io/badge/IntelliJ%20IDEA-000000?style=for-the-badge&logo=intellij-idea&logoColor=white)
- ![Nexus](https://img.shields.io/badge/Nexus-512888?style=for-the-badge&logo=sonatype-nexus&logoColor=white)
- ![Docker](https://img.shields.io/badge/Docker-0db7ed?style=for-the-badge&logo=docker&logoColor=white)


<p align="right">(<a href="#jenkins-top">Retour au debut de Jenkins</a>)</p>

---

<!-- MIGRATIONS EXIGEES PIPELINE-->
## Migrations Exigees Pipeline

Migrer le projet initial *Maven* sur Jenkins et vous assurer qu'il fonctionne toujours tout en respectant certains critères de qualité.

---

<!-- COUVERTURE DE TESTS ACCRUE -->
## Couverture de tests accrue

Ajout de tests unitaires pour le code actuel. La couverture de tests s'élève présentement à 70%
Afin de visualiser la couverture de test, exécutez la commande *mvn clean test*.
Un fichier *index.html* sera généré comme ceci : <br>
![image](https://github.com/user-attachments/assets/00a4933b-fb89-4f34-b397-9b7020b6523f)

Double cliquez sur *index.html* et dans l'onglet de code, choissisez votre navigateur : <br>
![image](https://github.com/user-attachments/assets/ffe874ef-f818-4c87-872d-b40e843d84af)

Voici la couverture de tests visuellement représentée par Jacoco <br>
![image](https://github.com/user-attachments/assets/9668763d-0e71-4c28-99a2-a192e5f766a9)

---

<!-- MECANISME ARRET DE BUILD DOCKER-->
## Mecanisme arret de build Docker

Ajout d'un mécanisme qui empêche la génération de l'image Docker si la couverture de tests unitaires est inférieure à un seuil prédéfini (60 %).

![image](https://github.com/user-attachments/assets/069b97f5-a2fe-4d65-ae93-ba7979df5bb4)

---

<!-- ACCES JENKINS ET NEXUS -->
## Acces a Jenkins et Nexus

- Jenkins: [http://10.10.0.30:8080/](http://10.10.0.30:8080/)
- Nexus: [http://10.10.0.30:8081/](http://10.10.0.30:8081/)


---

<!-- VARIABLES ENVIRONNEMENT -->
## Variables environnement

- `NEXUS_1` (URL du dépôt avec le port)
- `NEXUS_DOCKER_PASSWORD` (mot de passe Nexus)
- `NEXUS_DOCKER_USERNAME` (nom d'utilisateur Nexus)
  
<p align="right">(<a href="#jenkins-top">Retour au debut de Jenkins</a>)</p>
<p align="right">(<a href="#readme-top">Retour en haut du readme</a>)</p>



