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

    createChart : function(){

        Chart.defaults.global.animationSteps = Math.round(5000 / 17);

        var label_color = '#ffffff';
        var graph_color = '#ffffff';

        var ctx_result = $('.content-pane.details').find('canvas').get(0).getContext('2d');
        ctx_result.height = 800;
        var myChart = new Chart(ctx_result, {
            type: 'bar',
            data: {
                animationSteps: 300,
                labels: _chart_labels,
                datasets: [{
                    label: 'average',
                    data: _chart_data,
                    backgroundColor: _chart_colors
                }],
            },


            options: {
                responsive: true,
                legend: {
                    display: false,
                    labels:{
                        fontColor: label_color
                    }
                },

                scales: {
                    maintainAspectRatio: false,
                    yAxes: [{
                        gridLines:{
                            display: false
                        },
                        ticks: {
                            fontColor: label_color,
                            fontSize: 18,
                            stepSize: 1,
                            beginAtZero: true,
                            display: false
                        }
                    }],
                    xAxes: [{
                        gridLines:{
                            display: false
                        },
                        ticks: {
                            fontColor: label_color,
                            fontSize: 14,
                            stepSize: 1,
                            beginAtZero: true,
                            display: false
                        }
                    }]
                }
            }

        });
    },

    createTable : function(){

        // clean table
        $('.result-table .result-row').remove();

        $.each(_chart_data, function(index, value) {

            var label = _chart_labels[index];
            var data = value;
            var color = _chart_colors[index];
            var markup = '<tr class="result-row"><td><span class="color" style="background-color: '+color+';"></span>'+label+'</td><td>'+data+' %</td></tr>';
            $('.result-table').append(markup);

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
