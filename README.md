
# Tain wallet technical interview

The application is a wallet which allows players to deposit, withdraw and place bets. Additionally 
transaction can be retrieved and bets can be marked as winning.




## Documentation
**Transaction Types:**

Transaction types allow for different type of channels to deposit, withdraw or place bets.  
By default it has 4 Transaction Types:

  Code | Name | Type 
--- | --- | --- 
CGPAY | Google Pay Deposit | DEBIT 
DGPAY | Google Pay Withdraw | CREDIT
CBET | Win Bet| CREDIT
DBET | Place Bet | DEBIT 

CGPAY should be included in the request to deposit. This allows the system to know which channel to use for deposit. In future if Apple pay is integrated we can add it into the transaction type table. If the business only allow for deposit by apple pay we will only insert only type CREDIT.

DGPAY should be included in the request to withdraw. 

DBET ishould be included in the request to place bet. In future we could use a different betting channel for example if the Bet is done by a third party the channel can be CQBET.

CBET is the channel used for winning bets.

The use of transaction types will allow flexibility when new deposit or withdrawl methods are added.


**Charges:**

Charges work together with transaction types, charges are applied to transaction types and wallets. The below table explains the fields:  


  Field | Example Value | Explaination 
--- | --- | --- 
wallet_type | PLAYER_BONUS_WALLET | charge applies to only this wallet type 
fixed_charge | 10 | fixed charge of 10 eur will be applied
min_amount | 20 | charge is only applied if amount is more than 20
max_amount | 100 | charge is only applied if amount is less than 10 
percent_charge | 10 | instead of fixed fee a percentage of the amount is charged
min_charge | 5 | if the percentage of amount is less than 5 then 5 will be charged
max_charge | 100 | if the percentage of amount is more than 100 then 100 will be charged
transaction_type | CGPAY | if the transaction type is CGPAY apply charge
type | CREDIT | if the charge is applied, it will credit the PLAYER_BONUS_WALLET


The use of this is important so that the bonus deposit can be applied to PLAYER_BONUS_WALLET. It also allows the flexible to have a debit charge when money is deposited or withdrawn with a specific channel.







## Deployment

To deploy this project run

1. installed PostgreSQL 8.x
2. update src/main/resources/application.properties with databse credentials
```bash
spring.datasource.url= jdbc:postgresql://<ip-address>:<port>/<database-name>
spring.datasource.username= <username>
spring.datasource.password= <password>
```
3. in order to build the jar you should run the following command in the root directory of the project
```bash
mvn clean install
```
4. the built jar can be found in the target folder
5. in the repository i have included Wallet Collection.postman_collection.json for api testing


## Improvements 

- Add request validations
- Use data.sql for unit testing
- Add spring security
- Add Idempotency
