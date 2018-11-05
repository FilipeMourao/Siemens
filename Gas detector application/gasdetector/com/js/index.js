//--------------------------------------------------------------------------
//  const var
//--------------------------------------------------------------------------
const COUNT = 250;
const CONTROLL_R = 10;
const ANCHOR_R = 10;
const SPEED = 7;
const LAYOUT_H = 5;
const LAYOUT_R = 100;
const PI = Math.PI;
var _context;
var _stageWidth = window.innerWidth;
var _stageHeight = window.innerHeight;
var _mouseX = _stageWidth*0.5;
var _mouseY = _stageHeight*0.5;
var _bezierPointList = [];
var _color = 255;
var _rotation = 1 / 200;
//--------------------------------------------------------------------------
//  init
//--------------------------------------------------------------------------
function init()
{
  var canvas = document.getElementById( 'world' );
  if ( ! canvas || ! canvas.getContext ) { return false; }
  _context = canvas.getContext('2d');
  _build();
  window.addEventListener('resize', _onWindowResizeHandler, false);
  _onWindowResizeHandler();
  document.addEventListener('mousemove', _onMouseMoveHandler, false);
  document.addEventListener('touchstart', _onTouchHandler, false);
  document.addEventListener('touchmove', _onTouchHandler, false);
  document.addEventListener("touchend", _onTouchHandler, false);
  setInterval( _onEnterFrameHandler, 1000 / 30 );
}
//--------------------------------------------------------------------------
//  function
//--------------------------------------------------------------------------
function _build()
{
  for(var i = 0, len = COUNT; i < len; i++)
  {
    var bp = new BezierPoint();
    _layout(bp, i/len);
    bp.saveDefault();
    _bezierPointList[i] = bp;
  }
}
function _layout(bp, percent)
{
  var rad = 2 * PI * percent * 5;
  bp.x = LAYOUT_R * Math.sin(rad) + _stageWidth * 0.5;
  bp.y = LAYOUT_R * Math.cos(rad) + _stageHeight * 0.5;
  var pt = new Pt(LAYOUT_H * Math.cos(PI * 0.5 + rad) + bp.x, LAYOUT_H * Math.sin(PI * 0.5 + rad) + bp.y);
  bp.setInPt(pt);
}
function _move()
{
  for(var i = 0, len = _bezierPointList.length; i < len; i++)
  {
    var bp = _bezierPointList[i];
    bp.move();
  }
}
function _draw()
{
  _color = _color //(_color< 2 * PI) ? _color + 0.1 : _color - 2 * PI;
  _context.beginPath();
  _context.strokeStyle = _getColor(Math.sin(_color) * 100 + 200, _mouseX / _stageWidth * 200 + 50, _mouseY / _stageHeight * 200 + 50);
  _context.strokeStyle = "#47b3f7";
    _context.lineWidth = 2;
  for(var i = 0, len = _bezierPointList.length - 1; i < len; i++)
  {
    _lineTo(_bezierPointList[i], _bezierPointList[i + 1]);
  }
  _lineTo(_bezierPointList[_bezierPointList.length -1], _bezierPointList[0]);
  _context.stroke();
}
function _lineTo(p0, p1)
{
  var p0A = p0.getAncherPt();
  var p0CO = p0.getOutPt();
  var p1A = p1.getAncherPt();
  var p1CI = p1.getInPt();
  _context.moveTo(p0A.x, p0A.y);
  _context.bezierCurveTo(p0CO.x, p0CO.y, p1CI.x, p1CI.y, p1A.x, p1A.y);
}
function _fade()
{
  var n = _stageHeight / _stageWidth;
  var speedX, speedY;
  if(n<1)
  {
    speedX = SPEED;
    speedY = SPEED * n;
  }else
  {
    speedX = SPEED / n;
    speedY = SPEED;
  }
  var w = _context.canvas.width;
  var h = _context.canvas.height;
  _context.save();
  _context.translate(w * 0.5, h *0.5);
    
  _context.rotate(_rotation);
  _context.drawImage( _context.canvas, -speedX - w *0.5, -speedY - h * 0.5, w + 2 * speedX, h + 2 * speedY);
  _context.restore();
  _context.fillStyle = "rgba(0, 0, 0, 0.05)";
  _context.fillRect (0, 0, _stageWidth, _stageHeight);
}
function _getColor(r, g, b)
{
  var num = Math.ceil(r << 16 | g << 8 | b);
  return "#" + num.toString(16);
}
//--------------------------------------------------------------------------
//  handler
//--------------------------------------------------------------------------
function _onEnterFrameHandler()
{
  _context.save();
  _fade();
  _move();
  _draw();
  _context.restore();
  
  _stats.update();
}
function _onTouchHandler(event)
{
  if(!event.touches[0])
    return;
      
  _mouseX = event.touches[0].screenX;
  _mouseY = event.touches[0].screenY;
  event.preventDefault();
}
function _onMouseMoveHandler(event)
{
  _mouseX = event.clientX;
  _mouseY = event.clientY;
}
function _onWindowResizeHandler(event)
{
    _stageWidth = window.innerWidth;
    _stageHeight = window.innerHeight;
    _context.canvas.width = _stageWidth;
    _context.canvas.height = _stageHeight;

  for(var i = 0, len = _bezierPointList.length; i < len; i++)
  {
    var bp = _bezierPointList[i];
    bp.move();
    _layout(bp, i/len);
    bp.saveDefault();
  }
    
}
//--------------------------------------------------------------------------
//  class
//--------------------------------------------------------------------------
function BezierPoint()
{
  this.init.apply(this, arguments);
}
BezierPoint.prototype =
{
  init: function()
  {
    this.aP = new Pt(0, 0);
    this.cP0 = new Pt(0, 0);
    this.cP1 = new Pt(0, 0);
    this.controllRad = 2 * PI * (Math.random() * (2 - 1) + 1);
    this.anchorRad = 2 * PI * (Math.random() * (2 - 1) + 1);
    
    this.x = 100;
    this.y = 100;
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
    var speed = 0.01 + 0.04 * Math.random();
    this.controllRad = (this.controllRad + speed * 2 ) % (20 * PI);
    this.anchorRad = (this.anchorRad + speed * 2 ) % (2 * PI);
    
    this.setInPt(new Pt(CONTROLL_R * Math.cos(this.controllRad) + this._controllDefaultPoint.x,  CONTROLL_R * Math.sin(this.anchorRad) + this._controllDefaultPoint.y));
    
    this.x = ANCHOR_R * Math.cos(this.anchorRad) + this._anchorDefaultPoint.x;
    this.y = ANCHOR_R * Math.sin(this.anchorRad) + this._anchorDefaultPoint.y;
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
}
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
//--------------------------------------------------------------------------
//  run
//--------------------------------------------------------------------------
window.onload = function(){init();};
