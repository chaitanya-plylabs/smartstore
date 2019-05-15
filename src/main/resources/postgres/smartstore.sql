--- AXON Framework JPA Tables ---
CREATE TABLE token_entry(
    processor_name text NOT NULL,
    segment integer NOT NULL,
    token oid,
    token_type text,
    owner text,
    timestamp text NOT NULL,
    PRIMARY KEY (processor_name, segment)
);
---------------------------------

CREATE TABLE product(
    store_id text NOT NULL,
    sku_id text NOT NULL,
    name text NOT NULL,
    category text NOT NULL,
    sub_category text NOT NULL,
    price numeric(10,2) NOT NULL,
    weight numeric(10,2) NOT NULL,
    PRIMARY KEY (store_id, sku_id)
);

CREATE TABLE sale(
    store_id text NOT NULL,
    cart_id text NOT NULL,
    user_id text NOT NULL,
    created_epoch bigint NOT NULL,
    amount numeric(10,2) NOT NULL,
    sale_date date NOT NULL,
    PRIMARY KEY(store_id, cart_id)
);

CREATE TABLE line_item(
    store_id text NOT NULL,
    cart_id text NOT NULL,
    sku_id text NOT NULL,
    quantity integer NOT NULL,
    weight numeric(10,2) NOT NULL,
    price numeric(10,2) NOT NULL,
    PRIMARY KEY(store_id, cart_id, sku_id)
);


CREATE TABLE proximity_configuration(
    config_id serial NOT NULL,
    store_id text NOT NULL,
    beacon_id text NOT NULL,
    sku_id text NOT NULL,
    PRIMARY KEY(config_id)
);

CREATE VIEW sku_sale_view AS
SELECT row_number() over() as row_num, s.store_id AS store_id, li.sku_id AS sku_id, s.sale_date AS date, sum(quantity)::integer AS quantity
FROM sale s INNER JOIN line_item li  ON s.store_id = li.store_id AND  s.cart_id = li.cart_id
GROUP BY li.sku_id, s.store_id, s.sale_date;

CREATE VIEW user_sku_view AS
SELECT row_number() over() as row_num, s.user_id AS user_id, s.sale_date AS date, s.store_id AS store_id, li.sku_id AS sku_id, sum(quantity)::integer AS quantity, sum(quantity*price) AS amount
FROM sale s INNER JOIN line_item li  ON s.store_id = li.store_id AND  s.cart_id = li.cart_id
GROUP BY s.user_id, s.store_id, s.sale_date, li.sku_id;

INSERT INTO product(store_id, sku_id, name, category, sub_category, price, weight ) VALUES ('STORE00001', 'SKU0000001', 'Steam Rice - Kolam', 'Food Grains', 'Rice Products', 60.00, 1000);
INSERT INTO product(store_id, sku_id, name, category, sub_category, price, weight ) VALUES ('STORE00001', 'SKU0000002', 'Poha', 'Food Grains', 'Rice Products', 49.00, 1000);
INSERT INTO product(store_id, sku_id, name, category, sub_category, price, weight ) VALUES ('STORE00001', 'SKU0000003', 'Basmati Rice - Dubar', 'Food Grains', 'Rice Products', 156.50, 1000);
INSERT INTO product(store_id, sku_id, name, category, sub_category, price, weight ) VALUES ('STORE00001', 'SKU0000004', 'Maida', 'Food Grains', 'Atta, Flours & Sooji', 35.00, 1000);
INSERT INTO product(store_id, sku_id, name, category, sub_category, price, weight ) VALUES ('STORE00001', 'SKU0000005', 'Besan', 'Food Grains', 'Atta, Flours & Sooji', 85.00, 1000);
INSERT INTO product(store_id, sku_id, name, category, sub_category, price, weight ) VALUES ('STORE00001', 'SKU0000006', 'Atta - Whole Wheat', 'Food Grains', 'Atta, Flours & Sooji', 33.90, 1000);
INSERT INTO product(store_id, sku_id, name, category, sub_category, price, weight ) VALUES ('STORE00001', 'SKU0000007', 'Atta - Multigrains', 'Food Grains', 'Atta, Flours & Sooji', 38.50, 1000);
INSERT INTO product(store_id, sku_id, name, category, sub_category, price, weight ) VALUES ('STORE00001', 'SKU0000008', 'Milk', 'Bakery, Cakes & Diary', 'Diary', 23.00, 1000);
INSERT INTO product(store_id, sku_id, name, category, sub_category, price, weight ) VALUES ('STORE00001', 'SKU0000009', 'Curd', 'Bakery, Cakes & Diary', 'Diary', 33.36, 500);
INSERT INTO product(store_id, sku_id, name, category, sub_category, price, weight ) VALUES ('STORE00001', 'SKU0000010', 'Cheese', 'Bakery, Cakes & Diary', 'Diary', 129.00, 200);

INSERT INTO product(store_id, sku_id, name, category, sub_category, price, weight ) VALUES ('STORE00002', 'SKU0000001', 'Steam Rice - Kolam', 'Food Grains', 'Rice Products', 60.00, 1000);
INSERT INTO product(store_id, sku_id, name, category, sub_category, price, weight ) VALUES ('STORE00002', 'SKU0000002', 'Poha', 'Food Grains', 'Rice Products', 49.00, 1000);
INSERT INTO product(store_id, sku_id, name, category, sub_category, price, weight ) VALUES ('STORE00002', 'SKU0000003', 'Basmati Rice - Dubar', 'Food Grains', 'Rice Products', 156.50, 1000);
INSERT INTO product(store_id, sku_id, name, category, sub_category, price, weight ) VALUES ('STORE00002', 'SKU0000004', 'Maida', 'Food Grains', 'Atta, Flours & Sooji', 35.00, 1000);
INSERT INTO product(store_id, sku_id, name, category, sub_category, price, weight ) VALUES ('STORE00002', 'SKU0000005', 'Besan', 'Food Grains', 'Atta, Flours & Sooji', 85.00, 1000);
INSERT INTO product(store_id, sku_id, name, category, sub_category, price, weight ) VALUES ('STORE00002', 'SKU0000006', 'Atta - Whole Wheat', 'Food Grains', 'Atta, Flours & Sooji', 33.90, 1000);
INSERT INTO product(store_id, sku_id, name, category, sub_category, price, weight ) VALUES ('STORE00002', 'SKU0000007', 'Atta - Multigrains', 'Food Grains', 'Atta, Flours & Sooji', 38.50, 1000);
INSERT INTO product(store_id, sku_id, name, category, sub_category, price, weight ) VALUES ('STORE00002', 'SKU0000008', 'Milk', 'Bakery, Cakes & Diary', 'Diary', 23.00, 1000);
INSERT INTO product(store_id, sku_id, name, category, sub_category, price, weight ) VALUES ('STORE00002', 'SKU0000009', 'Curd', 'Bakery, Cakes & Diary', 'Diary', 33.36, 500);
INSERT INTO product(store_id, sku_id, name, category, sub_category, price, weight ) VALUES ('STORE00002', 'SKU0000010', 'Cheese', 'Bakery, Cakes & Diary', 'Diary', 129.00, 200);


INSERT INTO proximity_configuration (store_id, beacon_id, sku_id) SELECT 'STORE00001', 'BEACON0001', 'SKU0000001';
INSERT INTO proximity_configuration (store_id, beacon_id, sku_id) SELECT 'STORE00001', 'BEACON0001', 'SKU0000002';
INSERT INTO proximity_configuration (store_id, beacon_id, sku_id) SELECT 'STORE00001', 'BEACON0001', 'SKU0000003';
INSERT INTO proximity_configuration (store_id, beacon_id, sku_id) SELECT 'STORE00001', 'BEACON0002', 'SKU0000004';
INSERT INTO proximity_configuration (store_id, beacon_id, sku_id) SELECT 'STORE00001', 'BEACON0002', 'SKU0000005';
INSERT INTO proximity_configuration (store_id, beacon_id, sku_id) SELECT 'STORE00001', 'BEACON0002', 'SKU0000006';
INSERT INTO proximity_configuration (store_id, beacon_id, sku_id) SELECT 'STORE00001', 'BEACON0003', 'SKU0000007';
INSERT INTO proximity_configuration (store_id, beacon_id, sku_id) SELECT 'STORE00001', 'BEACON0003', 'SKU0000008';
INSERT INTO proximity_configuration (store_id, beacon_id, sku_id) SELECT 'STORE00001', 'BEACON0003', 'SKU0000009';
INSERT INTO proximity_configuration (store_id, beacon_id, sku_id) SELECT 'STORE00001', 'BEACON0003', 'SKU0000010';

INSERT INTO proximity_configuration (store_id, beacon_id, sku_id) SELECT 'STORE00002', 'BEACON0001', 'SKU0000001';
INSERT INTO proximity_configuration (store_id, beacon_id, sku_id) SELECT 'STORE00002', 'BEACON0001', 'SKU0000002';
INSERT INTO proximity_configuration (store_id, beacon_id, sku_id) SELECT 'STORE00002', 'BEACON0001', 'SKU0000003';
INSERT INTO proximity_configuration (store_id, beacon_id, sku_id) SELECT 'STORE00002', 'BEACON0002', 'SKU0000004';
INSERT INTO proximity_configuration (store_id, beacon_id, sku_id) SELECT 'STORE00002', 'BEACON0002', 'SKU0000005';
INSERT INTO proximity_configuration (store_id, beacon_id, sku_id) SELECT 'STORE00002', 'BEACON0002', 'SKU0000006';
INSERT INTO proximity_configuration (store_id, beacon_id, sku_id) SELECT 'STORE00002', 'BEACON0003', 'SKU0000007';
INSERT INTO proximity_configuration (store_id, beacon_id, sku_id) SELECT 'STORE00002', 'BEACON0003', 'SKU0000008';
INSERT INTO proximity_configuration (store_id, beacon_id, sku_id) SELECT 'STORE00002', 'BEACON0003', 'SKU0000009';
INSERT INTO proximity_configuration (store_id, beacon_id, sku_id) SELECT 'STORE00002', 'BEACON0003', 'SKU0000010';

