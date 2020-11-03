# Bank Application Demo
Small test application to implement some banking features using Java, Spring, JUnit and H2 Database

This application has 2 controllers :
 - AccountController 
   - contains endpoints to get all accounts, a specific account
   - it allows you to create a account, to update it status or it accountValue and to delete it
   - contains endpoints to display all transaction for a specific account and some alternative methods that are filtering the query(all trasaction sincer a specified date(as parameter), today, yesterday or from the lastX hours where X is parametrizable)
 - TransactionController
    - contains endpoint to get all transactions or a specific transaction
    - it also has methods to create a transaction and to update the account Value for both people and a method to delete a specific transaction
    
Endpoints for Account:
  - /accounts - retrieve all accounts
  - /accounts/{id} - retrieve a specific account by id
  - /account - creates an account by sending in the body of the request and Account object in JSON format. I will provide an example of account json below 
  - /accounts/status/{id} - updates the status of a specific account
  - /accounts/delete/{id}
  - /accounts/{id}/transactions - get all the transactions for a specific account that is identified by id
  - /accounts/{id}/transactions/{desiredDate} - returns all transaction for a specific account identified by id that were processed after a desiredDate
  - /accounts/{id}/transactions/today - returns all the transaction for a specific account from today
  - /accounts/{id}/transactions/yesterday - returns all the transaction for a specific account from yesterday
  - /accounts/{id}/transactions/last{hours}h -returns all the transactions for a specific account that were done in the last X hours - where X is the parameter
  
Endpoints for Transaction:
  - /transactions - get all the transactions
  - /transactions/{id} - get a specific trasaction
  - /transaction - creates a transaction and updates the accounts Value for both people
  - /transactions/delete/{id} - deletes a specific transaction

Account JSON model:  
```json
{  
    "accountId": "1",  
    "accountCurrency": "RON",  
    "accountNumber": "1234ABC",  
    "accountValue": "100",
    "accountStatus": "OPEN"
} 
```

Transaction JSON model:
```json
{
  "transactionId": "1",
  "transactionValue": 1,
  "transactionDate": "2020-11-02T21:34:55",
  "transactionSender": "1234",
  "transactionReceiver": "5678",
  "transactionCurrency": "RON",
  "account": {  
    "accountId": "1",  
    "accountCurrency": "RON",  
    "accountNumber": "1234",  
    "accountValue": 100,
    "accountStatus": "OPEN"
    }  
}
```

This application is also using spring security so I have defined to roles
  - USER - username:user ; password: password
  - ADMIN - username: admin ; password: adminPassword
  
Most endpoints are requiring the role of ADMIN to be accesed


