# Pre-req
- ubuntu
- java 11
- maven
- docker
- docker-compose
- minikube
# Naviagate the workspace, nagp folder
        cd nagp
# Goto microservice folder and build package and docker images
        cd microservice
        mvn clean package
        export SPRING_DATASOURCE_USERNAME=k8sapp
        export SPRING_DATASOURCE_PASSWORD=k8sapp
        export SPRING_DATASOURCE_URL=mysql://database:3306/nagp
        java -jar target/k8sapp.jar
        or
        java -jar -DSPRING_DATASOURCE_USERNAME=k8sapp -DSPRING_DATASOURCE_PASSWORD=k8sapp -DSPRING_DATASOURCE_URL=mysql://database:3306/nagp target/k8sapp.jar
        docker build -t k8sapp .
# Run application in host environment and test it
        docker images
        docker-compose up

        curl --location --request GET 'http://192.168.56.11:8080/api/records' \
        --header 'Content-Type: application/json'

        curl --location --request POST 'http://192.168.56.11:8080/api/records' \
        --header 'Content-Type: application/json' \
        --data-raw '{
                "column1": "Value 17",
                "column2": "Value 18",
                "column3": "Value 19"
            }'
# Push docker image
        docker build -t dullaraviteja/k8sapp:1.0 .
        docker push dullaraviteja/k8sapp:1.0









#!/bin/bash

# Start Minikube
minikube start

# Build the microservice Docker image
docker build -t your-microservice-image microservice/

# Build the database Docker image
cd database/
docker build -t your-database-image .
cd ..

# Apply the Kubernetes manifests
kubectl apply -f kubernetes/deployment.yaml
kubectl apply -f kubernetes/secrets.yaml
kubectl apply -f kubernetes/persistentvolumeclaim.yaml

# Verify deployments and services
kubectl get pods
kubectl get services

# Access the microservice API
minikube service my-microservice
