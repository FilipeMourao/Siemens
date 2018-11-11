var http = require('http');
var database = require('./Database');
var express =  require('express');
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

function getAllResults(request, response) {
    response.writeHead(200,{'Content-Type': 'application/json'});
    response.write(JSON.stringify(database.measures));
    response.end();
}
app.get('/getAllResults',getAllResults);

function addValueToDatabase(request, response) {
    response.writeHead(200,{'Content-Type': 'application/json'});
    var parseObject = JSON.parse(JSON.stringify(request.body));
    if (database.convertMeasure(parseObject))  database.measures.push(parseObject);
    response.write(JSON.stringify(request.body));
    response.end();
}
app.post('/addMeasure',addValueToDatabase);

var server = http.createServer(app).listen(8888);
//var server = http.createServer(onRequest).listen(8888);
console.log("Server is now running..." );


var connection = require('./MySQLDatabase');
function test(response) {
    connection.connection.query("SELECT * FROM "+ TABLE_GAS_SENSOR, function (error,rows,fields) {
        if (!!error){
            console.log("Error in the query!");
        } else{
            var text = "";
            for (var i = 0; i < rows.length; i++) {
                text += JSON.stringify(rows[i]);
                text += "\n";
     }
            response.write("Data base is running! Currently values are:\n"+ text );
            response.end();
        }
    })
}
function intialize(request, response) {
    response.writeHead(200,{'Content-Type': 'application/json'});
    var text = "";
    for (var i = 0; i < database.measures.length; i++) {
        text += JSON.stringify(database.measures[i]);
        text += "\n";
    }
    test(response);
}
app.get('/',intialize);
