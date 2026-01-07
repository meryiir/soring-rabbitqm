# Projet Spring Boot avec RabbitMQ

Ce projet contient deux mini-projets Spring Boot qui démontrent l'utilisation de RabbitMQ pour l'échange de messages entre microservices.

## 📋 Structure du Projet

### Mini-projet 1 : Communication basique Producer → RabbitMQ → Consumer
- **Producer** : `com.example.demo.producer`
- **Consumer** : `com.example.demo.consumer`

### Mini-projet 2 : Communication avec persistance SQLite
- **Producer** : `com.example.demo.producer2`
- **Consumer** : `com.example.demo.consumer2` (avec JPA/SQLite)

## 🚀 Prérequis

- **RabbitMQ** en cours d'exécution (port 5672 pour AMQP, 15672 pour l'interface web)
  - Docker recommandé : `docker run -d --name rabbitmq -p 5672:5672 -p 15672:15672 rabbitmq:3-management`
- **JDK 17+**
- **Maven 3.6+**
- **SQLite** (pour le mini-projet 2, base de données embarquée)
  - Le fichier `rabbitmq_demo.db` sera créé automatiquement dans le répertoire du projet

## 🔧 Configuration

### RabbitMQ
Les configurations par défaut dans `application-*.properties` :
- Host: `localhost`
- Port: `5672`
- Username: `guest`
- Password: `guest`

### SQLite (Mini-projet 2)
La configuration SQLite est déjà prête dans `application-consumer2.properties` :
```properties
spring.datasource.url=jdbc:sqlite:rabbitmq_demo.db
spring.datasource.driver-class-name=org.sqlite.JDBC
```
Le fichier de base de données sera créé automatiquement dans le répertoire du projet lors du premier lancement.

## 📦 Mini-projet 1 : Communication basique

### Méthode 1 : Utiliser les scripts de lancement (Windows)

**Lancer le Consumer :**
```bash
run-consumer.bat
```

**Lancer le Producer (dans un autre terminal) :**
```bash
run-producer.bat
```

### Méthode 2 : Lancer avec Maven

**Lancer le Consumer :**
```bash
mvn exec:java -Dexec.mainClass="com.example.demo.consumer.ConsumerApplication" -Dexec.args="--spring.config.location=classpath:/application-consumer.properties"
```

**Lancer le Producer :**
```bash
mvn exec:java -Dexec.mainClass="com.example.demo.producer.ProducerApplication" -Dexec.args="--spring.config.location=classpath:/application-producer.properties"
```

Le Consumer écoute sur le port **8081** et le Producer sur le port **8080**.

### Tester le Mini-projet 1

#### Méthode 1 : Endpoint de test
```bash
curl http://localhost:8080/api/messages/test
```

#### Méthode 2 : Envoyer un message personnalisé
```bash
curl -X POST http://localhost:8080/api/messages \
  -H "Content-Type: application/json" \
  -d '{
    "content": "Mon message de test",
    "id": "msg-001",
    "timestamp": "2024-01-15T10:30:00"
  }'
```

**Observation** :
- Les messages apparaissent dans les logs du Consumer
- Vérifier dans l'interface RabbitMQ Management (http://localhost:15672) :
  - Exchange : `demo.exchange`
  - Queue : `demo.queue`
  - Messages envoyés/reçus

## 🗄️ Mini-projet 2 : Communication avec persistance SQLite

### Base de données SQLite
Aucune configuration supplémentaire n'est nécessaire ! SQLite est une base de données embarquée :
- Le fichier `rabbitmq_demo.db` sera créé automatiquement lors du premier lancement du Consumer2
- La table `users` sera créée automatiquement via JPA/Hibernate

### Méthode 1 : Utiliser les scripts de lancement (Windows)

**Lancer le Consumer2 :**
```bash
run-consumer2.bat
```

**Lancer le Producer2 (dans un autre terminal) :**
```bash
run-producer2.bat
```

### Méthode 2 : Lancer avec Maven

**Lancer le Consumer2 :**
```bash
mvn exec:java -Dexec.mainClass="com.example.demo.consumer2.Consumer2Application" -Dexec.args="--spring.config.location=classpath:/application-consumer2.properties"
```

**Lancer le Producer2 :**
```bash
mvn exec:java -Dexec.mainClass="com.example.demo.producer2.Producer2Application" -Dexec.args="--spring.config.location=classpath:/application-producer2.properties"
```

Le Consumer2 écoute sur le port **8082** et crée automatiquement la table `users` via JPA.
Le Producer2 écoute sur le port **8083**.

### Tester le Mini-projet 2

#### Envoyer un message User
```bash
curl -X POST http://localhost:8083/api/users \
  -H "Content-Type: application/json" \
  -d '{
    "userId": "user-123",
    "name": "Jean Dupont",
    "email": "jean.dupont@example.com"
  }'
```

#### Endpoint de test
```bash
curl http://localhost:8083/api/users/test
```

#### Consulter les utilisateurs persistés
```bash
# Lister tous les utilisateurs
curl http://localhost:8082/api/users

# Consulter un utilisateur par ID
curl http://localhost:8082/api/users/1

# Consulter un utilisateur par userId
curl http://localhost:8082/api/users/userId/user-123
```

**Observation** :
- Les messages User sont persistés dans SQLite (fichier `rabbitmq_demo.db`)
- Vérifier le fichier `rabbitmq_demo.db` dans le répertoire du projet
- Utiliser un outil SQLite (comme DB Browser for SQLite) pour visualiser la table `users`
- Vérifier dans l'interface RabbitMQ Management :
  - Exchange : `demo.user.exchange`
  - Queue : `demo.user.queue`

## 📚 Concepts implémentés

### Mini-projet 1
- ✅ Déclaration dynamique d'un Exchange, d'une Queue et d'un Binding
- ✅ Publication de messages via REST (Producer)
- ✅ Consommation de messages via `@RabbitListener` (Consumer)
- ✅ Sérialisation/désérialisation JSON avec `Jackson2JsonMessageConverter`

### Mini-projet 2
- ✅ Tous les concepts du mini-projet 1
- ✅ Persistance des messages consommés dans SQLite via Spring Data JPA
- ✅ Modèle `User` avec JPA annotations
- ✅ Repository JPA pour les opérations CRUD

## 🐛 Dépannage

### RabbitMQ non accessible
Vérifier que RabbitMQ est démarré :
```bash
docker ps | grep rabbitmq
```

### SQLite non accessible
SQLite est une base de données embarquée qui ne nécessite pas de serveur. Si vous rencontrez des problèmes, vérifier que le répertoire du projet est accessible en écriture pour créer le fichier `rabbitmq_demo.db`.

### Port déjà utilisé
Modifier le port dans les fichiers `application-*.properties` :
```properties
server.port=XXXX
```

## 📝 Notes

- Les applications peuvent être lancées simultanément si elles utilisent des ports différents
- Chaque application Spring Boot utilise son propre package et sa propre configuration
- L'interface RabbitMQ Management est accessible à http://localhost:15672 (guest/guest)
