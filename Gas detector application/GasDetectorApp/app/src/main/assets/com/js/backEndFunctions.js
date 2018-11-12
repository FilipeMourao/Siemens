   <!-- JS Hooks -->

    function connectDeviceHook(){

    //window.JSInterface.saveMeasureIntoServer();
  //  window.JSInterface.getSensorPoints();
     if(window.JSInterface.connectToDevice()) app.deviceConnected();
   //    app.deviceConnected();

    }

    function startAnalyzingHook(){

        // start analyzing code here
       // var analyse = window.JSInterface.analyze();
        if( window.JSInterface.analyze()) {
     //  if( true) {
//                setTimeout(function(){
//
//                }, 12000);
              setTimeout(function(){
//                  if(window.JSInterface.isAlcohol() ){
//                      var gas_name = "Alcohol found";
//                  } else var gas_name = "No alcohol found";
                  var gas_name = "New measure available!";
                  $('.result-name').text(gas_name);


                  // fill in the result data here


                  // give the result a color if needed
                  // var color = '#47b3f7';

                  //random color for testing
                 // var color = '#'+Math.floor(Math.random()*16777215).toString(16);
                  app.showresult(getRandomColor());

              }, 10000);
        }



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
