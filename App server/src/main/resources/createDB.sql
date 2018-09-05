CREATE DATABASE IF NOT EXISTS ios18siemensmobile;

CREATE TABLE IF NOT EXISTS ios18siemensmobile.component
(
    article_number VARCHAR(255) PRIMARY KEY,
    weight double NOT NULL,
    width double NOT NULL,
    price double NOT NULL,
    power_loss double NOT NULL,
    num_dinputs int NOT NULL,
    num_doutputs int NOT NULL,
    num_ainputs int NOT NULL,
    num_aoutputs int NOT NULL,
    voltage int NOT NULL,
    delivery_time int NOT NULL,
    cad_model_link VARCHAR(255) NULL
);