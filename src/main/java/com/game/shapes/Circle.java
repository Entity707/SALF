package com.game.shapes;

public value class Circle {
  private long x;
  private long y;
  private long radius;
  
  public Circle(long x, long y, long radius) {
    this.x = x;
    this.y = y;
    this.radius = radius;
  }
  
  public long getRadius() {
    return radius;
  }
  
  public long getX() {
    return x;
  }
  
  public long getY() {
    return y;
  }
  
  public Circle set(long x, long y, long radius){
    return new Circle(x,y,radius);
  }
  public Circle change(long x, long y, long radius){
    return new Circle(x,y,radius);
  }
  
}
