
import javax.media.opengl.GL;

/**
 * Main Scene class that handles the node system
 * @author Andrew
 */
public class Scene
{

    /** The one and only rootNode */
    private final Node rootNode;



    /**
     * Creates a new Scene
     */
    public Scene()
    {
        this.rootNode = new Node("rootNode");
    }


    /**
     * Gets the rootNode for this Scene
     * @return the rootNode
     */
    public Node getRootNode()
    {
        return rootNode;
    }



    /**
     * Inits the scene and the nodes
     * @param gl the graphics to use
     */
    public void init(GL gl)
    {
        //init node tree
        rootNode.init(gl);
    }


    /**
     * Updates the scene and the nodes
     */
    public void update()
    {

        //update node tree
        rootNode.update();
    }



    /**
     * Draws the scene and the nodes
     * @param gl the graphics to use
     */
    public void draw(GL gl)
    {

        //draw node tree
        rootNode.draw(gl);
    }
}
