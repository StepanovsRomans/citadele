# Payment Form

## Docker Setup
### Prerequisites for Docker setup
- Java installed
- Docker installed

Also you may want to install 'Lombok' plugin for your IntellijIdea.

##### Create docker image for back-end: 
To create docker image for backend in citadele/payment-form-core run
```bash
./wgradle jibDockerBuild
```
This command will be generate a docker image with name **payment-form-core** and **tag 0.0.1** and push it to your local Docker Daemon. Plugin behavior could be configured in **build.gradle**

##### Create docker image for front-end:
To create docker image for front-end in citadele/payment-form-ui run
````bash
docker build -t payment-form-ui:0.0.1 .
````
This command will be generate a docker image with name **payment-citadele-ui** and **0.0.1**. You can use any name for service, but then it should be changed in **docker-compose.yml**

#### Change Environment Variables in docker-compose.yaml
To do so go to citadele/docker-compose.yaml. Example of properties which should be changed:
````bash
environment:
      - SMTP_HOST=smtp.gmail.com
      - SMTP_PORT=587
      - SMTP_USERNAME=sender.email@gmail.com
      - SMTP_PASSWORD=sender.password
      - RECEIVER_EMAIL_ADDRESS=receiver.email
````
#### Confirm properties in docker-compose.yaml
Please verify, that Environment Variables are set and service image names correspond to which you have beed created.

Run the docker compose: 
```bash
docker-compose up
```

Now application front-end should be available on **http://localhost:8080**

## Local Setup
Also you could run application without docker installed
### Prerequisites for Local setup
- Java installed
- NPM installed

##### To Run front-end:
Run in citadele/payment-form-ui
````bash
npm run serve
````

This command will run local web server for you in dev mode.

##### To Run backend-end:
First of all you need to add variables to your environment which where described earlier or simply change application.properties file in resources.

Then run in citadele/payment-form-core
````bash
./gradlew bootRun
````

This command will run application server for your spring application.