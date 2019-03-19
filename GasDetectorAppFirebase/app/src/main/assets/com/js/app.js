var app = {

    init : function(){

        app.initListeners();
        setTimeout(function(){

            app.appLoaded();

        }, 3000);

    },

    appLoaded : function(){

        $('body').removeClass('init');
        $('.content-pane.connect').addClass('current');

        visual._vColor = "#22b3f7";

        app.resetVisual();

    },

    connectDevice : function(){

        connectDeviceHook();

    },

    deviceConnected : function(){
        $('.content-pane.connect').addClass('past').removeClass('current');
        $('.content-pane.start').addClass('current');
        app.resetVisual();
    },

    initListeners : function(){

        $('.analyze-button').on('click', function(){

            app.startAnalyzing();

        });

        $('.details-button').on('click', function(){

            app.showDetails();

        });

        $('.back-to-start').on('click', function(){

            $('.content-pane.result').removeClass('current');
            $('.content-pane.start').removeClass('past').addClass('current');
            app.resetVisual();
            //adding this code to reconnect to the device
             app.connectDevice();
        });

        $('.back-to-result').on('click', function(){

            $('.content-pane.details').removeClass('current');
            $('.content-pane.result').removeClass('past').addClass('current');

        });


        $('.connect-device').on('click', function(){

            app.connectDevice();

        });

        $('.logo').on('click', function(){

            app.resetVisual();


        });

    },

    startAnalyzing : function(){

        startAnalyzingHook();
        $('.content-pane.start').addClass('past').removeClass('current');

        visual._vColor = "#47b3f7";

        var obj = {
            radius: visual.CONTROLL_R,
            count : visual.COUNT,
            speed : visual.SPEED,
            rotation: visual._rotation,
            strokeWidth : visual._strokeWidth,
            controll_r : visual.CONTROLL_R,
            anchor_r : visual.ANCHOR_R
        };

        TweenLite.to(obj, 3, {strokeWidth: 6 , ease:Linear.easeOut, onUpdate:function(){
            visual._strokeWidth = obj.strokeWidth;
        }});

        TweenLite.to(obj, 3, {anchor_r: 2 , ease:Elastic.easeOut, onUpdate:function(){
            visual.ANCHOR_R = obj.anchor_r;
        }});

        TweenLite.to(obj, 3, {rotation: (1 / 10) , ease:Elastic.easeOut, onUpdate:function(){
            visual._rotation = obj.rotation;
        }});

        TweenLite.to(obj, 3, {speed: 20 , ease:Elastic.easeOut, onUpdate:function(){
            visual.SPEED = obj.speed;
        }});

    },

    showresult : function(resultColor){

        $('.content-pane.result').addClass('current');

        // result found

        var randColor = resultColor;


        visual._vColor = randColor;

        var obj = {
            radius: visual.CONTROLL_R,
            count : visual.COUNT,
            speed : visual.SPEED,
            rotation: visual._rotation,
            strokeWidth : visual._strokeWidth,
            controll_r : visual.CONTROLL_R,
            anchor_r : visual.ANCHOR_R
        };

        TweenLite.to(obj, 3, {controll_r: 100, ease:Elastic.easeOut, onUpdate:function(){
            visual.CONTROLL_R = obj.controll_r;
        }});

        TweenLite.to(obj, 3, {anchor_r: 10, ease:Elastic.easeOut, onUpdate:function(){
            visual.ANCHOR_R = obj.anchor_r;
        }});

        TweenLite.to(obj, 1, {strokeWidth: 1.5 , ease:Elastic.easeOut, onUpdate:function(){
            visual._strokeWidth = obj.strokeWidth;
        }});

        TweenLite.to(obj, 1, {rotation: (1 / 50000) , ease:Linear.easeOut, onUpdate:function(){
            visual._rotation = obj.rotation;
        }});

        TweenLite.to(obj, 2, {speed: 10, ease:Linear.easeOut, onUpdate:function(){
            visual.SPEED = obj.speed;
        }});

        app.createTable();


    },

    showDetails : function(){

        $('.content-pane.result').addClass('past').removeClass('current');
        $('.content-pane.details').scrollTop(0);
        $('.content-pane.details').addClass('current');
        app.createChart();


    },
   createChartNew : function(data){
    Chart.defaults.global.animationSteps = Math.round(5000 / 17);

           var label_color = '#ffffff';
           var graph_color = '#ffffff';

   //        var _chart_data_structure =  JSON.parse(window.JSInterface.getSensorPoints());
//           var _chart_data_structure =  JSON.parse(data);
//           var methanMeasure = _chart_data_structure.pop();
//           var airQualityMeasure =_chart_data_structure.pop();
//           var alcoholMeasure = _chart_data_structure.pop();
//           var label = _chart_data_structure.pop();

           var methanMeasure = data[3];
           var airQualityMeasure =data[2];
           var alcoholMeasure = data[1];
           var label = data[0];

           var ctx_result = $('.content-pane.details').find('canvas').get(0).getContext('2d');
        //   ctx_result.height = "200%" ;
        //    ctx_result.height = 500 ;
            var myChart = new Chart(ctx_result, {
       type: 'line',
         data: {
           //labels: [1500,1600,1700,1750,1800,1850,1900,1950,1999,2050],
           labels: label,
           datasets: [{
               //data: [86,114,106,106,107,111,133,221,783,2478],
               data: alcoholMeasure,
               label: "Alcohol",
               borderColor: "#9A2A13",
               fill: false
             }, {
               //data: [282,350,411,502,635,809,947,1402,3700,5267],
               data: airQualityMeasure,
               label: "Air quality",
               borderColor: "#139A29",
               fill: false
             }, {
               //data: [168,170,178,190,203,276,408,547,675,734],
               data: methanMeasure,
               label: "Methan",
               borderColor: "#1E139A",
               fill: false
             }
           ]
         },
         options: {
           responsive: true
   //                title: {
   //                  display: false,
   //                  text: 'Sensors measures',
   //                }
         }
       });
   //        var myChart = new Chart(ctx_result, {
   //            type: 'bar',
   //            data: {
   //                animationSteps: 300,
   //                labels: _chart_labels,
   //                datasets: [{
   //                    label: 'average',
   //                    data: _chart_data,
   //                    backgroundColor: _chart_colors
   //                }],
   //            },
   //
   //
   //            options: {
   //                responsive: true,
   //                legend: {
   //                    display: false,
   //                    labels:{
   //                        fontColor: label_color
   //                    }
   //                },
   //
   //                scales: {
   //                    maintainAspectRatio: false,
   //                    yAxes: [{
   //                        gridLines:{
   //                            display: false
   //                        },
   //                        ticks: {
   //                            fontColor: label_color,
   //                            fontSize: 18,
   //                            stepSize: 1,
   //                            beginAtZero: true,
   //                            display: false
   //                        }
   //                    }],
   //                    xAxes: [{
   //                        gridLines:{
   //                            display: false
   //                        },
   //                        ticks: {
   //                            fontColor: label_color,
   //                            fontSize: 14,
   //                            stepSize: 1,
   //                            beginAtZero: true,
   //                            display: false
   //                        }
   //                    }]
   //                }
   //            }
   //
   //        });

   },
    createChart : function(){
       window.JSInterface.getSensorPoints();

    },

 //createTable code
    createTable : function(){

        // clean table
        $('.result-table .result-row').remove();

        //var functionInformation=  JSON.parse(window.JSInterface.getSensorPointClassification());
        var gasesClassification =  JSON.parse(window.JSInterface.getSensorPointClassification());
        $.each(gasesClassification, function(index, value) {
//            var stringToWrite = value.toString();
            var stringToWrite = value[0].toString();
            var markup = '<tr class="result-row"><td>'+stringToWrite+'</td><td>'+index+'</td><td>'+value[1].toString()+'</td></tr>';
            $('.result-table').append(markup);
        });

        $( ".result-table .result-row" ).each(function(index) {
        $(this).css("text-align","center");
        $(this).on("click", function(){
                window.JSInterface.deleteOrClassifyMeasure(parseInt(gasesClassification[index][1]));
            });
        });


    },

    resetVisual : function(){

        visual._vColor = "#47b3f7";

        var obj = {
            radius: visual.CONTROLL_R,
            count : visual.COUNT,
            speed : visual.SPEED,
            rotation: 1 / 1000,
            strokeWidth : visual._strokeWidth
        };


        TweenLite.to(obj, 3, {speed:10, ease:Elastic.easeOut, onUpdate:updateSpped});
        TweenLite.to(obj, 2, {strokeWidth: 1, ease:Linear.easeOut, onUpdate:updateStroke});



        function updateStroke(){
            visual._strokeWidth = obj.strokeWidth;
        }

        function updateCount(){
            visual.COUNT = Math.round(obj.count);
        }


        function updateRotation(){
            visual._rotation = obj.rotation;
        }

        function updateSpped() {
            visual.SPEED = obj.speed;
        }

    },
}

var results = [];
results[0] = "#FF66FF";
results[1] = "#0066FF";
results[2] = "#FF6600";
results[3] = "#47b3f7";
results[4] = "#FF00FF";
results[5] = "#a3f346";
results[6] = "#ff0000";
results = shuffle(results);


function shuffle(a) {
    var j, x, i;
    for (i = a.length - 1; i > 0; i--) {
        j = Math.floor(Math.random() * (i + 1));
        x = a[i];
        a[i] = a[j];
        a[j] = x;
    }
    return a;
}

