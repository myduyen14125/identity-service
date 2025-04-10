# Identity service
This microservice is responsible for:
* Onboarding users
* Roles and permissions
* Authentication

## Tech stack
* Build tool: maven >= 3.9.5
* Java: 21
* Framework: Spring boot 3.2.x
* DBMS: MySQL

## Prerequisites
* Java SDK 21
* A MySQL server

## Start application
`mvn spring-boot:run`

## Build application
`mvn clean package`

## About Docker 
### Build docker image
`docker build -t <account>/identity-service:0.9.0 .`
### Push docker image to Docker Hub
`docker image push <account>/identity-service:0.9.0`
### Create network:
`docker network create <network_name>`
### Start MySQL in name-network
`docker run --network <network_name> --name mysql -p 3306:3306 -e MYSQL_ROOT_PASSWORD=root -d mysql:9.2.0-oracle`
### Run your application in custom network
`docker run --name identity-service --network <network_name> -p 8080:8080 -e DBMS_CONNECTION=jdbc:mysql://mysql:3306/identity_service 
<account_docker_name>/identity-service:0.9.0`

## Install Docker on ubuntu

# Add Docker's official GPG key:
sudo apt-get update
sudo apt-get install ca-certificates curl
sudo install -m 0755 -d /etc/apt/keyrings
sudo curl -fsSL https://download.docker.com/linux/ubuntu/gpg -o /etc/apt/keyrings/docker.asc
sudo chmod a+r /etc/apt/keyrings/docker.asc

# Add the repository to Apt sources:
echo \
"deb [arch=$(dpkg --print-architecture) signed-by=/etc/apt/keyrings/docker.asc] https://download.docker.com/linux/ubuntu \
$(. /etc/os-release && echo "$VERSION_CODENAME") stable" | sudo tee /etc/apt/sources.list.d/docker.list > /dev/null

sudo apt-get update

sudo apt-get install docker-ce docker-ce-cli containerd.io docker-buildx-plugin docker-compose-plugin
