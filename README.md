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
  -Dfile=path/to/TestProject_SDK_0.65.0.jar\
  -DgroupId=io.testproject\
  -DartifactId=java-sdk\
  -Dversion=0.65.0\
  -Dpackaging=jar
```

# Testing
In order to run the automated test suite, the following environment variables need to be set:
```
username=<admin_email>
password=<admin_password>
dev_token=<testproject.io_dev_token>
api_root=<wappsto_api_url>
app_url=<wappsto_dashboard_url>
admin_panel=<admin_panel_url>
network=<iot_test_network_uuid>
developer_username=<dev_dashboard_account_email>
developer_password=<dev_dashboard_account_password>
```
Note that the developer account needs to have permission to claim the network UUID.
