package Logic;

/**
 *
 * @author pytel
 */
class Pixel {
    
    byte R;
    byte G;
    byte B;

    public Pixel(byte R, byte G, byte B) {
        this.R = R;
        this.G = G;
        this.B = B;
    }

    public int getR() {
        return R & 0xFF;
    }

    public int getG() {
        return G & 0xFF;
    }

    public int getB() {
        return B & 0xFF;
    }

    public void setR(byte R) {
        this.R = R;
    }

    public void setG(byte G) {
        this.G = G;
    }

    public void setB(byte B) {
        this.B = B;
    }

    @Override
    public String toString() {
        return "Pixel{" + "R=" + getR() + ", G=" + getG()+ ", B=" + getB() + '}';
    }
    
}
