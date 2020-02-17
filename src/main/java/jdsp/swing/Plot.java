/**
 * Plot JPanel
 * 
 * @author Keith Chow
  */
package jdsp.swing;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.lang.RuntimeException;
import java.util.ArrayList;
import javax.swing.*;
import jdsp.dataformat.DataObject;


public class Plot extends JPanel{
    public final static long serialVersionUID = 0;
    /** Color of the background */
    protected final Color colorBG = Color.WHITE;

    /** Color of the foreground */
    protected final Color colorFG = Color.BLACK;

    /** Color map, to alternate colors */
    public static final Color[] COLOR_MAP = {
        Color.RED, Color.BLUE, Color.GREEN, Color.MAGENTA,
        Color.ORANGE, Color.CYAN, Color.PINK};

    /** The label on the x-axis */
    protected String labelX = "X";

    /** The label on the y-axis */
    protected String labelY = "Y";

    /** Title on the plot */
    protected String labelTitle = "Default Plot Title";

    /** Specify if the grid should be drawn */
    public boolean displayGrid = true;

    /** Specify if the legend should be drawn */
    public boolean displayLegend = true;

    /** Specify the type of line */
    protected String lineType = "-";

    /** Specify the axes */
    protected float[] axes = {0.0f, 1.0f, 0.0f, 1.0f};

    /** Specify the number of grid lines to draw */
    protected int nGridLines = 8;

    /** Specify the horizontal margin in pixels */
    protected int marginX = 40;

    /** Specify the vertical margin in pixels */
    protected int marginY = 40;

    /** The width of the plot */
    protected int plotWidth;

    /** The height of the plot */
    protected int plotHeight;

    /** The number of pixels on the left side */
    protected int sideLeft;

    /** The number of pixels on the right side */
    protected int sideRight;

    /** The number of pixels on the top */
    protected int sideTop;

    /** The number of pixels on the bottom */
    protected int sideBottom;

    /** Marker to use */
    protected String marker = " ";

    /** The width of the marker */
    protected int markerWidth = 10;

    /** The height of the marker */
    protected int markerHeight = 10;

    /** Location of the x-axis */
    protected int locXAxis = 0;

    /** Location of the y-axis */
    protected int locYAxis = 0;

    /** Width of the line */
    protected int lineWidth = 1;

    private FontMetrics _fontMetrics;
    private Rectangle2D _stringBounds;
    private final JToolBar toolBar;

    /** The data object of the plot */
    protected DataObject data;

    /** Constructor for the plot */
    public Plot() {
        data = new DataObject("Default Plot Data");
        toolBar = new JToolBar();

        // -------------------------- marker comboBox -----------------------
        final String[] markerTypes = { " ", "O", "X", "+", "*" };
        final JComboBox comboMarker = new JComboBox(markerTypes);
        comboMarker.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent event) {
                setMarker(comboMarker.getSelectedItem().toString());
            }
        });
        toolBar.add(comboMarker);

        // --------------------------  line comboBox  -----------------------
        final String[] LINE_TYPES = { "-", "--", ":", ".-" };
        final JComboBox comboLines = new JComboBox(LINE_TYPES);
        comboLines.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent event) {
                setLine(comboLines.getSelectedItem().toString());
            }
        });
        toolBar.add(comboLines);

        this.add(toolBar);
    }

    /** Set the new axes
     * 
     * @param newAxes The new axes (min x, max x, min y, max y)
     */
    public void setAxes(final float[] newAxes) {
        // -------------------------- error checking ------------------------
        assert newAxes.length != 4 : "Axes should be 4 element";

        // update axes
        System.arraycopy(newAxes, 0, axes, 0, 4);
        updateAxesLoc();
    }

    /** Set the data object
     * @param data The data object
     */
    public void setData(final DataObject data){
        // update the date
        this.data = data;

        // update the plot
        this.updateUI();
    }

    /** Set the labels
     * 
     * @param x     The x-label (horizontal)
     * @param y     The y-label (vertical)
     * @param title The title of the plot.
     */
    public void setLabels(final String x, final String y, final String title){
        this.labelX = x;
        this.labelY = y;
        this.labelTitle = title;
    }

    /** Set the line type
     * 
     * @param newVal The new line type
     */
    public void setLine(final String newVal) {
        this.lineType = newVal;
        this.updateUI();
    }

    /** Set the margins
     * 
     * @param marginX Set the horizontal margins
     * @param marginY Set the vertical margins
     */
    public void setMargin(final int marginX, final int marginY) {
        this.marginX = marginX;
        this.marginY = marginY;
    }

    /** Set the marker
     * 
     * Adding a marker indicates more clearly where the data points
     * are, rather than just the interpolated line.
     * @param marker The marker
     */
    public void setMarker(final String marker) {
        this.marker = marker;
        this.updateUI();
    }

    /** Set the marker
     * 
     * @param marker The marker
     * @param width  Width of the marker
     * @param height Height of the marker
     */
    public void setMarker(final String marker, final int width, final int height) {
        this.marker = marker;
        this.markerWidth = width;
        this.markerHeight = height;
        this.updateUI();
    }

    /** Set the size of the plot
     * 
     * @param width  New width
     * @param height New height
     */
    public void setPlotSize(final int width, final int height) {
        this.plotWidth = width;
        this.plotHeight = height;
    }

    /** paintComponent handles the drawing of the plot.
     * @param g Graphics object
     */
    @Override
    public void paintComponent(Graphics g) {
        g = (Graphics2D) g;

        // -------------------- update parameters -------------------------
        updatePlotLocations(); // in case of resizing
        updateMarkerDetail(g);

        // ---------------- draw background grid axes ---------------------
        drawBackground(g);

        // limit area to draw
        g.setClip(sideLeft, sideTop, plotWidth, plotHeight);
        drawData(g);

        // return to full clip
        g.setClip(0, 0, this.getWidth(), this.getHeight());

        // draw labels and titles
        drawLabels(g);
    }

    /** Draw the background.
     * 
     * This includes: 
     *      1) Background
     *      2) Draw grid
     *      3) Draw ticks on the axis.
     *      4) Draw labels and title.
     * 
     * @param g Graphics object
     */
    public void drawBackground(final Graphics g) {
        // -------------------- initialize variables ----------------------
        final Graphics2D g2 = (Graphics2D) g;
        final float[] dash = { 1.0f, 3.0f };
        BasicStroke myStroke;
        final float incX = (axes[1] - axes[0]) / nGridLines;
        final float incY = (axes[3] - axes[2]) / nGridLines;

        final FontMetrics fm = g.getFontMetrics();
        Rectangle2D sb;
        sb = fm.getStringBounds(labelY, g);
        int h = fm.getHeight();
        int w;
        String tmpStr;

        // ---------------------- draw background -------------------------
        g2.setColor(colorBG);
        g2.fillRect(sideLeft, sideTop, plotWidth, plotHeight);

        // ---------------------------- draw grid -------------------------
        g2.setColor(colorFG);
        myStroke = new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_ROUND, 10.0f, dash, 0.0f);
        g2.setStroke(myStroke);

        for (int ind0 = 0; ind0 < nGridLines + 1; ind0++) {
            // --------------------- draw y-ticks -------------------------
            tmpStr = Float.toString(axes[3] - incY * ind0);
            sb = fm.getStringBounds(tmpStr, g2);
            w = (int) sb.getWidth();
            g2.drawString(tmpStr, sideLeft - markerWidth - w, sideTop + ind0 * plotHeight / nGridLines);

            // ------------------- draw x-ticks ---------------------------
            tmpStr = Float.toString(axes[0] + incX * ind0);
            sb = fm.getStringBounds(tmpStr, g2);
            w = (int) sb.getWidth();
            h = (int) sb.getHeight();
            g2.drawString(tmpStr, sideLeft + ind0 * plotWidth / nGridLines - w / 2, sideBottom + markerHeight + h / 2);

            // -------------------- draw gridlines ------------------------
            if (displayGrid) {
                // horizontal grid lines
                g2.drawLine(sideLeft, sideTop + ind0 * plotHeight / nGridLines, sideRight,
                        sideTop + ind0 * plotHeight / nGridLines);

                // vertical grid lines
                g2.drawLine(sideLeft + ind0 * plotWidth / nGridLines, sideTop, sideLeft + ind0 * plotWidth / nGridLines,
                        sideBottom);
            }
        }

        // --------------------------- draw axes --------------------------
        // set line characteristics for axes
        myStroke = new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_ROUND);
        g2.setStroke(myStroke);

        // draw axis
        g2.drawLine(sideLeft, locXAxis, sideRight, locXAxis);
        g2.drawLine(locYAxis, sideTop, locYAxis, sideBottom);
    }

    /** Performs a line plot.
     * 
     * @param g Graphics object
     */
    public void drawData(final Graphics g) {
        final Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        loadLineType(g2);
        for (int ind0 = 0; ind0 < data.getNumFeatures(); ind0++) {
            g2.setColor(COLOR_MAP[ind0 % COLOR_MAP.length]);
            draw(g2, data.getFeature(ind0));
        }
    }

    /** Draw a line based on ArrayList vector
     * 
     * @param g2    The Graphics2D object.
     * @param aList The input signal (real)
     */
    public void draw(final Graphics2D g2, final ArrayList aList) {
        final int[] datX = new int[aList.size()];
        final int[] datY = new int[aList.size()];
        final float scaleX = axes[1] - axes[0];
        final float scaleY = axes[3] - axes[2];
        float normX, normY;
        for (int ind0 = 0; ind0 < aList.size(); ind0++) {
            // assume x is just the index used
            normX = (ind0 - axes[0]) / scaleX;
            datX[ind0] = (int) (sideLeft + plotWidth * normX);

            // convert aList scaled by vertical margins and scale
            normY = ((float) aList.get(ind0) - axes[2]) / scaleY;
            datY[ind0] = (int) (sideBottom - plotHeight * normY);
        }
        if (lineType != " ")
            g2.drawPolyline(datX, datY, aList.size());

        if (marker != " ") {
            for (int ind0 = 0; ind0 < aList.size(); ind0++) {
                g2.drawString(marker,
                    datX[ind0] - markerWidth / 2,
                    datY[ind0] + markerHeight / 2);
            }
        }
    }

    /** Draw a line based on ArrayList vector given a float array
     * 
     * @param g2      The Graphics2D object.
     * @param realSig The real signal
     */
    public void draw(final Graphics2D g2, final float[] realSig) {
        final int[] datX = new int[realSig.length];
        final int[] datY = new int[realSig.length];
        final float scaleX = axes[1] - axes[0];
        final float scaleY = axes[3] - axes[2];
        float normX, normY;

        for (int ind0 = 0; ind0 < realSig.length; ind0++) {
            // assume x is just the index
            normX = (ind0 - axes[0]) / scaleX;
            datX[ind0] = (int) (sideLeft + plotWidth * normX);

            // scale the vertical value based on margins and scale
            normY = (realSig[ind0] - axes[2]) / scaleY;
            datY[ind0] = (int) (sideBottom - plotHeight * normY);
        }
        // --------------- use strokes for various line types --------------
        if (lineType != " ")
            g2.drawPolyline(datX, datY, realSig.length);

        // ------------------ draw markers --------------------------------
        // markers can show the time resolution
        if (marker != " ") {
            for (int ind0 = 0; ind0 < realSig.length; ind0++) {
                g2.drawString(marker, datX[ind0] - markerWidth / 2, datY[ind0] + markerHeight / 2);
            }
        }
    }

    /** Draw the labels (x-label, y-label, title, legend)
     * 
     * @param g Graphics object
     */
    public void drawLabels(final Graphics g) {
        // initialize some variables
        _fontMetrics = g.getFontMetrics();
        int labelHeight, labelWidth;

        // initialize foreground color for labels
        g.setColor(colorFG);

        // ---------------------- draw y-labels ---------------------------
        _stringBounds = _fontMetrics.getStringBounds(labelY, g);
        labelHeight = (int) _stringBounds.getHeight();
        labelWidth = (int) _stringBounds.getWidth();
        g.drawString(labelY, (marginX - labelWidth) / 2, (int) (getHeight()) / 2);

        // ----------------------- draw X label ---------------------------
        _stringBounds = _fontMetrics.getStringBounds(labelX, g);
        labelHeight = (int) _stringBounds.getHeight();
        labelWidth = (int) _stringBounds.getWidth();
        g.drawString(labelX, marginX / 2 + (int) (getWidth() - _stringBounds.getWidth()) / 2,
                getHeight() - (marginY - labelHeight) / 2);

        // ------------------------ draw title ----------------------------
        _stringBounds = _fontMetrics.getStringBounds(labelTitle, g);
        labelHeight = (int) _stringBounds.getHeight();
        labelWidth = (int) _stringBounds.getWidth();
        g.drawString(labelTitle, marginX / 2 + (int) (getWidth() - labelWidth) / 2, (marginY + labelHeight) / 2);

        // -------------------- display legend ----------------------------
        if (displayLegend && data != null) {
            String featName;
            for (int ind0 = 0; ind0 < data.getNumFeatures(); ind0++) {
                featName = data.getFeatureName(ind0);
                _stringBounds = _fontMetrics.getStringBounds(featName, g);
                labelHeight = (int) _stringBounds.getHeight();
                labelWidth = (int) _stringBounds.getWidth();

                // match the color to the plot
                g.setColor(COLOR_MAP[ind0]);
                g.drawString(featName, (marginX - labelWidth) / 2, marginY + ind0 * (labelHeight + 5));
            }
        }
    }

    /** Load the type of stroke to use.
     * 
     * @param g Graphics object
     */
    public void loadLineType(final Graphics2D g) {
        final int cap = BasicStroke.CAP_BUTT;
        final int join = BasicStroke.JOIN_ROUND;
        final float miterlimit = 10.0f;
        float[] dash = { 5.0f, 0.0f };
        final float dashPhase = 0.0f;
        BasicStroke myStroke;

        switch (this.lineType) {
        case "-":
            // regular line
            myStroke = new BasicStroke(lineWidth, cap, join, miterlimit);
            break;
        case "--":
            dash[0] = 5.0f;
            dash[1] = 1.0f;
            myStroke = new BasicStroke(lineWidth, cap, join, miterlimit, dash, dashPhase);
            break;
        case ":":
            dash[0] = 1.0f;
            dash[1] = 3.0f;
            myStroke = new BasicStroke(lineWidth, cap, join, miterlimit, dash, dashPhase);
            break;
        case "-.":
        case ".-":
            dash = new float[4];
            dash[0] = 5.0f;
            dash[1] = 2.0f;
            dash[2] = 1.0f;
            dash[3] = 2.0f;
            myStroke = new BasicStroke(lineWidth, cap, join, miterlimit, dash, dashPhase);
            break;
        default:
            myStroke = new BasicStroke(lineWidth, cap, join, miterlimit);
            break;
        }
        g.setStroke(myStroke);
    }

    /** Update the axes location. */
    protected final void updateAxesLoc() {
        final float scaleX = axes[1] - axes[0];
        final float scaleY = axes[3] - axes[2];
        float normX, normY;
        normX = (-axes[0] / scaleX);
        locYAxis = (int) (sideLeft + plotWidth * normX);
        normY = (-axes[2] / scaleY);
        locXAxis = (int) (sideBottom - plotHeight * normY);
    }

    /** Update marker detail.
     * 
     * @param g Graphics object
     */
    protected final void updateMarkerDetail(final Graphics g) {}

    /** Update the plot location
     * 
     * Specify spacing and margins for the plot
     */
    protected final void updatePlotLocations(){
        // update plot dimensions (if panel has been resized)
        plotWidth = this.getWidth() - 3 * marginX;
        plotHeight = this.getHeight() - 2 * marginY;

        // update margins
        sideLeft = 2 * marginX;
        sideRight = sideLeft + plotWidth;
        sideTop = marginY;
        sideBottom = sideTop + plotHeight;

        // update axes location
        updateAxesLoc();
    }

}