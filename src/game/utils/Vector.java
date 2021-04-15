package game.utils;

public class Vector {
    private double x;
    private double y;

    public Vector(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double x() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double y() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public void add(Vector vector){
        this.x += vector.x();
        this.y += vector.y();
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

    public void offSetX(double dx){
        this.x += dx;
    }

    public void offSetY(double dy){
        this.y += dy;
    }

    public void reverse(){
        this.x = -this.x;
        this.y = -this.y;
    }

    public static Vector reverse(Vector vector){
        return new Vector(-vector.x(),-vector.y());
    }



}
