# Dropwizard MongoDB Demo

Dropwizard mongodb demo project.


### Running the Service

```
cd ~/your-path/dropwizard_mongodb_demo/

mvn clean package

java -jar target/dropwizard-mongodb-demo-0.0.1.jar server config.yml
```


### Requests

**1. Create Emplyee**
```
POST: http://localhost:4000/employee

{
    "name": "Daniel",
    "department": "Accounts",
    "salary": 8000
}
```

**2. Get all employees**
```
GET: http://localhost:4000/employee
```


### Health Check
```
GET: http://localhost:4000/admin/healthcheck

{
    "MongodbHealthCheck": {
        "healthy": true,
        "message": "Database names in MongogDB are: [admin, config, employeedb, local, userdb]",
        "duration": 49,
        "timestamp": "2020-07-11T05:41:03.076+05:30"
    },
    "deadlocks": {
        "healthy": true,
        "duration": 2,
        "timestamp": "2020-07-11T05:41:03.079+05:30"
    }
}
```
