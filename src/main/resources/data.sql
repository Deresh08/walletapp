--Create deposit transaction_type
INSERT INTO transaction_type (id,code,create_date,description,modify_date,name,"type") 
VALUES (10000,'CGPAY','2022-02-03 11:31:38.021','Deposit Google Pay','2022-02-03 11:31:38.021','Google Pay','CREDIT')
ON CONFLICT (id) DO NOTHING;
----Create deposit bonus
INSERT INTO charge (id,wallet_type,fixed_charge,max_amount,max_charge,min_amount,min_charge,"name",percent_charge,"type",transaction_type) 
VALUES (100001,'PLAYER_BONUS_WALLET',0.00,1000000.00,1000000.00,0.00,0.00,'Deposit Bonus',100.00,'CREDIT',(select ID from transaction_type tt where tt.code ='CGPAY' and "type" = 'CREDIT'))
ON CONFLICT (id) DO NOTHING;;
----Create withdrawl transaction_type
INSERT INTO transaction_type (id,code,create_date,description,modify_date,name,"type") 
VALUES (100002,'DGPAY','2022-02-03 11:31:38.021','Withdraw Google Pay','2022-02-03 11:31:38.021','Google Pay','DEBIT')
ON CONFLICT (id) DO NOTHING;;
----Create credit bet transaction_type
INSERT INTO transaction_type (id,code,create_date,description,modify_date,name,"type") 
VALUES (100003,'CBET','2022-02-03 11:31:38.021','Win Bet','2022-02-03 11:31:38.021','Win Bet','CREDIT')
ON CONFLICT (id) DO NOTHING;;
----Create debit bet transaction_type
INSERT INTO transaction_type (id,code,create_date,description,modify_date,name,"type") 
VALUES (100004,'DBET','2022-02-03 11:31:38.021','Placed Bet','2022-02-03 11:31:38.021','Placed Bet','DEBIT')
ON CONFLICT (id) DO NOTHING;;
----Create user
INSERT INTO wallet_user (user_type,id,create_date,email,modify_date,"password",profile,username) 
VALUES ('Player',100001,'2022-02-03 11:52:21.899','testWalletService@gmail.com','2022-02-03 11:52:21.899','12345Kebab!','PLAYER','JOHN08')
ON CONFLICT (id) DO NOTHING;;
----Create player
INSERT INTO player (address,id_number,last_name,"name",id) 
VALUES ('Hal Gharhur','0129129A','John','Doe',(select id from wallet_user wu where wu.email = 'testWalletService@gmail.com'))
ON CONFLICT (id) DO NOTHING;;
----Create PLAYER_WALLET
INSERT INTO wallet (id, balance, create_date, modify_date, wallet_number, wallet_status, wallet_type, user_id) 
VALUES (100001, 1000.00, '2022-02-03 10:42:39.875', '2022-02-03 10:42:49.613', '30001', 'ACTIVE', 'PLAYER_WALLET', (select id from wallet_user wu where wu.email = 'testWalletService@gmail.com'))
ON CONFLICT (id) DO NOTHING;;
----Create PLAYER_BONUS_WALLET
INSERT INTO wallet (id, balance, create_date, modify_date, wallet_number, wallet_status, wallet_type, user_id) 
VALUES (100002, 1000.00, '2022-02-03 10:42:39.875', '2022-02-03 10:42:49.613', '30002', 'ACTIVE', 'PLAYER_BONUS_WALLET', (select id from wallet_user wu where wu.email = 'testWalletService@gmail.com'))
ON CONFLICT (id) DO NOTHING;;
----Create Transactions
INSERT INTO "transaction" (id,amount,create_date,description,external_id,modify_date,parent_transaction_id,transaction_type,wallet_id) VALUES
(100001,-100.00,'2022-02-03 10:07:36.483','Bet Placed','7e3396be-f1as29-40ea-ab43-e2933810b11e','2022-02-03 10:07:36.483',NULL,(select ID from transaction_type tt where tt.code ='CGPAY' and "type" = 'CREDIT'),(select id from wallet w where wallet_number = '30001')),
(100002,-20.00,'2022-02-03 10:13:27.801','Bet Placed','7e3396be-f12x9-40ea-ab43-e2933810b11e7e3396be-f129-40ea-ab43-e2933810b11e','2022-02-03 10:13:27.801',NULL,(select ID from transaction_type tt where tt.code ='CGPAY' and "type" = 'CREDIT'),(select id from wallet w where wallet_number = '30001')),
(100003,-100.00,'2022-02-03 10:07:36.483','Bet Placed','7e3396be-f1xa29-40ea-ab43-e2933810b11e','2022-02-03 10:07:36.483',NULL,(select ID from transaction_type tt where tt.code ='CGPAY' and "type" = 'CREDIT'),(select id from wallet w where wallet_number = '30001')),
(100004,-100.00,'2022-02-03 10:07:36.483','Bet Placed','7e3396be-f12xs9-40ea-ab43-e2933810b11e','2022-02-03 10:07:36.483',NULL,(select ID from transaction_type tt where tt.code ='CGPAY' and "type" = 'CREDIT'),(select id from wallet w where wallet_number = '30001')),
(100005,-100.00,'2022-02-03 10:07:36.483','Bet Placed','7e3396be-f12gb9-40ea-ab43-e2933810b11e','2022-02-03 10:07:36.483',NULL,(select ID from transaction_type tt where tt.code ='CGPAY' and "type" = 'CREDIT'),(select id from wallet w where wallet_number = '30001')),
(100006,-100.00,'2022-02-03 10:07:36.483','Bet Placed','7e3396be-f12ed9-40ea-ab43-e2933810b11e','2022-02-03 10:07:36.483',NULL,(select ID from transaction_type tt where tt.code ='CGPAY' and "type" = 'CREDIT'),(select id from wallet w where wallet_number = '30001')),
(100007,-100.00,'2022-02-03 10:07:36.483','Bet Placed','7e3396be-f12asd9-40ea-ab43-e2933810b11e','2022-02-03 10:07:36.483',NULL,(select ID from transaction_type tt where tt.code ='CGPAY' and "type" = 'CREDIT'),(select id from wallet w where wallet_number = '30001'))
ON CONFLICT (id) DO NOTHING;