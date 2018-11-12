TABLE_GAS_SENSOR  = "GasMeasures";
DATABASE  = "GasMeasurementsDatabase";
var http = require('http');
var database = require('./Database');
var express =  require('express');
var connection = require('./MySQLDatabase');
var classifyGas = require('./GasesClassification');
var app  = express();
app.use(express.json());
// function intialize(request, response) {
//     response.writeHead(200,{'Content-Type': 'application/json'});
//     var text = "";
//     for (var i = 0; i < database.measures.length; i++) {
//         text += JSON.stringify(database.measures[i]);
//         text += "\n";
//     }
//     response.write("Data base is running! Currently values are:\n"+ text );
//     response.end();
// }
// app.get('/',intialize);

// function getAllResults(request, response) {
//     response.writeHead(200,{'Content-Type': 'application/json'});
//     response.write(JSON.stringify(database.measures));
//     response.end();
// }
// app.get('/getAllResults',getAllResults);
//
// function addValueToDatabase(request, response) {
//     response.writeHead(200,{'Content-Type': 'application/json'});
//     var parseObject = JSON.parse(JSON.stringify(request.body));
//     if (database.convertMeasure(parseObject))  database.measures.push(parseObject);
//     response.write(JSON.stringify(request.body));
//     response.end();
// }
// app.post('/addMeasure',addValueToDatabase);

var server = http.createServer(app).listen(8888);
console.log("Server is now running..." );
function intialize(request, response) {
    response.writeHead(200,{'Content-Type': 'application/json'});
    connection.connection.query("SELECT * FROM "+ TABLE_GAS_SENSOR, function (error,rows,fields) {
        if (!!error){
            console.log("Error in the query!");
        } else{
            if (rows.length > 0){
                var text = "";
                for (var i = 0; i < rows.length; i++) {
                    text += JSON.stringify(rows[i]);
                    text += "\n";
                }
                response.write("Data base is running! Currently values are:\n"+ text );
                response.end();

            } else {
                response.write("Data base is running! There are no values yet!\n");
                response.end();
            }

        }
    })
}
app.get('/',intialize);

function getAllResults(request, response) {
    response.writeHead(200,{'Content-Type': 'application/json'});
    connection.connection.query("SELECT * FROM "+ TABLE_GAS_SENSOR, function (error,rows,fields) {
        if (!!error){
            console.log("Error in the query!");
        } else {
            if (rows.length > 0){
                response.write(JSON.stringify(rows));
                response.end();
            } else {
                response.write("");
                response.end();
            }
        }
    })
}
app.get('/getAllResults',getAllResults);

function addValueToDatabase(request, response) {
    response.writeHead(200,{'Content-Type': 'application/json'});
    var parseObject = JSON.parse(JSON.stringify(request.body));
    if (database.convertMeasure(parseObject)) {
        var sql = "INSERT INTO " + TABLE_GAS_SENSOR + "(ID1,ID2,ID3, sensor1, sensor2, sensor3,thermistor )" +
            " VALUES (" + parseObject.ID1.toString()+ ","+ parseObject.ID2.toString() + "," + parseObject.ID3.toString() + ","
            + parseObject.sensor1.toString() + "," + parseObject.sensor2.toString() + "," + parseObject.sensor3.toString()
            + ","+ parseObject.thermistor.toString() + ")";
        connection.connection.query(sql, function (err, result) {
            if (err) throw err;
            console.log("1 record inserted");
        });
    }
    response.write(JSON.stringify(request.body));
    response.end();
}
app.post('/addMeasure',addValueToDatabase);


function classifyGases(request, response) {
    response.writeHead(200,{'Content-Type': 'application/json'});
    connection.connection.query("SELECT * FROM "+ TABLE_GAS_SENSOR, function (error,rows,fields) {
        if (!!error){
            console.log("Error in the query!");
        } else {
            if (rows.length > 0){
                var text = [];
                //response.write(JSON.stringify(rows));
                rows.forEach(function (element) {
                    text.push(classifyGas.gasClassifier(element));
                });
                response.write(JSON.stringify(text));
                response.end();
            } else {
                response.write("");
                response.end();
            }
        }
    })
}
app.get('/classifyGases',classifyGases);


//
//
// ALTER USER 'root'@'localhost' IDENTIFIED WITH mysql_native_password BY 'Solidario1';
// drop table if exists GasMeasures;
// CREATE TABLE  GasMeasures (
//                id   INTEGER AUTO_INCREMENT ,
//                 ID1   TEXT,
//                 ID2   TEXT,
//                 ID3   TEXT,
//                 sensor1   TEXT,
//                 sensor2   TEXT,
//                 sensor3   TEXT,
//                 thermistor   TEXT,
//                 PRIMARY KEY(id)
//                 );
// INSERT INTO  GasMeasures (ID1, ID2, ID3,sensor1, sensor2, sensor3, thermistor)
// VALUES ('1','1','1','1','1','1','1');
// INSERT INTO  GasMeasures (ID1, ID2, ID3,sensor1, sensor2, sensor3, thermistor)
// VALUES ('2','2','2','2','2','2','2');
// INSERT INTO  GasMeasures (ID1, ID2, ID3,sensor1, sensor2, sensor3, thermistor)
// VALUES ('3','3','3','3','3','3','3');
