CREATE TABLE payments (
    id bigint(20) NOT NULL AUTO_INCREMENT,
    value decimal(19,2) NOT NULL,
    name varchar(100) NOT NULL,
    card_number varchar(19) NOT NULL,
    expiration varchar(7) NOT NULL,
    code varchar(3) NOT NULL,
    status varchar(100) NOT NULL,
    order_id bigint(20) NOT NULL,
    payment_method_id bigint(20) NOT NULL,

 PRIMARY KEY (id)
);