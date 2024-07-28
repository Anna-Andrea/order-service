# order-service

NOTE: You don't have to provide actual Google Maps API key to us, just describe in the README how to use a custom key with your solution.

A: In my application, the actual Google Maps API key is used to fetch actual distance information. For security reasons and to follow best practices, the API key is not hard-coded in the source code but is instead read from an environment variable.

  - *Note:* Please run the following command (Assume Docker is already installed in the test machine.):  
    ```
    cd order-service
    chmod +x start.sh
    ./start.sh
    ```
