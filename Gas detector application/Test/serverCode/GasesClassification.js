 database = require('./Database');
 alcoholDrinkGas = new database.gasMeasure(100,100,100,100,100,300,'test');
 fartGas = new database.gasMeasure(100,100,100,100,100,300,'test');
 trashGas = new database.gasMeasure(100,100,100,100,100,300,'test');
 normalAirGas = new database.gasMeasure(100,100,100,100,100,300,'test');
 availableGases = [
    "Alcohol Drink",
    "Fart",
    "Trash",
    "Normal Air"
];
 availableGasesData = [
    alcoholDrinkGas,
    fartGas,
    trashGas,
    normalAirGas
];

function classifyGas(element) {
    distanceArray = [];
    availableGasesData.forEach(function (value) {
        distanceArray.push(
            calculateDistanceBetween2Points(Number(element.sensor1),Number(element.sensor2),Number(element.sensor1),
                value.sensor1,value.sensor2,value.sensor3)
        )
    }
    )
    return availableGases[indexOfMin(distanceArray)];
}


exports.gasClassifier = classifyGas;

function calculateDistanceBetween2Points(x1,y1,z1,x2,y2,z2) {
    partialResult = Math.pow(x1-x2,2) + Math.pow(y1-y2,2) + Math.pow(z1-z2,2);
    return Math.sqrt(partialResult);

}
function indexOfMin(arr) {
    if (arr.length === 0) {
        return -1;
    }

    var min = arr[0];
    var minIndex = 0;

    for (var i = 1; i < arr.length; i++) {
        if (arr[i] < min) {
            minIndex = i;
            min = arr[i];
        }
    }

    return minIndex;
}