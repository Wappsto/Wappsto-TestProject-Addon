# Wappsto-TestProject-Addon
Addon for TestProject. This addon provides actions that support UI testing of the Wappsto dashboard using the TestProject framework. These actions set up test-preconditions directly through the Wappsto API rather than the web interface. The TestProject Java SDK is provided in the `external-dep` folder along with a shell-script which installs the dependency into your local maven repository.

# Set up development environment
* Ensure that Apache Maven is installed in your environment
* Navigate to `external-dep/`
* Run `add-dependency-to-maven.sh`

Alternatively:
* Run the following command from terminal:
```
mvn install:install-file\
  -Dfile=external-dep/TestProject_SDK_0.65.0.jar\
  -DgroupId=io.testproject\
  -DartifactId=java-sdk\
  -Dversion=0.65.0\
  -Dpackaging=jar
```

# Compile
To compile the addon, run this maven command:
```
mvn compile
```

# Testing
In order to run the automated test suite, the following environment variables need to be set:
```
WAPPSTO_ADMIN_USERNAME : <admin_email>
WAPPSTO_ADMIN_PASSWORD : <admin_password>
TESTPROJECT_DEV_TOKEN : <testproject.io_dev_token>
WAPPSTO_API_ROOT : <wappsto_api_url> (eg. https//qa.wappsto.com/services)
WAPPSTO_APP_URL : <wappsto_dashboard_url> (eg. https//:qa.wappsto.com)
WAPPSTO_ADMIN_PANEL_URL : <admin_panel_url> (eg. https://admin.qa.wappsto.com)
WAPPSTO_SOCKET_URL : <ssl_socket_address> (eg. qa.wappsto.com)
WAPPSTO_SOCKET_PORT : <ssl_socket_port>
```
Furthermore, a TestProject agent is needed in order to run the acceptance tests. The docker-compose provided with the repo can be used for this purpose. Simply start the container, and run the tests on your local machines - the testrunner will connect to the dockerized agent through localhost.

To compile the Addon and run the tests at the same time:
```
mvn package
```

# Documentaion
Documentation is available on the published GitHub pages [here](https://wappsto.github.io/Wappsto-TestProject-Addon/).
