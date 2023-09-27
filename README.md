# RESTAnti-FraudSystem

The Anti-Fraud System project is a demonstration of the principles of anti-fraud systems in the financial sector. This project provides a simplified implementation of an anti-fraud system, including user authentication and authorization, transaction validation, and the management of stolen cards and suspicious IP addresses.
## About

**Anti-Fraud System** is a project focusing on core topics related to Spring Security, Spring Data for Java Backend Developers. The project is structured into several stages, each building upon the previous one. 
## Stages

### Stage 1/6: Simple Transaction Validation

#### Description

In this stage, we implement a simple anti-fraud system that evaluates transactions based on their amounts and categorizes them as ALLOWED, PROHIBITED, or requiring MANUAL_PROCESSING.
Consider the procedure of online payment (a transaction):

![Transactions](https://github.com/mdfarhananwar/RESTAnti-FraudSystem/assets/91552730/e82dcc9a-c1d9-460b-a75e-dd9227237976)

#### Objectives

- **Create and Run a Spring Boot Application on Port 28852**:
  - Set up a Spring Boot application and configure it to run on port 28852.

- **Create the POST /api/antifraud/transaction**:
  - Implement the `POST /api/antifraud/transaction` endpoint.
  - Apply heuristic rules for transaction validation.
  - Implement the following rules:
    1. Transactions with a sum of lower or equal to 200 are ALLOWED;
    2. Transactions with a sum of greater than 200 but lower or equal than 1500 require MANUAL_PROCESSING;
    3. Transactions with a sum of greater than 1500 are PROHIBITED.

  - Return appropriate responses based on transaction validation results.
Here are some examples of requests and responses for the `POST /api/antifraud/transaction` endpoint:

**Example 1: Successful Registration**

- **Request (POST /api/antifraud/transaction)**:

  ```json
  {
     "amount": 150
  }
  ```
   **Response(200 OK)**:

  ```json
  {
     "result": "ALLOWED"
  }
  ```


**Example 2: Transaction Requires Manual Processing**

- **Request (POST /api/antifraud/transaction)**:

  ```json
  {
     "amount": 870
  }
  ```

  **Response (200 OK)**:

  ```json
  {
     "result": "MANUAL_PROCESSING"
  }
  ```

**Example 3: Prohibited Transaction**

- **Request (POST /api/antifraud/transaction)**:

  ```json
  {
   "amount": 1700
  }
  ```

  **Response (200 OK)**:
  ```json
  {
   "result": "PROHIBITED"
  }
  ```

**Example 4: Invalid Transaction Amount**

- **Request (POST /api/antifraud/transaction)**:

  ```json
  {
   "amount": -1
  }
  ```

  **Response (400 BAD REQUEST)**:


### 2nd Stage - The authentication

#### Description

In this stage, we add user authentication using Spring Security and a JDBC-based UserDetailsService. Users can register, view user lists, and delete their accounts.

### Authentication Setup

To ensure secure authentication, we will use the Spring Security module, a reliable and tested solution. Here are the steps to implement authentication:

1. **HTTP Basic Authentication**: We will provide HTTP Basic authentication for our REST service. This will be done using JDBC implementations of `UserDetailsService` for user management.

2. **User Registration Endpoint**:
   - Endpoint: `POST /api/auth/user`
   - Description: This endpoint allows users to register on our service. Users can submit their information in JSON format, including name, username, and password.
   - Validation: The service will validate the provided information.
   - Response (HTTP OK - 200):
     ```json
     {
        "id": "<Long value, not empty>",   
        "name": "<String value, not empty>",
        "username": "<String value, not empty>",
     }
     ```
   - Response (HTTP Bad Request - 400) in case of wrong data.
   - Response (HTTP CONFLICT status (409) in case of registeration of an existing user;

3. **Getting list of all users**:
   - Endpoint: `GET /api/auth/list`
   - Description: This endpoint will be available only to authenticated users. It will return  an array of objects representing the users sorted by ID in ascending order.
   - Response (HTTP OK - 200):
     ```json
       [
        {
            "id": <user1 id>,
            "name": "<user1 name>",
            "username": "<user1 username>"
        },
         ...
        {
            "id": <userN id>,
            "name": "<userN name>",
            "username": "<userN username>"
        }
      ]
     ```
     
   - Return an empty JSON array if there's no information;

4. **Delete a User**:
   - Endpoint: `DELETE /api/auth/user/{username}`
   - Description:  {username} specifies the user that should be deleted.This endpoint will be available only to authenticated users. The endpoint must delete the user and respond with the HTTP OK status (200) and the following body:
   - Response (HTTP OK - 200):
     ```json
      {
         "username": "<username>",
         "status": "Deleted successfully!"
      }
     ```
   - Return HTTP Not Found status (404), If a user is not found;

4. **Database Configuration**:
   - We will use an H2 database for persistence.
   - Configure the application.properties file with `spring.datasource.url=jdbc:h2:file:../service_db` for database URL.
   
5. **Customization**:
   - As we are implementing a REST architecture without sessions, configure HTTP Basic authentication and handle unauthorized access attempts.
   - Configure access for the API using `HttpSecurity` object.

```java
@Bean
public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    return http
            .httpBasic(Customizer.withDefaults())
            .csrf(CsrfConfigurer::disable)                           // For modifying requests via Postman
            .exceptionHandling(handing -> handing
                    .authenticationEntryPoint(restAuthenticationEntryPoint) // Handles auth error
            )
            .headers(headers -> headers.frameOptions().disable())           // for Postman, the H2 console
            .authorizeHttpRequests(requests -> requests                     // manage access
                    .requestMatchers(HttpMethod.POST, "/api/auth/user").permitAll()
                    .requestMatchers("/actuator/shutdown").permitAll()      // needs to run test
                    // other matchers
            )
            .sessionManagement(session -> session
                    .sessionCreationPolicy(SessionCreationPolicy.STATELESS) // no session
            )
            // other configurations
            .build();
    }
```
#### Objectives

- Implement HTTP Basic authentication.
- Create the `GET api/empl/payment` endpoint, accessible only for authenticated users.
- Use an H2 database for user persistence.
- Update the POST api/auth/signup endpoint to handle user registration and authentication.

##### Examples
Here are some example scenarios and their corresponding responses:

User Registration (POST api/auth/signup)
**Example 1: User Registration**

- **Request (POST /api/auth/user)**:
```json
{
   "name": "John Doe",
   "username": "JohnDoe",
   "password": "secret"
}

```
- **Response(201 CREATED)**:
```json
{
   "id": 1,
   "name": "John Doe",
   "username": "JohnDoe"
}

```

**Example 2: Occupied email**

- **Request body (POST /api/auth/user)**:
```json
{
   "name": "John Doe",
   "username": "JohnDoe",
   "password": "secret"
}
```

- **Response: 409 CONFLICT**

**Example 3: Wrong Format**:

- **Request body (POST /api/auth/user)**:
```json
{
   "name": "John Doe",
   "password": "secret"
}
```

- **Response: 400 BAD REQUEST**

**Example 4: List Users**:

- **Request(GET /api/auth/list)**

- **Response: 200 OK**:
```json
[
  {
    "name":"John Doe",
    "username":"JohnDoe",
  },
  {
    "name":"JohnDoe2",
    "username":"JohnDoe2",
  }
]
```

**Example 5: DELETE User**:

- **Request( DELETE /api/auth/user/johndoe)**
  
- **Response: 200 OK**
```json
{
   "username": "JohnDoe",
   "status": "Deleted successfully!"
}
```


### Stage 3/6: Authorization

#### Description

This stage focuses on adding authorization to the system. Different users have different roles and permissions.
Let's implement the role model for our system:

![Authorization](https://github.com/mdfarhananwar/RESTAnti-FraudSystem/assets/91552730/8793025b-d26e-495f-befe-0eed9adbaaa1)

#### Objectives

- Implement role-based authorization for users (ANONYMOUS, MERCHANT, ADMINISTRATOR, SUPPORT).
- Assign roles to users based on their registration order. The first registered user should receive the ADMINISTRATOR role; the rest — MERCHANT
- All users, except ADMINISTRATOR, must be locked immediately after registration; only ADMINISTRATOR can unlock users;
- Change the response for the POST /api/auth/user endpoint. Add the role field in the response:
```json
{
   "id": <Long value, not empty>,
   "name": "<String value, not empty>",
   "username": "<String value, not empty>",
   "role": "<String value, not empty>"
}
```
- Change the response for the GET /api/auth/list endpoint. Add the role field in the response:
```json
[
    {
        "id": <user1 id>,
        "name": "<user1 name>",
        "username": "<user1 username>",
        "role": "<user1 role>"
    },
     ...
    {
        "id": <userN id>,
        "name": "<userN name>",
        "username": "<userN username>",
        "role": "<userN role>"
    }
]
```

- Add the PUT /api/auth/role endpoint that changes user roles. It must accept the following JSON body:
```json
{
   "username": "<String value, not empty>",
   "role": "<String value, not empty>"
}
```
If successful, respond with the HTTP OK status (200) and the body like this:
```json
{
   "id": <Long value, not empty>,
   "name": "<String value, not empty>",
   "username": "<String value, not empty>",
   "role": "<String value, not empty>"
}
```
- If a user is not found, respond with the HTTP Not Found status (404);
- If a role is not SUPPORT or MERCHANT, respond with HTTP Bad Request status (400);
- If a role is already assigned, respond with the HTTP Conflict status (409);
  
- Add the PUT /api/auth/access endpoint that locks/unlocks users. It accepts the following JSON body:
```json
{
   "username": "<String value, not empty>",
   "operation": "<[LOCK, UNLOCK]>"  // determines whether the user will be activated or deactivated
}
```
- If successful, respond with the HTTP OK status (200) and the following body:
```json
{
    "status": "User <username> <[locked, unlocked]>!"
}
```
- For safety reasons, ADMINISTRATOR cannot be blocked. In this case, respond with the HTTP Bad Request status (400);
- If a user is not found, the endpoint must respond with HTTP Not Found status (404).

  
**Examples**
Here are some example scenarios and their corresponding responses:

**Example 1: User Registration**

- **Request (POST /api/auth/user)**:
```json
{
   "name": "John Doe",
   "username": "JohnDoe",
   "password": "secret"
}
```

- **Response(201 CREATED)**:
```json
{
   "id": 1,
   "name": "John Doe",
   "username": "JohnDoe",
   "role": "ADMINISTRATOR"
}
```

**Example 2: List Users**

- **Request (GET /api/auth/list)**:

- **Response (200 OK)**:
```json
[
  {
    "name":"John Doe",
    "username":"JohnDoe",
    "role": "MERCHANT"
  },
  {
    "name":"JohnDoe2",
    "username":"JohnDoe2",
    "role": "ADMINISTRATOR"
  }
]

```

**Example 3: Change User Role**

- **Request (PUT /api/auth/role)**:
```json
{
   "username": "JohnDoe1",
   "role": "SUPPORT"
}

```

- **Response (200 OK)**:
```json
{
   "id": 1,
   "name": "John Doe 1",
   "username": "JohnDoe1",
   "role": "SUPPORT"
}
```

**Example 4: Lock User Account**

- **Request (PUT /api/auth/access)**:
```json
{
   "username": "JohnDoe1",
   "operation": "LOCK"
}
```

- **Response (200 OK)**:
```json
{
   "status": "User JohnDoe1 locked!"
}
```

Ensure your authentication service follows these security measures to protect user passwords effectively.


### Stage 4/6: Stolen Cards & Suspicious IPs

#### Description

In this stage, we enhance the anti-fraud system to handle stolen cards and suspicious IP addresses. 
In our service, we will check IP addresses for compliance with IPv4. Any address following this format consists of four series of numbers from 0 to 255 separated by dots. Here is an example of a valid IP address: 132.245.4.216

Card numbers must be checked according to the Luhn algorithm. During testing, we will use the following card format: 4000008449433403.

The first six digits are the Issuer Identification Number (IIN). The first digit is the Major Industry Identifier (MII);
The seventh to second-to-last digits indicate the customer account number;
The last digit is the check digit (or checksum). It validates the credit card number using the Luhn algorithm.

#### Objectives

Change the role model:

![UpdatedAuthorizationRole](https://github.com/mdfarhananwar/RESTAnti-FraudSystem/assets/91552730/58bdb08a-9537-430d-9bee-e8fc4007fe4e)

1. **POST /api/antifraud/suspicious-ip**:
   - Saves suspicious IP addresses to the database.
   - It must accept the following JSON body:

 
     ```json
      {
       "ip": "<String value, not empty>"
      }
     ```
   
   - Successful response:
   
     ```json
      {
       "id": "<Long value, not empty>",
       "ip": "<String value, not empty>"
      }
     ```
   
   - Return HTTP Conflict status (409), If an IP is already in the database.

   - Return HTTP Bad Request status (400), If an IP address has the wrong format.
   

2. **DELETE /api/antifraud/suspicious-ip/{ip}**:
   - Deletes IP addresses from the database.
   
   - Successful response:
   
     ```json
     {
       "status": "IP <ip address> successfully removed!"
     }
     ```
   
   - Error response (HTTP Bad Request - 400), If an IP address has the wrong format.
   - Error response (HTTP Not Found - 404), If an IP is not found in the database.


3. **GET /api/antifraud/suspicious-ip**:
   - Shows list of IP addresses in the database.
   -  Returns HTTP OK status (200) and body with an array of JSON objects representing IP address sorted by ID in ascending order (or an empty array if the database is empty).

   - The response format:

     ```json
      [
          {
              "id": 1,
              "ip": "192.168.1.1"
          },
           ...
          {
              "id": 100,
              "ip": "192.168.1.254"
          }
      ]
      or
      
      []
     ```
  4. **POST /api/antifraud/stolencard**:

      - Saves stolen card data in the database
      - It must accept the following JSON body:
        ```json
        {
           "number": "<String value, not empty>"
        }
        ```

        - Successful response:
        ```json
        {
         "id": "<Long value, not empty>",
         "number": "<String value, not empty>"
        }
        ```

       - Error response (HTTP Bad Request - 400), If a card number has the wrong format.
       - Error response (HTTP Conflict - 409), If the card number is already in the database.


  5. **DELETE /api/antifraud/stolencard/{number}**:

      - Deletes a card number from the database
  
        - Successful response:
        ```json
        {
           "status": "Card <number> successfully removed!"
        }
        ```

       - Error response (HTTP Bad Request - 400), If a card number has the wrong format.
       - Error response (HTTP Conflict - 404), If the card number is not found in the database.
Add the  endpoint that. The endpoint must respond with the HTTP OK status (200) and a body with an array of JSON objects

  6. **GET /api/antifraud/stolencard**:

      -  Shows list of card numbers stored in the database
  
      -  Returns HTTP OK status (200) and body with an array of JSON objects  that display the card numbers sorted by ID in ascending order (or an empty array if the database is empty).

      - The response format:

       ```json
        [
            {
                "id": 1,
                "number": "4000008449433403"
            },
             ...
            {
                "id": 100,
                "number": "4000009455296122"
            }
        ]
       ```

  7. **POST /api/antifraud/transaction**:

      -  Change the request body. Now it accepts ip address and card number :
         {
            "amount": <Long>,
            "ip": "<String value, not empty>",
            "number": "<String value, not empty>"
         }
       - Implement New Rules :
         If an IP address is in the blacklist, the transaction is PROHIBITED;
         If a card number is in the blacklist, the transaction is PROHIBITED.
  
        - Successful response:
        ```json
         {
          "result": <String>,
          "info": <String>
         }
        ```
        
        - If the result is ALLOWED, the info field must be set to none (a string value).

        - In the case of PROHIBITED or MANUAL_PROCESSING, the info field must contain the reason for rejecting the transaction; the reason must be separated by , and sorted alphabetically. For example, amount, card-            number, ip.

        - If a request contains wrong data, an IP address and a card number must be validated as described in the Description section, the endpoint should respond with the status HTTP Bad Request (400).
  
  Here are some example scenarios and their corresponding responses:
            
**Example 1: Add Suspicious IP**:
- **Request body (POST /api/antifraud/suspicious-ip)**:
```json
{
   "ip": "192.168.1.1"
}
```

- **Response(200 OK)**:
```json
{
   "id": 1,
   "ip": "192.168.1.1"
}
```

**Example 2: Add Stolen Card**:
- **Request body (POST /api/antifraud/stolencard)**:
```json
{
   "number": "4000008449433403"
}
```

-**Response (200 OK)**:
```json
{
   "id": 1,
   "number": "4000008449433403"
}
```

**Example 3: Transaction with Prohibited IP**
- **Request body (POST /api/antifraud/transaction)**:
```json
{
   "amount": 800,
   "ip": "192.168.1.1",
   "number": "4000008449433403"
}
```
- **Response (200 OK)**:
```json
{
   "result": "PROHIBITED",
   "info": "amount, ip"
}
```

**Example 4: Transaction with Stolen Card**:
- **Request body (POST /api/antifraud/transaction)**:
```json
{
   "amount": 150,
   "ip": "192.168.1.1",
   "number": "4000008449433403"
}
```

- **Response (200 OK)**:
```json
{
   "result": "PROHIBITED",
   "info": "amount, card-number"
}
```


### Stage 5/6: Rule-based system

#### Description

In this stage, you need to implement transaction history, enrich transaction data with region and date, and update the rules for reviewing transactions based on historical data.
 There is the table for world region codes:
 
![Region](https://github.com/mdfarhananwar/RESTAnti-FraudSystem/assets/91552730/ca14a44b-380c-4002-b23e-dc26aeb44d22)

#### Objectives

1. **Implement Transaction History**:
   - Database Setup: Create a database table to store transaction history. This table should include columns for transaction_id, amount, ip, number, region, date, result, and feedback. Ensure that it can store all       transactions, even the PROHIBITED ones.


2. **Update Transaction Endpoint**:
   - Modify the POST /api/antifraud/transaction endpoint to accept the following JSON format:
   - Modified request body:
     ```json
      {
        "amount": <Long>,
        "ip": "<String value, not empty>",
        "number": "<String value, not empty>",
        "region": "<String value, not empty>",
        "date": "yyyy-MM-ddTHH:mm:ss"
      }
     ```
    - Transaction Validation Rules: Update the rules for reviewing a transaction based on historical data. A transaction containing a card number is PROHIBITED if:

       - There are transactions from more than 2 regions of the world other than the region of the transaction that is being verified in the last hour in the transaction history.
       - There are transactions from more than 2 unique IP addresses other than the IP of the transaction that is being verified in the last hour in the transaction history.
       - A transaction containing a card number is sent for MANUAL_PROCESSING if:
          - There are transactions from 2 regions of the world other than the region of the transaction that is being verified in the last hour in the transaction history.
          - There are transactions from 2 unique IP addresses other than the IP of the transaction that is being verified in the last hour in the transaction history.
    - Successful response:
        ```json
          {
            "result": "<String>",
            "info": "<String>"
          }  
        ```
    - If the result is ALLOWED, the info field must be set to "none."

    - In the case of the PROHIBITED or MANUAL_PROCESSING result, the info field must contain the reason for rejecting the transaction, separated by a comma and sorted alphabetically (e.g., "amount, card-number, ip,        ip-correlation, region-correlation").

    - Request Validation: If a request contains wrong data, validate the region and date as described above. If they are invalid, respond with HTTP Bad Request (400).

Here are some example scenarios and their corresponding responses:

**Example 1: Valid Transaction**

- **Request (POST /api/antifraud/transaction)**:
```json
{
  "amount": 150,
  "ip": "192.168.1.1",
  "number": "4000008449433403",
  "region": "EAP",
  "date": "2022-01-22T16:04:00"
}
```

- **Response**:
```json
{
  "result": "ALLOWED",
  "info": "none"
}
```

**Example 2: Wrong Date Format**

- **Request (POST /api/antifraud/transaction)**:
```json
{
  "amount": 150,
  "ip": "192.168.1.1",
  "number": "4000008449433403",
  "region": "EAP",
  "date": "2022-13-22T16:04:00"
}
```

- **Response (400 BAD REQUEST)**:

**Example 3: Wrong Region**

- **Request (POST /api/antifraud/transaction)**:
```json
{
  "amount": 150,
  "ip": "192.168.1.1",
  "number": "4000008449433403",
  "region": "EAPP",
  "date": "2022-01-22T16:04:00"
}
```

- **Response (400 BAD REQUEST)**:
  

**Example 4: Transaction Chain**

- **Request 1 (POST /api/antifraud/transaction)**:
```json
{
  "amount": 150,
  "ip": "192.168.1.1",
  "number": "4000008449433403",
  "region": "EAP",
  "date": "2022-12-22T16:04:00"
}
```

- **Response 1**:
```json
{
  "result": "ALLOWED",
  "info": "none"
}
```
- **Request 2 (POST /api/antifraud/transaction)**:
```json
{
  "amount": 150,
  "ip": "192.168.1.2",
  "number": "4000008449433403",
  "region": "EAP",
  "date": "2022-12-22T16:05:00"
}
```

- **Response 2**:
```json
{
  "result": "ALLOWED",
  "info": "none"
}
```

- **Request 3 (POST /api/antifraud/transaction)**:

```json
{
  "amount": 150,
  "ip": "192.168.1.2",
  "number": "4000008449433403",
  "region": "EAP",
  "date": "2022-12-22T16:06:00"
}
```

- **Response 3 (IP has not changed)**:
```json
{
  "result": "ALLOWED",
  "info": "none"
}
```

- **Request 4 (POST /api/antifraud/transaction)**:
```json
{
  "amount": 150,
  "ip": "192.168.1.3",
  "number": "4000008449433403",
  "region": "EAP",
  "date": "2022-12-22T16:07:00"
}
```

- **Response 4**:
```json
{
  "result": "MANUAL_PROCESSING",
  "info": "ip-correlation"
}
```

- **Request 5 (POST /api/antifraud/transaction)**:
```json
{
  "amount": 150,
  "ip": "192.168.1.4",
  "number": "4000008449433403",
  "region": "EAP",
  "date": "2022-12-22T16:08:00"
}
```

- **Response 5**:
```json
{
  "result": "PROHIBITED",
  "info": "ip-correlation"
}
```

### Stage 6/6: Feedback

#### Description

In this stage, you need to implement a feedback system for completed transactions and update transaction validation limits based on feedback.
 Feedback will be carried out manually by a SUPPORT specialist for completed transactions. Based on the feedback results, we will change the limits of fraud detection algorithms following the special rules. Take a look at the table below that shows the logic of our feedback system:

![FeedbackSystem](https://github.com/mdfarhananwar/RESTAnti-FraudSystem/assets/91552730/05999e22-f1b7-4f6d-9132-01b9c71e70fe)



#### Objectives

1. **Update Role Model**:
   - Role Model Update: Update the role model to include a new role, "SUPPORT," which can provide feedback on transactions.

2. **Implement Feedback System**:
     - Feedback Endpoint: Add a new PUT method to the /api/antifraud/transaction endpoint that allows users with the SUPPORT role to provide feedback for a transaction. The endpoint should accept the following JSON         body:
        ```json
        {
           "transactionId": <Long>,
           "feedback": "<String>"
        }
        ```
    - Feedback Logic: Based on the feedback provided, update the limits of transaction validation following the table provided in the project description.

    - Use the formula for increasing the limit:
      ```java
        new_limit = ceil(0.8 * current_limit + 0.2 * value_from_transaction)
      ```
      
    - Use the formula for decreasing the limit:
      ```java
        new_limit = ceil(0.8 * current_limit - 0.2 * value_from_transaction)
      ```
    - Ensure that the new limit is rounded up to the nearest integer (use ceil).

    - Response: If the feedback is provided successfully, update the limits, and respond with HTTP OK (200). Include the transaction details and the updated limits in the response body.

    - Limitations: Consider the limitations for feedback:

    - Only one feedback is allowed for each transaction.
    - Feedback can be provided only by users with the SUPPORT role.
    - Handle cases where feedback has the wrong format (other than ALLOWED, MANUAL_PROCESSING, PROHIBITED), and respond with HTTP Bad Request (400).
    - Handling Exceptions: If the feedback throws an Exception following the table provided, respond with HTTP Unprocessable Entity (422).

    - Transaction Not Found: If the transaction is not found in history, respond with HTTP Not Found (404).

3. **Transaction History Endpoints**:

     - Add a new endpoint GET /api/antifraud/history that shows the transaction history. Respond with HTTP OK (200) and an array of JSON objects representing transactions sorted by ID in ascending order. If the history is empty, return an empty array.

     - Add another endpoint GET /api/antifraud/history/{number} that shows the transaction history for a specified card number. If transactions for a specified card number are found, respond with HTTP OK (200) and the transaction details in an array. If transactions are not found, respond with HTTP Not Found (404).

     - Validate the card number format, and if it doesn't follow the correct format, respond with HTTP Bad Request (400).


## Examples

Here are some example scenarios and their corresponding responses:

**Example 1: Providing Feedback**

- **Request**:
```json
{
   "transactionId": 1,
   "feedback": "ALLOWED"
}
```

- **Response**:
```json
{
  "transactionId": 1,
  "amount": 210,
  "ip": "192.168.1.1",
  "number": "4000008449433403",
  "region": "EAP",
  "date": "2022-01-22T16:04:00",
  "result": "MANUAL_PROCESSING",
  "feedback": "ALLOWED"
}
```

**Example 2: Getting Transaction History**

- **Request (GET /api/antifraud/history/4000008449433403)**:

- **Response**:
```json
[
  {
  "transactionId": 1,
  "amount": 210,
  "ip": "192.168.1.1",
  "number": "4000008449433403",
  "region": "EAP",
  "date": "2022-01-22T16:04:00",
  "result": "MANUAL_PROCESSING",
  "feedback": "ALLOWED"
  }
]
```

**Example 3: Handling Invalid Feedback**

- **Request**:
```json
{
   "transactionId": 2,
   "feedback": "MAY BE OK"
}
```

- **Response (400 BAD REQUEST)**:

**Example 4: Handling Duplicate Feedback**

- **Request**:
```json
{
   "transactionId": 1,
   "feedback": "PROHIBITED"
}
```

**Response (409 CONFLICT)**:


**Example 5: Handling Card Number Format**

- **Request (GET /api/antifraud/history/4000008449433402)**:

- **Response (400 BAD REQUEST)**:

**Example 6: Handling Not Found**

- **Request (GET /api/antifraud/history/4000009455296122)**:

- **Response (404 NOT FOUND)**:




## Getting Started

To get started with this project, follow the instructions provided in each stage's documentation. Ensure you have the necessary prerequisites, such as Java, Spring Boot, and any required libraries.

