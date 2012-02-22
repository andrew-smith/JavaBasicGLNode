package scene;


import static org.lwjgl.opengl.GL11.*;
import java.util.ArrayList;
import java.nio.FloatBuffer;
import org.lwjgl.BufferUtils;



/**
 * Basic node for a JOGL 3D Application
 * @author Andrew
 */
public class Node
{

    /** Name of this node */
    private String nodeName;

    /** Parent node (if null then this is rootNode - or not attached to scene */
    protected Node parentNode;

    /** A list of all the children nodes below this node */
    private ArrayList<Node> childrenNodes;

    /* Transformations */
    private final float[] scale;
    private final float[] translation;
    private final float[] rotation;

    /** Global Transformation Matrix */
    private final FloatBuffer m;

    /** Temp matrix to use to calculate transformation m */
    private final FloatBuffer temp;

    /** True when transformation matrix is needed to be recalculated */
    private boolean recalculateTransformMatrix;



    /**
     * Creates a new node
     * @param nodeName the name of this node
     */
    public Node(String nodeName)
    {
        this.nodeName = nodeName;
        parentNode = null;
        childrenNodes = new ArrayList<Node>();

        scale = new float[3];
        translation = new float[3];
        rotation = new float[4];

        resetLocalTransformations();

        m = BufferUtils.createFloatBuffer(16);
        temp = BufferUtils.createFloatBuffer(16);

        recalculateTransformMatrix = true;
    }

    /**
     * Sets all translations back to original
     */
    protected void resetLocalTransformations()
    {
        scale[0] = 1.0f; scale[1] = 1.0f; scale[2] = 1.0f;
        translation[0] = 0.0f; translation[1] = 0.0f; translation[2] = 0.0f;
        rotation[0] = 0.0f; rotation[1] = 0.0f; rotation[2] = 1.0f; rotation[3] = 0.0f;

        setRecalculateTransformMatrix();
    }

    /**
     * Sets the transformation matrix to be recalculated in this
     * node and children nodes
     */
    protected void setRecalculateTransformMatrix()
    {
        this.recalculateTransformMatrix = true;
        for(Node c : childrenNodes)
            c.setRecalculateTransformMatrix();
    }


    /**
     * Adds a child to this node
     * (Removes it from any other node it was attached to
     * @param child the child to attach
     * @return true if successfully added - false if not.
     */
    public boolean addChild(Node child)
    {
        boolean success = false;

        //if it has a parent already - remove it from the parent
        if(child.parentNode != null)
        {
            child.parentNode.removeChild(child);
        }

        //now add it to this child
        if(childrenNodes.add(child))
        {
            child.parentNode = this;
            //recalculate because changed parent
            child.setRecalculateTransformMatrix();
            success = true;
        }

        return success;
    }

    /**
     * Removes a child from this node
     * @param child the child to remove from this node
     * @return true if successfully removed - false if not.
     */
    public boolean removeChild(Node child)
    {
        return childrenNodes.remove(child);
    }


    /**
     * Sets the scale of this node
     * @param x x scale
     * @param y y scale
     * @param z z scale
     */
    public void setScale(float x, float y, float z)
    {
        scale[0] = x;
        scale[1] = y;
        scale[2] = z;

        setRecalculateTransformMatrix();
    }

    /**
     * Sets the scale of all 3 vertices
     * @param xyz the x, y and z scale
     */
    public void setScale(float xyz)
    {
        setScale(xyz, xyz, xyz);
    }


    /**
     * Sets the translation of this node
     * @param x x translation
     * @param y y translation
     * @param z z translation
     */
    public void setTranslation(float x, float y, float z)
    {
        translation[0] = x;
        translation[1] = y;
        translation[2] = z;

        setRecalculateTransformMatrix();
    }


    /**
     * Sets the translation of x and y, and sets z to 0.0
     * @param x x translation
     * @param y y translation
     */
    public void setTranslation(float x, float y)
    {
        setTranslation(x, y, 0.0f);
    }


    /**
     * Sets the rotation of this node
     * @param degrees how many degrees to rotate this node
     * @param x amount of rotation around x axis
     * @param y amount of rotation around y axis
     * @param z amount of rotation around z axis
     */
    public void setRotation(float degrees, float x, float y, float z)
    {
        rotation[0] = degrees;
        rotation[1] = x;
        rotation[2] = y;
        rotation[3] = z;

        setRecalculateTransformMatrix();
    }

    /** Identity Matrix */
    private static final float[] IDENTITY_MATRIX = {
        1.0f, 0.0f, 0.0f, 0.0f,
        0.0f, 1.0f, 0.0f, 0.0f,
        0.0f, 0.0f, 1.0f, 0.0f,
        0.0f, 0.0f, 0.0f, 1.0f
    };

    /**
     * Sets the identify matrix
     * @param matrix the matrix to reset
     */
    private static void identityMatrix(FloatBuffer matrix)
    {
        matrix.rewind();
        matrix.put(IDENTITY_MATRIX);
    }

    /**
     * Multiplies two matrices and puts result in b
     * @param a first matrix
     * @param b second matrix (and output)
     */
    private static void multiplyMatrix(FloatBuffer a, FloatBuffer b)
    {

        for (int col = 0; col < 4; col++)
        {
                float p0 = b.get(col * 4);
                float p1 = b.get(col * 4 + 1);
                float p2 = b.get(col * 4 + 2);
                float p3 = b.get(col * 4 + 3);

                for (int row = 0; row < 4; row++)
                {
                        b.put(col * 4 + row, a.get(row) * p0 + a.get(row + 4) * p1 + a.get(row + 8) * p2 + a.get(row + 12) * p3);
                }
        }
    }


    /**
     * Calculates the node global transformation matrix
     * @return this node global transformation matrix
     */
    public FloatBuffer getNodeGlobalTransform()
    {
        if(recalculateTransformMatrix)
        {
            identityMatrix(m);
            identityMatrix(temp);

            //scale
            temp.put(0, scale[0]);
            temp.put(5, scale[1]);
            temp.put(10, scale[2]);
            multiplyMatrix(temp, m);

            //rotation
            identityMatrix(temp);
            float cosAngle = (float) Math.cos(Math.toRadians(rotation[0]));
            float sinAngle = (float) Math.sin(Math.toRadians(rotation[0]));
            float oneMinusCos = 1.0f - cosAngle;
            float ux = rotation[1]; float uy = rotation[2]; float uz = rotation[3];

            temp.put(0, oneMinusCos*ux*ux + cosAngle);
            temp.put(1, oneMinusCos*ux*uy + sinAngle*uz);
            temp.put(2, oneMinusCos*ux*uz - sinAngle*uy);
            temp.put(4, oneMinusCos*ux*uy - sinAngle*uz);
            temp.put(5, oneMinusCos*uy*uy + cosAngle);
            temp.put(6, oneMinusCos*uy*uz + sinAngle*ux);
            temp.put(8, oneMinusCos*ux*uz + sinAngle*uy);
            temp.put(9, oneMinusCos*uy*uz - sinAngle*ux);
            temp.put(10, oneMinusCos*uz*uz + cosAngle);
            multiplyMatrix(temp, m);

            //translation
            identityMatrix(temp);
            temp.put(12, translation[0]);
            temp.put(13, translation[1]);
            temp.put(14, translation[2]);
            multiplyMatrix(temp, m);

            //applies parent transform also (if it has a parent)
            if (parentNode != null) //then get the parent node global transform
                    multiplyMatrix(parentNode.getNodeGlobalTransform(), m);

            //finished recalculating
            recalculateTransformMatrix = false;
        }

        m.rewind();
        return m;
    }


    /**
     * Initilisation method.
     * Also inits children nodes
     */
    public void init()
    {
        //inits position
        getNodeGlobalTransform();

        for(Node n : childrenNodes)
            n.init();
    }


    /**
     * Updates this node.
     * Also updates children nodes
     */
    public void update()
    {
        //this node does nothing so update children nodes
        for(Node n : childrenNodes)
            n.update();
    }


    /**
     * Function to run before drawing
     * @param gl
     */
    protected void preDraw()
    {
        glPushMatrix();

        //push transformation on stack
        glMultMatrix(getNodeGlobalTransform());
    }

    /**
     * Function to run after drawing
     * @param gl
     */
    protected void postDraw()
    {
        glPopMatrix();
    }

    /**
     * Draws the node.
     * Also draws children nodes
     */
    public void draw()
    {
        preDraw();

        //this node does nothing so draw children nodes
        for(Node n : childrenNodes)
            n.draw();

        postDraw();
    }


    /**
     * Gets the shader program of this node.
     * If this is not a shader node then it will get the parent shader program.
     * Default is zero.
     * @return The shader program ID
     */
    public int getShaderProgram()
    {
        if(parentNode != null)
            return parentNode.getShaderProgram();
        //zero is default shader
        return 0;
    }

    /**
     * toString method
     * @return String representation of this 
     */
    @Override
    public String toString()
    {
        return "Node: " + nodeName + " with (" + childrenNodes.size() + ") children";
    }
}
