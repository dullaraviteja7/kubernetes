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

        curl --location --request GET 'http://192.168.59.101:30823/api/records' \
        --header 'Content-Type: application/json'

        curl --location --request POST 'http://192.168.59.101:30823/api/records' \
        --header 'Content-Type: application/json' \
        --data-raw '{
                "column1": "Ravi Teja",
                "column2": "Maths",
                "column3": "100"
            }'

# Push docker image
        docker build -t dullaraviteja/k8sapp:1.0 .
        docker push dullaraviteja/k8sapp:1.0

# Start Minikube and create namespace
        minikube start
        kubectl create namespace nagp

# Apply the Kubernetes manifests
        kubectl apply -f kubernetes/persistent-volume.yaml --namespace nagp
        kubectl apply -f kubernetes/persistent-volume-claim.yaml --namespace nagp
        kubectl apply -f kubernetes/configmap.yaml --namespace nagp
        kubectl apply -f kubernetes/secrets.yaml --namespace nagp
        kubectl apply -f kubernetes/database-init-job.yaml --namespace nagp
        kubectl apply -f kubernetes/deployment.yaml --namespace nagp
        or
        kubectl apply -f kubernetes/deployment-all.yaml --namespace nagp

# Verify deployments and services
        kubectl get deployments --namespace nagp
        kubectl get services --namespace nagp
        kubectl get pods --namespace nagp
        or
        kubectl get all --namespace nagp

# Access the microservice API
### Note down the port number listed under the "PORT(S)" column. This is the NodePort that maps to the microservice
        kubectl get services microservice --namespace nagp
###Access the microservice using the Minikube node's IP and the NodePort. In a separate terminal window, run:
        minikube ip
### Note down the IP address displayed. Then, in your web browser or using tools like cURL or Postman, access the microservice using the following URL format:
        http://<minikube-ip>:<node-port>

### (OR)For LoadBalancer serice type  
        minikube service microservice --namespace nagp

# Testing
### Post some data and see it gettin save in database
        curl --location --request GET 'http://192.168.59.101:30823/api/records' \
        --header 'Content-Type: application/json'

        curl --location --request POST 'http://192.168.59.101:30823/api/records' \
        --header 'Content-Type: application/json' \
        --data-raw '{
                "column1": "Ravi Teja",
                "column2": "Maths",
                "column3": "100"
            }'
        curl --location --request GET 'http://192.168.59.101:30823/api/records' \
        --header 'Content-Type: application/json'

### Delete a pod and see it is getting recreated automatically 
        kubectl get pods --namespace nagp
        kubectl delete pod --namespace nagp <pod-name>
        kubectl get pods --namespace nagp

### stop minikube and start again, see database data is retained
        minikube stop
        minikube start
        curl --location --request GET 'http://192.168.59.101:30823/api/records' \
        --header 'Content-Type: application/json'
# Debuging 
        kubectl delete ns nagp

        kubectl logs <pod-name> --namespace nagp

        kubectl get jobs --namespace nagp
        kubectl logs --namespace nagp <job-name>
        kubectl logs <job-name> --namespace nagp --previous
