# Using Open Feign HTTP Client with MSF4J

This is the MSF4J sample that demonstrates how to use [Open Feign](https://github.com/OpenFeign/feign) client to call microservices deployed in MSF4J and trace the request response flow with [WSO2 DAS](http://wso2.com/products/data-analytics-server/).
Feign is a java to http client binder that allows you to write Java HTTP REST clients easily. You can create a Java interface and declare your service contracts using annotations.
In this example we create 3 microservices; CustomerService, InvoiceService, ReportService. This sample contains Java HTTP/REST bindings and Feign clients created for CustomerService and InvoiceService microservices.
The service message flow is as follows;

![Message Flow](resources/message-flow.png)

The ReportService uses Feign clients to call InvoiceService and CustomerService to generate the report. It uses a TracingClient (which extends feign.Client.Default class) to trace request/response flow by injecting traceable headers to the request. TracingClient publishes trace events which are created in request/response exchanges to WSO2 Data Analytics Server.

## Points to note in the code

Add the msf4j tracing and Open Feign dependencies to the pom of you MSF4J service.
```xml
    <dependencies>
        <dependency>
            <groupId>org.wso2.msf4j</groupId>
            <artifactId>msf4j-analytics</artifactId>
        </dependency>
        
        <!-- Feign -->
        <dependency>
            <groupId>io.github.openfeign</groupId>
            <artifactId>feign-core</artifactId>            
        </dependency>
        <dependency>
            <groupId>io.github.openfeign</groupId>
            <artifactId>feign-slf4j</artifactId>            
        </dependency>
        <dependency>
            <groupId>io.github.openfeign</groupId>
            <artifactId>feign-gson</artifactId>            
        </dependency>
    </dependencies>
```

Register the tracing interceptor when starting the microservice.
```java
        MSF4JTracingInterceptor tracingInterceptor = new MSF4JTracingInterceptor("Service-Chaining");
        new MicroservicesRunner()
                .addExceptionMapper(new EntityNotFoundMapper(), new CustomerNotFoundMapper())
                .registerGlobalRequestInterceptor(httpMonitoringInterceptor)
                .registerGlobalResponseInterceptor(httpMonitoringInterceptor)
                .deploy(new InvoiceService())
                .deploy(new CustomerService())
                .deploy(new ReportService())
                .start();
```

CustomerServiceAPI is defined as a Java interface which binds to HTTP/REST API via annotations.
```java
        public interface CustomerServiceAPI {
            @RequestLine("GET /customer/{id}")
            Customer getCustomer(@Param("id") String id) throws ClientException;
        }
```


Register the FeignTracingClient (which extends feign.Client.Default) when building the Feign client instance. You only need FeignTracingClient to trace request/response flow with WSO2 Data Analytics Server.
This client will publish client request/response trace events to WSO2 DAS. You can use MSF4J Message Tracing Dashboard to visualize the trace events. See below for installation instructions.

```java
        Feign.Builder b = Feign.builder()
                .client(new FeignTracingClient("Customer-Service-Client", analyticsEndpoint))
                .encoder(new GsonEncoder(ModelUtils.GSON))
                .logger(new Slf4jLogger())
                .decoder(new GsonDecoder(ModelUtils.GSON))
                .errorDecoder(new ClientErrorDecoder());
        CustomerServiceAPI client = b.target(CustomerServiceAPI.class, serviceEndpoint);
```

## How to build the sample

From this directory, run

```
mvn clean install
```

## How to run the sample

Download [WSO2 DAS Version 3.0.1](http://wso2.com/products/data-analytics-server/) and start it with default configurations.

Build [wso2das-tracing-capp](../../analytics/wso2das-tracing-capp) module and get the wso2das-msf4j-tracing-*.car file. 
Then, [deploy wso2das-msf4j-tracing-*.car file](https://docs.wso2.com/display/DAS301/Packaging+Artifacts+as+a+C-App+Archive#PackagingArtifactsasaC-AppArchive-DeployingacAppDeployingaC-App) 
in the WSO2 DAS instance you just started. Now you can check the MSF4J Message Tracing Dashboard at 
[https://localhost:9443/portal/dashboards/msf4j-message-tracing](https://localhost:9443/portal/dashboards/msf4j-message-tracing).

After that, from the target directory of this sample, run the following command to start the sample microservice.
```
java -jar das-tracing-feign-client-*.jar
```

## How to test the sample

We will use the cURL command line tool for testing. You can use your preferred HTTP or REST client too.

```
curl http://localhost:8080/report/invoice/I001
```

You should get a response similar to the following:

```
{"id":"I001","customer":{"id":"C001","firstName":"Akila","lastName":"Perera","address":"DreamWorld!"},"amount":250.15,"date":"Aug 22, 2016 4:50:23 PM"}
```

The above response was generated by 'ReportService' which in turn calls 'InvoiceService' and 'CustomerService' microservices to aggregate the result.
So after receiving the above response, you can go to the WSO2 DAS dashboard and find the traces.


![WSO2 DAS Tracing Dashboard](resources/wso2das-tracing-gadget.png)