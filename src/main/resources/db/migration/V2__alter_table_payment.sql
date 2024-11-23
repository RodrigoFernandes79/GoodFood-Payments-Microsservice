
ALTER TABLE payments MODIFY COLUMN card_number varchar(19) DEFAULT NULL;
ALTER TABLE payments MODIFY COLUMN expiration varchar(7) DEFAULT NULL;
ALTER TABLE payments MODIFY COLUMN code varchar(3) DEFAULT NULL;
