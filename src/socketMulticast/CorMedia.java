package socketMulticast;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

/**
 * @author vinicius
 */
public class CorMedia {
    public static void main(String[] args) {
        
        CorMedia.image();
        
    }
    public static void image(){
        File file = new File("/home/vinicius/NetBeansProjects/CorMedia/src/image/RGB.png");
        Color color;
        int r=0, g=0, b=0;
        
        try {
            BufferedImage buffered = ImageIO.read(file);
            for(int i = 0; i< buffered.getWidth();i++){
                for(int j = 0; j< buffered.getHeight();j++){
                       color = new Color(buffered.getRGB(i,j));
                       r+=color.getRed();
                       g+=color.getGreen();
                       b+=color.getBlue();
                      // System.out.println(color);
                }
            }
           int[] avg = new int[3];
           avg[0] =  (int)(r/(buffered.getWidth()*buffered.getHeight()));
           avg[1] =  (int)(g/(buffered.getWidth()*buffered.getHeight()));
           avg[2] =  (int)(b/(buffered.getWidth()*buffered.getHeight()));
           
            System.out.println(avg[0]);
            System.out.println(avg[1]);
            System.out.println(avg[2]);
        } catch (IOException ex) {
            System.out.println("Erro: "+ ex);
        }
    }
}
