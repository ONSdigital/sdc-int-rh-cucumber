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


## Debugging

To help with debugging you can trigger a file saving of the page content on each page transition. 

    ./run.sh -DdumpPageContent=true

    