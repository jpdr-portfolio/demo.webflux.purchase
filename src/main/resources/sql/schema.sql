--DROP TABLE IF EXISTS purchase;

CREATE TABLE IF NOT EXISTS purchase (
  id bigint AUTO_INCREMENT primary key,
  creation_date TIMESTAMP WITH TIME ZONE NOT NULL,
  description VARCHAR(50) NOT NULL,
  user_id int not null,
  user_email VARCHAR(254) NULL,
  user_address VARCHAR(200) NOT NULL,
  quantity int not null,
  unit_price NUMERIC(20,2) NOT NULL,
  total_price NUMERIC(20,2) NOT NULL,
  status varchar(1) not null,
  account_id int not null,
  account_number UUID NOT NULL,
  product_id int not null,
  product_name varchar(200),
  retailer_id int not null,
  retailer_name varchar(200),
  cancellation_date TIMESTAMP WITH TIME ZONE NULL
);