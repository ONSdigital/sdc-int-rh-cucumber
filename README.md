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

    $ ./run.sh -Dcucumber.filter.tags="@RequestUAC-TestT65"

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

### From the IDE

An Intellij [run config template](/.run/Template%20Cucumber%20Java.run.xml) is provided to make it easy to run and debug
the tests. This is set up to use the fully local/Firestore emulator config by default. If you're using Intellij, it
should pick up this template automatically and enable you to run or debug the tests without any extra configuration,
provided RH service and UI are being run locally with default settings.

If you're using non-default settings, for example using a real remote firestore, you will need to edit the run config to
match, removing the `FIRESTORE_EMULATOR_HOST` environment variable and setting the project and credentials variables to
match RH Service.

### Command Line

To help with debugging from the command line you can trigger a file saving of the page content on each page transition.

    ./run.sh -DdumpPageContent=true

## Troubleshooting

If Docker is running an old version of rh-ui or another service then you can add '--force-recreate' to the
docker-compose command:

    $ docker-compose -f <service.yml> up --force-recreate -d  

There are other solutions, but this one came from https://vsupalov.com/docker-compose-runs-old-containers/
 
