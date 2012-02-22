package scene;

 

/**
 * Describes a renderable node
 * @author Andrew
 */
public class RenderableNode extends Node
{

    /** The renderable target to draw */
    private GLRenderable renderTarget;

    /**
     * Creates a new renderable node with no render target
     * @param name the name of the node
     */
    public RenderableNode(String name)
    {
        this(name, null);
    }

    /**
     * Creates a new renderable node
     * @param name the name of the node
     * @param target the renderable object to draw
     */
    public RenderableNode(String name, GLRenderable target)
    {
        super(name);
        setRenderTarget(target);
    }


    /**
     * Sets the render target to draw
     * @param target the the render target to draw
     */
    public void setRenderTarget(GLRenderable target)
    {
        this.renderTarget = target;
    }

    /**
     * Inits this renderable node
     * @param gl
     */
    @Override
    public void init()
    {
        if(renderTarget != null)
            renderTarget.init();

        super.init();
    }

    /**
     * Updates this renderable node
     */
    @Override
    public void update()
    {
        if(renderTarget != null)
            renderTarget.update();

        super.update();
    }

    /**
     * Inits this renderable node
     * @param gl
     */
    @Override
    public void draw()
    {
        preDraw();

        if(renderTarget != null)
            renderTarget.draw();

        super.draw();

        postDraw();
    }

}
