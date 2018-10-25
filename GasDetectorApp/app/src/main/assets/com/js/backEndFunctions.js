   <!-- JS Hooks -->

    function connectDeviceHook(){

        // add connection code here
       // var connected = window.JSInterface.connectToDevice();
       // if(window.JSInterface.connectToDevice())
         app.deviceConnected();
       //  //if connected then call this func and remove timeout
      //  setTimeout(function(){

     //     app.deviceConnected();

      //  }, 1000);

    }

    function startAnalyzingHook(){

        // start analyzing code here
       // var analyse = window.JSInterface.analyze();
       // if( window.JSInterface.analyze()) {
       if( true) {
              setTimeout(function(){

                  // what gas has been analyzed
                  var gas_name = "New measure available";
                  $('.result-name').text(gas_name);
//                   var _chart_labels = [
//                   "Sensor 1"
//
//                      ];
//                      var _chart_data = window.JSInterface.getSensor1Points();
//                      var _chart_colors = [
//                      '#'+Math.floor(Math.random()*16777215).toString(16)
//
//                      ];

                  // fill in the result data here

                  _chart_labels = [
                          "Oxygen",
                          "Carbon dioxid",
                          "Amonia",
                          "hydrogen",
                          "others",
                          "Oxygen",
                          "Carbon dioxid",
                          "Amonia",
                          "hydrogen",
                          "others"
                  ];

                  _chart_data = [
                      32.4,
                      17.6,
                      12.2,
                      8.8,
                      12.7,
                      32.4,
                      17.6,
                      12.2,
                      8.8,
                      12.7
                  ];

                  _chart_colors = [
                      '#'+Math.floor(Math.random()*16777215).toString(16),
                      '#'+Math.floor(Math.random()*16777215).toString(16),
                      '#'+Math.floor(Math.random()*16777215).toString(16),
                      '#'+Math.floor(Math.random()*16777215).toString(16),
                      '#'+Math.floor(Math.random()*16777215).toString(16),
                      '#'+Math.floor(Math.random()*16777215).toString(16),
                      '#'+Math.floor(Math.random()*16777215).toString(16),
                      '#'+Math.floor(Math.random()*16777215).toString(16),
                      '#'+Math.floor(Math.random()*16777215).toString(16),
                      '#'+Math.floor(Math.random()*16777215).toString(16)
                  ];


                  // give the result a color if needed
                  // var color = '#47b3f7';

                  //random color for testing
                  var color = '#'+Math.floor(Math.random()*16777215).toString(16);
                  app.showresult(color);

              }, 1000);
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
