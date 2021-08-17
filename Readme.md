# Persistent KeyValue Store Service

<h4>About Service </h4>
Above service allows user to do CRUD operation on a key store. Also it persist the state (i.e it holds the data even after restart of the service).

For subscription it has a kafka publisher which sends any key value added to the topic evt.keystore for anyone to consume.

<h4>Tech Stack</h4>
Framework used:- Spring boot  
Language:- Java  
Documentation:- Swagger 

<h4> Usage </h4>
Run KeyValueStoreApplication.java class

Open SwaggerURL:- http://localhost:8080/keyvalue-service/swagger-ui.html


<h4> CI/CD </h4>
Have put a simple CI/CD using Jenkins pipeline 2.0 to deploy the service in EKS cluster of AWS while pushing the docker image to ecr.


<h4>Future Scope</h4> 
Exposing the functionality via CLI, there is library which does the same (Picoli https://picocli.info/quick-guide.html)
