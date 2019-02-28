function initConfiDetail(color, count){

    var app_color = color;
    var app_count = count;

    var hslvalue = toHSL(app_color);
    console.log(hslvalue);

    $('#horiz').html(hslvalue.h);

    $('.copyHEX').bind('mousedown touchstart',function() {
        $(this).addClass('ripple');
        setTimeout(function() {
            $('.copyHEX').removeClass('Buttonleft');
            $('.copyRGB').removeClass('Buttoncenter');
            $('.copyHSL').removeClass('Buttonright');
            $('.copy').show();
            $('.copyHEX').removeClass('ripple');
            $('.result').slideDown(300);
            $('.result input').val(rgb2hex($('#drag').css('background-color'))).select();
        },800);
    });

    $('.copyRGB').bind('mousedown touchstart',function() {
        $(this).addClass('ripple');
        setTimeout(function() {
            $('.copyHEX').removeClass('Buttonleft');
            $('.copyRGB').removeClass('Buttoncenter');
            $('.copyHSL').removeClass('Buttonright');
            $('.copy').show();
            $('.copyRGB').removeClass('ripple');
            $('.result').slideDown(300);
            $('.result input').val($('#drag').css('background-color')).select();
        },800);
    });

    $('.copyHSL').bind('mousedown touchstart',function() {
        $(this).addClass('ripple');
        setTimeout(function() {
            $('.copyHEX').removeClass('Buttonleft');
            $('.copyRGB').removeClass('Buttoncenter');
            $('.copyHSL').removeClass('Buttonright');
            $('.copy').show();
            $('.copyHSL').removeClass('ripple');
            $('.result').slideDown(300);
            $('.result input').val(HSLvalue).select();
        },800);
    });

///// Convert to hex. Source (http://stackoverflow.com/questions/1740700/how-to-get-hex-color-value-rather-than-rgb-value)
    var hexDigits = new Array
    ("0","1","2","3","4","5","6","7","8","9","a","b","c","d","e","f");
    function rgb2hex(rgb) {
        rgb = rgb.match(/^rgb\((\d+),\s*(\d+),\s*(\d+)\)$/);
        return "#" + hex(rgb[1]) + hex(rgb[2]) + hex(rgb[3]);
    }
    function hex(x) {
        return isNaN(x) ? "00" : hexDigits[(x - x % 16) / 16] + hexDigits[x % 16];
    }
/////

    var widthInitial = hslvalue.h;
    var heightInitial = app_count * 10;
    var windowWidth = $(window).width()/100;
    var windowHeight = $(window).height()/100;

    if(heightInitial >= 0 && heightInitial < 50) {
        $('#drag, .button').css('color','white');
        $('.button').css('background','rgba(255,255,255,0.15)');
    }
    else if(heightInitial > 50 && heightInitial <= 100) {
        $('#drag, .button').css('color','black');
        $('.button').css('background','rgba(0,0,0,0.15)');
    }

    $('#c_holder').css('background','hsla('+widthInitial+',85%,'+heightInitial+'%,1)');

    $('#drag').bind('mousedown touchstart',function(e) {
        //e.preventDefault();
        var widthInitial = hslvalue.h;
        var heightInitial = parseInt($('#vert').html());
        var xInitial = e.originalEvent.pageX;
        var yInitial = e.originalEvent.pageY;

        $(document).bind('mousemove touchmove',function(e) {
            //e.preventDefault();

            var movePos = Math.min(Math.max(parseInt(Math.round((e.originalEvent.pageX-xInitial)/windowWidth*3.6)+widthInitial), 0), 360);
            var movePosVert =  Math.min(Math.max(parseInt(Math.round((e.originalEvent.pageY-yInitial)/windowHeight)+heightInitial), 0), 100);

            $('#c_holder').css('background','hsla('+movePos+',85%,'+40+'%,1)');
            $('.result').css('background','hsla('+(movePos+5)+',85%,'+(40+15)+'%,1)');

            _bgcolor = rgb2hex($('#c_holder').css('background-color'));
            $('.app-config-trigger.current').attr('data-color' , _bgcolor);

            $('#horiz').html(movePos);
            $('#vert').html(Math.round(movePosVert / 10));

            /*

            if(movePosVert >= 0 && movePosVert < 50) {
                $('#drag, .button').css('color','white');
                $('.button').css('background','rgba(255,255,255,0.15)');
            }
            else if(movePosVert > 50 && movePosVert <= 100) {
                $('#drag, .button').css('color','black');
                $('.button').css('background','rgba(0,0,0,0.15)');
            }

            */

            HSLvalue = 'hsl('+movePos+',85%,'+40+'%)';
        });

    });

    $(document).bind('mouseup touchend',function(e) {
        // e.preventDefault();
        $(document).unbind('mousemove touchmove');
    });
}


function toHSL (hex) {
    var result = /^#?([a-f\d]{2})([a-f\d]{2})([a-f\d]{2})$/i.exec(hex);

    var r = parseInt(result[1], 16);
    var g = parseInt(result[2], 16);
    var b = parseInt(result[3], 16);

    r /= 255, g /= 255, b /= 255;
    var max = Math.max(r, g, b), min = Math.min(r, g, b);
    var h, s, l = (max + min) / 2;

    if (max == min) {
        h = s = 0; // achromatic
    } else {
        var d = max - min;
        s = l > 0.5 ? d / (2 - max - min) : d / (max + min);
        switch (max) {
            case r:
                h = (g - b) / d + (g < b ? 6 : 0);
                break;
            case g:
                h = (b - r) / d + 2;
                break;
            case b:
                h = (r - g) / d + 4;
                break;
        }
        h /= 6;
    }

    s = s * 100;
    s = Math.round(s);
    l = l * 100;
    l = Math.round(l);
    h = Math.round(360*h);

    var hslColor = {
        "h" : h,
        "s" : s,
        "l" : l
    }

    return hslColor;
    //var colorInHSL = 'hsl(' + h + ', ' + s + '%, ' + l + '%)';
    //$rootScope.$emit('colorChanged', {colorInHSL});
}