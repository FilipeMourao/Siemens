   <!-- JS Hooks -->
$(document).ready(function(){// this function initializes the javascript objects

           visual.init();
           app.init();

       });

$(".downloadButton").click(function() {// this functions will call the corresponding android functions if the respective button is pressed
        window.JSInterface.downloadData();
   });
$(".uploadButton").click(function() {// this functions will call the corresponding android functions if the respective button is pressed
        window.JSInterface.updloadData();
   });

    function connectDeviceHook(){// this calls a function on android that will connect to the device

        window.JSInterface.testConnection()// this is how you call an android function from the javascript file
    }

    function startAnalyzingHook(){// this function calls a function on android to get the next measurement on android
        window.JSInterface.analyze()
    }
  function backToConnectScreen(){// this fucntion is used to get to the connect screen if the device was lost during the process of reconnecting to it

                $('.content-pane.result').removeClass('current');
                $('.content-pane.start').removeClass('current');
                $('.content-pane.details').removeClass('current');
                $('.content-pane.connect').removeClass('past').addClass('current');
                app.resetVisual();
   }
     function goDirectToMeasures(){
                   $('.content-pane.result').removeClass('current');
                   $('.content-pane.start').removeClass('current');
                   $('.content-pane.details').removeClass('current');
                     $('.content-pane.connect').removeClass('current');
                   $('.content-pane.result').removeClass('past').addClass('current');
                   var gas_name = 'Not able to get the new measure!';
                   $('.result-name').text(gas_name);
                   app.showresult(getRandomColor());
                   app.resetVisual();
      }



    // just some default values

    var _chart_labels = [

    ];

    var _chart_data = [

    ];

    var _chart_colors = [

    ];
function getRandomColor() {// this function generate random colors for the animations
  var letters = '0123456789ABCDEF';
  var color = '#';
  for (var i = 0; i < 6; i++) {
    color += letters[Math.floor(Math.random() * 16)];
  }
  return color;
}
