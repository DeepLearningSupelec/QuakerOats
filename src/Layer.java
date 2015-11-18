import java.util.ArrayList;
import java.util.List;


public abstract class Layer<T> {

	private List<T> layer;

	public Layer(int size) {
		// TODO Auto-generated constructor stub
		this.layer = new ArrayList<T>(size);
	}

	public List<T> getLayer() {
		return layer;
	}

	public void setLayer(List<T> layer) {
		this.layer = layer;
	}

}
