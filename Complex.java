// Mehrad Hajati, COMP 2631, Complex Class

public class Complex{
    
    // Instance variables
    private double imaginary;
    private double real;

    // Constructor 1
    public Complex(double inReal, double inImag){
        this.imaginary = inImag;
        this.real = inReal;
    }

    // Constructor 2
    public Complex(Complex c){
        double inReal = c.getReal();
        double inImag = c.getImag();
        this.imaginary = inImag;
        this.real = inReal;
    }

    // Getter methods
    public double getImag(){
        return this.imaginary;
    }

    public double getReal(){
        return this.real;
    }

    // Setter methods
    public void setImag(double inImag){
        this.imaginary = inImag;
    }

    public void setReal(double inReal){
        this.real = inReal;
    }

    
    // Multiplication method
    public void multiply(Complex c){
        double inReal = c.getReal();
        double inImag = c.getImag();
        double currReal = getReal();
        double currImag = getImag();
        this.imaginary = (currImag * inReal) + (currReal * inImag);
        this.real = (currReal * inReal) + (currImag * inImag * (-1));
    }

    // Addition method
    public void add(Complex c){
        this.imaginary = imaginary + c.getImag();
        this.real = real + c.getReal(); 
    }

    // Subtraction method
    public void subtract(Complex c){
        this.imaginary = imaginary - c.getImag();;
        this.real = real - c.getReal(); 
    }

    // Modulus method
    public double modulus(){
        double total = Math.pow(imaginary, 2) + Math.pow(real, 2);
        return Math.sqrt(total);
    }

}