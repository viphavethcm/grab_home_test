### Hello. Thank for taking your time ^.^ ###
### Here is some guideline to build and run my home test ###
### Programming Language: Java"
### Build: Gradle"
### Programming Framework: SpringBoot"

### Clone project ###
```
git clone https://github.com/viphavethcm/grab_home_test.git
```

### Option 1: Run with Java (If you have JVM installed) ### 
* Build project
```
./gradlew build
```

* Run the application
```
./gradlew bootRun
```

### Option 2: Run with Docker (No Java setup needed) ###

```
* Build (It takes up to 4-5m. Let's take a rest ^.^)
docker build -t home-test .

* Run
docker run -p 8080:8080 home-test-app

* Stop
docker stop home-test-app
```

### API Testing ###
* The account DB is already has data with 2 accounts
```
accountId 1: c914b91f-8675-4002-a5f8-fd5135d22c87
accountId 2: bca0a12a-ef57-4900-bfb0-efb570cc4f7e
```

* Create Account(/account) - POST method
```
curl 'http://localhost:8080/account' \
--header 'Content-Type: application/json' \
--data-raw '{
    "name": "HomeTest"
    "balance": 5000
}'
```

* Get Account(/account/{accountId}) - GET method
```
curl http://localhost:8080/account/c914b91f-8675-4002-a5f8-fd5135d22c87
```

* Make transfer
```
curl 'http://localhost:8080/transactions' \
--header 'Content-Type: application/json' \
--data-raw '{
    "amount": 5000,
    "fromAccount": {
        "accountId": "c914b91f-8675-4002-a5f8-fd5135d22c87",
        "paymentMethod": "DEBIT"
    },
    "toAccount": {
        "accountId": "bca0a12a-ef57-4900-bfb0-efb570cc4f7e",
        "paymentMethod": "CREDIT"
    }
}'
```

* Project Structure
```
src/
├── main/
│   ├── java/com/grab/home_test/
│   │   ├── controller/          # REST API endpoints
│   │   ├── service/             # Business logic
│   │   ├── repo/                # Data access layer
│   │   ├── exception/           # Custom exceptions
│   │   └── common/              # Enums and constants
│   └── resources/
│       └── application.yml
```

