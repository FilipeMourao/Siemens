ALTER USER 'root'@'localhost' IDENTIFIED WITH mysql_native_password BY 'Solidario1';
drop table if exists GasMeasures;
CREATE TABLE  GasMeasures (
                id   INTEGER AUTO_INCREMENT , 
                 ID1   TEXT,
                 ID2   TEXT,
                 ID3   TEXT,
                 sensor1   TEXT,
                 sensor2   TEXT,
                 sensor3   TEXT, 
                 thermistor   TEXT,
                 PRIMARY KEY(id)
                 );
INSERT INTO  GasMeasures (ID1, ID2, ID3,sensor1, sensor2, sensor3, thermistor)
VALUES ('1','1','1','1','1','1','1');
INSERT INTO  GasMeasures (ID1, ID2, ID3,sensor1, sensor2, sensor3, thermistor)
VALUES ('2','2','2','2','2','2','2');
INSERT INTO  GasMeasures (ID1, ID2, ID3,sensor1, sensor2, sensor3, thermistor)
VALUES ('3','3','3','3','3','3','3');                
                 