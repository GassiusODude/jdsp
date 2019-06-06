package jdsp.swing;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Rectangle2D;
import java.security.InvalidParameterException;
import java.lang.RuntimeException;
import java.util.ArrayList;
import javax.swing.JPanel;
import jdsp.dataformat.DataObject;

public class Plot extends JPanel {
    public final static long serialVersionUID = 0;
    protected final Color colorBG = Color.WHITE;
    protected final Color colorFG = Color.BLACK;
    public static final Color[] COLOR_MAP = {
        Color.RED, Color.BLUE, Color.GREEN, Color.MAGENTA,
        Color.ORANGE, Color.CYAN, Color.PINK};
    
    protected String labelX = "X";
    protected String labelY = "Y";
    protected String labelTitle = "Default Plot Title";
    public boolean displayGrid = true;
    public boolean displayLegend = true;
    protected String lineType = "--";

    protected float[] axes = {0.0f, 1.0f, 0.0f, 1.0f};
    protected int nGridLines = 8;
    protected int marginX = 40;
    protected int marginY = 40;
    protected int plotWidth;
    protected int plotHeight;
    protected int sideLeft;
    protected int sideRight;
    protected int sideTop;
    protected int sideBottom;
    protected String marker = " ";
    protected int markerWidth = 10;
    protected int markerHeight = 10;
    protected int locXAxis = 0;
    protected int locYAxis = 0;
    protected int lineWidth = 1;
    private FontMetrics _fontMetrics;
    private Rectangle2D _stringBounds;

    protected DataObject data;
    public Plot(){
        data = new DataObject("Default Plot Data");
    }

    /**
     * Set the new axes
     * @param newAxes The new axes (min x, max x, min y, max y)
     */
    public void setAxes(float[] newAxes){
        // -------------------------- error checking  -----------------------
        if (newAxes.length != 4)
            throw new InvalidParameterException("Axes should be 4 element");

        // update axes
        System.arraycopy(newAxes, 0, axes, 0, 4);
        updateAxesLoc();
    }

    public void setData(DataObject data){
        this.data = data;
    }

    /**
     * Set the labels
     * @param x The x-label (horizontal)
     * @param y The y-label (vertical)
     * @param title The title of the plot.
     */
    public void setLabels(String x, String y, String title){
        this.labelX = x;
        this.labelY = y;
        this.labelTitle = title;
    }

    /**
     * Set the margins
     * @param marginX Set the horizontal margins
     * @param marginY Set the vertical margins
     */
    public void setMargin(int marginX, int marginY){
        this.marginX = marginX;
        this.marginY = marginY;
    }
    public void setMarker(String marker, int width, int height){
        this.marker = marker;
        this.markerWidth = width;
        this.markerHeight = height;
    }

    /**
     * Set the size of the plot
     * @param width New width
     * @param height New height
     */
    public void setPlotSize(int width, int height){
        this.plotWidth = width;
        this.plotHeight = height;
    }

    /**
     * paintComponent handles the drawing of the plot.
     */
    @Override
    public void paintComponent(Graphics g){
        g = (Graphics2D) g;

        // update parameters
        updatePlotLocations();
        updateMarkerDetail(g);

        // --------------------  draw background grid axes  ---------------------------------------
        drawBackground(g);

        // limit area to draw
        g.setClip(sideLeft, sideTop, plotWidth, plotHeight);
        drawData(g);

        // return to full clip
        g.setClip(0, 0, this.getWidth(), this.getHeight());

        // draw labels and titles
        drawLabels(g);
    }

    /**
     * Draw the background.  This includes:
     * 1) Background
     * 2) Draw grid
     * 3) Draw ticks on the axis.
     * 4) Draw labels and title.
     * 
     * @param g Graphics object
     */
    public void drawBackground(Graphics g){
        Graphics2D g2 = (Graphics2D) g;
        float[] dash = {1.0f, 3.0f};
        BasicStroke myStroke;

        // draw background
        g2.setColor(colorBG);
        g2.fillRect(sideLeft, sideTop, plotWidth, plotHeight);

        // ----------------------------  draw grid  -------------------------
        g2.setColor(colorFG);
        
        myStroke = new BasicStroke(1.0f, BasicStroke.CAP_BUTT,
            BasicStroke.JOIN_ROUND, 10.0f, dash, 0.0f);
        g2.setStroke(myStroke);
        float incX = (axes[1] - axes[0]) / nGridLines;
        float incY = (axes[3] - axes[2]) / nGridLines;

        FontMetrics fm = g.getFontMetrics();
        Rectangle2D sb;
        sb = fm.getStringBounds(labelY, g);
        int h = fm.getHeight();
        int w;
        String tmpStr;
        for (int ind0 = 0; ind0 < nGridLines + 1; ind0++){
            // draw y-ticks
            tmpStr = Float.toString(axes[3] - incY * ind0);
            sb = fm.getStringBounds(tmpStr, g2);
            w = (int) sb.getWidth();
            g2.drawString(tmpStr, sideLeft - markerWidth - w,
                sideTop + ind0 * plotHeight / nGridLines);

            // draw x-ticks
            tmpStr = Float.toString(axes[0] + incX * ind0);
            sb = fm.getStringBounds(tmpStr, g2);
            w = (int) sb.getWidth();
            h = (int) sb.getHeight();
            g2.drawString(tmpStr,
                sideLeft + ind0 * plotWidth / nGridLines - w / 2,
                sideBottom + markerHeight + h / 2);

            if (displayGrid){
                // horizontal grid lines
                g2.drawLine(sideLeft,
                    sideTop + ind0 * plotHeight / nGridLines,
                    sideRight, sideTop + ind0 * plotHeight / nGridLines);
                // vertical grid lines
                g2.drawLine(sideLeft + ind0 * plotWidth / nGridLines, sideTop,
                    sideLeft + ind0 * plotWidth / nGridLines, sideBottom);
            }
        }

        // -----------------------------  draw axes  --------------------------------
        // set line characteristics for axes
        myStroke = new BasicStroke(1.0f, BasicStroke.CAP_BUTT,
            BasicStroke.JOIN_ROUND);
        g2.setStroke(myStroke);

        // draw axis
        g2.drawLine(sideLeft, locXAxis, sideRight, locXAxis);
        g2.drawLine(locYAxis, sideTop, locYAxis, sideBottom);

    }


    /**
     * Performs a line plot.
     * @param g Graphics object
     */
    public void drawData(Graphics g){
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
            RenderingHints.VALUE_ANTIALIAS_ON);
        loadLineType(g2);
        for (int ind0 = 0; ind0 < data.getNumFeatures(); ind0++){
            g2.setColor(COLOR_MAP[ind0 % COLOR_MAP.length]);
            draw(g2, data.getFeature(ind0));
        }
    }

    /**
     * Draw a line based on ArrayList vector
     * @param g2 The Graphics2D object.
     * @param aList The
     */
    public void draw(Graphics2D g2, ArrayList aList){
        int[] datX = new int[aList.size()];
        int[] datY = new int[aList.size()];
        float scaleX = axes[1] - axes[0];
        float scaleY = axes[3] - axes[2];
        float normX, normY;
        for (int ind0 = 0; ind0 < aList.size(); ind0++){
            // assume x is just the index used
            normX = (ind0 - axes[0]) / scaleX;
            datX[ind0] = (int) (sideLeft + plotWidth * normX);

            // convert aList scaled by vertical margins and scale 
            normY = ((float) aList.get(ind0) - axes[2]) / scaleY;
            datY[ind0] = (int) (sideBottom - plotHeight * normY);
        }
        if (lineType != " ")
            g2.drawPolyline(datX, datY, aList.size());
        if (marker != " "){
            for (int ind0 = 0; ind0 < aList.size(); ind0++){
                g2.drawString(marker, datX[ind0] - markerWidth / 2, datY[ind0]);
            }
        }
    }
    /**
     * Draw a line based on ArrayList vector
     * @param g2 The Graphics2D object.
     * @param realSig The real sign
     */
    public void draw(Graphics2D g2, float[] realSig){
        int[] datX = new int[realSig.length];
        int[] datY = new int[realSig.length];
        float scaleX = axes[1] - axes[0];
        float scaleY = axes[3] - axes[2];
        float normX, normY;
        for (int ind0 = 0; ind0 < realSig.length; ind0++){
            // assume x is just the index
            normX = (ind0 - axes[0]) / scaleX;
            datX[ind0] = (int) (sideLeft + plotWidth * normX);

            // scale the vertical value based on margins and scale
            normY = (realSig[ind0] - axes[2]) / scaleY;
            datY[ind0] = (int) (sideBottom - plotHeight * normY);
        }
        if (lineType != " ")
            g2.drawPolyline(datX, datY, realSig.length);
        if (marker != " "){
            for (int ind0 = 0; ind0 < realSig.length; ind0++){
                g2.drawString(marker, datX[ind0] - markerWidth / 2, datY[ind0]);
            }
        }
    }
    /**
     * Draw the labels
     * @param g Graphics object
     */
    public void drawLabels(Graphics g){
        _fontMetrics = g.getFontMetrics();
        int labelHeight, labelWidth;
        g.setColor(colorFG);

        // ----------------------  draw labels  ---------------------------
        // draw y label
        _stringBounds = _fontMetrics.getStringBounds(labelY, g);
        labelHeight = (int) _stringBounds.getHeight();
        labelWidth = (int) _stringBounds.getWidth();
        g.drawString(labelY,
            (marginX - labelWidth) / 2,
            (int)(getHeight()) / 2);

        // draw X label
        _stringBounds = _fontMetrics.getStringBounds(labelX, g);
        labelHeight = (int) _stringBounds.getHeight();
        labelWidth = (int) _stringBounds.getWidth();
        g.drawString(labelX, 
            marginX / 2 + (int) (getWidth() - _stringBounds.getWidth()) / 2,
            getHeight() - (marginY - labelHeight) / 2);

        // draw title
        _stringBounds = _fontMetrics.getStringBounds(labelTitle, g);
        labelHeight = (int) _stringBounds.getHeight();
        labelWidth = (int) _stringBounds.getWidth();
        g.drawString(labelTitle,
            marginX / 2 + (int) (getWidth() - labelWidth) / 2,
            (marginY + labelHeight) / 2 );

        if (displayLegend && data != null){
            String featName;
            for (int ind0 = 0; ind0 < data.getNumFeatures(); ind0 ++){
                featName = data.getFeatureName(ind0); 
                _stringBounds = _fontMetrics.getStringBounds(featName, g);
                labelHeight = (int) _stringBounds.getHeight();
                labelWidth = (int) _stringBounds.getWidth();
                g.setColor(COLOR_MAP[ind0]);
                g.drawString(featName, (marginX - labelWidth) / 2,
                    marginY + ind0 * (labelHeight + 5));

            }
        }
    }
    public void loadLineType(Graphics2D g){
        int cap = BasicStroke.CAP_BUTT;
        int join = BasicStroke.JOIN_ROUND;
        float miterlimit = 10.0f;
        float[] dash = {5.0f, 0.0f};
        float dashPhase = 0.0f;
        BasicStroke myStroke;
        switch (this.lineType){
            case "-":
                // regular line
                myStroke = new BasicStroke(lineWidth, cap, join, miterlimit);
                break;
            case "--":
                dash[0] = 5.0f;
                dash[1] = 1.0f;
                myStroke = new BasicStroke(
                    lineWidth, cap, join, miterlimit, dash, dashPhase);
                break;
            case ":":
                dash[0] = 1.0f;
                dash[1] = 3.0f;
                myStroke = new BasicStroke(
                    lineWidth, cap, join, miterlimit, dash, dashPhase);
                break;
            case "-.":
                dash = new float[4];
                dash[0] = 5.0f;
                dash[1] = 2.0f;
                dash[2] = 1.0f;
                dash[3] = 2.0f;
                myStroke = new BasicStroke(
                    lineWidth, cap, join, miterlimit, dash, dashPhase);
                break;
            default:
                throw new RuntimeException(
                    "Line type is not supported (" + lineType + ")");

        }
        g.setStroke(myStroke);
    }
    /**
     * Update the axes location
     */
    protected final void updateAxesLoc(){
        float scaleX = axes[1] - axes[0];
        float scaleY = axes[3] - axes[2];
        float normX, normY;
        normX = (-axes[0] / scaleX);
        locYAxis = (int) (sideLeft + plotWidth * normX);
        normY = (-axes[2] / scaleY);
        locXAxis = (int)(sideBottom - plotHeight * normY);
    }

    /**
     * 
     * @param g Graphics object
     */
    protected final void updateMarkerDetail(Graphics g){}
    protected final void updatePlotLocations(){
        plotWidth = this.getWidth() - 3 * marginX;
        plotHeight = this.getHeight() - 2 * marginY;
        sideLeft = 2 * marginX;
        sideRight = sideLeft + plotWidth;
        sideTop = marginY;
        sideBottom = sideTop + plotHeight;
        updateAxesLoc();
    }
    public static void main(String args[]){;
        java.awt.EventQueue.invokeLater(() -> {
            javax.swing.JFrame newFrame = new javax.swing.JFrame();
            // ------------------------  setup plot  ----------------------------
            Plot p = new Plot();
            float[] newAxes = {-0.f, 1000f, -1f, 1.f};
            p.setAxes(newAxes);
            p.setMargin(60, 40);
            p.setPlotSize(540, 340);
            p.setLabels("Frequency", "Feat. 1", "Freq. vs Feature 1");
            p.setMarker("o", 5, 10);
            DataObject d = new DataObject("Cosine");
            float[] cos = new float[1000];
            float[] sin = new float[1000];
            for (int ind0 = 0; ind0 < cos.length; ind0++){
                cos[ind0] = (float) Math.cos(2 * Math.PI * 0.01 * ind0);
                sin[ind0] = (float) Math.sin(2 * Math.PI * 0.01 * ind0);
            }
            //import jdsp.math.DTFT;
            //float[] dtft = DTFT.discreteFourierTransform(cos, 128);
            //float[] magnArray = DTFT.magnitude(dtft);
            //d.addFeature(cos, "Real");
            //d.addFeature(sin, "Imag");
            //d.addFeature(magnArray, "MagnPSD");
            p.setData(d);
            newFrame.setSize(600,400);
            newFrame.add(p);
            newFrame.setVisible(true);
        });
    }
}