import javax.swing.JPanel;
import javax.swing.JTextField;

public class TFCText extends TFCComponent {
	
	private JTextField textField;
	private String text = "";
	private String path = "";
	
	public TFCText() {}
	
	public TFCText(String name) {
		super("   " + name);
		this.textField = new JTextField();
		this.inFileName = "File*" + name;
	}
	
	public TFCText(String name, String text) {
		super("   " + name);
		this.textField = new JTextField(text);
		this.text = text;
	}
	
	public TFCText(String name, String text, String path) {
		super("   " + name);
		this.textField = new JTextField(text);
		this.text = text;
		this.path = path;
	}
	
	public TFCText(String name, String text, String path, String inFileName) {
		super("   " + name);
		this.textField = new JTextField(text);
		this.text = text;
		this.path = path;
		this.inFileName = inFileName;
	}
	
	
	public String getName() {
		return name.getText().substring(3);
	}
	
	public void setName(String name) {
		this.name.setText("   " + name);
	}
	
	public void resetText() {
		textField.setText(text);
	}
	
	public void saveTextChanges() {
		text = textField.getText();
	}
	
	public boolean wasChanged() {
		return text != textField.getText();
	}
	
	
	public String getText() {
		return text;
	}
	
	public void setText(String text) {
		this.textField.setText(text);
	}
	
	public JTextField getTextField() {
		return textField;
	}
	
	public String getPath() {
		return path;
	}
	
	public void setPath(String path) {
		this.path = path;
	}
	

	@Override
	public void dropComponents(JPanel panel) {
		panel.add(name);
		panel.add(textField);
	}

	@Override
	public String getInFileName() {
		return inFileName;
	}
	
}