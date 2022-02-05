import javax.swing.JLabel;
import javax.swing.JPanel;

public abstract class TFCComponent {
	
	JLabel name;
	String inFileName;
	
	public TFCComponent() {
		name = new JLabel();
	}
	
	public TFCComponent(String name) {
		this.name = new JLabel(name);
		
	}
	
	
	public String getName() {
		return name.getText();
	}
	
	public void setName(String name) {
		this.name.setText(name);
	}
	
	public JLabel getNameLabel() {
		return name;
	}
	
	public void setNameLabel(JLabel name) {
		this.name = name;
	}
	
	public String getInFileName() {
		return inFileName;
	}
	
	public void setInFileName(String inFileName) {
		this.inFileName = inFileName;
	}
	
	public abstract void dropComponents(JPanel panel);
	
	public String toString() {
		return this.getClass() + ", name: " + name.getText();
	}
}