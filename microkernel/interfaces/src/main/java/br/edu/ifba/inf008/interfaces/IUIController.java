package br.edu.ifba.inf008.interfaces;

import javafx.scene.control.MenuItem;
import javafx.scene.Node;

public interface IUIController
{
    public abstract MenuItem createMenuItem(String menuText, String menuItemText);
    public abstract boolean createTabOnLeft(String tabText, String resultText);
    public abstract boolean createTabOnRight(String tabText, Node contents);
    public abstract void showPopup(String text);
}
