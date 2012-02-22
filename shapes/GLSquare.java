

package scene.shapes;

import java.nio.IntBuffer;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.ARBVertexBufferObject;
import scene.GLTextureable;
import static org.lwjgl.opengl.GL11.*;


/**
 * Basic square (1.0 x 1.0)
 * Use scale to resize.
 * @author Andrew
 */
public class GLSquare extends GLTextureable
{
    public void update()
    {
        //empty implementation
    }

    public void draw()
    {

        bindTexture();

        //glDisable(GL_LIGHTING);
        glBegin(GL_TRIANGLE_FAN);
            glColor3f(1.0f,1.0f,1.0f);
            glNormal3f(0.0f, 1.0f, 0.0f);

            glTexCoord2f(0.0f, 0.0f);
            glVertex2f(-1.0f, 1.0f);
            glTexCoord2f(1.0f, 0.0f);
            glVertex2f( 1.0f, 1.0f);
            glTexCoord2f(1.0f, 1.0f);
            glVertex2f( 1.0f,-1.0f);
            glTexCoord2f(0.0f, 1.0f);
            glVertex2f(-1.0f,-1.0f);
        glEnd();

        unbindTexture();
    }

}
