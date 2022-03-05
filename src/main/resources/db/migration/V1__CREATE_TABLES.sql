CREATE SEQUENCE sequence_currency;

CREATE TABLE currency
(
    id     BIGINT      NOT NULL PRIMARY KEY AUTO_INCREMENT,
    name   VARCHAR(30) NOT NULL,
    code   VARCHAR(3)  NOT NULL,
    symbol VARCHAR(5)  NOT NULL
);

INSERT INTO currency(name, code, symbol)
VALUES ('Euro', 'EUR', '€'),
       ('Dollar', 'USD', '$'),
       ('Lats', 'LVL', 'Ls');

CREATE SEQUENCE sequence_customer;

CREATE TABLE customer
(
    id       BIGINT       NOT NULL PRIMARY KEY AUTO_INCREMENT,
    name     VARCHAR(50)  NOT NULL,
    surname  VARCHAR(50)  NOT NULL,
    country  VARCHAR(56)  NOT NULL,
    email    VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(60)  NOT NULL
);

CREATE SEQUENCE sequence_debt;

CREATE TABLE debt
(
    id          BIGINT  NOT NULL PRIMARY KEY AUTO_INCREMENT,
    amount      DECIMAL NOT NULL,
    due_date    DATE    NOT NULL,
    customer_id BIGINT,
    currency_id BIGINT  NOT NULL,
    FOREIGN KEY (customer_id) references customer (id),
    FOREIGN KEY (currency_id) references currency (id)
);






