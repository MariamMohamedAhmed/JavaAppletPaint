/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package project;

import java.applet.Applet;
import java.awt.BasicStroke;
import java.awt.Button;
import java.awt.Checkbox;
import java.awt.Color;
import static java.awt.Color.*;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Stroke;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

/**
 *
 * @author Mariam
 */
public class Paint extends Applet {

    int count = -1;//count for all shapes to show drawings in order

    Button red;
    Button green;
    Button blue;
    Button rect;
    Button line;
    Button oval;
    Button draw;// free style
    Button clear;
    Button clearall;
    Button undo;
    Button save;// to save drawing
    Button open;// to open an image

    Checkbox dots;
    Checkbox filled;

    int x1, x2, y1, y2, x3, y3;// variables for points of pressing, releasing, and dragging respectively4
    Color color;//for changing colors
    boolean fill;// true for filled shapes if checkbox pressed
    boolean dash;// true for dashed lines of shapes if checkbox pressed

    int width = 2000;//for the image to be saved
    int height = 2000;
    BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

    int colorFlag = 0; // 1,2,3 for red, green, and blue respectivly
    int shapeFlag = -1; // 0,1,2,3 for line, rect, oval, and draw respoectivly
    int freeflag = 0;
    int dotFlag = 0;// to check on the checkbox
    int filledFlag = 0;
    int imgFlag = 0;

    ArrayList<Line> lines; // dynamic array
    ArrayList<Rectangle> rects;
    ArrayList<Oval> ovals;
    ArrayList<Color> colors;
    ArrayList<Line> frees;// array of class line to carry points to do the free drawing
    ArrayList<Line> whiteLines;

    @Override
    public void init() {

        //b7gz el arrays ely hshel fehom el past shapes
        lines = new ArrayList<>();
        rects = new ArrayList<>();
        ovals = new ArrayList<>();
        colors = new ArrayList<>();
        frees = new ArrayList<>();
        whiteLines = new ArrayList<>();

        //buttons
        red = new Button("Red");
        green = new Button("Green");
        blue = new Button("Blue");
        rect = new Button("Rectangle");
        line = new Button("Line");
        oval = new Button("Oval");
        draw = new Button("Draw");
        clear = new Button("Eraser");
        clearall = new Button("Clear All");
        undo = new Button("Undo");
        save = new Button("Save");
        open = new Button("Open");

        //checkbox
        dots = new Checkbox("Dotted");
        filled = new Checkbox("Fill Shapes");

        //buttons listeners
        red.addActionListener( // all buttons listener using anonymous inner class 
                new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ev) {
                colorFlag = 1;

            }
        });
        green.addActionListener(
                new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ev) {
                colorFlag = 2;

            }
        });
        blue.addActionListener(
                new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ev) {
                colorFlag = 3;

            }
        });
        line.addActionListener(
                new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ev) {
                shapeFlag = 0;
                freeflag = 0;//to avoid repainting in dragging
            }
        });
        rect.addActionListener(
                new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ev) {
                shapeFlag = 1;
                freeflag = 0;//to avoid repainting in dragging
            }
        });
        oval.addActionListener(
                new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ev) {
                shapeFlag = 2;
                freeflag = 0;//to avoid repainting in dragging
            }
        });
        draw.addActionListener(
                new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ev) {
                shapeFlag = 3;
                freeflag = 1;// to repaint while dragging
            }
        });
        clear.addActionListener(
                new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ev) {
                shapeFlag = 4;
                freeflag = 0;
            }
        });
        clearall.addActionListener(
                new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ev) {

                freeflag = 0;
                lines.clear();
                frees.clear();
                rects.clear();
                ovals.clear();
                x1 = x2 = x3 = y1 = y2 = y3 = 0;
                repaint();

            }
        });
        undo.addActionListener(
                new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ev) {
                if (lines.size() > 0) {//3shan lma yegy y3ml get lw l size b 0 hyb2a out of bound exception
                    if (lines.get(lines.size() - 1).count == count) {// lw a5r wa7da fl lines hya de a5r 7aga et7atet emsa7ha(3shan ana ay w7da b3mlha bb3tlha l count l gded)
                        lines.remove((lines.size()) - 1);// shel a5r line lw kan a5r 7aga mrsoma kant line
                        count--;//because I removed one shape I should decrase the count
                    }
                }
                if (rects.size() > 0) {
                    if (rects.get(rects.size() - 1).count == count) {
                        rects.remove((rects.size()) - 1);
                        count--;
                    }
                }
                if (ovals.size() > 0) {
                    if (ovals.get(ovals.size() - 1).count == count) {
                        ovals.remove((ovals.size()) - 1);
                        count--;
                    }
                }
                if (frees.size() > 0) {
                    if (frees.get(frees.size() - 1).count == count) {
                        frees.remove((frees.size()) - 1);
                        count--;

                    }
                }
                if (whiteLines.size() > 0) {
                    if (whiteLines.get(whiteLines.size() - 1).count == count) {
                        whiteLines.remove((whiteLines.size()) - 1);
                        count--;

                    }
                }

                shapeFlag = -1;// 3shan myro7sh y add tany bl x wl y el mwgoden
                repaint();
            }
        });
        save.addActionListener(
                new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ev) {
                imgFlag = 1;
                repaint();
            }
        });

        //checkboxes listerners
        dots.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == 1) {
                    dotFlag = 1;

                } else {
                    dotFlag = 0;

                }
            }

        });

        filled.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == 1) {
                    filledFlag = 1;

                } else {
                    filledFlag = 0;

                }
            }

        });

        //buttons adding
        add(red);
        add(green);
        add(blue);
        add(line);
        add(rect);
        add(oval);
        add(draw);
        add(clear);
        add(clearall);
        add(undo);
        add(save);

        //checkbox adding
        add(dots);
        add(filled);

        //mouse liseners for all the drawing
        this.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            public void mousePressed(MouseEvent e) {
                x1 = e.getX();
                y1 = e.getY();
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                x2 = e.getX();
                y2 = e.getY();
                repaint();
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            public void mouseExited(MouseEvent e) {
                //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }
        }
        );

        this.addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {

                if (freeflag == 1) { // to avoid repainting in drag except in that case (by3ml 5tot kter gmb b3d)
                    x3 = e.getX();
                    y3 = e.getY();
                    repaint();

                }
            }

            @Override
            public void mouseMoved(MouseEvent e) {
                //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

        });
    }

    @Override
    public void paint(Graphics g) {
       
        
        Graphics2D g2 = (Graphics2D) g;
        g2.setStroke(new BasicStroke(10));//make lines thicker

        Graphics2D g2d = image.createGraphics();

        g2d.setColor(white);
        g2d.fillRect(0, 0, width, height);
        

        // to know the selected color for any instance
        switch (colorFlag) {
            case 0:
                color = BLACK;
                //colors.add(BLACK);// cant set color every repaint bec it will color everything with it
                //g.setColor(Color.black);
                break;
            case 1:
                color = RED;
                //colors.add(RED);
                //g.setColor(Color.red);
                break;
            case 2:
                color = GREEN;
                //colors.add(GREEN);
                //g.setColor(Color.green);
                break;
            case 3:
                color = BLUE;
                //colors.add(BLUE);
                //g.setColor(Color.blue);
                break;
            default:
                break;
        }

        switch (shapeFlag) {
            case 0:
                if (dotFlag == 1) {
                    count++;
                    lines.add(new Line(x1, y1, x2, y2, color, true, count));
                    //shapes.add(new Shape(lines.get((lines.size()) - 1)));
                } else {
                    count++;
                    lines.add(new Line(x1, y1, x2, y2, color, false, count));
                    //shapes.add(new Shape(lines.get((lines.size()) - 1)));
                }

                break;
            case 1:
                if (dotFlag == 1) {
                    if (filledFlag == 1) {
                        count++;
                        rects.add(new Rectangle(x1, y1, x2 - x1, y2 - y1, color, true, true, count));// filled and dashed
                        //shapes.add(new Shape(rects.get((rects.size()) - 1)));

                    } else {
                        count++;
                        rects.add(new Rectangle(x1, y1, x2 - x1, y2 - y1, color, false, true, count));// not filled and dashed
                        // shapes.add(new Shape(rects.get((rects.size()) - 1

                    }
                } else {
                    if (filledFlag == 1) {
                        count++;
                        rects.add(new Rectangle(x1, y1, x2 - x1, y2 - y1, color, true, false, count));// filled and not dashed
                        //shapes.add(new Shape(rects.get((rects.size()) - 1)));

                    } else {
                        count++;
                        rects.add(new Rectangle(x1, y1, x2 - x1, y2 - y1, color, false, false, count));// not filled and not dashed
                        //shapes.add(new Shape(rects.get((rects.size()) - 1)));

                    }
                }

                break;
            case 2:
                //ovals
                if (dotFlag == 1) {
                    if (filledFlag == 1) {
                        count++;
                        ovals.add(new Oval(x1, y1, x2 - x1, y2 - y1, color, true, true, count));// filled and dashed
                        //shapes.add(new Shape(ovals.get((ovals.size()) - 1)));

                    } else {
                        count++;
                        ovals.add(new Oval(x1, y1, x2 - x1, y2 - y1, color, false, true, count));// not filled and dashed
                        //shapes.add(new Shape(ovals.get((ovals.size()) - 1)));
                    }
                } else {
                    if (filledFlag == 1) {
                        count++;
                        ovals.add(new Oval(x1, y1, x2 - x1, y2 - y1, color, true, false, count));// filled and not dashed
                        //shapes.add(new Shape(ovals.get((ovals.size()) - 1)));
                    } else {
                        count++;
                        ovals.add(new Oval(x1, y1, x2 - x1, y2 - y1, color, false, false, count));// not filled and not dashed
                        //shapes.add(new Shape(ovals.get((ovals.size()) - 1)));
                    }
                }
                break;
            case 3:
                //free drawing
                count++;
                frees.add(new Line(x1, y1, x3, y3, color, false, count));// same place to draw points not lines
                //shapes.add(new Shape(frees.get((frees.size()) - 1)));
                x1 = x3;
                y1 = y3;
                break;
            case 4:
                //eraser
                count++;
                whiteLines.add(new Line(x1, y1, x2, y2, white, false, count));
                x1 = x3;
                y1 = y3;
                break;
            default:

                break;
        }

        // print all shapes
        for (int j = 0; j <= count; j++) {
            //lines
            for (int i = 0; i < lines.size(); i++) {// loop over the array of the Line class and get every time all the attributes of this iteration
                //get(i) of the array .x1 of the class
                if (lines.get(i).count == j) {
                    if (lines.get(i).dash) {// lw m7tota dashed ersemha dashed
                        Stroke dashed = new BasicStroke(3, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[]{9}, 0);// I took this line from the internet, its a known thing I guess
                        g2.setStroke(dashed);
                        g2d.setStroke(dashed);
                    } else {
                        g2.setStroke(new BasicStroke(10));
                        g2d.setStroke(new BasicStroke(10));
                    }
                    //for the applet
                    g.setColor(lines.get(i).color);
                    g.drawLine((lines.get(i)).x1, (lines.get(i)).y1, (lines.get(i)).x2, (lines.get(i)).y2);
                    //for the image
                    g2d.setColor(lines.get(i).color);
                    g2d.drawLine((lines.get(i)).x1, (lines.get(i)).y1, (lines.get(i)).x2, (lines.get(i)).y2);
                }
            }
            //rectangles
            for (int i = 0; i < rects.size(); i++) {// loop over the array of the Rectangle class and get every time all the attributes of this iteration
                if (rects.get(i).count == j) {
                    if (rects.get(i).dash) {// lw m7tota dashed ersemha dashed
                        Stroke dashed = new BasicStroke(3, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[]{9}, 0);// I took this line from the internet, its a known thing I guess
                        g2.setStroke(dashed);
                        g2d.setStroke(dashed);
                    } else {
                        g2.setStroke(new BasicStroke(10));
                        g2d.setStroke(new BasicStroke(10));
                    }
                    //for the applet
                    g.setColor(rects.get(i).color);
                    g.drawRect(rects.get(i).x, rects.get(i).y, rects.get(i).width, rects.get(i).height);
                    
                    //for the image 
                    g2d.setColor(rects.get(i).color);
                    g2d.drawRect(rects.get(i).x, rects.get(i).y, rects.get(i).width, rects.get(i).height);

                    if (rects.get(i).fill) {//lw m7tota filled ersmha filled
                        //for the applet
                        g.fillRect(rects.get(i).x, rects.get(i).y, rects.get(i).width, rects.get(i).height);
                        //for the image
                        g2d.fillRect(rects.get(i).x, rects.get(i).y, rects.get(i).width, rects.get(i).height);
                    }
                }
            }

            //ovals
            for (int i = 0; i < ovals.size(); i++) {// loop over the array of the Oval class and get every time all the attributes of this iteration
                if (ovals.get(i).count == j) {

                    if (ovals.get(i).dash) {// lw m7tota dashed ersemha dashed
                        Stroke dashed = new BasicStroke(3, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[]{9}, 0);// I took this line from the internet, its a known thing I guess
                        g2.setStroke(dashed);
                        g2d.setStroke(dashed);
                    } else {
                        g2.setStroke(new BasicStroke(10));
                        g2d.setStroke(new BasicStroke(10));
                    }
                    //for the applet
                    g.setColor(ovals.get(i).color);
                    g.drawOval(ovals.get(i).x, ovals.get(i).y, ovals.get(i).width, ovals.get(i).height);
                    //for the image
                    g2d.setColor(ovals.get(i).color);
                    g2d.drawOval(ovals.get(i).x, ovals.get(i).y, ovals.get(i).width, ovals.get(i).height);

                    if (ovals.get(i).fill) {//lw m7tota filled ersmha filled
                        g.fillOval(ovals.get(i).x, ovals.get(i).y, ovals.get(i).width, ovals.get(i).height);
                        g2d.fillOval(ovals.get(i).x, ovals.get(i).y, ovals.get(i).width, ovals.get(i).height);
                    }
                }
            }

            //the free drawing
            for (int i = 0; i < frees.size(); i++) {// loop over the array of the Line class and get every time all the attributes of this iteration
                //get(i) of the array .x1 of the class
                if (frees.get(i).count == j) {
                    //applet
                    g.setColor(frees.get(i).color);
                    g.drawLine((frees.get(i)).x1, (frees.get(i)).y1, (frees.get(i)).x2, (frees.get(i)).y2);
                    //image
                    g2d.setColor(frees.get(i).color);
                    g2d.drawLine((frees.get(i)).x1, (frees.get(i)).y1, (frees.get(i)).x2, (frees.get(i)).y2);
                }
            }

            //the eraser
            for (int i = 0; i < whiteLines.size(); i++) {// loop over the array of the Line class and get every time all the attributes of this iteration
                //get(i) of the array .x1 of the class
                if (whiteLines.get(i).count == j) {
                    //applet
                    g.setColor(whiteLines.get(i).color);
                    g.drawLine((whiteLines.get(i)).x1, (whiteLines.get(i)).y1, (whiteLines.get(i)).x2, (whiteLines.get(i)).y2);
                    //image
                    g2d.setColor(whiteLines.get(i).color);
                    g2d.drawLine((whiteLines.get(i)).x1, (whiteLines.get(i)).y1, (whiteLines.get(i)).x2, (whiteLines.get(i)).y2);

                }
            }

        }
        if (imgFlag == 1) {

            g2d.dispose();
            imgFlag = 0;
            File file = new File("newPaint.jpg");
            try {
                ImageIO.write(image, "jpg", file);
            } catch (IOException ex) {
                Logger.getLogger(Paint.class.getName()).log(Level.SEVERE, null, ex);
            }

        }

    }

}
