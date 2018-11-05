var visual = {

    COUNT : 35,
    CONTROLL_R : 50,
    ANCHOR_R : 10,
    SPEED : 10,
    LAYOUT_H : 10,
    LAYOUT_R : 80,
    _canvas : document.getElementById( 'world'),
    _context : '',
    _stageWidth : window.innerWidth,
    _stageHeight : window.innerHeight,
    _mouseX : window.innerWidth*0.5,
    _mouseY : window.innerHeight*0.5,
    _bezierPointList : [],
    _color : 255,
    _rotation : 1 / 100,
    _vColor : "#22b3f7",
    _strokeWidth : 3,
    _globalAlpha : .2,
   // _vColor : "#FF00FF",


    init : function()
    {
        //if ( ! visual.canvas || ! visual.canvas.getContext ) { return false; }
        visual._context = visual._canvas.getContext('2d');
        visual._build();
        window.addEventListener('resize', visual._onWindowResizeHandler, false);
        visual._onWindowResizeHandler();
        document.addEventListener('mousemove', visual._onMouseMoveHandler, false);
        document.addEventListener('touchstart', visual._onTouchHandler, false);
        document.addEventListener('touchmove', visual._onTouchHandler, false);
        document.addEventListener("touchend", visual._onTouchHandler, false);
        setInterval( visual._onEnterFrameHandler, 1000 / 60 );

    },


    _build : function()
    {

        if( visual._bezierPointList.length < visual.COUNT){

            console.log('less : ' + (visual.COUNT - visual._bezierPointList.length));

            for(var i = 0, len = visual.COUNT - visual._bezierPointList.length; i < len; i++)
            {

                var bp = new BezierPoint();
                visual._layout(bp, i/len);
                bp.saveDefault();
                visual._bezierPointList[i] = bp;
            }

        }

    },

    _layout : function(bp, percent)
    {

        var rad = 2 * Math.PI * percent * 180;
        bp.x = visual.LAYOUT_R * Math.sin(rad) + visual._stageWidth * 0.5;
        bp.y = visual.LAYOUT_R * Math.cos(rad) + visual._stageHeight * 0.5;
        var pt = new Pt(visual.LAYOUT_H * Math.cos(Math.PI * 0.5 + rad) + bp.x, visual.LAYOUT_H * Math.sin(Math.PI * 0.5 + rad) + bp.y);
        bp.setInPt(pt);
        console.log('laoyut Function')
    },

    _move : function()
    {
        for(var i = 0, len = visual._bezierPointList.length; i < len; i++)
        {
            var bp = visual._bezierPointList[i];
            bp.move();
        }
    },

    _draw : function()
    {

        visual._build();

        visual._context.globalAlpha = visual._globalAlpha;
        visual._context.globalCompositeOperation = 'lighter';


        var grd = visual._context.createLinearGradient(0,0,visual._stageWidth,visual._stageHeight);
        grd.addColorStop(0,"rgba(20,20,20, .1)");
        grd.addColorStop(1,"rgba(0,0,0," +1+")");
        visual._context.fillStyle = "#262a2f";
        visual._context.fillStyle = grd;
        visual._context.fillRect(0, 0, visual._stageWidth, visual._stageHeight);


        visual._context.beginPath();
        var gradient=visual._context.createLinearGradient(0,0,visual._stageWidth,0);
        gradient.addColorStop("0","rgba(38,49,60,0)");
        gradient.addColorStop("0.5",visual._vColor);
        gradient.addColorStop("1.0",visual._vColor);
        visual._context.lineWidth = visual._strokeWidth;
        //Math.random() * (ww - 0) + 0
        visual._context.strokeStyle = gradient;

        for(var i = 0, len = visual._bezierPointList.length - 1; i < len; i++)
        {
            visual._lineTo(visual._bezierPointList[i], visual._bezierPointList[i + 1]);
        }
        visual._lineTo(visual._bezierPointList[visual._bezierPointList.length - 1], visual._bezierPointList[0]);
        visual._context.stroke();







    },

    _lineTo : function(p0, p1)
    {
        var p0A = p0.getAncherPt();
        var p0CO = p0.getOutPt();
        var p1A = p1.getAncherPt();
        var p1CI = p1.getInPt();

        visual._context.moveTo(p0A.x, p0A.y);
        visual._context.bezierCurveTo(p0CO.x, p0CO.y, p1CI.x, p1CI.y, p1A.x, p1A.y);
    },

    _fade : function ()
    {


        var n = visual._stageHeight / visual._stageWidth ;

        if(visual._stageHeight < visual._stageWidth){

            n = visual._stageWidth / visual._stageHeight;
        }


        var speedX, speedY;
        if(n<1)
        {
            speedX = visual.SPEED * 1000;
            speedY = visual.SPEED * n;
        }else
        {
            speedX = visual.SPEED / n;
            speedY = visual.SPEED;
        }

        var w = visual._context.canvas.width;
        var h = visual._context.canvas.height;
        visual._context.save();
        visual._context.translate(w * 0.5, h *0.5);

        visual._context.rotate(visual._rotation);
        visual._context.drawImage( visual._canvas, -speedX - w *0.5, -speedY - h * 0.5, w + 2 * speedX, h + 2 * speedY);
        visual._context.restore();
        visual._context.fillStyle = "rgba(0, 0, 20, 0.1)";
        visual._context.fillRect (0, 0, visual._stageWidth, visual._stageHeight);

    },

    _getColor : function(r, g, b)
    {
        var num = Math.ceil(r << 16 | g << 8 | b);
        return "#" + num.toString(16);
    },

    _onEnterFrameHandler : function()
    {
        visual._context.save();
        visual._fade();
        visual._move();
        visual._draw();
        visual._context.restore();
        //_stats.update();
    },

    _onTouchHandler : function(event)
    {
        if(!event.touches[0])
            return;
        this._mouseX = event.touches[0].screenX;
        this._mouseY = event.touches[0].screenY;
        //event.preventDefault();
    },

    _onMouseMoveHandler : function(event)
    {
        this._mouseX = event.clientX;
        this._mouseY = event.clientY;
    },

    _onWindowResizeHandler : function(event)
    {
        visual._stageWidth = window.innerWidth;
        visual._stageHeight = window.innerHeight;
        visual._context.canvas.width = visual._stageWidth;
        visual._context.canvas.height = visual._stageHeight;

        for(var i = 0, len = visual._bezierPointList.length; i < len; i++)
        {
            var bp = visual._bezierPointList[i];
            bp.move();
            visual._layout(bp, i/len);
            bp.saveDefault();
        }

    }

};

function BezierPoint()
{
    this.init.apply(this, arguments);
};

BezierPoint.prototype =
{
    init: function()
    {
        this.aP = new Pt(0, 0);
        this.cP0 = new Pt(0, 0);
        this.cP1 = new Pt(0, 0);
        this.controllRad = 2 * Math.PI * (Math.random() * (3 - 1) + 1);
        this.anchorRad = 2 * Math.PI * (Math.random() * (2 - 1) + 1);

        x = 100;
        y = 100;
        this._controllDefaultPoint = new Pt(0, 0);
        this._anchorDefaultPoint = new Pt(0, 0);
    },
    saveDefault: function()
    {
        var pt = this.getInPt();
        this._controllDefaultPoint = new Pt(pt.x, pt.y);
        this._anchorDefaultPoint = new Pt(this.x, this.y);

    },
    move : function()
    {
        var speed = 0.005 + 0.02 * Math.random();
        this.controllRad = (this.controllRad + speed * 2 ) % (20 * Math.PI);
        this.anchorRad = (this.anchorRad + speed * 2 ) % (2 * Math.PI);
        this.setInPt(new Pt(visual.CONTROLL_R * Math.cos(this.controllRad) + this._controllDefaultPoint.x,  visual.CONTROLL_R * Math.sin(this.anchorRad) + this._controllDefaultPoint.y));

        this.x = visual.ANCHOR_R * Math.cos(this.anchorRad) + this._anchorDefaultPoint.x;
        this.y = visual.ANCHOR_R * Math.sin(this.anchorRad) + this._anchorDefaultPoint.y;
    },
    getInPt : function()
    {
        return new Pt(this.cP0.x + this.x, this.cP0.y + this.y);
    },
    setInPt : function(pt)
    {
        this.cP0.x = pt.x - this.x;
        this.cP0.y = pt.y - this.y;
    },
    getOutPt : function()
    {
        return new Pt(this.cP1.x + this.x, this.cP1.y + this.y);
    },
    setOutPt : function(pt)
    {
        this.cP1.x = pt.x - this.x;
        this.cP1.y = pt.y - this.y;
    },
    getAncherPt : function()
    {
        return new Pt(this.x, this.y);
    }
};

function Pt(x, y)
{
    this.init.apply(this, arguments);
};

Pt.prototype =
{
    init: function(x, y)
    {
        this.x = x;
        this.y = y;
    },
    clone : function()
    {
        return new Pt(this.x, this.y);
    }
};


