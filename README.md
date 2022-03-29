# i3-Market Semantics Engine

## 1. Introduction
The semantic engine is a data microservice, which can interact with other services of the i3-Market for data offering registration. The semantic engine also provides necessary information such as contract parameters, pricing models, etc for other services and users. The repository of the semantic engine on the i3-Market gitlab can be found [here](https://gitlab.com/i3-market/code/wp4/semantic-engine) . The Swagger reference of the semantic engine itself can be found [here](http://95.211.3.249:8082/swagger-ui.html)

## 2. Objectives

The semantic engine provides necessary API endpoints in relation to the data offering purpose of the i3Market. Details of each API endpoint can be found in the following sections.


## 3. How to build the semantic engine docker images and containers

The Semantic engine 's repository is stored in Gitlab [click here](https://gitlab.com/i3-market/code/wp4/semantic-engine) . The semantic engine is developed by Java Spring Boot with SparQL query using Virtuoso Database tech stack. In order to run semantic engine in Docker, following steps can be applied:

a) Install your Docker engine.

b) Create docker image of the semantic engine. Before creating a docker image, it is required to build a ".jar" file of the semantic engine at first.

c) Try the following to create your docker image and run a container for the semantic engine

```
docker build --no-cache -t registry.gitlab.com/i3-market/code/wp4/semantic-engine:latest .
```

d) Get a virtuoso database from Docker hub in [here](https://hub.docker.com/r/tenforce/virtuoso/) . Try the following to create a Virtuoso data base container to allow the semantic engine to connect with.

```
 docker run --name virtuoso -p 8890:8890 --restart=always --network=insight_chi-hung.le-network -e DBA_PASSWORD=myDbaPassword -e SPARQL_UPDATE=true -e DEFAULT_GRAPH=http://i3market/data -v /virtuoso_db/db:/data -d tenforce/virtuoso:latest
```

Note: The term  
``` --network=insight_chi-hung.le-network ```
is optional. You can replace this with your network, but it is recommended to define a network in here.

## 4. Use cases of the semantic engine

- User registration with the semantic engine:

> ```POST /api/registration/```

Example of user registration is shown below:

```
{
    "providerId": "uiot-provider",
    "name": "My name",
    "description": "This is UIoT data Provider",
    "organization": [
       {
       "organizationId": "NUIGP1",
       "name": "National University of Ireland Galway",
       "description": "Education Organisation",
       "address": "University Road, Galway",
       "contactPoint": "chihung.le@nuigalway.ie"
       }
    ]
}

```


- Data offering registration with the semantic engine

> ```POST /api/registration/data-offering```

A sample of a data offering can be found [here](https://gitlab.com/i3-market/code/wp4/semantic-engine/-/blob/dev/DataOfferingSample.json) in a JSON data format.

- Update an existing data offering:

> ```PATCH /api/registration/update-offering```

- Get a list of providers using category:

>```GET /api/registration/providers{category}/category```

- Get a list of all providers:

>```GET/api/registration/provider-list```

- Get a number of total offerings for a specific data provider. This number would be helpful for loading pages in a Web UI Application:

>```GET/api/registration/offerings/{providerId}/totalOfferings```

- Get a number of total offerings for a specific data provider at a specific category. This number would be helpful for loading pages in a Web UI Application:

>```GET/api/registration/offerings/category={category}/providerId={providerId}```

- Get a list of offeringId:

>```GET/api/registration/offering-list```

- Get a list of data offering by data profiderId:

>```GET/api/registration/offering/{id}/providerId```

- Get a list of data offering by data offeringId:

>```GET/api/registration/offering/{id}/offeringId```

- Get a list of data offering by category. This API will getch data from all node, e.g. in this case where have four nodes 244, 249, 250, and 251:

>```GET/api/registration/offering/{category}```

- Download a template of the data offering in the JSON (JavaScript Object Notation) format. This might be helpfull in some case, e.g. convert from this JSON file into a Java Class:
>```GET/api/registration/offering/offering-template```

- Get contract parameters object of a data offering by data offeringId:

>```GET/api/registration/contract-parameter/{offeringId}/offeringId```

- Get a list of category from distributed storage at its original API endpoint [click here](http://95.211.3.244:7500/data_categories) :

>```GET/api/registration/categories-list```

- Delete a data offering by data offeringid. This will delete all information of a data offering from the database:

>```DELETE/api/registration/delete-offering/{offeringId}```




## 6. License

This is free and unencumbered software released into the public domain.

Anyone is free to copy, modify, publish, use, compile, sell, or
distribute this software, either in source code form or as a compiled
binary, for any purpose, commercial or non-commercial, and by any
means.

In jurisdictions that recognize copyright laws, the author or authors
of this software dedicate any and all copyright interest in the
software to the public domain. We make this dedication for the benefit
of the public at large and to the detriment of our heirs and
successors. We intend this dedication to be an overt act of
relinquishment in perpetuity of all present and future rights to this
software under copyright law.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
IN NO EVENT SHALL THE AUTHORS BE LIABLE FOR ANY CLAIM, DAMAGES OR
OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE,
ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
OTHER DEALINGS IN THE SOFTWARE.

For more information, please refer to <http://unlicense.org/>

