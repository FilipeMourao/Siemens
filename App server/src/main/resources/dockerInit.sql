CREATE DATABASE IF NOT EXISTS ios18siemensmobile;

CREATE TABLE ios18siemensmobile.component
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

INSERT INTO ios18siemensmobile.component (`article_number`,`weight`,`width`,`price`,`power_loss`,`num_dinputs`,num_doutputs,`num_ainputs`,`num_aoutputs`,`voltage`,`delivery_time`,`cad_model_link`) VALUES ('6ED1052-1CC08-0BA0',0,246,71.5,133,13,8,4,4,24,10,null);
INSERT INTO ios18siemensmobile.component (`article_number`,`weight`,`width`,`price`,`power_loss`,`num_dinputs`,num_doutputs,`num_ainputs`,`num_aoutputs`,`voltage`,`delivery_time`,`cad_model_link`) VALUES ('6ED1052-1FB08-0BA0',0,239,71.5,151,11,8,4,0,115,7,null);
INSERT INTO ios18siemensmobile.component (`article_number`,`weight`,`width`,`price`,`power_loss`,`num_dinputs`,num_doutputs,`num_ainputs`,`num_aoutputs`,`voltage`,`delivery_time`,`cad_model_link`) VALUES ('6ED1052-1HB08-0BA0',0,29,71.5,147,10,8,4,0,24,2,null);
INSERT INTO ios18siemensmobile.component (`article_number`,`weight`,`width`,`price`,`power_loss`,`num_dinputs`,num_doutputs,`num_ainputs`,`num_aoutputs`,`voltage`,`delivery_time`,`cad_model_link`) VALUES ('6ED1052-1MD08-0BA0',0,296,71.5,147,9,8,4,4,24,19,null);
INSERT INTO ios18siemensmobile.component (`article_number`,`weight`,`width`,`price`,`power_loss`,`num_dinputs`,num_doutputs,`num_ainputs`,`num_aoutputs`,`voltage`,`delivery_time`,`cad_model_link`) VALUES ('6ED1052-2CC08-0BA0',0,211,71.5,113,30,4,4,4,24,4,null);
INSERT INTO ios18siemensmobile.component (`article_number`,`weight`,`width`,`price`,`power_loss`,`num_dinputs`,num_doutputs,`num_ainputs`,`num_aoutputs`,`voltage`,`delivery_time`,`cad_model_link`) VALUES ('6ED1052-2FB08-0BA0',0,254,71.5,121,20,8,4,0,115,4,null);
INSERT INTO ios18siemensmobile.component (`article_number`,`weight`,`width`,`price`,`power_loss`,`num_dinputs`,num_doutputs,`num_ainputs`,`num_aoutputs`,`voltage`,`delivery_time`,`cad_model_link`) VALUES ('6ED1052-2HB08-0BA0',0,254,71.5,117,25,8,4,0,24,0,null);
INSERT INTO ios18siemensmobile.component (`article_number`,`weight`,`width`,`price`,`power_loss`,`num_dinputs`,num_doutputs,`num_ainputs`,`num_aoutputs`,`voltage`,`delivery_time`,`cad_model_link`) VALUES ('6ED1052-2MD08-0BA0',0,25,71.5,117,24,8,4,4,24,10,null);
INSERT INTO ios18siemensmobile.component (`article_number`,`weight`,`width`,`price`,`power_loss`,`num_dinputs`,num_doutputs,`num_ainputs`,`num_aoutputs`,`voltage`,`delivery_time`,`cad_model_link`) VALUES ('6ED1055-1CB00-0BA2',0,136,35.5,70,5,4,4,0,24,16,null);
INSERT INTO ios18siemensmobile.component (`article_number`,`weight`,`width`,`price`,`power_loss`,`num_dinputs`,num_doutputs,`num_ainputs`,`num_aoutputs`,`voltage`,`delivery_time`,`cad_model_link`) VALUES ('6ED1055-1CB10-0BA2',0,234,71.5,115,25,8,8,0,24,8,null);
INSERT INTO ios18siemensmobile.component (`article_number`,`weight`,`width`,`price`,`power_loss`,`num_dinputs`,num_doutputs,`num_ainputs`,`num_aoutputs`,`voltage`,`delivery_time`,`cad_model_link`) VALUES ('6ED1055-1FB00-0BA2',0,172,35.5,76,5,4,4,0,115,3,null);
INSERT INTO ios18siemensmobile.component (`article_number`,`weight`,`width`,`price`,`power_loss`,`num_dinputs`,num_doutputs,`num_ainputs`,`num_aoutputs`,`voltage`,`delivery_time`,`cad_model_link`) VALUES ('6ED1055-1FB10-0BA2',0,301,71.5,124,21,8,8,0,115,4,null);
INSERT INTO ios18siemensmobile.component (`article_number`,`weight`,`width`,`price`,`power_loss`,`num_dinputs`,num_doutputs,`num_ainputs`,`num_aoutputs`,`voltage`,`delivery_time`,`cad_model_link`) VALUES ('6ED1055-1HB00-0BA2',0,172,35.5,76,29,4,4,0,24,9,null);
INSERT INTO ios18siemensmobile.component (`article_number`,`weight`,`width`,`price`,`power_loss`,`num_dinputs`,num_doutputs,`num_ainputs`,`num_aoutputs`,`voltage`,`delivery_time`,`cad_model_link`) VALUES ('6ED1055-1MA00-0BA2',0,137,35.5,93,30,0,0,2,24,1,null);
INSERT INTO ios18siemensmobile.component (`article_number`,`weight`,`width`,`price`,`power_loss`,`num_dinputs`,num_doutputs,`num_ainputs`,`num_aoutputs`,`voltage`,`delivery_time`,`cad_model_link`) VALUES ('6ED1055-1MB00-0BA2',0,172,35.5,76,4,4,4,0,24,12,null);
INSERT INTO ios18siemensmobile.component (`article_number`,`weight`,`width`,`price`,`power_loss`,`num_dinputs`,num_doutputs,`num_ainputs`,`num_aoutputs`,`voltage`,`delivery_time`,`cad_model_link`) VALUES ('6ED1055-1MD00-0BA2',0,136,35.5,113,9,0,0,2,24,2,null);
INSERT INTO ios18siemensmobile.component (`article_number`,`weight`,`width`,`price`,`power_loss`,`num_dinputs`,num_doutputs,`num_ainputs`,`num_aoutputs`,`voltage`,`delivery_time`,`cad_model_link`) VALUES ('6ED1055-1MM00-0BA2',0,137,35.5,133,24,0,0,0,24,20,null);
INSERT INTO ios18siemensmobile.component (`article_number`,`weight`,`width`,`price`,`power_loss`,`num_dinputs`,num_doutputs,`num_ainputs`,`num_aoutputs`,`voltage`,`delivery_time`,`cad_model_link`) VALUES ('6ED1055-1NB10-0BA2',0,299,71.5,125,14,8,8,0,24,4,null);
