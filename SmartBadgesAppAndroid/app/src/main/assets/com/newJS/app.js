var app = {


    connected : false,
    colors : [

        "#123456",
        "#FF0000",
        "#FFFF00",
        "#00FF00",
        "#00FFFF",
        "#FF00FF"

    ],


    init : function(){

        app.initListeners();

        setTimeout(function(){

            app.initWelcomeScreen();

            setTimeout(function(){

                if(!app.connected){
                    app.initConnectionScreen();
                }else{
                    app.initHomeScreen();
                }


            }, 4000);

        }, 2000);

    },


    initHomeScreen : function(){
        $('.page.current').removeClass('current');
        $('.page.home').addClass('current');
        $('.header').removeClass('hide');
        $('.footer').addClass('connected');
        grow = true;
        updateParticles(data["icon-home"]);
    },


    initWelcomeScreen : function(){

        var obj = {
            color : _bgcolor
        };

        TweenLite.to(obj, 2, {color: "#26313c", ease:Linear.easeOut, onUpdate:function(){
            _bgcolor = obj.color;
        }});

        $('.page.intro').addClass('current');
        grow = true;
        updateParticles(data["icon"]);

    },


    initConnectionScreen : function(){

        $('.page.current').removeClass('current');
        $('.page.connection').addClass('current');
        grow = true;
        updateParticles(data["wifi"]);

    },

    connectDevice: function(){

        grow = true;
        $('.page.current').removeClass('current');
        updateParticles(data["connecting"]);
        setTimeout(function(){
            $('.page.current').removeClass('current');
            $('.page.connection_success').addClass('current');
            app.connected = true;
            grow = true;
            updateParticles(data["check"]);

            setTimeout(function(){

                app.initHomeScreen();

            }, 4000);
        }, 4000);
    },

    initListeners : function(){

        $('.menu-trigger').on('click', function(){
            $('body').addClass('menuopen');
        });

        $('.close-menu').on('click', function(){
            $('body').removeClass('menuopen');
        });

        // connect to the device
        $('.connect-btn').on('click', function(){
            if(window.JSInterface.connectToDevice()) app.connectDevice();
           //app.connectDevice();
        });


        // change this function
        $('.save-notifications').on('click', function(){
        var appNameArray = [];
        var colorArray = [];
        $('ul .app-config-row ').each(function(i){

          var appName = $(this).text().replace(/(?:\s+\r\n|\r|\n)/g, '').trim(); // This is your rel value
          appNameArray.push(appName);

        });
        $('ul .app-config-row .color ').each(function(i){

                   var currentColorNum = parseInt($(this).attr('data-color'));
                   colorArray.push(app.colors[currentColorNum]);
                });

            window.JSInterface.saveConfiguration(appNameArray,colorArray);
            app.saveNotifications();
        });
        $('.save-calendar').on('click', function(){
            app.saveContacts();
        });

//        $('.save-contacts').on('click', function(){
//            app.saveContacts();
//        });
//
//        $('.save-preferences').on('click', function(){
//            app.savePreferences();
//        });

        $('.app-config-trigger').on('click', function(){

            $(this).addClass('current');

            var _appname = $(this).attr('data-name');
            var _appcolor = $(this).attr('data-color');
            var _appcount = $(this).attr('data-count');

            var obj = {
                color : _bgcolor
            }
            TweenLite.to(obj, 2, {color: _appcolor, ease:Linear.easeOut, onUpdate:function(){
                _bgcolor = obj.color;
            }});

            $('.page.config-list').addClass('out');
            $('.page.config-detail').addClass('current');


            initConfiDetail(_appcolor, _appcount);

            setTimeout(function(){
                grow = true;
                updateParticles(data[_appname]);
            }, 500);

        });


        $('.app-config-row .count').on('click', function(){

            var currentNum = parseInt($(this).attr('data-count'));

            if(currentNum < 4){
                currentNum++;
            }else{
                currentNum = 0;
            }
            $(this).attr('data-count', currentNum);
        });

        $('.app-config-row .color').on('click', function(){

            var currentColorNum = parseInt($(this).attr('data-color'));

            if(currentColorNum < app.colors.length){
                currentColorNum++;
            }else{
                currentColorNum = 0;
            }

            $(this).attr('data-color', currentColorNum);
            $(this).find('.app-color-preview').css('background-color', app.colors[currentColorNum]);
        });

        // menu listeners


        $('.connection-trigger').on('click', function(){

            app.closeMenu();

            if(app.connected){
                app.connected = false;
                $('.page.current').removeClass('current');
                $('.header').addClass('hide');
                $('.footer').removeClass('connected');
                app.initConnectionScreen();
                window.JSInterface.getIpAdress();
            }
        });

        $('.notifications-trigger').on('click', function(){

            app.closeMenu();
            setTimeout(function(){
                app.initConfiguration();
            }, 500);

        });
                $('.calendar-trigger').on('click', function(){

                    $('.content').addClass('dark');
                    app.closeMenu();
                    $('.page.current').removeClass('current');
                    $('.page.calendar').addClass('current');
                    grow = true;
                    updateParticles(data["calendar"]);

                });
// ADD CODE HERE
      $('.calendar-trigger').on('click', function(){
            $('.content').addClass('dark');
            app.closeMenu();
            $('.page.current').removeClass('current');
            $('.page.calendar').addClass('current');
             $('.page.calendar').removeClass('out');
             resetColor();
             $('.page.calendar .container').scrollTop(0);
            $('.calendar ul li').remove();
            var markup =
            '<li class="list-header"><div class="name">Name</div><div class="color">Meeting Colors</div></li>';
            $('.calendar ul').append(markup);
            var events = JSON.parse(window.JSInterface.getEventList());
                    $.each(events, function(index, value) {
                    var date = new Date(value.calendar.year, value.calendar.month, value.calendar.dayOfMonth, value.calendar.hourOfDay, value.calendar.minute, 0, 0);
                    var text = value.title + date.toString();
                        var markup =
                        '<li> ' +
                                   '<div class="app-config-row">' +
                                        '<div class="name">'+
                                            '<p>' + value.title+
                                            '</p>'+
                                         '</div>'+
                                     '<div class="color" data-color="0">'+
                                        '<div class="app-color-preview" style="background-color:'+value.color +' ">'+
                                        '</div>'+
                                     '</div>'
                        '</li>';
                        $('.calendar ul').append(markup);
                       // $('.app-config-row .color').find('.app-color-preview').css('background-color', value.color);
                   });
        });
//
//        $('.contacts-trigger').on('click', function(){
//
//            $('.content').addClass('dark');
//            app.closeMenu();
//            $('.page.current').removeClass('current');
//            $('.page.contacts').addClass('current');
//
//        });
//
//
//
//        $('.health-trigger').on('click', function(){
//
//            $('.content').addClass('dark');
//            app.closeMenu();
//            $('.page.current').removeClass('current');
//            $('.page.health').addClass('current');
//
//        });
//
//
//
//        $('.preferences-trigger').on('click', function(){
//
//            $('.content').addClass('dark');
//            app.closeMenu();
//            $('.page.current').removeClass('current');
//            $('.page.preferences').addClass('current');
//
//        });
//
//        $('.help-trigger').on('click', function(){
//
//            $('.content').addClass('dark');
//            app.closeMenu();
//            $('.page.current').removeClass('current');
//            $('.page.help').addClass('current');
//        });

        $('.logo').on('click', function(){

            $('.page.current').removeClass('current');
            $('.content').removeClass('dark');
            app.closeMenu();
            app.initHomeScreen();

        });

    },

//    savePreferences : function(){
//
//        grow = true;
//        updateParticles(data["check"]);
//        $('.page.current').removeClass('current');
//        $('.content').removeClass('dark');
//        setTimeout(function(){
//            app.initHomeScreen();
//        }, 2000);
//
//    },

//    saveContacts : function(){
//
//        grow = true;
//        updateParticles(data["check"]);
//        $('.page.current').removeClass('current');
//        $('.content').removeClass('dark');
//        setTimeout(function(){
//            app.initHomeScreen();
//        }, 2000);
//
//    },
    saveCalendar : function(){

        grow = true;
        updateParticles(data["check"]);
        $('.page.current').removeClass('current');
        $('.content').removeClass('dark');
        setTimeout(function(){
            app.initHomeScreen();
        }, 2000);
        window.JSInterface.createAlarmForMeetings()

    },
    saveNotifications : function(){

        grow = true;
        updateParticles(data["check"]);
        $('.page.current').removeClass('current');
        $('.content').removeClass('dark');
        setTimeout(function(){
            app.initHomeScreen();
        }, 2000);

    },

    closeMenu : function(){

        $('body').removeClass('menuopen');

    },

    initConfiguration : function(){

        $('.content').addClass('dark');
        $('.page.current').removeClass('current');
        $('.page.notifications-list').addClass('current');
        $('.page.notifications-list').removeClass('out');
        resetColor();
        $('.notifications-list .container').scrollTop(0);
        grow = true;
        updateParticles(data[Object.keys(data)[1]]);

    },


}

function resetColor(){

    //_bgcolor = "#26313c";

    var obj = {
        color : _bgcolor
    };

    TweenLite.to(obj, 2, {color: "#26313c", ease:Linear.easeOut, onUpdate:function(){
        _bgcolor = obj.color;
    }});

}
