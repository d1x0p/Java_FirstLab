public class Complex {
    private final double re;
    private final double im;

    public Complex(double re, double im){
        this.re = re;
        this.im = im;
    }
    public Complex(double re){
        this(re,0.0);
    }

    public double getRe(){
        return this.re;
    }
    public double getIm(){
        return this.im;
    }

    public Complex sum(Complex b){
        return new Complex(this.re + b.re, this.im + b.im);
    }

    public Complex subtract(Complex b){
        return new Complex( this.re - b.re, this.im - b.im );
    }

    public Complex multiply(Complex b){
        return new Complex(
                this.re * b.re - this.im * b.im,
                this.re * b.im + this.im * b.re );
    }

    public Complex divide(Complex b){
        double denominator = b.re * b.re + b.im * b.im;
        if( Math.abs(denominator) < 1e-12 ){
            throw new ArithmeticException("Деление на ноль");
        }
        return new Complex(
                (this.re * b.re + this.im * b.im) / denominator,
                (this.im * b.re - this.re * b.im) / denominator
        );
    }

    @Override
    public String toString(){
        if( Math.abs(im) < 1e-9 ) return String.format("%.2f", re);
        if( Math.abs(re) < 1e-9 ) return String.format("%.2fi", im);
        if( im < 0 ) return String.format("%.2f - %.2fi", re, -im);
        return String.format("%.2f + %.2fi", re, im);
    }


}