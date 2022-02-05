import javax.swing.JPanel;
import javax.swing.JSeparator;

public class TFCSeparator extends TFCComponent {
		
	public TFCSeparator() {}
	
	public TFCSeparator(String name) {
		name = name.toUpperCase();
		this.name.setText(name);
		this.inFileName = "Separator*" + name;
	}
	
	
	public void setName(String name) {
		name = name.toUpperCase();
		this.name.setText(name);
		this.inFileName = "Separator*" + name;
	}
	
	
	@Override
	public void dropComponents(JPanel panel) {
		panel.add(new JPanel());
		panel.add(name);
		panel.add(new JSeparator());
	}


}