import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
class Cell extends JButton {
	private int i;
	private int j;
	private int value;
	private boolean visible = false;
	public Cell(int i, int j) {
		super(" ");
		this.i = i;
		this.j = j;
	}
	public int getI(){
		return i;
	}
	public int getJ(){
		return j;
	}
	public int getValue() {
		return value;
	}
	public void setValue(int value) {
		this.value = value;
	}
	public boolean getVisible() {
		return visible;
	}
	public void setVisible(boolean visible) {
		this.visible = visible;
	}
}
