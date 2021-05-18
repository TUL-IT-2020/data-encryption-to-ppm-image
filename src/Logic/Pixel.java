package Logic;

/**
 * Store data internali as signed Byte.
 * @author pytel
 */
public class Pixel {
    
    private byte R;
    private byte G;
    private byte B;

    public Pixel(byte R, byte G, byte B) {
        this.R = R;
        this.G = G;
        this.B = B;
    }
    
    /**
     * Return value of unsigned Byte.
     * @return 
     */
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
