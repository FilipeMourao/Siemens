 TABLE_GAS_SENSOR  = "GasMeasures";
 DATABASE  = "GasMeasurementsDatabase";
var mysql = require('mysql');
var connection = mysql.createConnection({
    host:'localhost',
    user:'root',
    password:'Solidario1',
    database: DATABASE
});
connection.connect(function (error) {
    if (!!error){
        console.log("Error!");
    } else {
        console.log('Connected');
    }
})
 function getAllValuesFromDatabase() {
    connection.query("SELECT * FROM "+ TABLE_GAS_SENSOR, function (error,rows,fields) {
           if (!!error){
            console.log("Error in the query!");
        } else {
            //console.log('Sucessful query');
            console.log(rows)
        }
    })
 }
//console.log(data);
exports.connection = connection;