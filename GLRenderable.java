
import javax.media.opengl.GL;


/**
 * Defines a renderable object
 * @author Andrew
 */
public interface GLRenderable
{
    public void init(GL gl);
    public void update();
    public void draw(GL gl);
}
