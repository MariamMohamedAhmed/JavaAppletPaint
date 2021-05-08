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
public class Line {

    int x1;
    int x2;
    int y1;
    int y2;
    Color color;// reference to object from class Color to send the color to the shape to avoid conflicts printing the past shapes
    boolean dash;// to have for each object feature to be dashed or not
    int count=-1;

    public Line(int x1, int y1, int x2, int y2, Color color, boolean dash, int count) {
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
        this.color = color;
        this.dash = dash;
        this.count = count;
    }

}
