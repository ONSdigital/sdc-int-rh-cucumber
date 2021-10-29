# ONS SDC Integrations Team RespondentHome Cucumber

## Prerequisites

To run the RH Cucumber tests the following services will need to be running:
 - Mock Envoy
 - Mock AI
 - PubSub emulator
 - Redis
 - RH Service
 - RH UI
 
If you are running locally you can run a test script to verify that they are running:

    $ ./scripts/envCheck.sh
    Mock Envoy     : ok
    Mock AI        : ok
    PubSub emulator: ok
    Redis          : ok
    RH Service     : ok
    RH UI          : ok


## Single test execution

To run a single test use its tag, for example for the T65 test:

    $ ./run.sh -Dcucumber.filter.tags="@RequestUAC-TestT65

## Running services and tests in local Docker

If not using the RH Cucumber local profile, point at the emulator using environment variables:

    export PUBSUB_EMULATOR_HOST="localhost:9808"
    export PUBSUB_EMULATOR_USE="true"

To run the services and Cucumber tests:

    cd ~/sdc/source
    
    ./sdc-int-rh-service/docker/rh-service-up.sh 
    ./sdc-int-rh-ui/docker/rh-ui-up.sh 
    docker ps
    
    cd sdc-int-rh-cucumber/
    ./run.sh
    cd ..
    
    ./sdc-int-rh-service/docker/rh-service-stop.sh 
    ./sdc-int-rh-ui/docker/rh-ui-stop.sh 
    docker ps

## Debugging

To help with debugging you can trigger a file saving of the page content on each page transition. 

    ./run.sh -DdumpPageContent=true
    
## Troubleshooting

If Docker is running an old version of rh-ui or another service then you can add '--force-recreate' to the docker-compose command: 

    $ docker-compose -f <service.yml> up --force-recreate -d  
    
There are other solutions, but this one came from https://vsupalov.com/docker-compose-runs-old-containers/

