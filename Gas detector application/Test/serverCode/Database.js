function GasSensorMeasure(ID1, ID2, ID3, sensor1,sensor2,sensor3,thermistor) {
    this.ID1 = ID1;
    this.ID2 = ID2;
    this.ID3 = ID3;
    this.sensor1 = sensor1;
    this.sensor2 = sensor2;
    this.sensor3 = sensor3;
    this.thermistor = thermistor;
}
var measures = [];
var measure1 = new GasSensorMeasure(1, 2, 3, 1, 2, 3, "1");
var measure2 = new GasSensorMeasure(8, 7, 2, 8, 7, 4, "2");
var measure3 = new GasSensorMeasure(9, 0, 4, 9, 6, 5, "3");
measures.push(measure1);
measures.push(measure2);
measures.push(measure3);
exports.measures = measures;
exports.convertMeasure = convertObjectToMeasure;
exports.gasMeasure = GasSensorMeasure;
//measures.push(JSON.parse("{\"ID1\":9,\"ID2\":0,\"ID3\":4,\"sensor1\":9,\"sensor2\":6,\"sensor3\":5,\"thermistor\":\"3\"}"));

function convertObjectToMeasure(jsonObject) {
        if(jsonObject.ID1 === undefined || jsonObject.ID2 === undefined || jsonObject.ID3 === undefined || jsonObject.sensor1 === undefined
        || jsonObject.sensor2 === undefined || jsonObject.sensor3 === undefined || jsonObject.thermistor === undefined || jsonObject.id === undefined) return false;
        else return true;

}