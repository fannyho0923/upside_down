package game.utils;

public class Vector {
    private double x;
    private double y;

    public Vector(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public Vector(double x1, double x2, double y1, double y2){
        this.x = x2-x1;
        this.y = y2-y1;
    }

    public double x() {
        return x;
    }

    public double absX(){
        return Math.abs(x);
    }

    public void setX(double x) {
        this.x = x;
    }

    public double y() {
        return y;
    }

    public double absY(){
        return Math.abs(y);
    }

    public void setY(double y) {
        this.y = y;
    }

    public void add(Vector vector){
        this.x += vector.x();
        this.y += vector.y();
    }

    public void addX(double dx){
        this.x += dx;
    }

    public void addY(double dy){
        this.y += dy;
    }

    public void minus(Vector vector){
        this.x -= vector.x();
        this.y -= vector.y();
    }

    public void multiply(double multiplier){
        this.x *= multiplier;
        this.y *= multiplier;
    }

    public static Vector multiply(Vector vector, double multiplier){
        return new Vector(vector.x() * multiplier, vector.y * multiplier);
    }

    public double dot(Vector vector){
        return this.x * vector.x() + this.y * vector.y();
    }

    public double length(){
        return Math.sqrt(this.x * this.x + this.y * this.y);
    }

    public double angleX(){
        return Math.toDegrees(Math.atan(this.y/this.x));
    }

    public double angleY(){
        return Math.toDegrees(Math.atan(this.x/this.y));
    }

    public double angle(Vector vector){
        return this.angleX() - vector.angleX();
    }

    public Vector unitVector(){
        double n = length()/ Global.UNIT;
        return this.multiply(this,1.0/n);
    }

    public void zero(){
        this.x = 0;
        this.y = 0;
    }

    public void zeroX(){
        this.x = 0;
    }

    public void zeroY(){
        this.y = 0;
    }



}
