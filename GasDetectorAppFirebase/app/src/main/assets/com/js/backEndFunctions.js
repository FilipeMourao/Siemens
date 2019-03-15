   <!-- JS Hooks -->

$(".downloadButton").click(function() {
        window.JSInterface.downloadData();
   });
$(".uploadButton").click(function() {
        window.JSInterface.updloadData();
   });

    function connectDeviceHook(){

    //window.JSInterface.saveMeasureIntoServer();
  //  window.JSInterface.getSensorPoints();
        window.JSInterface.connectToDevice()
        //     if(window.JSInterface.connectToDevice()) app.deviceConnected();
   //    app.deviceConnected();

    }

    function startAnalyzingHook(){

//        if( window.JSInterface.analyze()) {
//                  setTimeout(function(){
//                  var gas_name = "New measure available!";
//                  $('.result-name').text(gas_name);
//                  app.showresult(getRandomColor());
//
//              }, 10000);
//        }
        window.JSInterface.analyze()
//        var gas_name = "New measure available!";
//        $('.result-name').text(gas_name);




    }


    // just some default values

    var _chart_labels = [

    ];

    var _chart_data = [

    ];

    var _chart_colors = [

    ];



    $(document).ready(function(){

        visual.init();
        app.init();

    });
function getRandomColor() {
  var letters = '0123456789ABCDEF';
  var color = '#';
  for (var i = 0; i < 6; i++) {
    color += letters[Math.floor(Math.random() * 16)];
  }
  return color;
}
