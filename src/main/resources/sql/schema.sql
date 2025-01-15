--DROP TABLE IF EXISTS purchase;
CREATE TABLE IF NOT EXISTS purchase (
  id bigserial primary key,
  purchase_date TIMESTAMP WITH TIME ZONE NOT NULL,
  purchase_description VARCHAR(50) NOT NULL,
  product_quantity int NOT NULL,
  product_unit_price NUMERIC(20,2) NOT NULL,
  product_total_price NUMERIC(20,2) NOT NULL,
  user_id bigint NOT NULL,
  user_email VARCHAR(254) NOT NULL,
  user_address VARCHAR(200) NOT NULL,
  purchase_status varchar(1) NOT NULL,
  account_id bigint NOT NULL,
  account_number UUID NOT NULL,
  product_id bigint NOT NULL,
  product_name varchar(200) NOT NULL,
  purchase_cancellation_date TIMESTAMP WITH TIME ZONE NULL
);