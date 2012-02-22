package scene;



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
     */
    public void init()
    {
        //init node tree
        rootNode.init();
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
     */
    public void draw()
    {

        //draw node tree
        rootNode.draw();
    }
}
