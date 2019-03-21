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
}
var _createClass = function () {
    function defineProperties(target, props) {
        for (var i = 0; i < props.length; i++) {
            var descriptor = props[i];
            descriptor.enumerable = descriptor.enumerable || false;
            descriptor.configurable = true;
            if ("value" in descriptor) descriptor.writable = true;
            Object.defineProperty(target, descriptor.key, descriptor);
        }
    }
    return function (Constructor, protoProps, staticProps) {
        if (protoProps) defineProperties(Constructor.prototype, protoProps);
        if (staticProps) defineProperties(Constructor, staticProps);
        return Constructor;
    };
}();
var grow = false;
var _bgcolor = '#26313c';
var svgImageData = function () {
    var _ref2 = _asyncToGenerator( /*#__PURE__*/ regeneratorRuntime.mark(


        function _callee(svg) {
            var src, img, data, destinations, i, j, p;
            return regeneratorRuntime.wrap(function _callee$(_context) {
                while (1) {
                    switch (_context.prev = _context.next) {
                        case 0:
                            src = "data:image/svg+xml," + encodeURIComponent(svg.outerHTML);
                            _context.next = 3;
                            return (
                                createImage(src));
                        case 3:
                            img = _context.sent;
                            ctx.drawImage(img, ww / 2 - img.width / 2, wh / 2 - img.height / 2, img.width, img.height);
                            data = ctx.getImageData(0, 0, ww, wh).data;
                            ctx.clearRect(0, 0, canvas.width, canvas.height);
                            destinations = [];
                            for (i = 0; i < ww; i += ppi) {
                                for (j = 0; j < wh; j += ppi) {
                                    if (data[(i + j * ww) * 4 + 3] > 150) {
                                        p = {
                                            x: i,
                                            y: j
                                        };
                                        destinations.push(p);
                                    }
                                }
                            }
                            return _context.abrupt("return",

                                shuffle(destinations));
                        case 10:
                        case "end":
                            return _context.stop();
                    }
                }
            }, _callee, this);
        }));
    return function svgImageData(_x) {
        return _ref2.apply(this, arguments);
    };
}();

function _asyncToGenerator(fn) {
    return function () {
        var gen = fn.apply(this, arguments);
        return new Promise(function (resolve, reject) {
            function step(key, arg) {
                try {
                    var info = gen[key](arg);
                    var value = info.value;
                } catch (error) {
                    reject(error);
                    return;
                }
                if (info.done) {
                    resolve(value);
                } else {
                    return Promise.resolve(value).then(function (value) {
                        step("next", value);
                    }, function (err) {
                        step("throw", err);
                    });
                }
            }
            return step("next");
        });
    };
}


function _classCallCheck(instance, Constructor) {
    if (!(instance instanceof Constructor)) {
        throw new TypeError("Cannot call a class as a function");
    }
}
var canvas = document.querySelector("#scene"),
    ctx = canvas.getContext("2d"),
    particles = [],
    amount = 0,
    mouse = {
        x: 0,
        y: 0
    },
    radius = 10;
var ppi = 8;
var blendModeString = "lighter";
var blendModeGlobal = "multiply";
var colors = ["#ECEFF1", "#CFD8DC", "#B0BEC5", "#90A4AE", "#78909C"];
var ww = canvas.width = window.innerWidth;
var wh = canvas.height = window.innerHeight;

function shuffle(a) {
    for (var i = a.length - 1; i > 0; i--) {
        var j = Math.floor(Math.random() * (i + 1));
        var _ref = [a[j], a[i]];
        a[i] = _ref[0];
        a[j] = _ref[1];
    }
    return a;
}

function createImage(path) {
    return new Promise(function (resolve, reject) {
        var img = new Image();
        img.onload = function () {
            return resolve(img);
        };
        img.onerror = function () {
            return reject({
                path: path,
                status: 'error'
            });
        };
        img.src = path;
    });
}
var Particle = function () {
    function Particle(options) {
        _classCallCheck(this, Particle);
        Object.assign(this, options);
        this.accX = 0;
        this.accY = 0; //
        this.on = true;
        this.friction = Math.random() * 10 + 5;
        //this.friction = .77;
        this.color = colors[Math.floor(Math.random() * 255)];
        this.color = '#33ccff';
        this.alpha = 1;
        this.radius = radius;
        this.dest = {
            x: ww / 2,
            y: wh / 2
        };
        this.visible = true;
        this.r = Math.random() * .1 + 1; //this.r = 3;
    }
    _createClass(Particle, [{
        key: "randomVelocity",
        value: function randomVelocity() {
            this.vx = (Math.random() - 0.1) * 100;
            this.vy = (Math.random() - 0.1) * 100;
            return this;
        }
    }, {
        key: "getDistanceTo",
        value: function getDistanceTo(target) {
            var a = (this.x - target.x) * 200;
            var b = (this.y - target.y) * 200;
            return Math.sqrt(a * a + b * b);
        }
    }, {
        key: "update",
        value: function update() {
            this.accX = (this.dest.x - this.x) / 50;
            this.accY = (this.dest.y - this.y) / 50;
            this.vx += this.accX;
            this.vy += this.accY;
            this.vx *= this.friction;
            this.vy *= this.friction;
            this.x += this.vx;
            this.y += this.vy;
            if(this.radius < radius){
                this.radius += .1;
            }


            return this;
        }
    }, {
        key: "render",
        value: function render() {

            var r = 150;
            var g = ~~( 255 * (1 - ( this.y / window.innerHeight / 2)));
            var b = 255;
            var a = 1;


            if(this.on){

                this.radius = radius;

            }else{

                this.radius = .5;

            }

            ctx.globalCompositeOperation = blendModeString;
            // Fill with gradient
            ctx.save();
            ctx.fillStyle = 'rgba(' + r + ',' + g + ',' + b + ',' + a + ')';
           // ctx.fillStyle=grd;
            ctx.beginPath();
            ctx.arc(this.x, this.y, this.radius, Math.PI * 4, false);
            if (!this.visible) {
                ctx.globalAlpha = 0;
            }
            ctx.fill();
            ctx.restore();
        }
    }], [{
        key: "getDistance",
        value: function getDistance(a, b) {
            return Math.sqrt(a * a + b * b);
        }
    }]);
    return Particle;
}();

function gatherData() {
    var _this = this;
    return new Promise(function () {
        var _ref3 = _asyncToGenerator( /*#__PURE__*/ regeneratorRuntime.mark(function _callee2(resolve, reject) {
            var data, svgs, _iteratorNormalCompletion, _didIteratorError, _iteratorError, _iterator, _step, name, svgData;
            return regeneratorRuntime.wrap(function _callee2$(_context2) {
                while (1) {
                    switch (_context2.prev = _context2.next) {
                        case 0:
                            data = {};
                            svgs = document.querySelectorAll('#svgs svg');
                            _iteratorNormalCompletion = true;
                            _didIteratorError = false;
                            _iteratorError = undefined;
                            _context2.prev = 5;
                            _iterator =
                                svgs[Symbol.iterator]();
                        case 7:
                            if (_iteratorNormalCompletion = (_step = _iterator.next()).done) {
                                _context2.next = 17;
                                break;
                            }
                            elm = _step.value;
                            name = elm.getAttribute('name');
                            _context2.next = 12;
                            return (
                                svgImageData(elm));
                        case 12:
                            svgData = _context2.sent;
                            data[name] = {
                                data: svgData,
                                length: svgData.length
                            };
                        case 14:
                            _iteratorNormalCompletion = true;
                            _context2.next = 7;
                            break;
                        case 17:
                            _context2.next = 23;
                            break;
                        case 19:
                            _context2.prev = 19;
                            _context2.t0 = _context2["catch"](5);
                            _didIteratorError = true;
                            _iteratorError = _context2.t0;
                        case 23:
                            _context2.prev = 23;
                            _context2.prev = 24;
                            if (!_iteratorNormalCompletion && _iterator.return) {
                                _iterator.return();
                            }
                        case 26:
                            _context2.prev = 26;
                            if (!_didIteratorError) {
                                _context2.next = 29;
                                break;
                            }
                            throw _iteratorError;
                        case 29:
                            return _context2.finish(26);
                        case 30:
                            return _context2.finish(23);
                        case 31:


                            resolve(data);
                        case 32:
                        case "end":
                            return _context2.stop();
                    }
                }
            }, _callee2, _this, [
                [5, 19, 23, 31],
                [24, , 26, 30]
            ]);
        }));
        return function (_x2, _x3) {
            return _ref3.apply(this, arguments);
        };
    }());

}

function generateParticles(data) {
    var particleCount = 0;
    for (var i in data) {
        particleCount = data[i].length > particleCount ? data[i].length : particleCount;
    }
    var particles = [];

    for (var i = 0; i < particleCount; i++) {
        var p = new Particle({
            //x: ww / 2 + (Math.random() - 100) + 100,
            //y: wh / 2 + (Math.random() - 100) + 100

            x: Math.random() * (ww - 0) + 0,
            y: Math.random() * (wh - 0) + 0

        });

        p.randomVelocity();
        particles.push(p);
    }
    return particles;
}

var data;

function initScene() {
    var _this2 = this;
    return new Promise(function () {
        var _ref4 = _asyncToGenerator( /*#__PURE__*/ regeneratorRuntime.mark(function _callee3(resolve) {

            return regeneratorRuntime.wrap(function _callee3$(_context3) {
                while (1) {
                    switch (_context3.prev = _context3.next) {
                        case 0:
                            ww = canvas.width = window.innerWidth;
                            wh = canvas.height = window.innerHeight;
                            _context3.next = 4;
                            return (
                                gatherData());
                        case 4:
                            data = _context3.sent;
                            particles = generateParticles(data);
                            resolve(data);
                        case 7:
                        case "end":
                            return _context3.stop();
                    }
                }
            }, _callee3, _this2);
        }));
        return function (_x4) {
            return _ref4.apply(this, arguments);
        };
    }());

}

function updateParticles(data) {
    var pLen = particles.length;
    var dLen = data.length;

    for (var i in particles) {
        //particles[i].visible = false;
        particles[i].dest = {
            x:  Math.random() * (ww - 0) + 0,
            y:  Math.random() * (wh - 0) + 0
        };
        if (data.data.hasOwnProperty(i)) {

            particles[i].dest = data.data[i];
            particles[i].visible = true;
            particles[i].on = true;
            particles[i].friction = Math.random() * (.77 - 0.6) + .6;
            particles[i].alpha = 1;
            particles[i].radius += .1;
            /*
            if(particles[i].radius < radius){
                particles[i].radius += .1;
            }
            */


        }else{
            //particles[i].visible = false;

            particles[i].on = false;
            particles[i].friction = Math.random() * (.77 - 0.1) + 0.1;
            particles[i].alpha = .1;
            particles[i].radius = Math.random() * (.1 - 0.01) + 0.01;
            particles[i].accX = .2;
            particles[i].accY = .2;


        }
    }
}
function render(a) {
    requestAnimationFrame(render);
    ctx.save();
    //ctx.globalCompositeOperation = "source-over";
    ctx.globalAlpha = .5;
    ctx.clearRect(0, 0, canvas.width, canvas.height);
    /*var grd=ctx.createLinearGradient(0,0,ww,wh);
    grd.addColorStop(0,_bgcolor);
    grd.addColorStop(1,"rgba(0,0,0,"+.2 +")");
    */

    //_context.strokeStyle = _getColor(Math.sin(_color) * 100 + 200, _mouseX / _stageWidth * 200 + 50, _mouseY / _stageHeight * 200 + 50);
    ctx.fillStyle = _bgcolor;
    ctx.fillRect(0, 0, canvas.width, canvas.height);
    //ctx.globalCompositeOperation = blendModeGlobal;
    ctx.restore();

    for (var i in particles) {
        particles[i].update().render();
    }
    if(!grow){

        if(radius > 4){
            radius -= .2;
        }

    }else{

        if(radius < 7){
            radius += .03;
        }else{
            grow = false;
        }

    }



}

window.addEventListener("resize", resizeScene);

function resizeScene(){

    canvas.width = $(window).innerWidth();
    canvas.height = $(window).innerHeight();
    ww = canvas.width = window.innerWidth;
    wh = canvas.height = window.innerHeight;
}

initScene().
then(render()).
then(function (data) {

    var length = Object.keys(data).length - 1;
    var index = 0;
    updateParticles(data[Object.keys(data)[index]]);
});