import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class ReadFile {
	
	private File f = new File("C:/java/hello");

	public void read(int numOfWeights) {
		try {
			InputStream in = new FileInputStream(f);
			int size = in.available();
			double[] weights = new double[numOfWeights];
			int index = 0;
			for(int i = 0; i < size; i++) {
	            if (!((char)in.read() == ' ')) {
	            
	            }
	         }
	         in.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public void write() {
		
	}
}
