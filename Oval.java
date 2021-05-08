/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package project;

import java.awt.Color;

/**
 *
 * @author Mariam
 */
public class Oval {

    int x;
    int y;
    int width;
    int height;
    Color color;// reference to object from class Color to send the color to the shape to avoid conflicts printing the past shapes
    boolean fill;// to have for each object feature to be filled or not
    boolean dash;// to have for each object feature to be dashed or not
    int count=-1;

    public Oval(int x, int y, int width, int height, Color color, boolean fill, boolean dash, int count) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.color = color;
        this.fill = fill;
        this.dash = dash;
        this.count = count;
    }

}
